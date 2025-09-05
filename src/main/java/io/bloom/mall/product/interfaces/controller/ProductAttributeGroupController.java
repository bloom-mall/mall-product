package io.bloom.mall.product.interfaces.controller;

import io.bloom.mall.product.application.dto.ProductAttributeGroupDTO;
import io.bloom.mall.product.application.service.ProductAttributeGroupAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-attribute-groups")
public class ProductAttributeGroupController {

    private final ProductAttributeGroupAppService productAttributeGroupAppService;

    @Autowired
    public ProductAttributeGroupController(ProductAttributeGroupAppService productAttributeGroupAppService) {
        this.productAttributeGroupAppService = productAttributeGroupAppService;
    }

    /**
     * 根据ID获取属性分组
     *
     * @param id 分组ID
     * @return 属性分组详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductAttributeGroupDTO> getAttributeGroupById(@PathVariable Long id) {
        ProductAttributeGroupDTO group = productAttributeGroupAppService.getAttributeGroupById(id);
        if (group == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(group);
    }

    /**
     * 根据商品ID获取属性分组列表
     *
     * @param productId 商品ID
     * @return 属性分组列表
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductAttributeGroupDTO>> getAttributeGroupsByProductId(@PathVariable Long productId) {
        List<ProductAttributeGroupDTO> groups = productAttributeGroupAppService.getAttributeGroupsByProductId(productId);
        return ResponseEntity.ok(groups);
    }

    /**
     * 创建属性分组
     *
     * @param groupDTO 属性分组信息
     * @return 创建后的属性分组
     */
    @PostMapping
    public ResponseEntity<ProductAttributeGroupDTO> createAttributeGroup(@RequestBody ProductAttributeGroupDTO groupDTO) {
        ProductAttributeGroupDTO createdGroup = productAttributeGroupAppService.createAttributeGroup(groupDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGroup);
    }

    /**
     * 批量创建属性分组
     *
     * @param groupDTOs 属性分组列表
     * @return 创建的属性分组列表
     */
    @PostMapping("/batch")
    public ResponseEntity<List<ProductAttributeGroupDTO>> createAttributeGroups(@RequestBody List<ProductAttributeGroupDTO> groupDTOs) {
        List<ProductAttributeGroupDTO> createdGroups = productAttributeGroupAppService.createAttributeGroups(groupDTOs);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGroups);
    }

    /**
     * 更新属性分组
     *
     * @param id        分组ID
     * @param groupDTO 更新后的属性分组信息
     * @return 更新后的属性分组
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductAttributeGroupDTO> updateAttributeGroup(@PathVariable Long id, @RequestBody ProductAttributeGroupDTO groupDTO) {
        groupDTO.setId(id);
        ProductAttributeGroupDTO updatedGroup = productAttributeGroupAppService.updateAttributeGroup(groupDTO);
        return ResponseEntity.ok(updatedGroup);
    }

    /**
     * 删除属性分组
     *
     * @param id 分组ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttributeGroup(@PathVariable Long id) {
        boolean deleted = productAttributeGroupAppService.deleteAttributeGroup(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 根据商品ID删除属性分组
     *
     * @param productId 商品ID
     * @return 删除结果
     */
    @DeleteMapping("/product/{productId}")
    public ResponseEntity<String> deleteAttributeGroupsByProductId(@PathVariable Long productId) {
        int count = productAttributeGroupAppService.deleteAttributeGroupsByProductId(productId);
        return ResponseEntity.ok("成功删除" + count + "个属性分组");
    }
}