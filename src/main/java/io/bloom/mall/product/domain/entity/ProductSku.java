package io.bloom.mall.product.domain.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.bloom.mall.product.domain.entity.Product;
import io.bloom.mall.product.domain.entity.ProductAttribute;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import java.io.Serial;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("product_sku")
public class ProductSku implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * SKU ID
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * SKU名称
     */
    private String name;

    /**
     * 商品属性列表，存储格式为逗号分隔的属性ID
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
     * 创建人
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;

    // 关联属性
    private Product product;
    private List<ProductAttribute> attributes;

    // 业务方法：获取属性ID列表
    public List<Long> getAttributeIdList() {
        if (attributeIds == null || attributeIds.isEmpty()) {
            return List.of();
        }
        return Arrays.stream(attributeIds.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

    // 业务方法：设置属性ID列表
    public void setAttributeIdList(List<Long> attributeIdList) {
        if (attributeIdList == null || attributeIdList.isEmpty()) {
            this.attributeIds = "";
        } else {
            this.attributeIds = attributeIdList.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
        }
    }

    // 业务方法：减少库存
    public void decreaseStock(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("减少的库存数量必须大于0");
        }
        if (this.stock < quantity) {
            throw new IllegalArgumentException("库存不足");
        }
        this.stock -= quantity;
    }

    // 业务方法：增加库存
    public void increaseStock(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("增加的库存数量必须大于0");
        }
        this.stock += quantity;
    }
}