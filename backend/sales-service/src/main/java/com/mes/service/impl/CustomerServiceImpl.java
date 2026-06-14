package com.mes.service.impl;

import com.mes.dto.CustomerDTO;
import com.mes.entity.Customer;
import com.mes.repository.CustomerRepository;
import com.mes.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 客户服务实现类
 */
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        if (customerRepository.existsByTenantIdAndName(customerDTO.getTenantId(), customerDTO.getName())) {
            throw new RuntimeException("客户名称已存在");
        }

        Customer customer = new Customer();
        customer.setTenantId(customerDTO.getTenantId());
        customer.setName(customerDTO.getName());
        customer.setContactName(customerDTO.getContactName());
        customer.setContactPhone(customerDTO.getContactPhone());
        customer.setContactEmail(customerDTO.getContactEmail());
        customer.setAddress(customerDTO.getAddress());
        customer.setTaxInfo(customerDTO.getTaxInfo());
        customer.setTags(customerDTO.getTags());
        customer.setNotes(customerDTO.getNotes());
        customer.setStatus(customerDTO.getStatus() != null ? customerDTO.getStatus() : 1);

        Customer savedCustomer = customerRepository.save(customer);
        return convertToDTO(savedCustomer);
    }

    @Override
    @Transactional
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("客户不存在"));

        customer.setName(customerDTO.getName());
        customer.setContactName(customerDTO.getContactName());
        customer.setContactPhone(customerDTO.getContactPhone());
        customer.setContactEmail(customerDTO.getContactEmail());
        customer.setAddress(customerDTO.getAddress());
        customer.setTaxInfo(customerDTO.getTaxInfo());
        customer.setTags(customerDTO.getTags());
        customer.setNotes(customerDTO.getNotes());
        if (customerDTO.getStatus() != null) {
            customer.setStatus(customerDTO.getStatus());
        }

        Customer updatedCustomer = customerRepository.save(customer);
        return convertToDTO(updatedCustomer);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("客户不存在"));
        return convertToDTO(customer);
    }

    @Override
    public List<CustomerDTO> getAllCustomers(Long tenantId) {
        List<Customer> customers = customerRepository.findByTenantIdAndStatus(tenantId, 1);
        return customers.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<CustomerDTO> searchCustomers(Long tenantId, String keyword) {
        List<Customer> customers = customerRepository.findByTenantIdAndNameContaining(tenantId, keyword);
        return customers.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private CustomerDTO convertToDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setTenantId(customer.getTenantId());
        dto.setName(customer.getName());
        dto.setContactName(customer.getContactName());
        dto.setContactPhone(customer.getContactPhone());
        dto.setContactEmail(customer.getContactEmail());
        dto.setAddress(customer.getAddress());
        dto.setTaxInfo(customer.getTaxInfo());
        dto.setTags(customer.getTags());
        dto.setNotes(customer.getNotes());
        dto.setStatus(customer.getStatus());
        dto.setCreatedAt(customer.getCreatedAt());
        dto.setUpdatedAt(customer.getUpdatedAt());
        return dto;
    }
}
