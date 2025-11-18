package io.bloom.mall.product.interfaces.controller;

import io.bloom.mall.product.application.dto.ProductSkuDTO;
import io.bloom.mall.product.application.service.ProductSkuAppService;
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

/**
 * 商品SKU控制器
 */
@RestController
@RequestMapping("/productSkus")
public class ProductSkuController {

    private final ProductSkuAppService productSkuAppService;

    @Autowired
    public ProductSkuController(ProductSkuAppService productSkuAppService) {
        this.productSkuAppService = productSkuAppService;
    }

    /**
     * 根据ID获取SKU
     *
     * @param id SKU ID
     * @return SKU信息
     */
    @GetMapping("/{id}")
    public Result<ProductSkuDTO> getSkuById(@PathVariable Long id) {
        ProductSkuDTO sku = productSkuAppService.getSkuById(id);
        return Result.success(sku);
    }

    /**
     * 根据商品ID获取SKU列表
     *
     * @param productId 商品ID
     * @return SKU列表
     */
    @GetMapping("/product/{productId}")
    public Result<List<ProductSkuDTO>> getSkusByProductId(@PathVariable Long productId) {
        List<ProductSkuDTO> skus = productSkuAppService.getSkusByProductId(productId);
        return Result.success(skus);
    }

    /**
     * 获取上架状态的SKU列表
     *
     * @param productId 商品ID
     * @return 上架状态的SKU列表
     */
    @GetMapping("/active/product/{productId}")
    public Result<List<ProductSkuDTO>> getActiveSkusByProductId(@PathVariable Long productId) {
        List<ProductSkuDTO> skus = productSkuAppService.getActiveSkusByProductId(productId);
        return Result.success(skus);
    }

    /**
     * 创建SKU
     *
     * @param skuDTO SKU信息
     * @return 创建后的SKU
     */
    @PostMapping
    public Result<ProductSkuDTO> createSku(@RequestBody ProductSkuDTO skuDTO) {
        ProductSkuDTO createdSku = productSkuAppService.createSku(skuDTO);
        return Result.success(createdSku);
    }

    /**
     * 批量创建SKU
     *
     * @param skuDTOs SKU列表
     * @return 创建的SKU数量
     */
    @PostMapping("/batch")
    public Result<Integer> createSkus(@RequestBody List<ProductSkuDTO> skuDTOs) {
        int count = productSkuAppService.createSkus(skuDTOs);
        return Result.success(count);
    }

    /**
     * 更新SKU
     *
     * @param id     SKU ID
     * @param skuDTO SKU信息
     * @return 更新后的SKU
     */
    @PutMapping("/{id}")
    public Result<ProductSkuDTO> updateSku(@PathVariable Long id, @RequestBody ProductSkuDTO skuDTO) {
        skuDTO.setId(id);
        ProductSkuDTO updatedSku = productSkuAppService.updateSku(skuDTO);
        return Result.success(updatedSku);
    }

    /**
     * 删除SKU
     *
     * @param id SKU ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteSku(@PathVariable Long id) {
        boolean result = productSkuAppService.deleteSku(id);
        return Result.success(result);
    }

    /**
     * 更新SKU状态
     *
     * @param id     SKU ID
     * @param status 状态：0-下架，1-上架，2-预售
     * @return 操作结果
     */
    @PutMapping("/{id}/status")
    public Result<Boolean> updateSkuStatus(@PathVariable Long id, @RequestParam Integer status) {
        boolean result = productSkuAppService.updateSkuStatus(id, status);
        return Result.success(result);
    }

    /**
     * 更新SKU库存
     *
     * @param id    SKU ID
     * @param stock 库存数量
     * @return 操作结果
     */
    @PutMapping("/{id}/stock")
    public Result<Boolean> updateSkuStock(@PathVariable Long id, @RequestParam Integer stock) {
        boolean result = productSkuAppService.updateSkuStock(id, stock);
        return Result.success(result);
    }

    /**
     * 减少SKU库存
     *
     * @param id       SKU ID
     * @param quantity 减少的数量
     * @return 操作结果
     */
    @PutMapping("/{id}/stock/decrease")
    public Result<Boolean> decreaseSkuStock(@PathVariable Long id, @RequestParam Integer quantity) {
        boolean result = productSkuAppService.decreaseSkuStock(id, quantity);
        return Result.success(result);
    }

    /**
     * 增加SKU库存
     *
     * @param id       SKU ID
     * @param quantity 增加的数量
     * @return 操作结果
     */
    @PutMapping("/{id}/stock/increase")
    public Result<Boolean> increaseSkuStock(@PathVariable Long id, @RequestParam Integer quantity) {
        boolean result = productSkuAppService.increaseSkuStock(id, quantity);
        return Result.success(result);
    }

    /**
     * 获取商品的最低价格
     *
     * @param productId 商品ID
     * @return 最低价格
     */
    @GetMapping("/minPrice/product/{productId}")
    public Result<Double> getMinPriceByProductId(@PathVariable Long productId) {
        Double minPrice = productSkuAppService.getMinPriceByProductId(productId);
        return Result.success(minPrice);
    }

    /**
     * 获取商品的最高价格
     *
     * @param productId 商品ID
     * @return 最高价格
     */
    @GetMapping("/maxPrice/product/{productId}")
    public Result<Double> getMaxPriceByProductId(@PathVariable Long productId) {
        Double maxPrice = productSkuAppService.getMaxPriceByProductId(productId);
        return Result.success(maxPrice);
    }
}