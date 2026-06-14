package com.mes.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 销售订单实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sales_orders")
@EqualsAndHashCode(callSuper = false)
public class SalesOrder extends BaseEntity {

    /**
     * 订单号
     */
    @Column(nullable = false, length = 50, unique = true)
    private String orderNo;

    /**
     * 客户 ID
     */
    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    /**
     * 关联报价单 ID
     */
    @Column(name = "quotation_id")
    private Long quotationId;

    /**
     * 状态：0-待审核，1-待排产，2-生产中，3-已完工，4-已发货，5-已完成
     */
    @Column(columnDefinition = "SMALLINT DEFAULT 0")
    private Integer status = 0;

    /**
     * 订单总金额
     */
    @Column(precision = 15, scale = 2)
    private BigDecimal totalAmount;

    /**
     * 要求交期
     */
    @Column(name = "delivery_date")
    private LocalDate deliveryDate;

    /**
     * 附件（图纸等）(JSON)
     */
    @Column(columnDefinition = "jsonb")
    private String attachments;

    /**
     * 特殊要求
     */
    @Column(columnDefinition = "TEXT")
    private String specialRequirements;

    /**
     * 创建人
     */
    @Column(name = "created_by")
    private Long createdBy;
}
