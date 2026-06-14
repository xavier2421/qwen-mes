package com.mes.repository;

import com.mes.entity.ProductionProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 工序数据访问层
 */
@Repository
public interface ProductionProcessRepository extends JpaRepository<ProductionProcess, Long> {

    List<ProductionProcess> findByTenantId(Long tenantId);

    List<ProductionProcess> findByWorkOrderIdAndTenantId(Long workOrderId, Long tenantId);

    List<ProductionProcess> findByStatusAndTenantId(String status, Long tenantId);

    @Query("SELECT p FROM ProductionProcess p WHERE p.tenantId = :tenantId AND " +
           "(:orderNo IS NULL OR p.orderNo LIKE %:orderNo%) AND " +
           "(:processName IS NULL OR p.processName LIKE %:processName%)")
    List<ProductionProcess> search(@Param("tenantId") Long tenantId,
                                   @Param("orderNo") String orderNo,
                                   @Param("processName") String processName);

    List<ProductionProcess> findByWorkOrderIdOrderBySequence(Long workOrderId);
}
