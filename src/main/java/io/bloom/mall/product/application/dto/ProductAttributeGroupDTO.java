package io.bloom.mall.product.application.dto;

import io.bloom.mall.product.domain.entity.ProductAttributeGroup;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品属性分组DTO，用于前端展示
 */
@Data
public class ProductAttributeGroupDTO {

    /**
     * 分组ID
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
     * 分组名
     */
    private String name;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 属性列表
     */
    private List<ProductAttributeDTO> attributes = new ArrayList<>();

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
     * @param group 商品属性分组实体
     * @return 商品属性分组DTO
     */
    public static ProductAttributeGroupDTO fromEntity(ProductAttributeGroup group) {
        if (group == null) {
            return null;
        }

        ProductAttributeGroupDTO dto = new ProductAttributeGroupDTO();
        dto.setId(group.getId());
        dto.setProductId(group.getProductId());
        dto.setName(group.getName());
        dto.setSort(group.getSort());
        dto.setCreateTime(group.getCreateTime());
        dto.setUpdateTime(group.getUpdateTime());

        return dto;
    }

    /**
     * 将DTO转换为实体
     *
     * @param dto 商品属性分组DTO
     * @return 商品属性分组实体
     */
    public static ProductAttributeGroup toEntity(ProductAttributeGroupDTO dto) {
        if (dto == null) {
            return null;
        }

        ProductAttributeGroup group = new ProductAttributeGroup();
        group.setId(dto.getId());
        group.setProductId(dto.getProductId());
        group.setName(dto.getName());
        group.setSort(dto.getSort());

        return group;
    }
}