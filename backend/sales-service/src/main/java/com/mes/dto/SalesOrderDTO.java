package com.mes.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 销售订单 DTO
 */
@Data
public class SalesOrderDTO {

    private Long id;

    private Long tenantId;

    @NotBlank(message = "订单号不能为空")
    private String orderNo;

    @NotNull(message = "客户 ID 不能为空")
    private Long customerId;

    private Long quotationId;

    private Integer status;

    @DecimalMin(value = "0.00", message = "订单金额必须大于等于 0")
    private BigDecimal totalAmount;

    private LocalDate deliveryDate;

    private String attachments;

    private String specialRequirements;

    private Long createdBy;

    private java.time.LocalDateTime createdAt;

    private java.time.LocalDateTime updatedAt;

    // 关联客户名称（用于列表展示）
    private String customerName;
}
