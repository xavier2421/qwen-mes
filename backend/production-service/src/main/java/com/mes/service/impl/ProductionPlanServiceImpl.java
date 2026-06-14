package com.mes.service.impl;

import com.mes.dto.ProductionPlanCreateRequest;
import com.mes.dto.ProductionPlanDTO;
import com.mes.entity.ProductionPlan;
import com.mes.repository.ProductionPlanRepository;
import com.mes.service.ProductionPlanService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 生产计划服务实现类
 */
@Service
public class ProductionPlanServiceImpl implements ProductionPlanService {

    private final ProductionPlanRepository productionPlanRepository;

    public ProductionPlanServiceImpl(ProductionPlanRepository productionPlanRepository) {
        this.productionPlanRepository = productionPlanRepository;
    }

    @Override
    @Transactional
    public ProductionPlanDTO createPlan(ProductionPlanCreateRequest request, Long tenantId) {
        ProductionPlan plan = new ProductionPlan();
        BeanUtils.copyProperties(request, plan);
        plan.setCompletedQuantity(0);
        plan.setStatus("DRAFT");
        plan.setTenantId(tenantId);
        
        ProductionPlan saved = productionPlanRepository.save(plan);
        return convertToDTO(saved);
    }

    @Override
    @Transactional
    public ProductionPlanDTO updatePlan(Long id, ProductionPlanCreateRequest request, Long tenantId) {
        ProductionPlan plan = productionPlanRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("生产计划不存在"));
        
        if (!plan.getTenantId().equals(tenantId)) {
            throw new RuntimeException("无权访问该生产计划");
        }
        
        if ("COMPLETED".equals(plan.getStatus()) || "CANCELLED".equals(plan.getStatus())) {
            throw new RuntimeException("已完成或已取消的计划不能修改");
        }
        
        BeanUtils.copyProperties(request, plan, "id", "createTime", "updateTime", 
                                  "completedQuantity", "status", "tenantId");
        
        ProductionPlan updated = productionPlanRepository.save(plan);
        return convertToDTO(updated);
    }

    @Override
    @Transactional
    public void deletePlan(Long id, Long tenantId) {
        ProductionPlan plan = productionPlanRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("生产计划不存在"));
        
        if (!plan.getTenantId().equals(tenantId)) {
            throw new RuntimeException("无权删除该生产计划");
        }
        
        if (!"DRAFT".equals(plan.getStatus())) {
            throw new RuntimeException("只能删除草稿状态的计划");
        }
        
        productionPlanRepository.delete(plan);
    }

    @Override
    public ProductionPlanDTO getPlanById(Long id, Long tenantId) {
        ProductionPlan plan = productionPlanRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("生产计划不存在"));
        
        if (!plan.getTenantId().equals(tenantId)) {
            throw new RuntimeException("无权访问该生产计划");
        }
        
        return convertToDTO(plan);
    }

    @Override
    public List<ProductionPlanDTO> getAllPlans(Long tenantId) {
        return productionPlanRepository.findByTenantId(tenantId)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<ProductionPlanDTO> searchPlans(Long tenantId, String planNo, String productName,
                                               String status, LocalDateTime startDate, LocalDateTime endDate) {
        return productionPlanRepository.search(tenantId, planNo, productName, status, startDate, endDate)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductionPlanDTO releasePlan(Long id, Long tenantId) {
        ProductionPlan plan = productionPlanRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("生产计划不存在"));
        
        if (!plan.getTenantId().equals(tenantId)) {
            throw new RuntimeException("无权操作该生产计划");
        }
        
        if (!"DRAFT".equals(plan.getStatus())) {
            throw new RuntimeException("只有草稿状态的计划可以发布");
        }
        
        plan.setStatus("RELEASED");
        ProductionPlan updated = productionPlanRepository.save(plan);
        return convertToDTO(updated);
    }

    @Override
    @Transactional
    public ProductionPlanDTO startPlan(Long id, Long tenantId) {
        ProductionPlan plan = productionPlanRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("生产计划不存在"));
        
        if (!plan.getTenantId().equals(tenantId)) {
            throw new RuntimeException("无权操作该生产计划");
        }
        
        if (!"RELEASED".equals(plan.getStatus())) {
            throw new RuntimeException("只有已发布的计划可以开始");
        }
        
        plan.setStatus("IN_PROGRESS");
        plan.setActualStartDate(LocalDateTime.now());
        ProductionPlan updated = productionPlanRepository.save(plan);
        return convertToDTO(updated);
    }

    @Override
    @Transactional
    public ProductionPlanDTO completePlan(Long id, Long tenantId) {
        ProductionPlan plan = productionPlanRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("生产计划不存在"));
        
        if (!plan.getTenantId().equals(tenantId)) {
            throw new RuntimeException("无权操作该生产计划");
        }
        
        if (!"IN_PROGRESS".equals(plan.getStatus())) {
            throw new RuntimeException("只有进行中的计划可以完成");
        }
        
        plan.setStatus("COMPLETED");
        plan.setActualEndDate(LocalDateTime.now());
        ProductionPlan updated = productionPlanRepository.save(plan);
        return convertToDTO(updated);
    }

    @Override
    @Transactional
    public ProductionPlanDTO cancelPlan(Long id, Long tenantId) {
        ProductionPlan plan = productionPlanRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("生产计划不存在"));
        
        if (!plan.getTenantId().equals(tenantId)) {
            throw new RuntimeException("无权操作该生产计划");
        }
        
        if ("COMPLETED".equals(plan.getStatus())) {
            throw new RuntimeException("已完成的计划不能取消");
        }
        
        plan.setStatus("CANCELLED");
        ProductionPlan updated = productionPlanRepository.save(plan);
        return convertToDTO(updated);
    }

    private ProductionPlanDTO convertToDTO(ProductionPlan plan) {
        ProductionPlanDTO dto = new ProductionPlanDTO();
        BeanUtils.copyProperties(plan, dto);
        return dto;
    }
}
