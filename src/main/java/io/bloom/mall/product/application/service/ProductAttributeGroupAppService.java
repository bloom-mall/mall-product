package io.bloom.mall.product.application.service;

import io.bloom.mall.product.application.dto.ProductAttributeDTO;
import io.bloom.mall.product.application.dto.ProductAttributeGroupDTO;
import io.bloom.mall.product.application.dto.ProductDTO;
import io.bloom.mall.product.domain.entity.ProductAttribute;
import io.bloom.mall.product.domain.entity.ProductAttributeGroup;
import io.bloom.mall.product.domain.service.ProductAttributeGroupService;
import io.bloom.mall.product.domain.service.ProductAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductAttributeGroupAppService {

    private final ProductAttributeGroupService productAttributeGroupService;
    private final ProductAttributeService productAttributeService;
    private final ProductAppService productAppService;

    @Autowired
    public ProductAttributeGroupAppService(ProductAttributeGroupService productAttributeGroupService, 
                                          ProductAttributeService productAttributeService, 
                                          ProductAppService productAppService) {
        this.productAttributeGroupService = productAttributeGroupService;
        this.productAttributeService = productAttributeService;
        this.productAppService = productAppService;
    }

    /**
     * 根据ID获取属性分组
     *
     * @param id 分组ID
     * @return 属性分组DTO
     */
    public ProductAttributeGroupDTO getAttributeGroupById(Long id) {
        ProductAttributeGroup group = productAttributeGroupService.getAttributeGroupById(id);
        if (group == null) {
            return null;
        }

        ProductAttributeGroupDTO dto = ProductAttributeGroupDTO.fromEntity(group);
        // 设置商品名称
        setProductInfo(dto);
        // 设置属性列表
        setAttributes(dto);

        return dto;
    }

    /**
     * 根据商品ID获取属性分组列表
     *
     * @param productId 商品ID
     * @return 属性分组DTO列表
     */
    public List<ProductAttributeGroupDTO> getAttributeGroupsByProductId(Long productId) {
        if (productId == null) {
            return Collections.emptyList();
        }

        List<ProductAttributeGroup> groups = productAttributeGroupService.getAttributeGroupsByProductId(productId);
        return groups.stream()
                .map(group -> {
                    ProductAttributeGroupDTO dto = ProductAttributeGroupDTO.fromEntity(group);
                    // 设置商品名称
                    setProductInfo(dto);
                    // 设置属性列表
                    setAttributes(dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * 创建属性分组
     *
     * @param groupDTO 属性分组DTO
     * @return 创建后的属性分组DTO
     */
    public ProductAttributeGroupDTO createAttributeGroup(ProductAttributeGroupDTO groupDTO) {
        if (groupDTO == null) {
            throw new IllegalArgumentException("属性分组信息不能为空");
        }

        // 验证商品是否存在
        if (groupDTO.getProductId() != null) {
            ProductDTO product = productAppService.getProductById(groupDTO.getProductId());
            if (product == null) {
                throw new IllegalArgumentException("商品不存在");
            }
        }

        ProductAttributeGroup group = ProductAttributeGroupDTO.toEntity(groupDTO);
        ProductAttributeGroup createdGroup = productAttributeGroupService.createAttributeGroup(group);

        ProductAttributeGroupDTO dto = ProductAttributeGroupDTO.fromEntity(createdGroup);
        // 设置商品名称
        setProductInfo(dto);

        return dto;
    }

    /**
     * 批量创建属性分组
     *
     * @param groupDTOs 属性分组DTO列表
     * @return 创建的属性分组DTO列表
     */
    public List<ProductAttributeGroupDTO> createAttributeGroups(List<ProductAttributeGroupDTO> groupDTOs) {
        if (groupDTOs == null || groupDTOs.isEmpty()) {
            return Collections.emptyList();
        }

        // 验证商品是否存在
        groupDTOs.forEach(dto -> {
            if (dto.getProductId() != null) {
                ProductDTO product = productAppService.getProductById(dto.getProductId());
                if (product == null) {
                    throw new IllegalArgumentException("商品不存在");
                }
            }
        });

        List<ProductAttributeGroup> groups = groupDTOs.stream()
                .map(ProductAttributeGroupDTO::toEntity)
                .collect(Collectors.toList());

        productAttributeGroupService.createAttributeGroups(groups);

        return groups.stream()
                .map(group -> {
                    ProductAttributeGroupDTO dto = ProductAttributeGroupDTO.fromEntity(group);
                    // 设置商品名称
                    setProductInfo(dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * 更新属性分组
     *
     * @param groupDTO 属性分组DTO
     * @return 更新后的属性分组DTO
     */
    public ProductAttributeGroupDTO updateAttributeGroup(ProductAttributeGroupDTO groupDTO) {
        if (groupDTO == null || groupDTO.getId() == null) {
            throw new IllegalArgumentException("属性分组ID不能为空");
        }

        // 验证属性分组是否存在
        ProductAttributeGroup existingGroup = productAttributeGroupService.getAttributeGroupById(groupDTO.getId());
        if (existingGroup == null) {
            throw new IllegalArgumentException("属性分组不存在");
        }

        // 验证商品是否存在
        if (groupDTO.getProductId() != null && !groupDTO.getProductId().equals(existingGroup.getProductId())) {
            ProductDTO product = productAppService.getProductById(groupDTO.getProductId());
            if (product == null) {
                throw new IllegalArgumentException("商品不存在");
            }
        }

        ProductAttributeGroup group = ProductAttributeGroupDTO.toEntity(groupDTO);
        // 保留原来的创建时间
        group.setCreateTime(existingGroup.getCreateTime());

        ProductAttributeGroup updatedGroup = productAttributeGroupService.updateAttributeGroup(group);

        ProductAttributeGroupDTO dto = ProductAttributeGroupDTO.fromEntity(updatedGroup);
        // 设置商品名称
        setProductInfo(dto);
        // 设置属性列表
        setAttributes(dto);

        return dto;
    }

    /**
     * 删除属性分组
     *
     * @param id 分组ID
     * @return 是否删除成功
     */
    public boolean deleteAttributeGroup(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("属性分组ID不能为空");
        }

        // 验证属性分组是否存在
        ProductAttributeGroup existingGroup = productAttributeGroupService.getAttributeGroupById(id);
        if (existingGroup == null) {
            throw new IllegalArgumentException("属性分组不存在");
        }

        // 先删除该分组下的所有属性
        productAttributeService.deleteAttributesByGroupId(id);
        
        // 再删除分组
        return productAttributeGroupService.deleteAttributeGroup(id);
    }

    /**
     * 根据商品ID删除属性分组
     *
     * @param productId 商品ID
     * @return 删除的属性分组数量
     */
    public int deleteAttributeGroupsByProductId(Long productId) {
        if (productId == null) {
            throw new IllegalArgumentException("商品ID不能为空");
        }

        // 验证商品是否存在
        ProductDTO product = productAppService.getProductById(productId);
        if (product == null) {
            throw new IllegalArgumentException("商品不存在");
        }

        // 先删除该商品下的所有属性
        productAttributeService.deleteAttributesByProductId(productId);
        
        // 再删除该商品下的所有属性分组
        return productAttributeGroupService.deleteAttributeGroupsByProductId(productId);
    }

    /**
     * 设置商品信息
     *
     * @param dto 属性分组DTO
     */
    private void setProductInfo(ProductAttributeGroupDTO dto) {
        if (dto.getProductId() != null) {
            ProductDTO productDTO = productAppService.getProductById(dto.getProductId());
            if (productDTO != null) {
                dto.setProductName(productDTO.getName());
            }
        }
    }

    /**
     * 设置属性列表
     *
     * @param dto 属性分组DTO
     */
    private void setAttributes(ProductAttributeGroupDTO dto) {
        List<ProductAttribute> attributes = productAttributeService.getAttributesByGroupId(dto.getId());
        List<ProductAttributeDTO> attributeDTOs = attributes.stream()
                .map(attribute -> {
                    ProductAttributeDTO attributeDTO = ProductAttributeDTO.fromEntity(attribute);
                    attributeDTO.setGroupName(dto.getName());
                    return attributeDTO;
                })
                .collect(Collectors.toList());
        dto.setAttributes(attributeDTOs);
    }
}