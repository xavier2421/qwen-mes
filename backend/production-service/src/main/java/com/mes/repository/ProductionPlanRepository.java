package com.mes.repository;

import com.mes.entity.ProductionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 生产计划数据访问层
 */
@Repository
public interface ProductionPlanRepository extends JpaRepository<ProductionPlan, Long> {

    List<ProductionPlan> findByTenantId(Long tenantId);

    List<ProductionPlan> findByStatusAndTenantId(String status, Long tenantId);

    ProductionPlan findByPlanNoAndTenantId(String planNo, Long tenantId);

    @Query("SELECT p FROM ProductionPlan p WHERE p.tenantId = :tenantId AND " +
           "(:planNo IS NULL OR p.planNo LIKE %:planNo%) AND " +
           "(:productName IS NULL OR p.productName LIKE %:productName%) AND " +
           "(:status IS NULL OR p.status = :status) AND " +
           "(:startDate IS NULL OR p.plannedStartDate >= :startDate) AND " +
           "(:endDate IS NULL OR p.plannedEndDate <= :endDate)")
    List<ProductionPlan> search(@Param("tenantId") Long tenantId,
                                @Param("planNo") String planNo,
                                @Param("productName") String productName,
                                @Param("status") String status,
                                @Param("startDate") LocalDateTime startDate,
                                @Param("endDate") LocalDateTime endDate);

    List<ProductionPlan> findByPlannedStartDateBetweenAndTenantId(LocalDateTime start, LocalDateTime end, Long tenantId);
}
