package com.mes.service.impl;

import com.mes.dto.InventoryTransactionDTO;
import com.mes.entity.InventoryTransaction;
import com.mes.entity.Product;
import com.mes.repository.InventoryTransactionRepository;
import com.mes.repository.ProductRepository;
import com.mes.service.InventoryTransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 库存流水服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryTransactionServiceImpl implements InventoryTransactionService {

    private final InventoryTransactionRepository transactionRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public InventoryTransactionDTO createTransaction(InventoryTransactionDTO dto, Long tenantId) {
        InventoryTransaction transaction = new InventoryTransaction();
        BeanUtils.copyProperties(dto, transaction);
        transaction.setTenantId(tenantId);
        transaction.setTransactionTime(dto.getTransactionTime() != null ? 
                dto.getTransactionTime() : LocalDateTime.now());

        InventoryTransaction saved = transactionRepository.save(transaction);
        log.info("创建库存交易记录成功：{}", saved.getId());

        InventoryTransactionDTO result = new InventoryTransactionDTO();
        BeanUtils.copyProperties(saved, result);
        return result;
    }

    @Override
    public InventoryTransactionDTO getTransactionById(Long id, Long tenantId) {
        InventoryTransaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("交易记录不存在：" + id));

        if (!transaction.getTenantId().equals(tenantId)) {
            throw new RuntimeException("无权访问该交易记录");
        }

        InventoryTransactionDTO result = new InventoryTransactionDTO();
        BeanUtils.copyProperties(transaction, result);
        return result;
    }

    @Override
    public Page<InventoryTransactionDTO> getTransactions(Long tenantId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("transactionTime").descending());
        return transactionRepository.findByTenantId(tenantId, pageRequest)
                .map(this::convertToDTO);
    }

    @Override
    public List<InventoryTransactionDTO> getProductTransactions(Long productId, Long tenantId) {
        return transactionRepository.findProductTransactions(productId, tenantId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InventoryTransactionDTO> getTransactionsByTimeRange(Long tenantId,
                                                                     LocalDateTime startTime,
                                                                     LocalDateTime endTime) {
        return transactionRepository.findByTimeRange(tenantId, startTime, endTime)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public InventoryTransactionDTO inbound(Long productId, Integer quantity,
                                            String referenceType, String referenceNo,
                                            Long warehouseId, Long locationId,
                                            BigDecimal unitPrice,
                                            String remark, Long operatorId,
                                            String operatorName, Long tenantId) {
        // 检查产品是否存在
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("产品不存在：" + productId));

        if (!product.getTenantId().equals(tenantId)) {
            throw new RuntimeException("无权操作该产品");
        }

        // 计算交易前后的库存
        int beforeQuantity = product.getStockQuantity();
        int afterQuantity = beforeQuantity + quantity;

        // 更新产品库存
        product.setStockQuantity(afterQuantity);
        productRepository.save(product);

        // 创建交易记录
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setTenantId(tenantId);
        transaction.setProductId(productId);
        transaction.setTransactionType("IN");
        transaction.setQuantity(quantity);
        transaction.setBeforeQuantity(beforeQuantity);
        transaction.setAfterQuantity(afterQuantity);
        transaction.setUnitPrice(unitPrice);
        transaction.setTotalAmount(unitPrice != null ? unitPrice.multiply(new BigDecimal(quantity)) : null);
        transaction.setReferenceType(referenceType);
        transaction.setReferenceNo(referenceNo);
        transaction.setWarehouseId(warehouseId);
        transaction.setLocationId(locationId);
        transaction.setTransactionTime(LocalDateTime.now());
        transaction.setOperatorId(operatorId);
        transaction.setOperatorName(operatorName);
        transaction.setRemark(remark);

        InventoryTransaction saved = transactionRepository.save(transaction);
        log.info("入库操作成功：产品={}, 数量={}, 新库存={}", product.getProductCode(), quantity, afterQuantity);

        return convertToDTO(saved);
    }

    @Override
    @Transactional
    public InventoryTransactionDTO outbound(Long productId, Integer quantity,
                                             String referenceType, String referenceNo,
                                             Long warehouseId, Long locationId,
                                             BigDecimal unitPrice,
                                             String remark, Long operatorId,
                                             String operatorName, Long tenantId) {
        // 检查产品是否存在
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("产品不存在：" + productId));

        if (!product.getTenantId().equals(tenantId)) {
            throw new RuntimeException("无权操作该产品");
        }

        // 检查库存是否充足
        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("库存不足：当前库存=" + product.getStockQuantity() + ", 需要出库=" + quantity);
        }

        // 计算交易前后的库存
        int beforeQuantity = product.getStockQuantity();
        int afterQuantity = beforeQuantity - quantity;

        // 更新产品库存
        product.setStockQuantity(afterQuantity);
        productRepository.save(product);

        // 创建交易记录
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setTenantId(tenantId);
        transaction.setProductId(productId);
        transaction.setTransactionType("OUT");
        transaction.setQuantity(quantity);
        transaction.setBeforeQuantity(beforeQuantity);
        transaction.setAfterQuantity(afterQuantity);
        transaction.setUnitPrice(unitPrice);
        transaction.setTotalAmount(unitPrice != null ? unitPrice.multiply(new BigDecimal(quantity)) : null);
        transaction.setReferenceType(referenceType);
        transaction.setReferenceNo(referenceNo);
        transaction.setWarehouseId(warehouseId);
        transaction.setLocationId(locationId);
        transaction.setTransactionTime(LocalDateTime.now());
        transaction.setOperatorId(operatorId);
        transaction.setOperatorName(operatorName);
        transaction.setRemark(remark);

        InventoryTransaction saved = transactionRepository.save(transaction);
        log.info("出库操作成功：产品={}, 数量={}, 新库存={}", product.getProductCode(), quantity, afterQuantity);

        return convertToDTO(saved);
    }

    @Override
    @Transactional
    public InventoryTransactionDTO adjustStock(Long productId, Integer quantity,
                                                String reason, Long tenantId) {
        // 检查产品是否存在
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("产品不存在：" + productId));

        if (!product.getTenantId().equals(tenantId)) {
            throw new RuntimeException("无权操作该产品");
        }

        // 计算调整后的库存
        int beforeQuantity = product.getStockQuantity();
        int afterQuantity = beforeQuantity + quantity;

        if (afterQuantity < 0) {
            throw new RuntimeException("调整后库存不能为负数");
        }

        // 更新产品库存
        product.setStockQuantity(afterQuantity);
        productRepository.save(product);

        // 创建交易记录
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setTenantId(tenantId);
        transaction.setProductId(productId);
        transaction.setTransactionType("ADJUST");
        transaction.setQuantity(quantity);
        transaction.setBeforeQuantity(beforeQuantity);
        transaction.setAfterQuantity(afterQuantity);
        transaction.setTransactionTime(LocalDateTime.now());
        transaction.setRemark(reason);

        InventoryTransaction saved = transactionRepository.save(transaction);
        log.info("库存调整成功：产品={}, 调整量={}, 新库存={}", product.getProductCode(), quantity, afterQuantity);

        return convertToDTO(saved);
    }

    private InventoryTransactionDTO convertToDTO(InventoryTransaction transaction) {
        InventoryTransactionDTO dto = new InventoryTransactionDTO();
        BeanUtils.copyProperties(transaction, dto);
        return dto;
    }
}
