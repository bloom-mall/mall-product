package io.bloom.mall.product.interfaces.controller;

import io.bloom.mall.product.application.dto.ProductCategoryDTO;
import io.bloom.mall.product.application.service.ProductCategoryAppService;
import io.bloom.mall.product.interfaces.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/productCategories")
public class ProductCategoryController {

    private final ProductCategoryAppService categoryAppService;

    @Autowired
    public ProductCategoryController(ProductCategoryAppService categoryAppService) {
        this.categoryAppService = categoryAppService;
    }

    /**
     * 获取分类树
     *
     * @return 分类树
     */
    @GetMapping("/tree")
    public Result<List<ProductCategoryDTO>> getCategoryTree() {
        List<ProductCategoryDTO> categoryTree = categoryAppService.getCategoryTree();
        return Result.success(categoryTree);
    }

    /**
     * 获取指定父分类下的子分类
     *
     * @param parentId 父分类ID
     * @return 子分类列表
     */
    @GetMapping("/children/{parentId}")
    public Result<List<ProductCategoryDTO>> getChildCategories(@PathVariable Long parentId) {
        List<ProductCategoryDTO> childCategories = categoryAppService.getChildCategories(parentId);
        return Result.success(childCategories);
    }

    /**
     * 创建商品分类
     *
     * @param categoryDTO 商品分类信息
     * @return 创建后的商品分类
     */
    @PostMapping
    public Result<ProductCategoryDTO> createCategory(@RequestBody ProductCategoryDTO categoryDTO) {
        ProductCategoryDTO createdCategory = categoryAppService.createCategory(categoryDTO);
        return Result.success(createdCategory);
    }

    /**
     * 更新商品分类
     *
     * @param id          分类ID
     * @param categoryDTO 商品分类信息
     * @return 更新后的商品分类
     */
    @PutMapping("/{id}")
    public Result<ProductCategoryDTO> updateCategory(@PathVariable Long id, @RequestBody ProductCategoryDTO categoryDTO) {
        categoryDTO.setId(id);
        ProductCategoryDTO productCategory = categoryAppService.updateCategory(categoryDTO);
        return Result.success(productCategory);
    }

    /**
     * 删除商品分类
     *
     * @param id 分类ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteCategory(@PathVariable Long id) {
        categoryAppService.deleteCategory(id);
        return Result.success();
    }
}