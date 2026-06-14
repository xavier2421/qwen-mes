package com.mes.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 客户实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customers")
@EqualsAndHashCode(callSuper = false)
public class Customer extends BaseEntity {

    /**
     * 客户名称
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * 联系人
     */
    @Column(length = 50)
    private String contactName;

    /**
     * 联系电话
     */
    @Column(length = 20)
    private String contactPhone;

    /**
     * 联系邮箱
     */
    @Column(length = 100)
    private String contactEmail;

    /**
     * 地址
     */
    @Column(columnDefinition = "TEXT")
    private String address;

    /**
     * 开票资料 (JSON)
     */
    @Column(columnDefinition = "jsonb")
    private String taxInfo;

    /**
     * 标签 (JSON)
     */
    @Column(columnDefinition = "jsonb")
    private String tags;

    /**
     * 备注
     */
    @Column(columnDefinition = "TEXT")
    private String notes;
}
