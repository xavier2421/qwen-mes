package com.mes.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 生产计划 DTO
 */
@Data
public class ProductionPlanDTO {

    private Long id;
    private String planNo;
    private String productName;
    private Integer plannedQuantity;
    private Integer completedQuantity;
    private String status;
    private LocalDateTime plannedStartDate;
    private LocalDateTime plannedEndDate;
    private LocalDateTime actualStartDate;
    private LocalDateTime actualEndDate;
    private Double priority;
    private String remarks;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
