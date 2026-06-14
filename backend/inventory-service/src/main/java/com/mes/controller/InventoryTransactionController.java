package com.mes.controller;

import com.mes.dto.ApiResponse;
import com.mes.dto.InventoryTransactionDTO;
import com.mes.service.InventoryTransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 库存流水控制器
 */
@RestController
@RequestMapping("/api/inventory/transactions")
@RequiredArgsConstructor
public class InventoryTransactionController {

    private final InventoryTransactionService transactionService;

    /**
     * 创建库存交易记录
     */
    @PostMapping
    public ApiResponse<InventoryTransactionDTO> create(@Valid @RequestBody InventoryTransactionDTO dto,
                                                        @RequestHeader("X-Tenant-Id") Long tenantId) {
        InventoryTransactionDTO result = transactionService.createTransaction(dto, tenantId);
        return ApiResponse.success("交易记录创建成功", result);
    }

    /**
     * 根据 ID 查询交易记录
     */
    @GetMapping("/{id}")
    public ApiResponse<InventoryTransactionDTO> getById(@PathVariable Long id,
                                                         @RequestHeader("X-Tenant-Id") Long tenantId) {
        InventoryTransactionDTO result = transactionService.getTransactionById(id, tenantId);
        return ApiResponse.success(result);
    }

    /**
     * 分页查询交易记录
     */
    @GetMapping
    public ApiResponse<Page<InventoryTransactionDTO>> list(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "20") int size,
                                                            @RequestHeader("X-Tenant-Id") Long tenantId) {
        Page<InventoryTransactionDTO> result = transactionService.getTransactions(tenantId, page, size);
        return ApiResponse.success(result);
    }

    /**
     * 查询产品的交易记录
     */
    @GetMapping("/product/{productId}")
    public ApiResponse<List<InventoryTransactionDTO>> getByProduct(@PathVariable Long productId,
                                                                    @RequestHeader("X-Tenant-Id") Long tenantId) {
        List<InventoryTransactionDTO> result = transactionService.getProductTransactions(productId, tenantId);
        return ApiResponse.success(result);
    }

    /**
     * 按时间范围查询交易记录
     */
    @GetMapping("/time-range")
    public ApiResponse<List<InventoryTransactionDTO>> getByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestHeader("X-Tenant-Id") Long tenantId) {
        List<InventoryTransactionDTO> result = transactionService.getTransactionsByTimeRange(tenantId, startTime, endTime);
        return ApiResponse.success(result);
    }

    /**
     * 入库操作
     */
    @PostMapping("/inbound")
    public ApiResponse<InventoryTransactionDTO> inbound(
            @RequestParam Long productId,
            @RequestParam Integer quantity,
            @RequestParam(required = false) String referenceType,
            @RequestParam(required = false) String referenceNo,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) Long locationId,
            @RequestParam(required = false) BigDecimal unitPrice,
            @RequestParam(required = false) String remark,
            @RequestParam(required = false) Long operatorId,
            @RequestParam(required = false) String operatorName,
            @RequestHeader("X-Tenant-Id") Long tenantId) {
        
        InventoryTransactionDTO result = transactionService.inbound(
                productId, quantity, referenceType, referenceNo,
                warehouseId, locationId, unitPrice, remark,
                operatorId, operatorName, tenantId);
        return ApiResponse.success("入库操作成功", result);
    }

    /**
     * 出库操作
     */
    @PostMapping("/outbound")
    public ApiResponse<InventoryTransactionDTO> outbound(
            @RequestParam Long productId,
            @RequestParam Integer quantity,
            @RequestParam(required = false) String referenceType,
            @RequestParam(required = false) String referenceNo,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) Long locationId,
            @RequestParam(required = false) BigDecimal unitPrice,
            @RequestParam(required = false) String remark,
            @RequestParam(required = false) Long operatorId,
            @RequestParam(required = false) String operatorName,
            @RequestHeader("X-Tenant-Id") Long tenantId) {
        
        InventoryTransactionDTO result = transactionService.outbound(
                productId, quantity, referenceType, referenceNo,
                warehouseId, locationId, unitPrice, remark,
                operatorId, operatorName, tenantId);
        return ApiResponse.success("出库操作成功", result);
    }

    /**
     * 库存调整
     */
    @PostMapping("/adjust")
    public ApiResponse<InventoryTransactionDTO> adjust(
            @RequestParam Long productId,
            @RequestParam Integer quantity,
            @RequestParam(required = false) String reason,
            @RequestHeader("X-Tenant-Id") Long tenantId) {
        
        InventoryTransactionDTO result = transactionService.adjustStock(productId, quantity, reason, tenantId);
        return ApiResponse.success("库存调整成功", result);
    }
}
