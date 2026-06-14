package com.mes.controller;

import com.mes.dto.ApiResponse;
import com.mes.dto.ProductionPlanCreateRequest;
import com.mes.dto.ProductionPlanDTO;
import com.mes.service.ProductionPlanService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 生产计划控制器
 */
@RestController
@RequestMapping("/api/production-plans")
public class ProductionPlanController {

    private final ProductionPlanService productionPlanService;

    public ProductionPlanController(ProductionPlanService productionPlanService) {
        this.productionPlanService = productionPlanService;
    }

    @PostMapping
    public ApiResponse<ProductionPlanDTO> create(@RequestBody ProductionPlanCreateRequest request,
                                                  @RequestHeader("X-Tenant-Id") Long tenantId) {
        ProductionPlanDTO dto = productionPlanService.createPlan(request, tenantId);
        return ApiResponse.success(dto);
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductionPlanDTO> update(@PathVariable Long id,
                                                  @RequestBody ProductionPlanCreateRequest request,
                                                  @RequestHeader("X-Tenant-Id") Long tenantId) {
        ProductionPlanDTO dto = productionPlanService.updatePlan(id, request, tenantId);
        return ApiResponse.success(dto);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id,
                                    @RequestHeader("X-Tenant-Id") Long tenantId) {
        productionPlanService.deletePlan(id, tenantId);
        return ApiResponse.success(null);
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductionPlanDTO> getById(@PathVariable Long id,
                                                   @RequestHeader("X-Tenant-Id") Long tenantId) {
        ProductionPlanDTO dto = productionPlanService.getPlanById(id, tenantId);
        return ApiResponse.success(dto);
    }

    @GetMapping
    public ApiResponse<List<ProductionPlanDTO>> getAll(@RequestHeader("X-Tenant-Id") Long tenantId) {
        List<ProductionPlanDTO> list = productionPlanService.getAllPlans(tenantId);
        return ApiResponse.success(list);
    }

    @GetMapping("/search")
    public ApiResponse<List<ProductionPlanDTO>> search(
            @RequestHeader("X-Tenant-Id") Long tenantId,
            @RequestParam(required = false) String planNo,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate) {
        List<ProductionPlanDTO> list = productionPlanService.searchPlans(
                tenantId, planNo, productName, status, startDate, endDate);
        return ApiResponse.success(list);
    }

    @PostMapping("/{id}/release")
    public ApiResponse<ProductionPlanDTO> release(@PathVariable Long id,
                                                   @RequestHeader("X-Tenant-Id") Long tenantId) {
        ProductionPlanDTO dto = productionPlanService.releasePlan(id, tenantId);
        return ApiResponse.success(dto);
    }

    @PostMapping("/{id}/start")
    public ApiResponse<ProductionPlanDTO> start(@PathVariable Long id,
                                                 @RequestHeader("X-Tenant-Id") Long tenantId) {
        ProductionPlanDTO dto = productionPlanService.startPlan(id, tenantId);
        return ApiResponse.success(dto);
    }

    @PostMapping("/{id}/complete")
    public ApiResponse<ProductionPlanDTO> complete(@PathVariable Long id,
                                                    @RequestHeader("X-Tenant-Id") Long tenantId) {
        ProductionPlanDTO dto = productionPlanService.completePlan(id, tenantId);
        return ApiResponse.success(dto);
    }

    @PostMapping("/{id}/cancel")
    public ApiResponse<ProductionPlanDTO> cancel(@PathVariable Long id,
                                                  @RequestHeader("X-Tenant-Id") Long tenantId) {
        ProductionPlanDTO dto = productionPlanService.cancelPlan(id, tenantId);
        return ApiResponse.success(dto);
    }
}
