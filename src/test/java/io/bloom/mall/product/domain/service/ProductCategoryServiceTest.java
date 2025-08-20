package io.bloom.mall.product.domain.service;

import io.bloom.mall.product.domain.entity.ProductCategory;
import io.bloom.mall.product.infrastructure.repository.ProductCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ProductCategoryServiceTest {

    @Mock
    private ProductCategoryRepository productCategoryMapper;

    @InjectMocks
    private ProductCategoryService productCategoryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllCategories() {
        // 准备测试数据
        ProductCategory category1 = new ProductCategory();
        category1.setId(1L);
        category1.setName("分类1");

        ProductCategory category2 = new ProductCategory();
        category2.setId(2L);
        category2.setName("分类2");

        List<ProductCategory> categories = Arrays.asList(category1, category2);

        // 设置模拟行为
        when(productCategoryMapper.selectAll()).thenReturn(categories);

        // 执行测试
        List<ProductCategory> result = productCategoryService.getAllCategories();

        // 验证结果
        assertEquals(2, result.size());
        assertEquals("分类1", result.get(0).getName());
        assertEquals("分类2", result.get(1).getName());
    }

    @Test
    public void testCreateCategory() {
        // 准备测试数据
        ProductCategory category = new ProductCategory();
        category.setName("测试分类");
        category.setParentId(0L);

        // 设置模拟行为
        when(productCategoryMapper.insert(any(ProductCategory.class))).thenReturn(1);

        // 执行测试
        ProductCategory result = productCategoryService.createCategory(category);

        // 验证结果
        assertNotNull(result);
        assertEquals("测试分类", result.getName());
        assertEquals(0L, result.getParentId());
        assertEquals(1, result.getLevel());
        assertNotNull(result.getCreateTime());
        assertNotNull(result.getUpdateTime());
    }
}