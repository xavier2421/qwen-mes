package com.mes.repository;

import com.mes.entity.WorkOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 工单数据访问层
 */
@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {

    List<WorkOrder> findByTenantId(Long tenantId);

    List<WorkOrder> findByProductionPlanIdAndTenantId(Long productionPlanId, Long tenantId);

    WorkOrder findByOrderNoAndTenantId(String orderNo, Long tenantId);

    List<WorkOrder> findByStatusAndTenantId(String status, Long tenantId);

    @Query("SELECT w FROM WorkOrder w WHERE w.tenantId = :tenantId AND " +
           "(:orderNo IS NULL OR w.orderNo LIKE %:orderNo%) AND " +
           "(:productName IS NULL OR w.productName LIKE %:productName%) AND " +
           "(:status IS NULL OR w.status = :status)")
    List<WorkOrder> search(@Param("tenantId") Long tenantId,
                          @Param("orderNo") String orderNo,
                          @Param("productName") String productName,
                          @Param("status") String status);

    List<WorkOrder> findByPlannedStartTimeBetweenAndTenantId(LocalDateTime start, LocalDateTime end, Long tenantId);
}
