package com.mes.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * 工序实体
 */
@Data
@Entity
@Table(name = "production_process")
@EqualsAndHashCode(callSuper = true)
public class ProductionProcess extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String processNo; // 工序编号

    @Column(nullable = false, length = 100)
    private String processName; // 工序名称

    @Column(nullable = false)
    private Long workOrderId; // 关联工单 ID

    @Column(length = 50)
    private String orderNo; // 工单编号（冗余）

    @Column(nullable = false)
    private Integer sequence; // 工序顺序

    @Column(length = 50)
    private String workCenter; // 工作中心

    @Column(nullable = false)
    private Integer standardTime; // 标准工时（分钟）

    @Column
    private Integer actualTime; // 实际工时（分钟）

    @Column(length = 20)
    private String status; // 状态：PENDING-待处理，IN_PROGRESS-进行中，COMPLETED-已完成，SKIPPED-已跳过

    @Column
    private LocalDateTime startTime; // 开始时间

    @Column
    private LocalDateTime endTime; // 结束时间

    @Column(length = 50)
    private String operator; // 操作员

    @Column(length = 500)
    private String remarks; // 备注

    @Column(nullable = false)
    private Long tenantId; // 租户 ID
}
