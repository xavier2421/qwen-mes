package com.mes.dto;

import lombok.Data;
import lombok.Builder;
import java.time.LocalDateTime;

@Data
@Builder
public class LoginResponse {

    private String accessToken;

    private String refreshToken;

    private String tokenType;

    private Long expiresIn;

    private UserInfo userInfo;

    @Data
    @Builder
    public static class UserInfo {
        private Long id;
        private String username;
        private String realName;
        private String phone;
        private String email;
        private Long roleId;
        private String roleName;
        private Long tenantId;
        private String tenantName;
    }
}
