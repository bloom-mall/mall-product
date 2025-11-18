package io.bloom.mall.product.domain.service;

import com.mybatisflex.core.query.QueryWrapper;
import io.bloom.mall.product.domain.entity.ProductAttributeGroup;
import io.bloom.mall.product.infrastructure.repository.ProductAttributeGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static io.bloom.mall.product.domain.entity.table.ProductAttributeGroupTableDef.PRODUCT_ATTRIBUTE_GROUP;

@Service
public class ProductAttributeGroupService {

    private final ProductAttributeGroupRepository productAttributeGroupRepository;

    @Autowired
    public ProductAttributeGroupService(ProductAttributeGroupRepository productAttributeGroupRepository) {
        this.productAttributeGroupRepository = productAttributeGroupRepository;
    }

    /**
     * 根据ID获取属性分组
     *
     * @param id 分组ID
     * @return 属性分组
     */
    public ProductAttributeGroup getAttributeGroupById(Long id) {
        return productAttributeGroupRepository.selectOneById(id);
    }

    /**
     * 根据商品ID获取属性分组列表
     *
     * @param productId 商品ID
     * @return 属性分组列表
     */
    public List<ProductAttributeGroup> getAttributeGroupsByProductId(Long productId) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(PRODUCT_ATTRIBUTE_GROUP.PRODUCT_ID.eq(productId))
                .orderBy(PRODUCT_ATTRIBUTE_GROUP.SORT.asc());
        return productAttributeGroupRepository.selectListByQuery(queryWrapper);
    }

    /**
     * 创建属性分组
     *
     * @param group 属性分组信息
     * @return 创建后的属性分组
     */
    @Transactional(rollbackFor = Exception.class)
    public ProductAttributeGroup createAttributeGroup(ProductAttributeGroup group) {
        // 设置创建时间和更新时间
        LocalDateTime now = LocalDateTime.now();
        group.setCreateTime(now);
        group.setUpdateTime(now);

        // 设置默认排序
        if (group.getSort() == null) {
            group.setSort(0);
        }

        productAttributeGroupRepository.insert(group);
        return group;
    }

    /**
     * 批量创建属性分组
     *
     * @param groups 属性分组列表
     * @return 创建的属性分组数量
     */
    @Transactional(rollbackFor = Exception.class)
    public int createAttributeGroups(List<ProductAttributeGroup> groups) {
        // 设置创建时间和更新时间
        LocalDateTime now = LocalDateTime.now();
        groups.forEach(group -> {
            group.setCreateTime(now);
            group.setUpdateTime(now);
            if (group.getSort() == null) {
                group.setSort(0);
            }
        });

        return productAttributeGroupRepository.insertBatch(groups);
    }

    /**
     * 更新属性分组
     *
     * @param group 属性分组信息
     * @return 更新后的属性分组
     */
    @Transactional(rollbackFor = Exception.class)
    public ProductAttributeGroup updateAttributeGroup(ProductAttributeGroup group) {
        // 设置更新时间
        group.setUpdateTime(LocalDateTime.now());

        productAttributeGroupRepository.update(group);
        return group;
    }

    /**
     * 删除属性分组
     *
     * @param id 分组ID
     * @return 是否删除成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAttributeGroup(Long id) {
        return productAttributeGroupRepository.deleteById(id) > 0;
    }

    /**
     * 根据商品ID删除属性分组
     *
     * @param productId 商品ID
     * @return 删除的属性分组数量
     */
    @Transactional(rollbackFor = Exception.class)
    public int deleteAttributeGroupsByProductId(Long productId) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(PRODUCT_ATTRIBUTE_GROUP.PRODUCT_ID.eq(productId));
        return productAttributeGroupRepository.deleteByQuery(queryWrapper);
    }
}