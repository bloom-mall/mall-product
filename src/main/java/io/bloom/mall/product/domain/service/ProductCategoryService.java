package io.bloom.mall.product.domain.service;

import com.mybatisflex.core.query.QueryWrapper;
import io.bloom.mall.product.domain.entity.ProductCategory;
import io.bloom.mall.product.infrastructure.repository.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.bloom.mall.product.domain.entity.table.ProductCategoryTableDef.PRODUCT_CATEGORY;

@Service
public class ProductCategoryService {

    private final ProductCategoryRepository productCategoryMapper;

    @Autowired
    public ProductCategoryService(ProductCategoryRepository productCategoryMapper) {
        this.productCategoryMapper = productCategoryMapper;
    }

    public List<ProductCategory> getAllCategories() {
        return productCategoryMapper.selectAll();
    }

    public ProductCategory getCategoryById(Long id) {
        return productCategoryMapper.selectOneById(id);
    }

    public List<ProductCategory> getChildCategories(Long parentId) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(PRODUCT_CATEGORY.PARENT_ID.eq(parentId))
                .and(PRODUCT_CATEGORY.STATUS.eq(1))
                .and(PRODUCT_CATEGORY.IS_DELETED.eq(0))
                .orderBy(PRODUCT_CATEGORY.SORT_ORDER.asc());
        return productCategoryMapper.selectListByQuery(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    public ProductCategory createCategory(ProductCategory category) {
        // 设置创建时间和更新时间
        LocalDateTime now = LocalDateTime.now();
        category.setCreateTime(now);
        category.setUpdateTime(now);

        // 如果未设置状态，默认为启用
        if (category.getStatus() == null) {
            category.setStatus(1);
        }

        // 如果未设置是否删除，默认为未删除
        if (category.getIsDeleted() == null) {
            category.setIsDeleted(0);
        }

        // 如果未设置排序，默认为0
        if (category.getSortOrder() == null) {
            category.setSortOrder(0);
        }

        // 设置分类层级
        if (category.getParentId() == 0) {
            // 一级分类
            category.setLevel(1);
        } else {
            // 获取父分类
            ProductCategory parentCategory = productCategoryMapper.selectOneById(category.getParentId());
            if (parentCategory != null) {
                category.setLevel(parentCategory.getLevel() + 1);
            } else {
                // 父分类不存在，设为一级分类
                category.setParentId(0L);
                category.setLevel(1);
            }
        }

        productCategoryMapper.insert(category);
        return category;
    }

    @Transactional(rollbackFor = Exception.class)
    public ProductCategory updateCategory(ProductCategory category) {
        // 设置更新时间
        category.setUpdateTime(LocalDateTime.now());

        productCategoryMapper.update(category);
        return category;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteCategory(Long id) {
        // 检查是否有子分类
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(PRODUCT_CATEGORY.PARENT_ID.eq(id))
                .and(PRODUCT_CATEGORY.IS_DELETED.eq(0));
        long count = productCategoryMapper.selectCountByQuery(queryWrapper);
        if (count > 0) {
            // 有子分类，不能删除
            return false;
        }

        // 逻辑删除
        ProductCategory category = new ProductCategory();
        category.setId(id);
        category.setIsDeleted(1);
        category.setUpdateTime(LocalDateTime.now());

        return productCategoryMapper.update(category) > 0;
    }

    public List<ProductCategory> getCategoryTree() {
        // 获取所有未删除的分类
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(PRODUCT_CATEGORY.IS_DELETED.eq(0))
                .and(PRODUCT_CATEGORY.STATUS.eq(1))
                .orderBy(PRODUCT_CATEGORY.SORT_ORDER.asc());
        List<ProductCategory> allCategories = productCategoryMapper.selectListByQuery(queryWrapper);

        // 按父ID分组
        Map<Long, List<ProductCategory>> parentIdMap = allCategories.stream()
                .collect(Collectors.groupingBy(ProductCategory::getParentId));

        // 获取一级分类
        List<ProductCategory> rootCategories = parentIdMap.getOrDefault(0L, new ArrayList<>());

        // 递归设置子分类
        rootCategories.forEach(root -> buildChildCategories(root, parentIdMap));

        return rootCategories;
    }

    /**
     * 递归构建子分类
     *
     * @param parent      父分类
     * @param parentIdMap 按父ID分组的分类Map
     */
    private void buildChildCategories(ProductCategory parent, Map<Long, List<ProductCategory>> parentIdMap) {
        List<ProductCategory> children = parentIdMap.getOrDefault(parent.getId(), new ArrayList<>());
        // 这里我们不能直接设置children属性，因为ProductCategory类中没有这个属性
        // 在实际应用中，可以创建一个DTO类来包含children属性
        // 或者在ProductCategory类中添加一个transient的children属性

        // 递归设置子分类的子分类
        children.forEach(child -> buildChildCategories(child, parentIdMap));
    }
}