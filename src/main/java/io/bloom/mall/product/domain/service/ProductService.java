package io.bloom.mall.product.domain.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import io.bloom.mall.product.domain.entity.Product;
import io.bloom.mall.product.infrastructure.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

import static io.bloom.mall.product.domain.entity.table.ProductTableDef.PRODUCT;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product getProductById(Long id) {
        return productRepository.selectOneById(id);
    }

    public Page<Product> getProductPage(int page, int pageSize, Long categoryId, String keyword) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(PRODUCT.IS_DELETED.eq(0));

        // 添加分类条件
        if (categoryId != null && categoryId > 0) {
            queryWrapper.and(PRODUCT.CATEGORY_ID.eq(categoryId));
        }

        // 添加关键词条件
        if (StringUtils.hasText(keyword)) {
            queryWrapper.and(PRODUCT.NAME.like("%%" + keyword + "%%"));
        }

        // 按创建时间降序排序
        queryWrapper.orderBy(PRODUCT.CREATE_TIME.desc());

        return productRepository.paginate(page, pageSize, queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    public Product createProduct(Product product) {
        // 设置创建时间和更新时间
        LocalDateTime now = LocalDateTime.now();
        product.setCreateTime(now);
        product.setUpdateTime(now);

        // 设置默认值
        if (product.getStatus() == null) {
            product.setStatus(0); // 默认下架
        }
        if (product.getIsHot() == null) {
            product.setIsHot(0); // 默认非热销
        }
        if (product.getIsNew() == null) {
            product.setIsNew(1); // 默认为新品
        }
        if (product.getIsRecommend() == null) {
            product.setIsRecommend(0); // 默认不推荐
        }
        product.setIsDeleted(0); // 默认未删除

        productRepository.insert(product);
        return product;
    }

    @Transactional(rollbackFor = Exception.class)
    public Product updateProduct(Product product) {
        // 设置更新时间
        product.setUpdateTime(LocalDateTime.now());

        productRepository.update(product);
        return product;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteProduct(Long id) {
        // 逻辑删除
        Product product = new Product();
        product.setId(id);
        product.setIsDeleted(1);
        product.setUpdateTime(LocalDateTime.now());

        return productRepository.update(product) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean updateProductStatus(Long id, Integer status) {
        Product product = new Product();
        product.setId(id);
        product.setStatus(status);
        product.setUpdateTime(LocalDateTime.now());

        return productRepository.update(product) > 0;
    }

    public List<Product> getHotProducts(int limit) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(PRODUCT.IS_DELETED.eq(0))
                .and(PRODUCT.STATUS.eq(1)) // 上架状态
                .and(PRODUCT.IS_HOT.eq(1)) // 热销商品
                .orderBy(PRODUCT.CREATE_TIME.desc()) // 按创建时间降序
                .limit(limit);

        return productRepository.selectListByQuery(queryWrapper);
    }

    public List<Product> getNewProducts(int limit) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(PRODUCT.IS_DELETED.eq(0))
                .and(PRODUCT.STATUS.eq(1)) // 上架状态
                .and(PRODUCT.IS_NEW.eq(1)) // 新品
                .orderBy(PRODUCT.CREATE_TIME.desc()) // 按创建时间降序
                .limit(limit);

        return productRepository.selectListByQuery(queryWrapper);
    }

    public List<Product> getRecommendProducts(int limit) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(PRODUCT.IS_DELETED.eq(0))
                .and(PRODUCT.STATUS.eq(1)) // 上架状态
                .and(PRODUCT.IS_RECOMMEND.eq(1)) // 推荐商品
                .orderBy(PRODUCT.CREATE_TIME.desc()) // 按创建时间降序
                .limit(limit);

        return productRepository.selectListByQuery(queryWrapper);
    }

}