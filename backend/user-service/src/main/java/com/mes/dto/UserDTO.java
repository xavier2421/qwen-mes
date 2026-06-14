package com.mes.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import java.time.LocalDateTime;

@Data
public class UserDTO {

    private Long id;

    @NotBlank(message = "用户名不能为空")
    private String username;

    private String password;

    private String realName;

    private String phone;

    @Email(message = "邮箱格式不正确")
    private String email;

    private Long roleId;

    private Integer status;

    private String wxOpenid;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
