package com.mes.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 租户实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tenants")
@EqualsAndHashCode(callSuper = false)
public class Tenant extends BaseEntity {

    /**
     * 租户名称
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * 租户编码（唯一）
     */
    @Column(unique = true, nullable = false, length = 50)
    private String code;

    /**
     * 独立 Schema 名称
     */
    @Column(length = 50)
    private String dbSchema;

    /**
     * 独立数据库实例（可选）
     */
    @Column(length = 100)
    private String dbInstance;

    /**
     * 状态：1-正常，0-禁用
     */
    @Column(columnDefinition = "SMALLINT DEFAULT 1")
    private Integer status = 1;
}
