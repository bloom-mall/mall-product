package io.bloom.mall.product.application.service;

import io.bloom.mall.product.application.dto.ProductCategoryDTO;
import io.bloom.mall.product.domain.entity.ProductCategory;
import io.bloom.mall.product.domain.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商品分类应用服务
 */
@Service
public class ProductCategoryAppService {

    private final ProductCategoryService productCategoryService;

    @Autowired
    public ProductCategoryAppService(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    /**
     * 获取分类树
     *
     * @return 分类树DTO列表
     */
    public List<ProductCategoryDTO> getCategoryTree() {
        // 获取所有分类
        List<ProductCategory> allCategories = productCategoryService.getAllCategories();

        // 过滤出未删除且启用的分类
        List<ProductCategory> validCategories = allCategories.stream()
                .filter(category -> category.getIsDeleted() != null && category.getIsDeleted() == 0)
                .filter(category -> category.getStatus() != null && category.getStatus() == 1)
                .collect(Collectors.toList());

        // 转换为DTO
        List<ProductCategoryDTO> categoryDTOs = validCategories.stream()
                .map(ProductCategoryDTO::fromEntity)
                .collect(Collectors.toList());

        // 构建树结构
        return buildCategoryTree(categoryDTOs);
    }

    /**
     * 获取指定父分类下的子分类
     *
     * @param parentId 父分类ID
     * @return 子分类DTO列表
     */
    public List<ProductCategoryDTO> getChildCategories(Long parentId) {
        List<ProductCategory> childCategories = productCategoryService.getChildCategories(parentId);
        return childCategories.stream()
                .map(ProductCategoryDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 创建商品分类
     *
     * @param categoryDTO 商品分类DTO
     * @return 创建后的商品分类DTO
     */
    public ProductCategoryDTO createCategory(ProductCategoryDTO categoryDTO) {
        // 转换为实体
        ProductCategory category = new ProductCategory();
        category.setParentId(categoryDTO.getParentId());
        category.setName(categoryDTO.getName());
        category.setLevel(categoryDTO.getLevel());
        category.setSortOrder(categoryDTO.getSortOrder());
        category.setIcon(categoryDTO.getIcon());
        category.setDescription(categoryDTO.getDescription());
        category.setStatus(categoryDTO.getStatus());

        // 创建分类
        ProductCategory createdCategory = productCategoryService.createCategory(category);

        // 转换为DTO返回
        return ProductCategoryDTO.fromEntity(createdCategory);
    }

    /**
     * 更新商品分类
     *
     * @param categoryDTO 商品分类DTO
     * @return 更新后的商品分类DTO
     */
    public ProductCategoryDTO updateCategory(ProductCategoryDTO categoryDTO) {
        // 检查分类是否存在
        ProductCategory existingCategory = productCategoryService.getCategoryById(categoryDTO.getId());
        if (existingCategory == null) {
            return null;
        }

        // 更新分类信息
        existingCategory.setName(categoryDTO.getName());
        existingCategory.setSortOrder(categoryDTO.getSortOrder());
        existingCategory.setIcon(categoryDTO.getIcon());
        existingCategory.setDescription(categoryDTO.getDescription());
        existingCategory.setStatus(categoryDTO.getStatus());

        // 更新分类
        ProductCategory updatedCategory = productCategoryService.updateCategory(existingCategory);

        // 转换为DTO返回
        return ProductCategoryDTO.fromEntity(updatedCategory);
    }

    /**
     * 删除商品分类
     *
     * @param id 分类ID
     * @return 是否删除成功
     */
    public boolean deleteCategory(Long id) {
        return productCategoryService.deleteCategory(id);
    }

    /**
     * 构建分类树结构
     *
     * @param categoryDTOs 分类DTO列表
     * @return 树结构的分类DTO列表
     */
    private List<ProductCategoryDTO> buildCategoryTree(List<ProductCategoryDTO> categoryDTOs) {
        // 按父ID分组
        Map<Long, List<ProductCategoryDTO>> parentIdMap = new HashMap<>();
        for (ProductCategoryDTO dto : categoryDTOs) {
            List<ProductCategoryDTO> children = parentIdMap.computeIfAbsent(dto.getParentId(), k -> new ArrayList<>());
            children.add(dto);
        }

        // 获取一级分类
        List<ProductCategoryDTO> rootCategories = parentIdMap.getOrDefault(0L, new ArrayList<>());

        // 递归设置子分类
        rootCategories.forEach(root -> setChildren(root, parentIdMap));

        return rootCategories;
    }

    /**
     * 递归设置子分类
     *
     * @param parent      父分类DTO
     * @param parentIdMap 按父ID分组的分类DTO Map
     */
    private void setChildren(ProductCategoryDTO parent, Map<Long, List<ProductCategoryDTO>> parentIdMap) {
        List<ProductCategoryDTO> children = parentIdMap.getOrDefault(parent.getId(), new ArrayList<>());
        parent.setChildren(children);

        // 递归设置子分类的子分类
        children.forEach(child -> setChildren(child, parentIdMap));
    }
}