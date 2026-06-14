package com.mes.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库存流水实体类
 */
@Data
@Entity
@Table(name = "inventory_transactions")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class InventoryTransaction extends BaseEntity {

    /**
     * 产品 ID
     */
    @Column(name = "product_id", nullable = false)
    @NotNull(message = "产品 ID 不能为空")
    private Long productId;

    /**
     * 交易类型：IN-入库，OUT-出库，ADJUST-调整
     */
    @Column(name = "transaction_type", nullable = false, length = 20)
    @NotBlank(message = "交易类型不能为空")
    @Pattern(regexp = "^(IN|OUT|ADJUST)$", message = "交易类型必须是 IN、OUT 或 ADJUST")
    private String transactionType;

    /**
     * 交易数量
     */
    @Column(name = "quantity", nullable = false)
    @NotNull(message = "交易数量不能为空")
    private Integer quantity;

    /**
     * 交易前库存
     */
    @Column(name = "before_quantity", nullable = false)
    private Integer beforeQuantity;

    /**
     * 交易后库存
     */
    @Column(name = "after_quantity", nullable = false)
    private Integer afterQuantity;

    /**
     * 单价
     */
    @Column(name = "unit_price", precision = 10, scale = 2)
    private BigDecimal unitPrice;

    /**
     * 总金额
     */
    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    /**
     * 关联单据类型：PURCHASE-采购单，SALES-销售单，PRODUCTION-生产单，OTHER-其他
     */
    @Column(name = "reference_type", length = 50)
    private String referenceType;

    /**
     * 关联单据编号
     */
    @Column(name = "reference_no", length = 100)
    private String referenceNo;

    /**
     * 仓库 ID
     */
    @Column(name = "warehouse_id")
    private Long warehouseId;

    /**
     * 库位 ID
     */
    @Column(name = "location_id")
    private Long locationId;

    /**
     * 交易时间
     */
    @Column(name = "transaction_time", nullable = false)
    private LocalDateTime transactionTime;

    /**
     * 操作人 ID
     */
    @Column(name = "operator_id")
    private Long operatorId;

    /**
     * 操作人姓名
     */
    @Column(name = "operator_name", length = 100)
    private String operatorName;

    /**
     * 备注
     */
    @Column(name = "remark", columnDefinition = "TEXT")
    private String remark;
}
