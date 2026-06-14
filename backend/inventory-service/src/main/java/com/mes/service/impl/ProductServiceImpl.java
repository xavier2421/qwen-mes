package com.mes.service.impl;

import com.mes.dto.ProductDTO;
import com.mes.entity.Product;
import com.mes.repository.ProductRepository;
import com.mes.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 产品服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO, Long tenantId) {
        // 检查产品编码是否已存在
        if (productRepository.existsByProductCodeAndTenantId(productDTO.getProductCode(), tenantId)) {
            throw new RuntimeException("产品编码已存在：" + productDTO.getProductCode());
        }

        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        product.setTenantId(tenantId);
        product.setStockQuantity(productDTO.getStockQuantity() != null ? productDTO.getStockQuantity() : 0);
        product.setStatus(productDTO.getStatus() != null ? productDTO.getStatus() : 1);

        Product saved = productRepository.save(product);
        log.info("创建产品成功：{}", saved.getProductCode());
        
        ProductDTO result = new ProductDTO();
        BeanUtils.copyProperties(saved, result);
        return result;
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO productDTO, Long tenantId) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("产品不存在：" + id));

        if (!product.getTenantId().equals(tenantId)) {
            throw new RuntimeException("无权访问该产品");
        }

        // 如果修改了产品编码，检查新编码是否已存在
        if (!product.getProductCode().equals(productDTO.getProductCode()) &&
            productRepository.existsByProductCodeAndTenantId(productDTO.getProductCode(), tenantId)) {
            throw new RuntimeException("产品编码已存在：" + productDTO.getProductCode());
        }

        BeanUtils.copyProperties(productDTO, product, "id", "tenantId", "createdAt", "updatedAt");
        
        Product updated = productRepository.save(product);
        log.info("更新产品成功：{}", updated.getProductCode());

        ProductDTO result = new ProductDTO();
        BeanUtils.copyProperties(updated, result);
        return result;
    }

    @Override
    @Transactional
    public void deleteProduct(Long id, Long tenantId) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("产品不存在：" + id));

        if (!product.getTenantId().equals(tenantId)) {
            throw new RuntimeException("无权删除该产品");
        }

        productRepository.delete(product);
        log.info("删除产品成功：{}", product.getProductCode());
    }

    @Override
    public ProductDTO getProductById(Long id, Long tenantId) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("产品不存在：" + id));

        if (!product.getTenantId().equals(tenantId)) {
            throw new RuntimeException("无权访问该产品");
        }

        ProductDTO result = new ProductDTO();
        BeanUtils.copyProperties(product, result);
        return result;
    }

    @Override
    public Page<ProductDTO> getProducts(Long tenantId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return productRepository.findByTenantId(tenantId, pageRequest)
                .map(this::convertToDTO);
    }

    @Override
    public Page<ProductDTO> searchProducts(Long tenantId, String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return productRepository.searchProducts(tenantId, keyword, pageRequest)
                .map(this::convertToDTO);
    }

    @Override
    public List<ProductDTO> getProductsByCategory(String category, Long tenantId) {
        return productRepository.findByCategoryAndTenantId(category, tenantId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getLowStockProducts(Long tenantId) {
        return productRepository.findLowStockProducts(tenantId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductDTO adjustStock(Long productId, Integer quantity, Long tenantId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("产品不存在：" + productId));

        if (!product.getTenantId().equals(tenantId)) {
            throw new RuntimeException("无权操作该产品");
        }

        int newQuantity = product.getStockQuantity() + quantity;
        if (newQuantity < 0) {
            throw new RuntimeException("库存数量不能为负数");
        }

        product.setStockQuantity(newQuantity);
        Product updated = productRepository.save(product);
        log.info("调整库存成功：{}, 新库存：{}", updated.getProductCode(), updated.getStockQuantity());

        ProductDTO result = new ProductDTO();
        BeanUtils.copyProperties(updated, result);
        return result;
    }

    @Override
    @Transactional
    public ProductDTO toggleProductStatus(Long id, Long tenantId) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("产品不存在：" + id));

        if (!product.getTenantId().equals(tenantId)) {
            throw new RuntimeException("无权操作该产品");
        }

        product.setStatus(product.getStatus() == 1 ? 0 : 1);
        Product updated = productRepository.save(product);
        log.info("切换产品状态成功：{}, 新状态：{}", updated.getProductCode(), updated.getStatus());

        ProductDTO result = new ProductDTO();
        BeanUtils.copyProperties(updated, result);
        return result;
    }

    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        BeanUtils.copyProperties(product, dto);
        return dto;
    }
}
