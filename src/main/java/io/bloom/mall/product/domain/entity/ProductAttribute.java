package io.bloom.mall.product.domain.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.bloom.mall.product.domain.entity.Product;
import io.bloom.mall.product.domain.entity.ProductAttributeGroup;
import java.io.Serializable;
import java.time.LocalDateTime;

import java.io.Serial;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("product_attribute")
public class ProductAttribute implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 属性ID
     */
    @Id(keyType = KeyType.Auto)
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
     * 商品ID
     */
    private Long productId;

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
     * 创建人
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;

    // 关联属性
    private Product product;
    private ProductAttributeGroup attributeGroup;
}