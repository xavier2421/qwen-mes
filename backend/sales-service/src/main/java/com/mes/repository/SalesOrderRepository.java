package com.mes.repository;

import com.mes.entity.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 销售订单数据访问接口
 */
@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {

    /**
     * 根据租户 ID 查询订单列表
     */
    List<SalesOrder> findByTenantIdAndStatusOrderByCreatedAtDesc(Long tenantId, Integer status);

    /**
     * 根据客户 ID 查询订单列表
     */
    List<SalesOrder> findByTenantIdAndCustomerId(Long tenantId, Long customerId);

    /**
     * 根据订单号查询
     */
    SalesOrder findByOrderNo(String orderNo);

    /**
     * 检查订单号是否存在
     */
    boolean existsByOrderNo(String orderNo);
}
