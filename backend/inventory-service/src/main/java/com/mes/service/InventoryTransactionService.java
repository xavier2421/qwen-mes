package com.mes.service;

import com.mes.dto.InventoryTransactionDTO;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 库存流水服务接口
 */
public interface InventoryTransactionService {

    /**
     * 创建库存交易记录
     */
    InventoryTransactionDTO createTransaction(InventoryTransactionDTO dto, Long tenantId);

    /**
     * 根据 ID 查询交易记录
     */
    InventoryTransactionDTO getTransactionById(Long id, Long tenantId);

    /**
     * 分页查询交易记录
     */
    Page<InventoryTransactionDTO> getTransactions(Long tenantId, int page, int size);

    /**
     * 根据产品 ID 查询交易记录
     */
    List<InventoryTransactionDTO> getProductTransactions(Long productId, Long tenantId);

    /**
     * 根据时间范围查询
     */
    List<InventoryTransactionDTO> getTransactionsByTimeRange(Long tenantId, 
                                                              LocalDateTime startTime, 
                                                              LocalDateTime endTime);

    /**
     * 入库操作
     */
    InventoryTransactionDTO inbound(Long productId, Integer quantity, 
                                     String referenceType, String referenceNo,
                                     Long warehouseId, Long locationId,
                                     java.math.BigDecimal unitPrice,
                                     String remark, Long operatorId, 
                                     String operatorName, Long tenantId);

    /**
     * 出库操作
     */
    InventoryTransactionDTO outbound(Long productId, Integer quantity,
                                      String referenceType, String referenceNo,
                                      Long warehouseId, Long locationId,
                                      java.math.BigDecimal unitPrice,
                                      String remark, Long operatorId,
                                      String operatorName, Long tenantId);

    /**
     * 库存调整
     */
    InventoryTransactionDTO adjustStock(Long productId, Integer quantity,
                                         String reason, Long tenantId);
}
