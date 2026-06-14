package com.mes.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 生产计划实体
 */
@Data
@Entity
@Table(name = "production_plan")
@EqualsAndHashCode(callSuper = true)
public class ProductionPlan extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String planNo; // 计划编号

    @Column(nullable = false, length = 100)
    private String productName; // 产品名称

    @Column(nullable = false)
    private Integer plannedQuantity; // 计划数量

    @Column(nullable = false)
    private Integer completedQuantity; // 已完成数量

    @Column(length = 20)
    private String status; // 状态：DRAFT-草稿，RELEASED-已发布，IN_PROGRESS-进行中，COMPLETED-已完成，CANCELLED-已取消

    @Column(nullable = false)
    private LocalDateTime plannedStartDate; // 计划开始时间

    @Column(nullable = false)
    private LocalDateTime plannedEndDate; // 计划结束时间

    @Column
    private LocalDateTime actualStartDate; // 实际开始时间

    @Column
    private LocalDateTime actualEndDate; // 实际结束时间

    @Column(precision = 10, scale = 2)
    private BigDecimal priority; // 优先级

    @Column(length = 500)
    private String remarks; // 备注

    @Column(nullable = false)
    private Long tenantId; // 租户 ID
}
