package com.mes.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * 工单实体
 */
@Data
@Entity
@Table(name = "work_order")
@EqualsAndHashCode(callSuper = true)
public class WorkOrder extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String orderNo; // 工单编号

    @Column(nullable = false)
    private Long productionPlanId; // 关联生产计划 ID

    @Column(length = 50)
    private String planNo; // 计划编号（冗余）

    @Column(nullable = false, length = 100)
    private String productName; // 产品名称

    @Column(nullable = false)
    private Integer quantity; // 工单数量

    @Column(nullable = false)
    private Integer completedQuantity; // 已完成数量

    @Column(nullable = false)
    private Integer defectiveQuantity; // 不良品数量

    @Column(length = 20)
    private String status; // 状态：CREATED-已创建，RELEASED-已下达，IN_PROGRESS-生产中，PAUSED-暂停，COMPLETED-已完成，CANCELLED-已取消

    @Column(nullable = false)
    private LocalDateTime plannedStartTime; // 计划开始时间

    @Column(nullable = false)
    private LocalDateTime plannedEndTime; // 计划结束时间

    @Column
    private LocalDateTime actualStartTime; // 实际开始时间

    @Column
    private LocalDateTime actualEndTime; // 实际结束时间

    @Column(length = 50)
    private String workCenter; // 工作中心

    @Column(length = 50)
    private String productionLine; // 生产线

    @Column(length = 500)
    private String remarks; // 备注

    @Column(nullable = false)
    private Long tenantId; // 租户 ID
}
