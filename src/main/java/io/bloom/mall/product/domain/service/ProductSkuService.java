package io.bloom.mall.product.domain.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.query.QueryMethods;
import io.bloom.mall.product.domain.entity.ProductSku;
import io.bloom.mall.product.infrastructure.repository.ProductSkuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static io.bloom.mall.product.domain.entity.table.ProductSkuTableDef.PRODUCT_SKU;

@Service
public class ProductSkuService {

    private final ProductSkuRepository productSkuRepository;

    @Autowired
    public ProductSkuService(ProductSkuRepository productSkuRepository) {
        this.productSkuRepository = productSkuRepository;
    }

    /**
     * 根据ID获取SKU
     *
     * @param id SKU ID
     * @return SKU
     */
    public ProductSku getSkuById(Long id) {
        return productSkuRepository.selectOneById(id);
    }

    /**
     * 根据商品ID获取SKU列表
     *
     * @param productId 商品ID
     * @return SKU列表
     */
    public List<ProductSku> getSkusByProductId(Long productId) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(PRODUCT_SKU.PRODUCT_ID.eq(productId))
                .orderBy(PRODUCT_SKU.CREATE_TIME.asc());
        return productSkuRepository.selectListByQuery(queryWrapper);
    }

    /**
     * 获取上架状态的SKU列表
     *
     * @param productId 商品ID
     * @return 上架状态的SKU列表
     */
    public List<ProductSku> getActiveSkusByProductId(Long productId) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(PRODUCT_SKU.PRODUCT_ID.eq(productId))
                .and(PRODUCT_SKU.STATUS.eq(1))
                .orderBy(PRODUCT_SKU.CREATE_TIME.asc());
        return productSkuRepository.selectListByQuery(queryWrapper);
    }

    /**
     * 创建SKU
     *
     * @param sku SKU信息
     * @return 创建后的SKU
     */
    @Transactional(rollbackFor = Exception.class)
    public ProductSku createSku(ProductSku sku) {
        // 设置创建时间和更新时间
        LocalDateTime now = LocalDateTime.now();
        sku.setCreateTime(now);
        sku.setUpdateTime(now);

        // 设置默认值
        if (sku.getStatus() == null) {
            sku.setStatus(1); // 默认上架
        }
        if (sku.getStock() == null) {
            sku.setStock(0); // 默认库存为0
        }

        productSkuRepository.insert(sku);
        return sku;
    }

    /**
     * 批量创建SKU
     *
     * @param skus SKU列表
     * @return 创建的SKU数量
     */
    @Transactional(rollbackFor = Exception.class)
    public int createSkus(List<ProductSku> skus) {
        // 设置创建时间和更新时间
        LocalDateTime now = LocalDateTime.now();
        skus.forEach(sku -> {
            sku.setCreateTime(now);
            sku.setUpdateTime(now);
            if (sku.getStatus() == null) {
                sku.setStatus(1);
            }
            if (sku.getStock() == null) {
                sku.setStock(0);
            }
        });

        return productSkuRepository.insertBatch(skus);
    }

    /**
     * 更新SKU
     *
     * @param sku SKU信息
     * @return 更新后的SKU
     */
    @Transactional(rollbackFor = Exception.class)
    public ProductSku updateSku(ProductSku sku) {
        // 设置更新时间
        sku.setUpdateTime(LocalDateTime.now());

        productSkuRepository.update(sku);
        return sku;
    }

    /**
     * 删除SKU
     *
     * @param id SKU ID
     * @return 是否删除成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteSku(Long id) {
        return productSkuRepository.deleteById(id) > 0;
    }

    /**
     * 更新SKU状态
     *
     * @param id     SKU ID
     * @param status 状态：0-下架，1-上架，2-预售
     * @return 是否更新成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSkuStatus(Long id, Integer status) {
        ProductSku sku = new ProductSku();
        sku.setId(id);
        sku.setStatus(status);
        sku.setUpdateTime(LocalDateTime.now());

        return productSkuRepository.update(sku) > 0;
    }

    /**
     * 更新SKU库存
     *
     * @param id    SKU ID
     * @param stock 库存数量
     * @return 是否更新成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSkuStock(Long id, Integer stock) {
        ProductSku sku = new ProductSku();
        sku.setId(id);
        sku.setStock(stock);
        sku.setUpdateTime(LocalDateTime.now());

        return productSkuRepository.update(sku) > 0;
    }

    /**
     * 减少SKU库存
     *
     * @param id       SKU ID
     * @param quantity 减少的数量
     * @return 是否减少成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean decreaseSkuStock(Long id, Integer quantity) {
        // 先获取当前库存
        ProductSku sku = productSkuRepository.selectOneById(id);
        if (sku == null) {
            return false;
        }

        // 检查库存是否足够
        if (sku.getStock() < quantity) {
            return false;
        }

        // 更新库存
        int newStock = sku.getStock() - quantity;
        sku.setStock(newStock);
        sku.setUpdateTime(LocalDateTime.now());

        return productSkuRepository.update(sku) > 0;
    }

    /**
     * 增加SKU库存
     *
     * @param id       SKU ID
     * @param quantity 增加的数量
     * @return 是否增加成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean increaseSkuStock(Long id, Integer quantity) {
        // 先获取当前库存
        ProductSku sku = productSkuRepository.selectOneById(id);
        if (sku == null) {
            return false;
        }

        // 更新库存
        int newStock = sku.getStock() + quantity;
        sku.setStock(newStock);
        sku.setUpdateTime(LocalDateTime.now());

        return productSkuRepository.update(sku) > 0;
    }

    /**
     * 获取商品的最低价格
     *
     * @param productId 商品ID
     * @return 最低价格
     */
    public Double getMinPriceByProductId(Long productId) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select(QueryMethods.min(PRODUCT_SKU.PRICE))
                .where(PRODUCT_SKU.PRODUCT_ID.eq(productId))
                .and(PRODUCT_SKU.STATUS.eq(1));
        return productSkuRepository.selectObjectByQueryAs(queryWrapper, Double.class);
    }

    /**
     * 获取商品的最高价格
     *
     * @param productId 商品ID
     * @return 最高价格
     */
    public Double getMaxPriceByProductId(Long productId) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select(QueryMethods.max(PRODUCT_SKU.PRICE))
                .where(PRODUCT_SKU.PRODUCT_ID.eq(productId))
                .and(PRODUCT_SKU.STATUS.eq(1));
        return productSkuRepository.selectObjectByQueryAs(queryWrapper, Double.class);
    }
}