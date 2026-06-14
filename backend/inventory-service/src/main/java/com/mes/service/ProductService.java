package com.mes.service;

import com.mes.dto.ProductDTO;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 产品服务接口
 */
public interface ProductService {

    /**
     * 创建产品
     */
    ProductDTO createProduct(ProductDTO productDTO, Long tenantId);

    /**
     * 更新产品
     */
    ProductDTO updateProduct(Long id, ProductDTO productDTO, Long tenantId);

    /**
     * 删除产品
     */
    void deleteProduct(Long id, Long tenantId);

    /**
     * 根据 ID 查询产品
     */
    ProductDTO getProductById(Long id, Long tenantId);

    /**
     * 分页查询产品列表
     */
    Page<ProductDTO> getProducts(Long tenantId, int page, int size);

    /**
     * 搜索产品
     */
    Page<ProductDTO> searchProducts(Long tenantId, String keyword, int page, int size);

    /**
     * 根据分类查询产品
     */
    List<ProductDTO> getProductsByCategory(String category, Long tenantId);

    /**
     * 查询库存低于安全库存的产品
     */
    List<ProductDTO> getLowStockProducts(Long tenantId);

    /**
     * 调整库存数量
     */
    ProductDTO adjustStock(Long productId, Integer quantity, Long tenantId);

    /**
     * 启用/禁用产品
     */
    ProductDTO toggleProductStatus(Long id, Long tenantId);
}
