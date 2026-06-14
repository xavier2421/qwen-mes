package com.mes.repository;

import com.mes.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 客户数据访问接口
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * 根据租户 ID 查询客户列表
     */
    List<Customer> findByTenantIdAndStatus(Long tenantId, Integer status);

    /**
     * 根据客户名称模糊查询
     */
    List<Customer> findByTenantIdAndNameContaining(Long tenantId, String name);

    /**
     * 检查客户名称是否存在
     */
    boolean existsByTenantIdAndName(Long tenantId, String name);
}
