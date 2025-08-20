package io.bloom.mall.product.domain.service;

import io.bloom.mall.product.domain.entity.Product;
import io.bloom.mall.product.infrastructure.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateProduct() {
        // 准备测试数据
        Product product = new Product();
        product.setName("测试商品");
        product.setCategoryId(1L);
        product.setPrice(BigDecimal.valueOf(100.0));
        product.setStock(100);

        // 模拟Mapper行为
        when(productRepository.insert(any(Product.class))).thenReturn(1);

        // 执行测试
        Product result = productService.createProduct(product);

        // 验证结果
        assertNotNull(result);
        assertEquals("测试商品", result.getName());
        assertEquals(1L, result.getCategoryId());
        assertEquals(BigDecimal.valueOf(100.0), result.getPrice());
        assertEquals(100, result.getStock());
        assertNotNull(result.getCreateTime());
        assertNotNull(result.getUpdateTime());
        assertEquals(0, result.getIsDeleted());
    }

    @Test
    public void testUpdateProduct() {
        // 准备测试数据
        Product product = new Product();
        product.setId(1L);
        product.setName("更新后的商品");
        product.setPrice(BigDecimal.valueOf(200.0));

        // 模拟Mapper行为
        when(productRepository.update(any(Product.class))).thenReturn(1);

        // 执行测试
        Product result = productService.updateProduct(product);

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("更新后的商品", result.getName());
        assertEquals(BigDecimal.valueOf(200.0), result.getPrice());
        assertNotNull(result.getUpdateTime());
    }

    @Test
    public void testDeleteProduct() {
        // 模拟Mapper行为
        when(productRepository.update(any(Product.class))).thenReturn(1);

        // 执行测试
        boolean result = productService.deleteProduct(1L);

        // 验证结果
        assertTrue(result);
    }

    @Test
    public void testUpdateProductStatus() {
        // 模拟Mapper行为
        when(productRepository.update(any(Product.class))).thenReturn(1);

        // 执行测试
        boolean result = productService.updateProductStatus(1L, 1);

        // 验证结果
        assertTrue(result);
    }
}