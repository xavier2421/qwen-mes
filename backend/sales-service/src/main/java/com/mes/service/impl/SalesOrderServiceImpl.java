package com.mes.service.impl;

import com.mes.dto.SalesOrderDTO;
import com.mes.entity.SalesOrder;
import com.mes.repository.CustomerRepository;
import com.mes.repository.SalesOrderRepository;
import com.mes.service.SalesOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 销售订单服务实现类
 */
@Service
@RequiredArgsConstructor
public class SalesOrderServiceImpl implements SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;
    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public SalesOrderDTO createSalesOrder(SalesOrderDTO salesOrderDTO) {
        if (salesOrderRepository.existsByOrderNo(salesOrderDTO.getOrderNo())) {
            throw new RuntimeException("订单号已存在");
        }

        // 验证客户是否存在
        customerRepository.findById(salesOrderDTO.getCustomerId())
                .orElseThrow(() -> new RuntimeException("客户不存在"));

        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setTenantId(salesOrderDTO.getTenantId());
        salesOrder.setOrderNo(salesOrderDTO.getOrderNo());
        salesOrder.setCustomerId(salesOrderDTO.getCustomerId());
        salesOrder.setQuotationId(salesOrderDTO.getQuotationId());
        salesOrder.setStatus(salesOrderDTO.getStatus() != null ? salesOrderDTO.getStatus() : 0);
        salesOrder.setTotalAmount(salesOrderDTO.getTotalAmount());
        salesOrder.setDeliveryDate(salesOrderDTO.getDeliveryDate());
        salesOrder.setAttachments(salesOrderDTO.getAttachments());
        salesOrder.setSpecialRequirements(salesOrderDTO.getSpecialRequirements());
        salesOrder.setCreatedBy(salesOrderDTO.getCreatedBy());

        SalesOrder savedOrder = salesOrderRepository.save(salesOrder);
        return convertToDTO(savedOrder);
    }

    @Override
    @Transactional
    public SalesOrderDTO updateSalesOrder(Long id, SalesOrderDTO salesOrderDTO) {
        SalesOrder salesOrder = salesOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        salesOrder.setCustomerId(salesOrderDTO.getCustomerId());
        salesOrder.setQuotationId(salesOrderDTO.getQuotationId());
        salesOrder.setTotalAmount(salesOrderDTO.getTotalAmount());
        salesOrder.setDeliveryDate(salesOrderDTO.getDeliveryDate());
        salesOrder.setAttachments(salesOrderDTO.getAttachments());
        salesOrder.setSpecialRequirements(salesOrderDTO.getSpecialRequirements());
        if (salesOrderDTO.getStatus() != null) {
            salesOrder.setStatus(salesOrderDTO.getStatus());
        }

        SalesOrder updatedOrder = salesOrderRepository.save(salesOrder);
        return convertToDTO(updatedOrder);
    }

    @Override
    @Transactional
    public void deleteSalesOrder(Long id) {
        salesOrderRepository.deleteById(id);
    }

    @Override
    public SalesOrderDTO getSalesOrderById(Long id) {
        SalesOrder salesOrder = salesOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        return convertToDTO(salesOrder);
    }

    @Override
    public List<SalesOrderDTO> getAllSalesOrders(Long tenantId) {
        List<SalesOrder> orders = salesOrderRepository.findByTenantIdAndStatusOrderByCreatedAtDesc(tenantId, null);
        return orders.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<SalesOrderDTO> getOrdersByCustomer(Long tenantId, Long customerId) {
        List<SalesOrder> orders = salesOrderRepository.findByTenantIdAndCustomerId(tenantId, customerId);
        return orders.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SalesOrderDTO updateOrderStatus(Long id, Integer status) {
        SalesOrder salesOrder = salesOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        salesOrder.setStatus(status);
        SalesOrder updatedOrder = salesOrderRepository.save(salesOrder);
        return convertToDTO(updatedOrder);
    }

    @Override
    public SalesOrderDTO getOrderProgress(Long id) {
        // TODO: 关联生产进度数据
        return getSalesOrderById(id);
    }

    private SalesOrderDTO convertToDTO(SalesOrder salesOrder) {
        SalesOrderDTO dto = new SalesOrderDTO();
        dto.setId(salesOrder.getId());
        dto.setTenantId(salesOrder.getTenantId());
        dto.setOrderNo(salesOrder.getOrderNo());
        dto.setCustomerId(salesOrder.getCustomerId());
        dto.setQuotationId(salesOrder.getQuotationId());
        dto.setStatus(salesOrder.getStatus());
        dto.setTotalAmount(salesOrder.getTotalAmount());
        dto.setDeliveryDate(salesOrder.getDeliveryDate());
        dto.setAttachments(salesOrder.getAttachments());
        dto.setSpecialRequirements(salesOrder.getSpecialRequirements());
        dto.setCreatedBy(salesOrder.getCreatedBy());
        dto.setCreatedAt(salesOrder.getCreatedAt());
        dto.setUpdatedAt(salesOrder.getUpdatedAt());

        // 获取客户名称
        customerRepository.findById(salesOrder.getCustomerId())
                .ifPresent(customer -> dto.setCustomerName(customer.getName()));

        return dto;
    }
}
