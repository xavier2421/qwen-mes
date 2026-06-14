package com.mes.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

/**
 * 产品实体类
 */
@Data
@Entity
@Table(name = "products")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity {

    /**
     * 产品编码
     */
    @Column(name = "product_code", nullable = false, length = 50)
    @NotBlank(message = "产品编码不能为空")
    @Size(max = 50, message = "产品编码长度不能超过 50 个字符")
    private String productCode;

    /**
     * 产品名称
     */
    @Column(name = "product_name", nullable = false, length = 200)
    @NotBlank(message = "产品名称不能为空")
    @Size(max = 200, message = "产品名称长度不能超过 200 个字符")
    private String productName;

    /**
     * 产品规格
     */
    @Column(name = "specification", length = 500)
    private String specification;

    /**
     * 单位
     */
    @Column(name = "unit", length = 20)
    private String unit;

    /**
     * 产品描述
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * 产品分类
     */
    @Column(name = "category", length = 100)
    private String category;

    /**
     * 产品图片 URL
     */
    @Column(name = "image_url", length = 500)
    private String imageUrl;

    /**
     * 单价
     */
    @Column(name = "unit_price", precision = 10, scale = 2)
    @DecimalMin(value = "0.00", message = "单价不能为负数")
    private BigDecimal unitPrice;

    /**
     * 成本价
     */
    @Column(name = "cost_price", precision = 10, scale = 2)
    @DecimalMin(value = "0.00", message = "成本价不能为负数")
    private BigDecimal costPrice;

    /**
     * 库存数量
     */
    @Column(name = "stock_quantity", nullable = false)
    @NotNull(message = "库存数量不能为空")
    @Min(value = 0, message = "库存数量不能为负数")
    private Integer stockQuantity = 0;

    /**
     * 安全库存
     */
    @Column(name = "safety_stock")
    @Min(value = 0, message = "安全库存不能为负数")
    private Integer safetyStock = 0;

    /**
     * 最大库存
     */
    @Column(name = "max_stock")
    @Min(value = 0, message = "最大库存不能为负数")
    private Integer maxStock;

    /**
     * 状态：1-启用，0-禁用
     */
    @Column(name = "status", nullable = false)
    private Integer status = 1;

    /**
     * 备注
     */
    @Column(name = "remark", length = 500)
    private String remark;
}
