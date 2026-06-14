package com.mes.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 工单 DTO
 */
@Data
public class WorkOrderDTO {

    private Long id;
    private String orderNo;
    private Long productionPlanId;
    private String planNo;
    private String productName;
    private Integer quantity;
    private Integer completedQuantity;
    private Integer defectiveQuantity;
    private String status;
    private LocalDateTime plannedStartTime;
    private LocalDateTime plannedEndTime;
    private LocalDateTime actualStartTime;
    private LocalDateTime actualEndTime;
    private String workCenter;
    private String productionLine;
    private String remarks;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
