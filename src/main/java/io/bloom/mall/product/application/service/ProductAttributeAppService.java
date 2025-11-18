package io.bloom.mall.product.application.service;

import io.bloom.mall.product.application.dto.ProductAttributeDTO;
import io.bloom.mall.product.application.dto.ProductDTO;
import io.bloom.mall.product.domain.entity.ProductAttribute;
import io.bloom.mall.product.domain.service.ProductAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductAttributeAppService {

    private final ProductAttributeService productAttributeService;
    private final ProductAppService productAppService;

    @Autowired
    public ProductAttributeAppService(ProductAttributeService productAttributeService, ProductAppService productAppService) {
        this.productAttributeService = productAttributeService;
        this.productAppService = productAppService;
    }

    /**
     * 根据ID获取属性
     *
     * @param id 属性ID
     * @return 属性DTO
     */
    public ProductAttributeDTO getAttributeById(Long id) {
        ProductAttribute attribute = productAttributeService.getAttributeById(id);
        if (attribute == null) {
            return null;
        }

        ProductAttributeDTO dto = ProductAttributeDTO.fromEntity(attribute);
        // 设置商品名称
        setProductInfo(dto);

        return dto;
    }

    /**
     * 根据分组ID获取属性列表
     *
     * @param groupId 分组ID
     * @return 属性DTO列表
     */
    public List<ProductAttributeDTO> getAttributesByGroupId(Long groupId) {
        if (groupId == null) {
            return Collections.emptyList();
        }

        List<ProductAttribute> attributes = productAttributeService.getAttributesByGroupId(groupId);
        return attributes.stream()
                .map(attribute -> {
                    ProductAttributeDTO dto = ProductAttributeDTO.fromEntity(attribute);
                    // 设置商品名称
                    setProductInfo(dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * 根据商品ID获取属性列表
     *
     * @param productId 商品ID
     * @return 属性DTO列表
     */
    public List<ProductAttributeDTO> getAttributesByProductId(Long productId) {
        if (productId == null) {
            return Collections.emptyList();
        }

        List<ProductAttribute> attributes = productAttributeService.getAttributesByProductId(productId);
        return attributes.stream()
                .map(attribute -> {
                    ProductAttributeDTO dto = ProductAttributeDTO.fromEntity(attribute);
                    // 设置商品名称
                    setProductInfo(dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * 创建属性
     *
     * @param attributeDTO 属性DTO
     * @return 创建后的属性DTO
     */
    public ProductAttributeDTO createAttribute(ProductAttributeDTO attributeDTO) {
        if (attributeDTO == null) {
            throw new IllegalArgumentException("属性信息不能为空");
        }

        // 验证商品是否存在
        if (attributeDTO.getProductId() != null) {
            ProductDTO product = productAppService.getProductById(attributeDTO.getProductId());
            if (product == null) {
                throw new IllegalArgumentException("商品不存在");
            }
        }

        ProductAttribute attribute = ProductAttributeDTO.toEntity(attributeDTO);
        ProductAttribute createdAttribute = productAttributeService.createAttribute(attribute);

        ProductAttributeDTO dto = ProductAttributeDTO.fromEntity(createdAttribute);
        // 设置商品名称
        setProductInfo(dto);

        return dto;
    }

    /**
     * 批量创建属性
     *
     * @param attributeDTOs 属性DTO列表
     * @return 创建的属性DTO列表
     */
    public List<ProductAttributeDTO> createAttributes(List<ProductAttributeDTO> attributeDTOs) {
        if (attributeDTOs == null || attributeDTOs.isEmpty()) {
            return Collections.emptyList();
        }

        // 验证商品是否存在
        attributeDTOs.forEach(dto -> {
            if (dto.getProductId() != null) {
                ProductDTO product = productAppService.getProductById(dto.getProductId());
                if (product == null) {
                    throw new IllegalArgumentException("商品不存在");
                }
            }
        });

        List<ProductAttribute> attributes = attributeDTOs.stream()
                .map(ProductAttributeDTO::toEntity)
                .collect(Collectors.toList());

        productAttributeService.createAttributes(attributes);

        return attributes.stream()
                .map(attribute -> {
                    ProductAttributeDTO dto = ProductAttributeDTO.fromEntity(attribute);
                    // 设置商品名称
                    setProductInfo(dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * 更新属性
     *
     * @param attributeDTO 属性DTO
     * @return 更新后的属性DTO
     */
    public ProductAttributeDTO updateAttribute(ProductAttributeDTO attributeDTO) {
        if (attributeDTO == null || attributeDTO.getId() == null) {
            throw new IllegalArgumentException("属性ID不能为空");
        }

        // 验证属性是否存在
        ProductAttribute existingAttribute = productAttributeService.getAttributeById(attributeDTO.getId());
        if (existingAttribute == null) {
            throw new IllegalArgumentException("属性不存在");
        }

        // 验证商品是否存在
        if (attributeDTO.getProductId() != null && !attributeDTO.getProductId().equals(existingAttribute.getProductId())) {
            ProductDTO product = productAppService.getProductById(attributeDTO.getProductId());
            if (product == null) {
                throw new IllegalArgumentException("商品不存在");
            }
        }

        ProductAttribute attribute = ProductAttributeDTO.toEntity(attributeDTO);
        // 保留原来的创建时间
        attribute.setCreateTime(existingAttribute.getCreateTime());

        ProductAttribute updatedAttribute = productAttributeService.updateAttribute(attribute);

        ProductAttributeDTO dto = ProductAttributeDTO.fromEntity(updatedAttribute);
        // 设置商品名称
        setProductInfo(dto);

        return dto;
    }

    /**
     * 删除属性
     *
     * @param id 属性ID
     * @return 是否删除成功
     */
    public boolean deleteAttribute(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("属性ID不能为空");
        }

        // 验证属性是否存在
        ProductAttribute existingAttribute = productAttributeService.getAttributeById(id);
        if (existingAttribute == null) {
            throw new IllegalArgumentException("属性不存在");
        }

        return productAttributeService.deleteAttribute(id);
    }

    /**
     * 根据分组ID删除属性
     *
     * @param groupId 分组ID
     * @return 删除的属性数量
     */
    public int deleteAttributesByGroupId(Long groupId) {
        if (groupId == null) {
            throw new IllegalArgumentException("分组ID不能为空");
        }

        return productAttributeService.deleteAttributesByGroupId(groupId);
    }

    /**
     * 根据商品ID删除属性
     *
     * @param productId 商品ID
     * @return 删除的属性数量
     */
    public int deleteAttributesByProductId(Long productId) {
        if (productId == null) {
            throw new IllegalArgumentException("商品ID不能为空");
        }

        // 验证商品是否存在
        ProductDTO product = productAppService.getProductById(productId);
        if (product == null) {
            throw new IllegalArgumentException("商品不存在");
        }

        return productAttributeService.deleteAttributesByProductId(productId);
    }

    /**
     * 设置商品信息
     *
     * @param dto 属性DTO
     */
    private void setProductInfo(ProductAttributeDTO dto) {
        if (dto.getProductId() != null) {
            ProductDTO productDTO = productAppService.getProductById(dto.getProductId());
            if (productDTO != null) {
                dto.setProductName(productDTO.getName());
            }
        }
    }
}