package com.mes.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 产品 DTO
 */
@Data
public class ProductDTO {

    private Long id;

    /**
     * 产品编码
     */
    @NotBlank(message = "产品编码不能为空")
    @Size(max = 50, message = "产品编码长度不能超过 50 个字符")
    private String productCode;

    /**
     * 产品名称
     */
    @NotBlank(message = "产品名称不能为空")
    @Size(max = 200, message = "产品名称长度不能超过 200 个字符")
    private String productName;

    /**
     * 产品规格
     */
    private String specification;

    /**
     * 单位
     */
    private String unit;

    /**
     * 产品描述
     */
    private String description;

    /**
     * 产品分类
     */
    private String category;

    /**
     * 产品图片 URL
     */
    private String imageUrl;

    /**
     * 单价
     */
    @DecimalMin(value = "0.00", message = "单价不能为负数")
    private BigDecimal unitPrice;

    /**
     * 成本价
     */
    @DecimalMin(value = "0.00", message = "成本价不能为负数")
    private BigDecimal costPrice;

    /**
     * 库存数量
     */
    @Min(value = 0, message = "库存数量不能为负数")
    private Integer stockQuantity;

    /**
     * 安全库存
     */
    @Min(value = 0, message = "安全库存不能为负数")
    private Integer safetyStock;

    /**
     * 最大库存
     */
    @Min(value = 0, message = "最大库存不能为负数")
    private Integer maxStock;

    /**
     * 状态：1-启用，0-禁用
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    private Long tenantId;
}
