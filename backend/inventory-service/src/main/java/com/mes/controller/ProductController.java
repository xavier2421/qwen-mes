package com.mes.controller;

import com.mes.dto.ApiResponse;
import com.mes.dto.ProductDTO;
import com.mes.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 产品管理控制器
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * 创建产品
     */
    @PostMapping
    public ApiResponse<ProductDTO> create(@Valid @RequestBody ProductDTO productDTO,
                                           @RequestHeader("X-Tenant-Id") Long tenantId) {
        ProductDTO result = productService.createProduct(productDTO, tenantId);
        return ApiResponse.success("产品创建成功", result);
    }

    /**
     * 更新产品
     */
    @PutMapping("/{id}")
    public ApiResponse<ProductDTO> update(@PathVariable Long id,
                                           @Valid @RequestBody ProductDTO productDTO,
                                           @RequestHeader("X-Tenant-Id") Long tenantId) {
        ProductDTO result = productService.updateProduct(id, productDTO, tenantId);
        return ApiResponse.success("产品更新成功", result);
    }

    /**
     * 删除产品
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id,
                                     @RequestHeader("X-Tenant-Id") Long tenantId) {
        productService.deleteProduct(id, tenantId);
        return ApiResponse.success("产品删除成功", null);
    }

    /**
     * 根据 ID 查询产品
     */
    @GetMapping("/{id}")
    public ApiResponse<ProductDTO> getById(@PathVariable Long id,
                                            @RequestHeader("X-Tenant-Id") Long tenantId) {
        ProductDTO result = productService.getProductById(id, tenantId);
        return ApiResponse.success(result);
    }

    /**
     * 分页查询产品列表
     */
    @GetMapping
    public ApiResponse<Page<ProductDTO>> list(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "20") int size,
                                               @RequestHeader("X-Tenant-Id") Long tenantId) {
        Page<ProductDTO> result = productService.getProducts(tenantId, page, size);
        return ApiResponse.success(result);
    }

    /**
     * 搜索产品
     */
    @GetMapping("/search")
    public ApiResponse<Page<ProductDTO>> search(@RequestParam String keyword,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "20") int size,
                                                 @RequestHeader("X-Tenant-Id") Long tenantId) {
        Page<ProductDTO> result = productService.searchProducts(tenantId, keyword, page, size);
        return ApiResponse.success(result);
    }

    /**
     * 根据分类查询产品
     */
    @GetMapping("/category/{category}")
    public ApiResponse<List<ProductDTO>> getByCategory(@PathVariable String category,
                                                        @RequestHeader("X-Tenant-Id") Long tenantId) {
        List<ProductDTO> result = productService.getProductsByCategory(category, tenantId);
        return ApiResponse.success(result);
    }

    /**
     * 查询库存低于安全库存的产品
     */
    @GetMapping("/low-stock")
    public ApiResponse<List<ProductDTO>> getLowStock(@RequestHeader("X-Tenant-Id") Long tenantId) {
        List<ProductDTO> result = productService.getLowStockProducts(tenantId);
        return ApiResponse.success(result);
    }

    /**
     * 调整库存
     */
    @PostMapping("/{id}/adjust-stock")
    public ApiResponse<ProductDTO> adjustStock(@PathVariable Long id,
                                                @RequestParam Integer quantity,
                                                @RequestHeader("X-Tenant-Id") Long tenantId) {
        ProductDTO result = productService.adjustStock(id, quantity, tenantId);
        return ApiResponse.success("库存调整成功", result);
    }

    /**
     * 启用/禁用产品
     */
    @PostMapping("/{id}/toggle-status")
    public ApiResponse<ProductDTO> toggleStatus(@PathVariable Long id,
                                                 @RequestHeader("X-Tenant-Id") Long tenantId) {
        ProductDTO result = productService.toggleProductStatus(id, tenantId);
        return ApiResponse.success("状态切换成功", result);
    }
}
