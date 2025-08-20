package io.bloom.mall.product.interfaces.controller;

import com.mybatisflex.core.paginate.Page;
import io.bloom.mall.product.application.dto.ProductDTO;
import io.bloom.mall.product.application.service.ProductAppService;
import io.bloom.mall.product.interfaces.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductAppService productAppService;

    @Autowired
    public ProductController(ProductAppService productAppService) {
        this.productAppService = productAppService;
    }

    /**
     * 根据ID获取商品
     *
     * @param id 商品ID
     * @return 商品信息
     */
    @GetMapping("/{id}")
    public Result<ProductDTO> getProductById(@PathVariable Long id) {
        ProductDTO product = productAppService.getProductById(id);
        return Result.success(product);
    }

    /**
     * 分页查询商品列表
     *
     * @param page       页码
     * @param pageSize   每页数量
     * @param categoryId 分类ID，可为null
     * @param keyword    关键词，可为null
     * @return 商品分页列表
     */
    @GetMapping
    public Result<Page<ProductDTO>> getProductPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword) {
        Page<ProductDTO> productPage = productAppService.getProductPage(page, pageSize, categoryId, keyword);
        return Result.success(productPage);
    }

    /**
     * 创建商品
     *
     * @param productDTO 商品信息
     * @return 创建后的商品
     */
    @PostMapping
    public Result<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        ProductDTO createdProduct = productAppService.createProduct(productDTO);
        return Result.success(createdProduct);
    }

    /**
     * 更新商品
     *
     * @param id         商品ID
     * @param productDTO 商品信息
     * @return 更新后的商品
     */
    @PutMapping("/{id}")
    public Result<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        productDTO.setId(id);
        ProductDTO updatedProduct = productAppService.updateProduct(productDTO);
        return Result.success(updatedProduct);
    }

    /**
     * 删除商品
     *
     * @param id 商品ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteProduct(@PathVariable Long id) {
        productAppService.deleteProduct(id);
        return Result.success();
    }

    /**
     * 更新商品状态
     *
     * @param id     商品ID
     * @param status 状态：0-下架，1-上架，2-预售
     * @return 操作结果
     * <p>
     * TODO: 增加status取值范围校验
     */
    @PutMapping("/{id}/status")
    public Result<Boolean> updateProductStatus(@PathVariable Long id, @RequestParam Integer status) {
        productAppService.updateProductStatus(id, status);
        return Result.success();
    }

    /**
     * 获取热销商品列表
     *
     * @param limit 数量限制
     * @return 热销商品列表
     */
    @GetMapping("/hot")
    public Result<List<ProductDTO>> getHotProducts(@RequestParam(defaultValue = "10") int limit) {
        List<ProductDTO> products = productAppService.getHotProducts(limit);
        return Result.success(products);
    }

    /**
     * 获取新品列表
     *
     * @param limit 数量限制
     * @return 新品列表
     */
    @GetMapping("/new")
    public Result<List<ProductDTO>> getNewProducts(@RequestParam(defaultValue = "10") int limit) {
        List<ProductDTO> products = productAppService.getNewProducts(limit);
        return Result.success(products);
    }

    /**
     * 获取推荐商品列表
     *
     * @param limit 数量限制
     * @return 推荐商品列表
     */
    @GetMapping("/recommend")
    public Result<List<ProductDTO>> getRecommendProducts(@RequestParam(defaultValue = "10") int limit) {
        List<ProductDTO> products = productAppService.getRecommendProducts(limit);
        return Result.success(products);
    }
}