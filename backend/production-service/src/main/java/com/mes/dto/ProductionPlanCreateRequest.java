package com.mes.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 生产计划创建请求 DTO
 */
@Data
public class ProductionPlanCreateRequest {

    private String planNo;
    private String productName;
    private Integer plannedQuantity;
    private LocalDateTime plannedStartDate;
    private LocalDateTime plannedEndDate;
    private Double priority;
    private String remarks;
}
