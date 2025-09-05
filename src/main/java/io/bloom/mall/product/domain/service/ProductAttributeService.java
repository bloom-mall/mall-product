package io.bloom.mall.product.domain.service;

import com.mybatisflex.core.query.QueryWrapper;
import io.bloom.mall.product.domain.entity.ProductAttribute;
import io.bloom.mall.product.infrastructure.repository.ProductAttributeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static io.bloom.mall.product.domain.entity.table.ProductAttributeTableDef.PRODUCT_ATTRIBUTE;

@Service
public class ProductAttributeService {

    private final ProductAttributeRepository productAttributeRepository;

    @Autowired
    public ProductAttributeService(ProductAttributeRepository productAttributeRepository) {
        this.productAttributeRepository = productAttributeRepository;
    }

    /**
     * 根据ID获取属性
     *
     * @param id 属性ID
     * @return 属性
     */
    public ProductAttribute getAttributeById(Long id) {
        return productAttributeRepository.selectOneById(id);
    }

    /**
     * 根据分组ID获取属性列表
     *
     * @param groupId 分组ID
     * @return 属性列表
     */
    public List<ProductAttribute> getAttributesByGroupId(Long groupId) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(PRODUCT_ATTRIBUTE.GROUP_ID.eq(groupId))
                .orderBy(PRODUCT_ATTRIBUTE.SORT.asc());
        return productAttributeRepository.selectListByQuery(queryWrapper);
    }

    /**
     * 根据商品ID获取属性列表
     *
     * @param productId 商品ID
     * @return 属性列表
     */
    public List<ProductAttribute> getAttributesByProductId(Long productId) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(PRODUCT_ATTRIBUTE.PRODUCT_ID.eq(productId))
                .orderBy(PRODUCT_ATTRIBUTE.GROUP_ID.asc())
                .orderBy(PRODUCT_ATTRIBUTE.SORT.asc());
        return productAttributeRepository.selectListByQuery(queryWrapper);
    }

    /**
     * 创建属性
     *
     * @param attribute 属性信息
     * @return 创建后的属性
     */
    @Transactional(rollbackFor = Exception.class)
    public ProductAttribute createAttribute(ProductAttribute attribute) {
        // 设置创建时间和更新时间
        LocalDateTime now = LocalDateTime.now();
        attribute.setCreateTime(now);
        attribute.setUpdateTime(now);

        // 设置默认排序
        if (attribute.getSort() == null) {
            attribute.setSort(0);
        }

        productAttributeRepository.insert(attribute);
        return attribute;
    }

    /**
     * 批量创建属性
     *
     * @param attributes 属性列表
     * @return 创建的属性数量
     */
    @Transactional(rollbackFor = Exception.class)
    public int createAttributes(List<ProductAttribute> attributes) {
        // 设置创建时间和更新时间
        LocalDateTime now = LocalDateTime.now();
        attributes.forEach(attribute -> {
            attribute.setCreateTime(now);
            attribute.setUpdateTime(now);
            if (attribute.getSort() == null) {
                attribute.setSort(0);
            }
        });

        return productAttributeRepository.insertBatch(attributes);
    }

    /**
     * 更新属性
     *
     * @param attribute 属性信息
     * @return 更新后的属性
     */
    @Transactional(rollbackFor = Exception.class)
    public ProductAttribute updateAttribute(ProductAttribute attribute) {
        // 设置更新时间
        attribute.setUpdateTime(LocalDateTime.now());

        productAttributeRepository.update(attribute);
        return attribute;
    }

    /**
     * 删除属性
     *
     * @param id 属性ID
     * @return 是否删除成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAttribute(Long id) {
        return productAttributeRepository.deleteById(id) > 0;
    }

    /**
     * 根据分组ID删除属性
     *
     * @param groupId 分组ID
     * @return 删除的属性数量
     */
    @Transactional(rollbackFor = Exception.class)
    public int deleteAttributesByGroupId(Long groupId) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(PRODUCT_ATTRIBUTE.GROUP_ID.eq(groupId));
        return productAttributeRepository.deleteByQuery(queryWrapper);
    }

    /**
     * 根据商品ID删除属性
     *
     * @param productId 商品ID
     * @return 删除的属性数量
     */
    @Transactional(rollbackFor = Exception.class)
    public int deleteAttributesByProductId(Long productId) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(PRODUCT_ATTRIBUTE.PRODUCT_ID.eq(productId));
        return productAttributeRepository.deleteByQuery(queryWrapper);
    }
}