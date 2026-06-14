package com.mes.repository;

import com.mes.entity.InventoryTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 库存流水数据访问层
 */
@Repository
public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, Long> {

    /**
     * 根据租户 ID 查询分页
     */
    Page<InventoryTransaction> findByTenantId(Long tenantId, Pageable pageable);

    /**
     * 根据产品 ID 查询
     */
    List<InventoryTransaction> findByProductIdAndTenantId(Long productId, Long tenantId);

    /**
     * 根据交易类型查询
     */
    List<InventoryTransaction> findByTransactionTypeAndTenantId(String transactionType, Long tenantId);

    /**
     * 根据时间范围查询
     */
    @Query("SELECT t FROM InventoryTransaction t WHERE t.tenantId = :tenantId AND " +
           "t.transactionTime BETWEEN :startTime AND :endTime")
    List<InventoryTransaction> findByTimeRange(@Param("tenantId") Long tenantId,
                                                @Param("startTime") LocalDateTime startTime,
                                                @Param("endTime") LocalDateTime endTime);

    /**
     * 根据关联单据查询
     */
    List<InventoryTransaction> findByReferenceNoAndTenantId(String referenceNo, Long tenantId);

    /**
     * 查询产品的所有交易记录
     */
    @Query("SELECT t FROM InventoryTransaction t WHERE t.productId = :productId AND t.tenantId = :tenantId ORDER BY t.transactionTime DESC")
    List<InventoryTransaction> findProductTransactions(@Param("productId") Long productId,
                                                        @Param("tenantId") Long tenantId);
}
