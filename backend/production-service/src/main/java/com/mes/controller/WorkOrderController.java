package com.mes.controller;

import com.mes.dto.ApiResponse;
import com.mes.dto.WorkOrderCreateRequest;
import com.mes.dto.WorkOrderDTO;
import com.mes.service.WorkOrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 工单控制器
 */
@RestController
@RequestMapping("/api/work-orders")
public class WorkOrderController {

    private final WorkOrderService workOrderService;

    public WorkOrderController(WorkOrderService workOrderService) {
        this.workOrderService = workOrderService;
    }

    @PostMapping
    public ApiResponse<WorkOrderDTO> create(@RequestBody WorkOrderCreateRequest request,
                                             @RequestHeader("X-Tenant-Id") Long tenantId) {
        WorkOrderDTO dto = workOrderService.createWorkOrder(request, tenantId);
        return ApiResponse.success(dto);
    }

    @PutMapping("/{id}")
    public ApiResponse<WorkOrderDTO> update(@PathVariable Long id,
                                             @RequestBody WorkOrderCreateRequest request,
                                             @RequestHeader("X-Tenant-Id") Long tenantId) {
        WorkOrderDTO dto = workOrderService.updateWorkOrder(id, request, tenantId);
        return ApiResponse.success(dto);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id,
                                    @RequestHeader("X-Tenant-Id") Long tenantId) {
        workOrderService.deleteWorkOrder(id, tenantId);
        return ApiResponse.success(null);
    }

    @GetMapping("/{id}")
    public ApiResponse<WorkOrderDTO> getById(@PathVariable Long id,
                                              @RequestHeader("X-Tenant-Id") Long tenantId) {
        WorkOrderDTO dto = workOrderService.getWorkOrderById(id, tenantId);
        return ApiResponse.success(dto);
    }

    @GetMapping
    public ApiResponse<List<WorkOrderDTO>> getAll(@RequestHeader("X-Tenant-Id") Long tenantId) {
        List<WorkOrderDTO> list = workOrderService.getAllWorkOrders(tenantId);
        return ApiResponse.success(list);
    }

    @GetMapping("/by-plan/{planId}")
    public ApiResponse<List<WorkOrderDTO>> getByPlanId(@PathVariable Long planId,
                                                        @RequestHeader("X-Tenant-Id") Long tenantId) {
        List<WorkOrderDTO> list = workOrderService.getWorkOrdersByPlanId(planId, tenantId);
        return ApiResponse.success(list);
    }

    @GetMapping("/search")
    public ApiResponse<List<WorkOrderDTO>> search(
            @RequestHeader("X-Tenant-Id") Long tenantId,
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String status) {
        List<WorkOrderDTO> list = workOrderService.searchWorkOrders(tenantId, orderNo, productName, status);
        return ApiResponse.success(list);
    }

    @PostMapping("/{id}/release")
    public ApiResponse<WorkOrderDTO> release(@PathVariable Long id,
                                              @RequestHeader("X-Tenant-Id") Long tenantId) {
        WorkOrderDTO dto = workOrderService.releaseWorkOrder(id, tenantId);
        return ApiResponse.success(dto);
    }

    @PostMapping("/{id}/start")
    public ApiResponse<WorkOrderDTO> start(@PathVariable Long id,
                                            @RequestHeader("X-Tenant-Id") Long tenantId) {
        WorkOrderDTO dto = workOrderService.startWorkOrder(id, tenantId);
        return ApiResponse.success(dto);
    }

    @PostMapping("/{id}/pause")
    public ApiResponse<WorkOrderDTO> pause(@PathVariable Long id,
                                            @RequestHeader("X-Tenant-Id") Long tenantId) {
        WorkOrderDTO dto = workOrderService.pauseWorkOrder(id, tenantId);
        return ApiResponse.success(dto);
    }

    @PostMapping("/{id}/resume")
    public ApiResponse<WorkOrderDTO> resume(@PathVariable Long id,
                                             @RequestHeader("X-Tenant-Id") Long tenantId) {
        WorkOrderDTO dto = workOrderService.resumeWorkOrder(id, tenantId);
        return ApiResponse.success(dto);
    }

    @PostMapping("/{id}/complete")
    public ApiResponse<WorkOrderDTO> complete(@PathVariable Long id,
                                               @RequestHeader("X-Tenant-Id") Long tenantId) {
        WorkOrderDTO dto = workOrderService.completeWorkOrder(id, tenantId);
        return ApiResponse.success(dto);
    }

    @PostMapping("/{id}/cancel")
    public ApiResponse<WorkOrderDTO> cancel(@PathVariable Long id,
                                             @RequestHeader("X-Tenant-Id") Long tenantId) {
        WorkOrderDTO dto = workOrderService.cancelWorkOrder(id, tenantId);
        return ApiResponse.success(dto);
    }

    @PostMapping("/{id}/report")
    public ApiResponse<WorkOrderDTO> reportProduction(
            @PathVariable Long id,
            @RequestParam Integer quantity,
            @RequestParam(required = false) Integer defectiveQuantity,
            @RequestParam(required = false) String defectReason,
            @RequestParam(required = false) String operator,
            @RequestHeader("X-Tenant-Id") Long tenantId) {
        WorkOrderDTO dto = workOrderService.reportProduction(
                id, quantity, defectiveQuantity, defectReason, operator, tenantId);
        return ApiResponse.success(dto);
    }
}
