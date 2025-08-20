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
        if (product.getSalesCount() == null) {
            product.setSalesCount(0); // 默认销量为0
        }
        if (product.getViewCount() == null) {
            product.setViewCount(0); // 默认浏览量为0
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
                .orderBy(PRODUCT.SALES_COUNT.desc()) // 按销量降序
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

    @Transactional(rollbackFor = Exception.class)
    public boolean incrementViewCount(Long id) {
        // 先查询当前浏览量
        Product product = productRepository.selectOneById(id);
        if (product == null || product.getIsDeleted() == 1) {
            return false;
        }

        // 更新浏览量
        Product updateProduct = new Product();
        updateProduct.setId(id);
        updateProduct.setViewCount(product.getViewCount() + 1);
        updateProduct.setUpdateTime(LocalDateTime.now());

        return productRepository.update(updateProduct) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean incrementSalesCount(Long id, int count) {
        // 先查询当前销量
        Product product = productRepository.selectOneById(id);
        if (product == null || product.getIsDeleted() == 1) {
            return false;
        }

        // 更新销量
        Product updateProduct = new Product();
        updateProduct.setId(id);
        updateProduct.setSalesCount(product.getSalesCount() + count);
        updateProduct.setUpdateTime(LocalDateTime.now());

        return productRepository.update(updateProduct) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean decrementStock(Long id, int count) {
        // 先查询当前库存
        Product product = productRepository.selectOneById(id);
        if (product == null || product.getIsDeleted() == 1) {
            return false;
        }

        // 检查库存是否足够
        if (product.getStock() < count) {
            return false;
        }

        // 更新库存
        Product updateProduct = new Product();
        updateProduct.setId(id);
        updateProduct.setStock(product.getStock() - count);
        updateProduct.setUpdateTime(LocalDateTime.now());

        return productRepository.update(updateProduct) > 0;
    }
}