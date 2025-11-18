package io.bloom.mall.product.application.dto;

import io.bloom.mall.product.domain.entity.ProductSku;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品SKU DTO，用于前端展示
 */
@Data
public class ProductSkuDTO {

    /**
     * SKU ID
     */
    private Long id;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * SKU名称
     */
    private String name;

    /**
     * 商品属性列表
     */
    private String attributeIds;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 库存数量
     */
    private Integer stock;

    /**
     * 状态：0-下架，1-上架，2-预售
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 将实体转换为DTO
     *
     * @param sku 商品SKU实体
     * @return 商品SKU DTO
     */
    public static ProductSkuDTO fromEntity(ProductSku sku) {
        if (sku == null) {
            return null;
        }

        ProductSkuDTO dto = new ProductSkuDTO();
        dto.setId(sku.getId());
        dto.setProductId(sku.getProductId());
        dto.setName(sku.getName());
        dto.setAttributeIds(sku.getAttributeIds());
        dto.setPrice(sku.getPrice());
        dto.setStock(sku.getStock());
        dto.setStatus(sku.getStatus());
        dto.setCreateTime(sku.getCreateTime());
        dto.setUpdateTime(sku.getUpdateTime());

        return dto;
    }

    /**
     * 将DTO转换为实体
     *
     * @param dto 商品SKU DTO
     * @return 商品SKU实体
     */
    public static ProductSku toEntity(ProductSkuDTO dto) {
        if (dto == null) {
            return null;
        }

        ProductSku sku = new ProductSku();
        sku.setId(dto.getId());
        sku.setProductId(dto.getProductId());
        sku.setName(dto.getName());
        sku.setAttributeIds(dto.getAttributeIds());
        sku.setPrice(dto.getPrice());
        sku.setStock(dto.getStock());
        sku.setStatus(dto.getStatus());

        return sku;
    }
}