package com.mes.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 角色实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
@EqualsAndHashCode(callSuper = false)
public class Role extends BaseEntity {

    /**
     * 角色名称
     */
    @Column(nullable = false, length = 50)
    private String name;

    /**
     * 角色编码
     */
    @Column(length = 50)
    private String code;

    /**
     * 角色描述
     */
    @Column(length = 255)
    private String description;

    /**
     * 状态：1-正常，0-禁用
     */
    @Column(columnDefinition = "SMALLINT DEFAULT 1")
    private Integer status = 1;
}
