package com.mes.service;

import com.mes.dto.WorkOrderCreateRequest;
import com.mes.dto.WorkOrderDTO;

import java.util.List;

/**
 * 工单服务接口
 */
public interface WorkOrderService {

    /**
     * 创建工单
     */
    WorkOrderDTO createWorkOrder(WorkOrderCreateRequest request, Long tenantId);

    /**
     * 更新工单
     */
    WorkOrderDTO updateWorkOrder(Long id, WorkOrderCreateRequest request, Long tenantId);

    /**
     * 删除工单
     */
    void deleteWorkOrder(Long id, Long tenantId);

    /**
     * 根据 ID 获取工单
     */
    WorkOrderDTO getWorkOrderById(Long id, Long tenantId);

    /**
     * 获取所有工单
     */
    List<WorkOrderDTO> getAllWorkOrders(Long tenantId);

    /**
     * 根据生产计划 ID 获取工单列表
     */
    List<WorkOrderDTO> getWorkOrdersByPlanId(Long productionPlanId, Long tenantId);

    /**
     * 搜索工单
     */
    List<WorkOrderDTO> searchWorkOrders(Long tenantId, String orderNo, String productName, String status);

    /**
     * 下达工单
     */
    WorkOrderDTO releaseWorkOrder(Long id, Long tenantId);

    /**
     * 开始工单
     */
    WorkOrderDTO startWorkOrder(Long id, Long tenantId);

    /**
     * 暂停工单
     */
    WorkOrderDTO pauseWorkOrder(Long id, Long tenantId);

    /**
     * 恢复工单
     */
    WorkOrderDTO resumeWorkOrder(Long id, Long tenantId);

    /**
     * 完成工单
     */
    WorkOrderDTO completeWorkOrder(Long id, Long tenantId);

    /**
     * 取消工单
     */
    WorkOrderDTO cancelWorkOrder(Long id, Long tenantId);

    /**
     * 报工（记录生产数量）
     */
    WorkOrderDTO reportProduction(Long id, Integer quantity, Integer defectiveQuantity, 
                                   String defectReason, String operator, Long tenantId);
}
