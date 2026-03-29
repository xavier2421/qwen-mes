package com.mes.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 用户实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@EqualsAndHashCode(callSuper = false)
public class User extends BaseEntity {

    /**
     * 用户名
     */
    @Column(nullable = false, length = 50, unique = true)
    private String username;

    /**
     * 密码（加密存储）
     */
    @Column(nullable = false, length = 255)
    private String password;

    /**
     * 真实姓名
     */
    @Column(length = 50)
    private String realName;

    /**
     * 手机号
     */
    @Column(length = 20)
    private String phone;

    /**
     * 邮箱
     */
    @Column(length = 100)
    private String email;

    /**
     * 角色 ID
     */
    @Column(name = "role_id")
    private Long roleId;

    /**
     * 状态：1-正常，0-禁用
     */
    @Column(columnDefinition = "SMALLINT DEFAULT 1")
    private Integer status = 1;

    /**
     * 微信小程序 openid
     */
    @Column(length = 100)
    private String wxOpenid;
}
