package com.mes.service.impl;

import com.mes.dto.WorkOrderCreateRequest;
import com.mes.dto.WorkOrderDTO;
import com.mes.entity.ProductionExecution;
import com.mes.entity.WorkOrder;
import com.mes.repository.ProductionExecutionRepository;
import com.mes.repository.WorkOrderRepository;
import com.mes.service.WorkOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 工单服务实现类
 */
@Service
public class WorkOrderServiceImpl implements WorkOrderService {

    private final WorkOrderRepository workOrderRepository;
    private final ProductionExecutionRepository executionRepository;

    public WorkOrderServiceImpl(WorkOrderRepository workOrderRepository,
                                ProductionExecutionRepository executionRepository) {
        this.workOrderRepository = workOrderRepository;
        this.executionRepository = executionRepository;
    }

    @Override
    @Transactional
    public WorkOrderDTO createWorkOrder(WorkOrderCreateRequest request, Long tenantId) {
        WorkOrder order = new WorkOrder();
        BeanUtils.copyProperties(request, order);
        order.setCompletedQuantity(0);
        order.setDefectiveQuantity(0);
        order.setStatus("CREATED");
        order.setTenantId(tenantId);
        
        WorkOrder saved = workOrderRepository.save(order);
        return convertToDTO(saved);
    }

    @Override
    @Transactional
    public WorkOrderDTO updateWorkOrder(Long id, WorkOrderCreateRequest request, Long tenantId) {
        WorkOrder order = workOrderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("工单不存在"));
        
        if (!order.getTenantId().equals(tenantId)) {
            throw new RuntimeException("无权访问该工单");
        }
        
        if ("COMPLETED".equals(order.getStatus()) || "CANCELLED".equals(order.getStatus())) {
            throw new RuntimeException("已完成或已取消的工单不能修改");
        }
        
        BeanUtils.copyProperties(request, order, "id", "createTime", "updateTime",
                                  "completedQuantity", "defectiveQuantity", "status", "tenantId");
        
