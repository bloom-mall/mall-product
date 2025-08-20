package io.bloom.mall.product.application.service;

import com.mybatisflex.core.paginate.Page;
import io.bloom.mall.product.application.dto.ProductDTO;
import io.bloom.mall.product.domain.entity.Product;
import io.bloom.mall.product.domain.entity.ProductCategory;
import io.bloom.mall.product.domain.service.ProductCategoryService;
import io.bloom.mall.product.domain.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品应用服务
 */
@Service
public class ProductAppService {

    private final ProductService productService;
    private final ProductCategoryService categoryService;

    @Autowired
    public ProductAppService(ProductService productService, ProductCategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    /**
     * 根据ID获取商品
     *
     * @param id 商品ID
     * @return 商品DTO
     */
    public ProductDTO getProductById(Long id) {
        Product product = productService.getProductById(id);
        if (product == null || product.getIsDeleted() == 1) {
            return null;
        }

        // 增加浏览量
        productService.incrementViewCount(id);

        // 获取分类名称
        ProductCategory category = categoryService.getCategoryById(product.getCategoryId());
        ProductDTO dto = ProductDTO.fromEntity(product);
        if (category != null) {
            dto.setCategoryName(category.getName());
        }

        return dto;
    }

    /**
     * 分页查询商品列表
     *
     * @param page       页码
     * @param pageSize   每页数量
     * @param categoryId 分类ID，可为null
     * @param keyword    关键词，可为null
     * @return 商品DTO分页列表
     */
    public Page<ProductDTO> getProductPage(int page, int pageSize, Long categoryId, String keyword) {
        Page<Product> productPage = productService.getProductPage(page, pageSize, categoryId, keyword);

        // 转换为DTO并设置分类名称
        List<ProductDTO> dtoList = productPage.getRecords().stream()
                .map(product -> {
                    ProductDTO dto = ProductDTO.fromEntity(product);
                    ProductCategory category = categoryService.getCategoryById(product.getCategoryId());
                    if (category != null) {
                        dto.setCategoryName(category.getName());
                    }
                    return dto;
                })
                .collect(Collectors.toList());

        Page<ProductDTO> dtoPage = new Page<>(productPage.getPageNumber(), productPage.getPageSize(), productPage.getTotalRow());
        dtoPage.setRecords(dtoList);

        return dtoPage;
    }

    /**
     * 创建商品
     *
     * @param productDTO 商品DTO
     * @return 创建后的商品DTO
     */
    @Transactional(rollbackFor = Exception.class)
    public ProductDTO createProduct(ProductDTO productDTO) {
        // 验证分类是否存在
        if (productDTO.getCategoryId() != null) {
            ProductCategory category = categoryService.getCategoryById(productDTO.getCategoryId());
            if (category == null) {
                throw new IllegalArgumentException("商品分类不存在");
            }
            productDTO.setCategoryName(category.getName());
        }

        // 转换为实体并创建
        Product product = ProductDTO.toEntity(productDTO);
        product = productService.createProduct(product);

        return ProductDTO.fromEntity(product);
    }

    /**
     * 更新商品
     *
     * @param productDTO 商品DTO
     * @return 更新后的商品DTO
     */
    @Transactional(rollbackFor = Exception.class)
    public ProductDTO updateProduct(ProductDTO productDTO) {
        // 验证商品是否存在
        Product existingProduct = productService.getProductById(productDTO.getId());
        if (existingProduct == null || existingProduct.getIsDeleted() == 1) {
            throw new IllegalArgumentException("商品不存在");
        }

        // 验证分类是否存在
        if (productDTO.getCategoryId() != null) {
            ProductCategory category = categoryService.getCategoryById(productDTO.getCategoryId());
            if (category == null) {
                throw new IllegalArgumentException("商品分类不存在");
            }
            productDTO.setCategoryName(category.getName());
        }

        // 转换为实体并更新
        Product product = ProductDTO.toEntity(productDTO);
        product = productService.updateProduct(product);

        return ProductDTO.fromEntity(product);
    }

    /**
     * 删除商品
     *
     * @param id 商品ID
     * @return 是否删除成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteProduct(Long id) {
        return productService.deleteProduct(id);
    }

    /**
     * 更新商品状态
     *
     * @param id     商品ID
     * @param status 状态：0-下架，1-上架，2-预售
     * @return 是否更新成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateProductStatus(Long id, Integer status) {
        return productService.updateProductStatus(id, status);
    }

    /**
     * 获取热销商品列表
     *
     * @param limit 数量限制
     * @return 热销商品DTO列表
     */
    public List<ProductDTO> getHotProducts(int limit) {
        List<Product> products = productService.getHotProducts(limit);

        return products.stream()
                .map(product -> {
                    ProductDTO dto = ProductDTO.fromEntity(product);
                    ProductCategory category = categoryService.getCategoryById(product.getCategoryId());
                    if (category != null) {
                        dto.setCategoryName(category.getName());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * 获取新品列表
     *
     * @param limit 数量限制
     * @return 新品DTO列表
     */
    public List<ProductDTO> getNewProducts(int limit) {
        List<Product> products = productService.getNewProducts(limit);

        return products.stream()
                .map(product -> {
                    ProductDTO dto = ProductDTO.fromEntity(product);
                    ProductCategory category = categoryService.getCategoryById(product.getCategoryId());
                    if (category != null) {
                        dto.setCategoryName(category.getName());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * 获取推荐商品列表
     *
     * @param limit 数量限制
     * @return 推荐商品DTO列表
     */
    public List<ProductDTO> getRecommendProducts(int limit) {
        List<Product> products = productService.getRecommendProducts(limit);

        return products.stream()
                .map(product -> {
                    ProductDTO dto = ProductDTO.fromEntity(product);
                    ProductCategory category = categoryService.getCategoryById(product.getCategoryId());
                    if (category != null) {
                        dto.setCategoryName(category.getName());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * 减少商品库存
     *
     * @param id    商品ID
     * @param count 减少数量
     * @return 是否减少成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean decrementStock(Long id, int count) {
        return productService.decrementStock(id, count);
    }
}