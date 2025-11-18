package io.bloom.mall.product.domain.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.bloom.mall.product.domain.entity.Product;
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
@Table("product_category")
public class ProductCategory implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 分类ID
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 父分类ID，0表示一级分类
     */
    private Long parentId;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类层级：1-一级分类，2-二级分类，3-三级分类
     */
    private Integer level;

    /**
     * 排序权重，数值越小越靠前
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
    private ProductCategory parent;
    private List<ProductCategory> children;
    private List<Product> products;

    // 业务方法：是否为根分类
    public boolean isRootCategory() {
        return parentId == null || parentId == 0;
    }

    // 业务方法：是否为叶子分类
    public boolean isLeafCategory() {
        return children == null || children.isEmpty();
    }

}
