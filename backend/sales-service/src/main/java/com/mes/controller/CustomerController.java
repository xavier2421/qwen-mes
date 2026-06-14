package com.mes.controller;

import com.mes.dto.ApiResponse;
import com.mes.dto.CustomerDTO;
import com.mes.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 客户管理控制器
 */
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ApiResponse<CustomerDTO> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        CustomerDTO created = customerService.createCustomer(customerDTO);
        return ApiResponse.success("客户创建成功", created);
    }

    @PutMapping("/{id}")
    public ApiResponse<CustomerDTO> updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerDTO customerDTO) {
        CustomerDTO updated = customerService.updateCustomer(id, customerDTO);
        return ApiResponse.success("客户更新成功", updated);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ApiResponse.success("客户删除成功", null);
    }

    @GetMapping("/{id}")
    public ApiResponse<CustomerDTO> getCustomerById(@PathVariable Long id) {
        CustomerDTO customer = customerService.getCustomerById(id);
        return ApiResponse.success(customer);
    }

    @GetMapping
    public ApiResponse<List<CustomerDTO>> getAllCustomers(@RequestParam Long tenantId) {
        List<CustomerDTO> customers = customerService.getAllCustomers(tenantId);
        return ApiResponse.success(customers);
    }

    @GetMapping("/search")
    public ApiResponse<List<CustomerDTO>> searchCustomers(
            @RequestParam Long tenantId,
            @RequestParam String keyword) {
        List<CustomerDTO> customers = customerService.searchCustomers(tenantId, keyword);
        return ApiResponse.success(customers);
    }
}
