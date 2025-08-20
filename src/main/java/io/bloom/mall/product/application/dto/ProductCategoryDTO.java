package io.bloom.mall.product.application.dto;

import io.bloom.mall.product.domain.entity.ProductCategory;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品分类DTO，用于前端展示
 */
@Data
public class ProductCategoryDTO {

    /**
     * 分类ID
     */
    private Long id;

    /**
     * 父分类ID
     */
    private Long parentId;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类层级
     */
    private Integer level;

    /**
     * 排序权重
     */
    private Integer sortOrder;

    /**
     * 分类图标
     */
    private String icon;

    /**
     * 分类描述
     */
    private String description;

    /**
     * 状态：0-禁用，1-启用
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
     * 子分类列表
     */
    private List<ProductCategoryDTO> children = new ArrayList<>();

    /**
     * 将实体转换为DTO
     *
     * @param category 商品分类实体
     * @return 商品分类DTO
     */
    public static ProductCategoryDTO fromEntity(ProductCategory category) {
        if (category == null) {
            return null;
        }

        ProductCategoryDTO dto = new ProductCategoryDTO();
        dto.setId(category.getId());
        dto.setParentId(category.getParentId());
        dto.setName(category.getName());
        dto.setLevel(category.getLevel());
        dto.setSortOrder(category.getSortOrder());
        dto.setIcon(category.getIcon());
        dto.setDescription(category.getDescription());
        dto.setStatus(category.getStatus());
        dto.setCreateTime(category.getCreateTime());
        dto.setUpdateTime(category.getUpdateTime());

        return dto;
    }
}