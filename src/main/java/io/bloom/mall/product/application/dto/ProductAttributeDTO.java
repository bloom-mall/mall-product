package io.bloom.mall.product.application.dto;

import io.bloom.mall.product.domain.entity.ProductAttribute;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 商品属性DTO，用于前端展示
 */
@Data
public class ProductAttributeDTO {

    /**
     * 属性ID
     */
    private Long id;

    /**
     * 属性名
     */
    private String name;

    /**
     * 分组ID
     */
    private Long groupId;

    /**
     * 分组名称
     */
    private String groupName;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 排序
     */
    private Integer sort;

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
     * @param attribute 商品属性实体
     * @return 商品属性DTO
     */
    public static ProductAttributeDTO fromEntity(ProductAttribute attribute) {
        if (attribute == null) {
            return null;
        }

        ProductAttributeDTO dto = new ProductAttributeDTO();
        dto.setId(attribute.getId());
        dto.setName(attribute.getName());
        dto.setGroupId(attribute.getGroupId());
        dto.setProductId(attribute.getProductId());
        dto.setSort(attribute.getSort());
        dto.setCreateTime(attribute.getCreateTime());
        dto.setUpdateTime(attribute.getUpdateTime());

        return dto;
    }

    /**
     * 将DTO转换为实体
     *
     * @param dto 商品属性DTO
     * @return 商品属性实体
     */
    public static ProductAttribute toEntity(ProductAttributeDTO dto) {
        if (dto == null) {
            return null;
        }

        ProductAttribute attribute = new ProductAttribute();
        attribute.setId(dto.getId());
        attribute.setName(dto.getName());
        attribute.setGroupId(dto.getGroupId());
        attribute.setProductId(dto.getProductId());
        attribute.setSort(dto.getSort());

        return attribute;
    }
}