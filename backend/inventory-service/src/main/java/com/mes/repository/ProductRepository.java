package com.mes.repository;

import com.mes.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 产品数据访问层
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * 根据租户 ID 查询产品列表
     */
    Page<Product> findByTenantId(Long tenantId, Pageable pageable);

    /**
     * 根据产品编码查询
     */
    Optional<Product> findByProductCodeAndTenantId(String productCode, Long tenantId);

    /**
     * 根据产品分类查询
     */
    List<Product> findByCategoryAndTenantId(String category, Long tenantId);

    /**
     * 根据状态查询
     */
    List<Product> findByStatusAndTenantId(Integer status, Long tenantId);

    /**
     * 搜索产品（支持产品编码、名称模糊匹配）
     */
    @Query("SELECT p FROM Product p WHERE p.tenantId = :tenantId AND " +
           "(p.productCode LIKE %:keyword% OR p.productName LIKE %:keyword%)")
    Page<Product> searchProducts(@Param("tenantId") Long tenantId, 
                                  @Param("keyword") String keyword, 
                                  Pageable pageable);

    /**
     * 查询库存低于安全库存的产品
     */
    @Query("SELECT p FROM Product p WHERE p.tenantId = :tenantId AND p.stockQuantity <= p.safetyStock")
    List<Product> findLowStockProducts(@Param("tenantId") Long tenantId);

    /**
     * 检查产品编码是否存在
     */
    boolean existsByProductCodeAndTenantId(String productCode, Long tenantId);
}
