package com.mes.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 工单创建请求 DTO
 */
@Data
public class WorkOrderCreateRequest {

    private Long productionPlanId;
    private String productName;
    private Integer quantity;
    private LocalDateTime plannedStartTime;
    private LocalDateTime plannedEndTime;
    private String workCenter;
    private String productionLine;
    private String remarks;
}
