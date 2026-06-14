package com.mes.service;

import com.mes.dto.SalesOrderDTO;
import java.util.List;

/**
 * 销售订单服务接口
 */
public interface SalesOrderService {

    /**
     * 创建销售订单
     */
    SalesOrderDTO createSalesOrder(SalesOrderDTO salesOrderDTO);

    /**
     * 更新销售订单
     */
    SalesOrderDTO updateSalesOrder(Long id, SalesOrderDTO salesOrderDTO);

    /**
     * 删除销售订单
     */
    void deleteSalesOrder(Long id);

    /**
     * 获取订单详情
     */
    SalesOrderDTO getSalesOrderById(Long id);

    /**
     * 获取订单列表
     */
    List<SalesOrderDTO> getAllSalesOrders(Long tenantId);

    /**
     * 根据客户 ID 获取订单列表
     */
    List<SalesOrderDTO> getOrdersByCustomer(Long tenantId, Long customerId);

    /**
     * 更新订单状态
     */
    SalesOrderDTO updateOrderStatus(Long id, Integer status);

    /**
     * 获取订单进度
     */
    SalesOrderDTO getOrderProgress(Long id);
}
