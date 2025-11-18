package io.bloom.mall.product.domain.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.bloom.mall.product.domain.entity.ProductCategory;
import io.bloom.mall.product.domain.entity.ProductSku;
import io.bloom.mall.product.domain.entity.ProductAttribute;
import io.bloom.mall.product.domain.entity.ProductAttributeGroup;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import java.io.Serial;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("product")
public class Product implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 商品ID
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 分类ID
     */
    private Long categoryId;

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
     * 是否删除：0-未删除，1-已删除
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;

    // 关联属性
    private ProductCategory category;
    private List<ProductSku> skus;
    private List<ProductAttribute> attributes;
    private List<ProductAttributeGroup> attributeGroups;

    // 业务方法：获取商品最低价格
    public Double getMinPrice() {
        if (skus == null || skus.isEmpty()) {
            return 0.0;
        }
        return skus.stream()
                .mapToDouble(sku -> sku.getPrice().doubleValue())
                .min()
                .orElse(0.0);
    }

    // 业务方法：获取商品最高价格
    public Double getMaxPrice() {
        if (skus == null || skus.isEmpty()) {
            return 0.0;
        }
        return skus.stream()
                .mapToDouble(sku -> sku.getPrice().doubleValue())
                .max()
                .orElse(0.0);
    }

    // 业务方法：获取商品总库存
    public Integer getTotalStock() {
        if (skus == null || skus.isEmpty()) {
            return 0;
        }
        return skus.stream()
                .mapToInt(ProductSku::getStock)
                .sum();
    }

}
