package com.mes.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 客户 DTO
 */
@Data
public class CustomerDTO {

    private Long id;

    private Long tenantId;

    @NotBlank(message = "客户名称不能为空")
    @Size(max = 100, message = "客户名称长度不能超过 100 个字符")
    private String name;

    @Size(max = 50, message = "联系人长度不能超过 50 个字符")
    private String contactName;

    @Size(max = 20, message = "联系电话长度不能超过 20 个字符")
    private String contactPhone;

    @Size(max = 100, message = "联系邮箱长度不能超过 100 个字符")
    private String contactEmail;

    private String address;

    private String taxInfo;

    private String tags;

    private String notes;

    private Integer status;

    private java.time.LocalDateTime createdAt;

    private java.time.LocalDateTime updatedAt;
}
