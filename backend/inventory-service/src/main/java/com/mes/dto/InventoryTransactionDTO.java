package com.mes.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库存流水 DTO
 */
@Data
public class InventoryTransactionDTO {

    private Long id;

    /**
     * 产品 ID
     */
    @NotNull(message = "产品 ID 不能为空")
    private Long productId;

    /**
     * 交易类型：IN-入库，OUT-出库，ADJUST-调整
     */
    @NotBlank(message = "交易类型不能为空")
    @Pattern(regexp = "^(IN|OUT|ADJUST)$", message = "交易类型必须是 IN、OUT 或 ADJUST")
    private String transactionType;

    /**
     * 交易数量
     */
    @NotNull(message = "交易数量不能为空")
    private Integer quantity;

    /**
     * 交易前库存
     */
    private Integer beforeQuantity;

    /**
     * 交易后库存
     */
    private Integer afterQuantity;

    /**
     * 单价
     */
    private BigDecimal unitPrice;

    /**
     * 总金额
     */
    private BigDecimal totalAmount;

    /**
     * 关联单据类型
     */
    private String referenceType;

    /**
     * 关联单据编号
     */
    private String referenceNo;

    /**
     * 仓库 ID
     */
    private Long warehouseId;

    /**
     * 库位 ID
     */
    private Long locationId;

    /**
     * 交易时间
     */
    private LocalDateTime transactionTime;

    /**
     * 操作人 ID
     */
    private Long operatorId;

    /**
     * 操作人姓名
     */
    private String operatorName;

    /**
     * 备注
     */
    private String remark;

    private Long tenantId;
}
