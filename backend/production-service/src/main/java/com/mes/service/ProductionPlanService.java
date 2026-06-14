package com.mes.service;

import com.mes.dto.ProductionPlanCreateRequest;
import com.mes.dto.ProductionPlanDTO;

import java.util.List;

/**
 * 生产计划服务接口
 */
public interface ProductionPlanService {

    /**
     * 创建生产计划
     */
    ProductionPlanDTO createPlan(ProductionPlanCreateRequest request, Long tenantId);

    /**
     * 更新生产计划
     */
    ProductionPlanDTO updatePlan(Long id, ProductionPlanCreateRequest request, Long tenantId);

    /**
     * 删除生产计划
     */
    void deletePlan(Long id, Long tenantId);

    /**
     * 根据 ID 获取生产计划
     */
    ProductionPlanDTO getPlanById(Long id, Long tenantId);

    /**
     * 获取所有生产计划
     */
    List<ProductionPlanDTO> getAllPlans(Long tenantId);

    /**
     * 搜索生产计划
     */
    List<ProductionPlanDTO> searchPlans(Long tenantId, String planNo, String productName, 
                                        String status, java.time.LocalDateTime startDate, 
                                        java.time.LocalDateTime endDate);

    /**
     * 发布生产计划
     */
    ProductionPlanDTO releasePlan(Long id, Long tenantId);

    /**
     * 开始生产计划
     */
    ProductionPlanDTO startPlan(Long id, Long tenantId);

    /**
     * 完成生产计划
     */
    ProductionPlanDTO completePlan(Long id, Long tenantId);

    /**
     * 取消生产计划
     */
    ProductionPlanDTO cancelPlan(Long id, Long tenantId);
}
