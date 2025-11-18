package io.bloom.mall.product.application.service;

import io.bloom.mall.product.application.dto.ProductDTO;
import io.bloom.mall.product.application.dto.ProductSkuDTO;
import io.bloom.mall.product.domain.entity.ProductSku;
import io.bloom.mall.product.domain.service.ProductService;
import io.bloom.mall.product.domain.service.ProductSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品SKU应用服务
 */
@Service
public class ProductSkuAppService {

    private final ProductSkuService productSkuService;
    private final ProductService productService;

    @Autowired
    public ProductSkuAppService(ProductSkuService productSkuService, ProductService productService) {
        this.productSkuService = productSkuService;
        this.productService = productService;
    }

    /**
     * 根据ID获取SKU
     *
     * @param id SKU ID
     * @return SKU DTO
     */
    public ProductSkuDTO getSkuById(Long id) {
        ProductSku sku = productSkuService.getSkuById(id);
        if (sku == null) {
            return null;
        }

        ProductSkuDTO dto = ProductSkuDTO.fromEntity(sku);
        // 设置商品名称
        ProductDTO productDTO = ProductAppService.getProductNameById(sku.getProductId(), productService);
        if (productDTO != null) {
            dto.setProductName(productDTO.getName());
        }

        return dto;
    }

    /**
     * 根据商品ID获取SKU列表
     *
     * @param productId 商品ID
     * @return SKU DTO列表
     */
    public List<ProductSkuDTO> getSkusByProductId(Long productId) {
        List<ProductSku> skus = productSkuService.getSkusByProductId(productId);
        
        // 获取商品名称
        ProductDTO productDTO = ProductAppService.getProductNameById(productId, productService);
        String productName = (productDTO != null) ? productDTO.getName() : null;

        // 转换为DTO
        return skus.stream()
                .map(sku -> {
                    ProductSkuDTO dto = ProductSkuDTO.fromEntity(sku);
                    dto.setProductName(productName);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * 获取上架状态的SKU列表
     *
     * @param productId 商品ID
     * @return 上架状态的SKU DTO列表
     */
    public List<ProductSkuDTO> getActiveSkusByProductId(Long productId) {
        List<ProductSku> skus = productSkuService.getActiveSkusByProductId(productId);

        // 获取商品名称
        ProductDTO productDTO = ProductAppService.getProductNameById(productId, productService);
        String productName = (productDTO != null) ? productDTO.getName() : null;

        // 转换为DTO
        return skus.stream()
                .map(sku -> {
                    ProductSkuDTO dto = ProductSkuDTO.fromEntity(sku);
                    dto.setProductName(productName);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * 创建SKU
     *
     * @param skuDTO SKU DTO
     * @return 创建后的SKU DTO
     */
    @Transactional(rollbackFor = Exception.class)
    public ProductSkuDTO createSku(ProductSkuDTO skuDTO) {
        // 验证商品是否存在
        if (skuDTO.getProductId() != null) {
            if (productService.getProductById(skuDTO.getProductId()) == null) {
                throw new IllegalArgumentException("商品不存在");
            }
        }

        // 转换为实体并创建
        ProductSku sku = ProductSkuDTO.toEntity(skuDTO);
        sku = productSkuService.createSku(sku);

        // 转换为DTO返回
        ProductSkuDTO dto = ProductSkuDTO.fromEntity(sku);
        // 设置商品名称
        ProductDTO productDTO = ProductAppService.getProductNameById(sku.getProductId(), productService);
        if (productDTO != null) {
            dto.setProductName(productDTO.getName());
        }

        return dto;
    }

    /**
     * 批量创建SKU
     *
     * @param skuDTOs SKU DTO列表
     * @return 创建的SKU数量
     */
    @Transactional(rollbackFor = Exception.class)
    public int createSkus(List<ProductSkuDTO> skuDTOs) {
        if (skuDTOs == null || skuDTOs.isEmpty()) {
            return 0;
        }

        // 验证商品是否存在（只需验证第一个，假设所有SKU属于同一个商品）
        Long productId = skuDTOs.get(0).getProductId();
        if (productId != null) {
            if (productService.getProductById(productId) == null) {
                throw new IllegalArgumentException("商品不存在");
            }
        }

        // 转换为实体并批量创建
        List<ProductSku> skus = skuDTOs.stream()
                .map(ProductSkuDTO::toEntity)
                .collect(Collectors.toList());

        return productSkuService.createSkus(skus);
    }

    /**
     * 更新SKU
     *
     * @param skuDTO SKU DTO
     * @return 更新后的SKU DTO
     */
    @Transactional(rollbackFor = Exception.class)
    public ProductSkuDTO updateSku(ProductSkuDTO skuDTO) {
        // 验证SKU是否存在
        ProductSku existingSku = productSkuService.getSkuById(skuDTO.getId());
        if (existingSku == null) {
            throw new IllegalArgumentException("SKU不存在");
        }

        // 验证商品是否存在
        if (skuDTO.getProductId() != null && !skuDTO.getProductId().equals(existingSku.getProductId())) {
            if (productService.getProductById(skuDTO.getProductId()) == null) {
                throw new IllegalArgumentException("商品不存在");
            }
        }

        // 转换为实体并更新
        ProductSku sku = ProductSkuDTO.toEntity(skuDTO);
        sku = productSkuService.updateSku(sku);

        // 转换为DTO返回
        ProductSkuDTO dto = ProductSkuDTO.fromEntity(sku);
        // 设置商品名称
        ProductDTO productDTO = ProductAppService.getProductNameById(sku.getProductId(), productService);
        if (productDTO != null) {
            dto.setProductName(productDTO.getName());
        }

        return dto;
    }

    /**
     * 删除SKU
     *
     * @param id SKU ID
     * @return 是否删除成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteSku(Long id) {
        return productSkuService.deleteSku(id);
    }

    /**
     * 更新SKU状态
     *
     * @param id     SKU ID
     * @param status 状态：0-下架，1-上架，2-预售
     * @return 是否更新成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSkuStatus(Long id, Integer status) {
        return productSkuService.updateSkuStatus(id, status);
    }

    /**
     * 更新SKU库存
     *
     * @param id    SKU ID
     * @param stock 库存数量
     * @return 是否更新成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSkuStock(Long id, Integer stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("库存数量不能为负数");
        }
        return productSkuService.updateSkuStock(id, stock);
    }

    /**
     * 减少SKU库存
     *
     * @param id       SKU ID
     * @param quantity 减少的数量
     * @return 是否减少成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean decreaseSkuStock(Long id, Integer quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("减少的数量必须大于0");
        }
        return productSkuService.decreaseSkuStock(id, quantity);
    }

    /**
     * 增加SKU库存
     *
     * @param id       SKU ID
     * @param quantity 增加的数量
     * @return 是否增加成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean increaseSkuStock(Long id, Integer quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("增加的数量必须大于0");
        }
        return productSkuService.increaseSkuStock(id, quantity);
    }

    /**
     * 获取商品的最低价格
     *
     * @param productId 商品ID
     * @return 最低价格
     */
    public Double getMinPriceByProductId(Long productId) {
        return productSkuService.getMinPriceByProductId(productId);
    }

    /**
     * 获取商品的最高价格
     *
     * @param productId 商品ID
     * @return 最高价格
     */
    public Double getMaxPriceByProductId(Long productId) {
        return productSkuService.getMaxPriceByProductId(productId);
    }
}