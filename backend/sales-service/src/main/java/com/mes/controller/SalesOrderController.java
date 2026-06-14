package com.mes.controller;

import com.mes.dto.ApiResponse;
import com.mes.dto.SalesOrderDTO;
import com.mes.service.SalesOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 销售订单控制器
 */
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class SalesOrderController {

    private final SalesOrderService salesOrderService;

    @PostMapping
    public ApiResponse<SalesOrderDTO> createSalesOrder(@Valid @RequestBody SalesOrderDTO salesOrderDTO) {
        SalesOrderDTO created = salesOrderService.createSalesOrder(salesOrderDTO);
        return ApiResponse.success("订单创建成功", created);
    }

    @PutMapping("/{id}")
    public ApiResponse<SalesOrderDTO> updateSalesOrder(@PathVariable Long id, @Valid @RequestBody SalesOrderDTO salesOrderDTO) {
        SalesOrderDTO updated = salesOrderService.updateSalesOrder(id, salesOrderDTO);
        return ApiResponse.success("订单更新成功", updated);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteSalesOrder(@PathVariable Long id) {
        salesOrderService.deleteSalesOrder(id);
        return ApiResponse.success("订单删除成功", null);
    }

    @GetMapping("/{id}")
    public ApiResponse<SalesOrderDTO> getSalesOrderById(@PathVariable Long id) {
        SalesOrderDTO order = salesOrderService.getSalesOrderById(id);
        return ApiResponse.success(order);
    }

    @GetMapping
    public ApiResponse<List<SalesOrderDTO>> getAllSalesOrders(@RequestParam Long tenantId) {
        List<SalesOrderDTO> orders = salesOrderService.getAllSalesOrders(tenantId);
        return ApiResponse.success(orders);
    }

    @GetMapping("/customer/{customerId}")
    public ApiResponse<List<SalesOrderDTO>> getOrdersByCustomer(
            @RequestParam Long tenantId,
            @PathVariable Long customerId) {
        List<SalesOrderDTO> orders = salesOrderService.getOrdersByCustomer(tenantId, customerId);
        return ApiResponse.success(orders);
    }

    @PutMapping("/{id}/status")
    public ApiResponse<SalesOrderDTO> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        SalesOrderDTO order = salesOrderService.updateOrderStatus(id, status);
        return ApiResponse.success("订单状态更新成功", order);
    }

    @GetMapping("/{id}/progress")
    public ApiResponse<SalesOrderDTO> getOrderProgress(@PathVariable Long id) {
        SalesOrderDTO order = salesOrderService.getOrderProgress(id);
        return ApiResponse.success(order);
    }
}
