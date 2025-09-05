package io.bloom.mall.product.interfaces.controller;

import io.bloom.mall.product.application.dto.ProductAttributeDTO;
import io.bloom.mall.product.application.service.ProductAttributeAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-attributes")
public class ProductAttributeController {

    private final ProductAttributeAppService productAttributeAppService;

    @Autowired
    public ProductAttributeController(ProductAttributeAppService productAttributeAppService) {
        this.productAttributeAppService = productAttributeAppService;
    }

    /**
     * 根据ID获取属性
     *
     * @param id 属性ID
     * @return 属性详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductAttributeDTO> getAttributeById(@PathVariable Long id) {
        ProductAttributeDTO attribute = productAttributeAppService.getAttributeById(id);
        if (attribute == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(attribute);
    }

    /**
     * 根据分组ID获取属性列表
     *
     * @param groupId 分组ID
     * @return 属性列表
     */
    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<ProductAttributeDTO>> getAttributesByGroupId(@PathVariable Long groupId) {
        List<ProductAttributeDTO> attributes = productAttributeAppService.getAttributesByGroupId(groupId);
        return ResponseEntity.ok(attributes);
    }

    /**
     * 根据商品ID获取属性列表
     *
     * @param productId 商品ID
     * @return 属性列表
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductAttributeDTO>> getAttributesByProductId(@PathVariable Long productId) {
        List<ProductAttributeDTO> attributes = productAttributeAppService.getAttributesByProductId(productId);
        return ResponseEntity.ok(attributes);
    }

    /**
     * 创建属性
     *
     * @param attributeDTO 属性信息
     * @return 创建后的属性
     */
    @PostMapping
    public ResponseEntity<ProductAttributeDTO> createAttribute(@RequestBody ProductAttributeDTO attributeDTO) {
        ProductAttributeDTO createdAttribute = productAttributeAppService.createAttribute(attributeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAttribute);
    }

    /**
     * 批量创建属性
     *
     * @param attributeDTOs 属性列表
     * @return 创建的属性列表
     */
    @PostMapping("/batch")
    public ResponseEntity<List<ProductAttributeDTO>> createAttributes(@RequestBody List<ProductAttributeDTO> attributeDTOs) {
        List<ProductAttributeDTO> createdAttributes = productAttributeAppService.createAttributes(attributeDTOs);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAttributes);
    }

    /**
     * 更新属性
     *
     * @param id          属性ID
     * @param attributeDTO 更新后的属性信息
     * @return 更新后的属性
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductAttributeDTO> updateAttribute(@PathVariable Long id, @RequestBody ProductAttributeDTO attributeDTO) {
        attributeDTO.setId(id);
        ProductAttributeDTO updatedAttribute = productAttributeAppService.updateAttribute(attributeDTO);
        return ResponseEntity.ok(updatedAttribute);
    }

    /**
     * 删除属性
     *
     * @param id 属性ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttribute(@PathVariable Long id) {
        boolean deleted = productAttributeAppService.deleteAttribute(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 根据分组ID删除属性
     *
     * @param groupId 分组ID
     * @return 删除结果
     */
    @DeleteMapping("/group/{groupId}")
    public ResponseEntity<String> deleteAttributesByGroupId(@PathVariable Long groupId) {
        int count = productAttributeAppService.deleteAttributesByGroupId(groupId);
        return ResponseEntity.ok("成功删除" + count + "个属性");
    }

    /**
     * 根据商品ID删除属性
     *
     * @param productId 商品ID
     * @return 删除结果
     */
    @DeleteMapping("/product/{productId}")
    public ResponseEntity<String> deleteAttributesByProductId(@PathVariable Long productId) {
        int count = productAttributeAppService.deleteAttributesByProductId(productId);
        return ResponseEntity.ok("成功删除" + count + "个属性");
    }
}