package com.mes.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * 生产执行记录实体
 */
@Data
@Entity
@Table(name = "production_execution")
@EqualsAndHashCode(callSuper = true)
public class ProductionExecution extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String executionNo; // 执行记录编号

    @Column(nullable = false)
    private Long workOrderId; // 关联工单 ID

    @Column(length = 50)
    private String orderNo; // 工单编号（冗余）

    @Column
    private Long processId; // 关联工序 ID

    @Column(length = 20)
    private String operationType; // 操作类型：START-开始，COMPLETE-完成，PAUSE-暂停，RESUME-恢复，DEFECT-不良品

    @Column
    private Integer quantity; // 数量

    @Column
    private Integer defectiveQuantity; // 不良品数量

    @Column(length = 50)
    private String defectReason; // 不良原因

    @Column(nullable = false)
    private LocalDateTime executionTime; // 执行时间

    @Column(length = 50)
    private String operator; // 操作员

    @Column(length = 50)
    private String workCenter; // 工作中心

    @Column(length = 500)
    private String remarks; // 备注

    @Column(nullable = false)
    private Long tenantId; // 租户 ID
}