        WorkOrder updated = workOrderRepository.save(order);
        return convertToDTO(updated);
    }

    @Override
    @Transactional
    public void deleteWorkOrder(Long id, Long tenantId) {
        WorkOrder order = workOrderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("工单不存在"));
        
        if (!order.getTenantId().equals(tenantId)) {
            throw new RuntimeException("无权删除该工单");
        }
        
        if (!"CREATED".equals(order.getStatus())) {
            throw new RuntimeException("只能删除已创建状态的工单");
        }
        
        workOrderRepository.delete(order);
    }

    @Override
    public WorkOrderDTO getWorkOrderById(Long id, Long tenantId) {
        WorkOrder order = workOrderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("工单不存在"));
        
        if (!order.getTenantId().equals(tenantId)) {
            throw new RuntimeException("无权访问该工单");
        }
        
        return convertToDTO(order);
    }

    @Override
    public List<WorkOrderDTO> getAllWorkOrders(Long tenantId) {
        return workOrderRepository.findByTenantId(tenantId)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<WorkOrderDTO> getWorkOrdersByPlanId(Long productionPlanId, Long tenantId) {
        return workOrderRepository.findByProductionPlanIdAndTenantId(productionPlanId, tenantId)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<WorkOrderDTO> searchWorkOrders(Long tenantId, String orderNo, String productName, String status) {
        return workOrderRepository.search(tenantId, orderNo, productName, status)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public WorkOrderDTO releaseWorkOrder(Long id, Long tenantId) {
        WorkOrder order = workOrderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("工单不存在"));
        
        if (!order.getTenantId().equals(tenantId)) {
            throw new RuntimeException("无权操作该工单");
        }
        
        if (!"CREATED".equals(order.getStatus())) {
            throw new RuntimeException("只有已创建状态的工单可以下达");
        }
        
        order.setStatus("RELEASED");
        WorkOrder updated = workOrderRepository.save(order);
        return convertToDTO(updated);
    }

    @Override
    @Transactional
    public WorkOrderDTO startWorkOrder(Long id, Long tenantId) {
        WorkOrder order = workOrderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("工单不存在"));
        
        if (!order.getTenantId().equals(tenantId)) {
            throw new RuntimeException("无权操作该工单");
        }
        
        if (!"RELEASED".equals(order.getStatus())) {
            throw new RuntimeException("只有已下达的工单可以开始");
        }
        
        order.setStatus("IN_PROGRESS");
        order.setActualStartTime(LocalDateTime.now());
        WorkOrder updated = workOrderRepository.save(order);
        return convertToDTO(updated);
    }

    @Override
    @Transactional
    public WorkOrderDTO pauseWorkOrder(Long id, Long tenantId) {
        WorkOrder order = workOrderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("工单不存在"));
        
        if (!order.getTenantId().equals(tenantId)) {
            throw new RuntimeException("无权操作该工单");
        }
        
        if (!"IN_PROGRESS".equals(order.getStatus())) {
            throw new RuntimeException("只有进行中的工单可以暂停");
        }
        
        order.setStatus("PAUSED");
        WorkOrder updated = workOrderRepository.save(order);
        return convertToDTO(updated);
    }

    @Override
    @Transactional
    public WorkOrderDTO resumeWorkOrder(Long id, Long tenantId) {
        WorkOrder order = workOrderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("工单不存在"));
        
        if (!order.getTenantId().equals(tenantId)) {
            throw new RuntimeException("无权操作该工单");
        }
        
        if (!"PAUSED".equals(order.getStatus())) {
            throw new RuntimeException("只有暂停的工单可以恢复");
        }
        
        order.setStatus("IN_PROGRESS");
        WorkOrder updated = workOrderRepository.save(order);
        return convertToDTO(updated);
    }

    @Override
    @Transactional
    public WorkOrderDTO completeWorkOrder(Long id, Long tenantId) {
        WorkOrder order = workOrderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("工单不存在"));
        
        if (!order.getTenantId().equals(tenantId)) {
            throw new RuntimeException("无权操作该工单");
        }
        
        if (!"IN_PROGRESS".equals(order.getStatus()) && !"PAUSED".equals(order.getStatus())) {
            throw new RuntimeException("只有进行中或暂停的工单可以完成");
        }
        
        order.setStatus("COMPLETED");
        order.setActualEndTime(LocalDateTime.now());
        WorkOrder updated = workOrderRepository.save(order);
        return convertToDTO(updated);
    }

    @Override
    @Transactional
    public WorkOrderDTO cancelWorkOrder(Long id, Long tenantId) {
        WorkOrder order = workOrderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("工单不存在"));
        
        if (!order.getTenantId().equals(tenantId)) {
            throw new RuntimeException("无权操作该工单");
        }
        
        if ("COMPLETED".equals(order.getStatus())) {
            throw new RuntimeException("已完成的工单不能取消");
        }
        
        order.setStatus("CANCELLED");
        WorkOrder updated = workOrderRepository.save(order);
        return convertToDTO(updated);
    }

    @Override
    @Transactional
    public WorkOrderDTO reportProduction(Long id, Integer quantity, Integer defectiveQuantity,
                                         String defectReason, String operator, Long tenantId) {
        WorkOrder order = workOrderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("工单不存在"));
        
        if (!order.getTenantId().equals(tenantId)) {
            throw new RuntimeException("无权操作该工单");
        }
        
        if (!"IN_PROGRESS".equals(order.getStatus())) {
            throw new RuntimeException("只有进行中的工单可以报工");
        }
        
        // 更新工单数量
        order.setCompletedQuantity(order.getCompletedQuantity() + quantity);
        if (defectiveQuantity != null && defectiveQuantity > 0) {
            order.setDefectiveQuantity(order.getDefectiveQuantity() + defectiveQuantity);
        }
        
        WorkOrder updatedOrder = workOrderRepository.save(order);
        
        // 创建生产执行记录
        ProductionExecution execution = new ProductionExecution();
        execution.setWorkOrderId(id);
        execution.setOrderNo(order.getOrderNo());
        execution.setOperationType("COMPLETE");
        execution.setQuantity(quantity);
        execution.setDefectiveQuantity(defectiveQuantity != null ? defectiveQuantity : 0);
        execution.setDefectReason(defectReason);
        execution.setExecutionTime(LocalDateTime.now());
        execution.setOperator(operator);
        execution.setWorkCenter(order.getWorkCenter());
        execution.setTenantId(tenantId);
        
        executionRepository.save(execution);
        
        return convertToDTO(updatedOrder);
    }

    private WorkOrderDTO convertToDTO(WorkOrder order) {
        WorkOrderDTO dto = new WorkOrderDTO();
        BeanUtils.copyProperties(order, dto);
        return dto;
    }
}
