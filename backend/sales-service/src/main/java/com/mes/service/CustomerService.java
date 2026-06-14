package com.mes.service;

import com.mes.dto.CustomerDTO;
import java.util.List;

/**
 * 客户服务接口
 */
public interface CustomerService {

    /**
     * 创建客户
     */
    CustomerDTO createCustomer(CustomerDTO customerDTO);

    /**
     * 更新客户
     */
    CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO);

    /**
     * 删除客户
     */
    void deleteCustomer(Long id);

    /**
     * 获取客户详情
     */
    CustomerDTO getCustomerById(Long id);

    /**
     * 获取客户列表
     */
    List<CustomerDTO> getAllCustomers(Long tenantId);

    /**
     * 搜索客户
     */
    List<CustomerDTO> searchCustomers(Long tenantId, String keyword);
}
