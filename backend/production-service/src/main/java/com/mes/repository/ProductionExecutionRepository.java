package com.mes.repository;

import com.mes.entity.ProductionExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 生产执行记录数据访问层
 */
@Repository
public interface ProductionExecutionRepository extends JpaRepository<ProductionExecution, Long> {

    List<ProductionExecution> findByTenantId(Long tenantId);

    List<ProductionExecution> findByWorkOrderIdAndTenantId(Long workOrderId, Long tenantId);

    List<ProductionExecution> findByOperationTypeAndTenantId(String operationType, Long tenantId);

    @Query("SELECT e FROM ProductionExecution e WHERE e.tenantId = :tenantId AND " +
           "(:orderNo IS NULL OR e.orderNo LIKE %:orderNo%) AND " +
           "(:operationType IS NULL OR e.operationType = :operationType) AND " +
           "(:startTime IS NULL OR e.executionTime >= :startTime) AND " +
           "(:endTime IS NULL OR e.executionTime <= :endTime)")
    List<ProductionExecution> search(@Param("tenantId") Long tenantId,
                                     @Param("orderNo") String orderNo,
                                     @Param("operationType") String operationType,
                                     @Param("startTime") LocalDateTime startTime,
                                     @Param("endTime") LocalDateTime endTime);

    List<ProductionExecution> findByExecutionTimeBetweenAndTenantId(LocalDateTime start, LocalDateTime end, Long tenantId);
}
