package io.bloom.mall.product.application.dto;

import io.bloom.mall.product.domain.entity.Product;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 商品DTO，用于前端展示
 */
@Data
public class ProductDTO {

    /**
     * 商品ID
     */
    private Long id;

    /**
     * 分类ID
     */
    private Long categoryId;
    
    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品主图
     */
    private String mainPicture;

    /**
     * 状态：0-下架，1-上架
     */
    private Integer status;

    /**
     * 是否热销：0-否，1-是
     */
    private Integer isHot;

    /**
     * 是否新品：0-否，1-是
     */
    private Integer isNew;

    /**
     * 是否推荐：0-否，1-是
     */
    private Integer isRecommend;

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
     * @param product 商品实体
     * @return 商品DTO
     */
    public static ProductDTO fromEntity(Product product) {
        if (product == null) {
            return null;
        }

        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setCategoryId(product.getCategoryId());
        dto.setName(product.getName());
        dto.setMainPicture(product.getMainPicture());
        dto.setStatus(product.getStatus());
        dto.setIsHot(product.getIsHot());
        dto.setIsNew(product.getIsNew());
        dto.setIsRecommend(product.getIsRecommend());
        dto.setCreateTime(product.getCreateTime());
        dto.setUpdateTime(product.getUpdateTime());

        return dto;
    }

    /**
     * 将DTO转换为实体
     *
     * @param dto 商品DTO
     * @return 商品实体
     */
    public static Product toEntity(ProductDTO dto) {
        if (dto == null) {
            return null;
        }

        Product product = new Product();
        product.setId(dto.getId());
        product.setCategoryId(dto.getCategoryId());
        product.setName(dto.getName());
        product.setMainPicture(dto.getMainPicture());
        product.setStatus(dto.getStatus());
        product.setIsHot(dto.getIsHot());
        product.setIsNew(dto.getIsNew());
        product.setIsRecommend(dto.getIsRecommend());

        return product;
    }
}