-- MES 系统 SaaS 版 V2.1 - PostgreSQL 数据库初始化脚本
-- 数据库版本：PostgreSQL 15.x

-- 创建数据库
CREATE DATABASE mes_saas;

\c mes_saas;

-- 启用 UUID 扩展
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ==================== 基础表 ====================

-- 租户表
CREATE TABLE tenants (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT '租户名称',
    code VARCHAR(50) UNIQUE NOT NULL COMMENT '租户编码',
    db_schema VARCHAR(50) COMMENT '独立 Schema 名称',
    db_instance VARCHAR(100) COMMENT '独立数据库实例（可选）',
    status SMALLINT DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE tenants IS '租户表';
COMMENT ON COLUMN tenants.id IS '主键 ID';
COMMENT ON COLUMN tenants.name IS '租户名称';
COMMENT ON COLUMN tenants.code IS '租户编码';
COMMENT ON COLUMN tenants.db_schema IS '独立 Schema 名称';
COMMENT ON COLUMN tenants.db_instance IS '独立数据库实例';
COMMENT ON COLUMN tenants.status IS '状态';

-- 客户表
CREATE TABLE customers (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL COMMENT '租户 ID',
    name VARCHAR(100) NOT NULL COMMENT '客户名称',
    contact_name VARCHAR(50) COMMENT '联系人',
    contact_phone VARCHAR(20) COMMENT '联系电话',
    contact_email VARCHAR(100) COMMENT '联系邮箱',
    address TEXT COMMENT '地址',
    tax_info JSONB COMMENT '开票资料',
    tags JSONB COMMENT '标签',
    notes TEXT COMMENT '备注',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_customers_tenant ON customers(tenant_id);
CREATE INDEX idx_customers_name ON customers(name);

-- 用户表
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL COMMENT '租户 ID',
    username VARCHAR(50) UNIQUE NOT NULL COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    real_name VARCHAR(50) COMMENT '真实姓名',
    phone VARCHAR(20) COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    role_id BIGINT COMMENT '角色 ID',
    status SMALLINT DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
    wx_openid VARCHAR(100) COMMENT '微信小程序 openid',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_tenant ON users(tenant_id);
CREATE INDEX idx_users_username ON users(username);

-- 角色表
CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL COMMENT '租户 ID',
    name VARCHAR(50) NOT NULL COMMENT '角色名称',
    code VARCHAR(50) COMMENT '角色编码',
    description VARCHAR(255) COMMENT '角色描述',
    status SMALLINT DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_roles_tenant ON roles(tenant_id);

-- ==================== 销售与订单管理 ====================

-- 销售订单表
CREATE TABLE sales_orders (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL COMMENT '租户 ID',
    order_no VARCHAR(50) UNIQUE NOT NULL COMMENT '订单号',
    customer_id BIGINT NOT NULL COMMENT '客户 ID',
    quotation_id BIGINT COMMENT '关联报价单 ID',
    status SMALLINT DEFAULT 0 COMMENT '状态：0-待审核，1-待排产，2-生产中，3-已完工，4-已发货，5-已完成',
    total_amount DECIMAL(15,2) COMMENT '订单总金额',
    delivery_date DATE COMMENT '要求交期',
    attachments JSONB COMMENT '附件（图纸等）',
    special_requirements TEXT COMMENT '特殊要求',
    created_by BIGINT COMMENT '创建人',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_sales_orders_tenant ON sales_orders(tenant_id);
CREATE INDEX idx_sales_orders_customer ON sales_orders(customer_id);
CREATE INDEX idx_sales_orders_status ON sales_orders(status);
CREATE INDEX idx_sales_orders_delivery_date ON sales_orders(delivery_date);

-- ==================== 生产进度管理 ====================

-- 工艺路线表
CREATE TABLE process_routes (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL COMMENT '租户 ID',
    name VARCHAR(100) NOT NULL COMMENT '工艺路线名称',
    product_type VARCHAR(100) COMMENT '适用产品类型',
    is_template BOOLEAN DEFAULT FALSE COMMENT '是否模板',
    steps JSONB NOT NULL COMMENT '工序步骤（有序数组）',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_process_routes_tenant ON process_routes(tenant_id);

-- 工单表
CREATE TABLE work_orders (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL COMMENT '租户 ID',
    order_no VARCHAR(50) UNIQUE NOT NULL COMMENT '工单号',
    sales_order_id BIGINT COMMENT '关联销售订单 ID',
    process_route_id BIGINT COMMENT '工艺路线 ID',
    current_step_index INT DEFAULT 0 COMMENT '当前工序索引',
    status SMALLINT DEFAULT 0 COMMENT '状态：0-未开始，1-进行中，2-已完成，3-已延误',
    plan_start_time TIMESTAMP COMMENT '计划开始时间',
    plan_end_time TIMESTAMP COMMENT '计划结束时间',
    actual_start_time TIMESTAMP COMMENT '实际开始时间',
    actual_end_time TIMESTAMP COMMENT '实际结束时间',
    quantity INT COMMENT '数量',
    completed_quantity INT DEFAULT 0 COMMENT '已完成数量',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_work_orders_tenant ON work_orders(tenant_id);
CREATE INDEX idx_work_orders_sales_order ON work_orders(sales_order_id);
CREATE INDEX idx_work_orders_status ON work_orders(status);

-- 报工记录表
CREATE TABLE work_reports (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL COMMENT '租户 ID',
    work_order_id BIGINT NOT NULL COMMENT '工单 ID',
    step_index INT NOT NULL COMMENT '工序索引',
    worker_id BIGINT NOT NULL COMMENT '操作工 ID',
    start_time TIMESTAMP COMMENT '开始时间',
    end_time TIMESTAMP COMMENT '结束时间',
    quantity INT COMMENT '完成数量',
    qualified_quantity INT COMMENT '合格数量',
    unqualified_quantity INT COMMENT '不合格数量',
    unqualified_reason TEXT COMMENT '不合格原因',
    notes TEXT COMMENT '备注',
    status SMALLINT DEFAULT 1 COMMENT '状态：1-进行中，2-已完成',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_work_reports_tenant ON work_reports(tenant_id);
CREATE INDEX idx_work_reports_work_order ON work_reports(work_order_id);
CREATE INDEX idx_work_reports_worker ON work_reports(worker_id);
CREATE INDEX idx_work_reports_start_time ON work_reports(start_time);

-- ==================== 智能仓储管理 ====================

-- 物料表
CREATE TABLE materials (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL COMMENT '租户 ID',
    code VARCHAR(50) NOT NULL COMMENT '物料编码',
    name VARCHAR(100) NOT NULL COMMENT '物料名称',
    specification VARCHAR(100) COMMENT '规格型号',
    barcode VARCHAR(100) COMMENT '条码',
    category VARCHAR(50) COMMENT '分类',
    unit VARCHAR(20) COMMENT '单位',
    is_spare_part BOOLEAN DEFAULT FALSE COMMENT '是否常备零件',
    safety_stock INT DEFAULT 0 COMMENT '安全库存',
    purchase_price DECIMAL(12,4) COMMENT '采购单价',
    applicable_equipment JSONB COMMENT '适用设备（常备零件）',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_materials_tenant ON materials(tenant_id);
CREATE INDEX idx_materials_code ON materials(code);
CREATE INDEX idx_materials_category ON materials(category);

-- 货位表
CREATE TABLE locations (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL COMMENT '租户 ID',
    warehouse_id BIGINT COMMENT '仓库 ID',
    code VARCHAR(50) NOT NULL COMMENT '货位编码（如 A-01-02-3）',
    zone VARCHAR(20) COMMENT '区域',
    row VARCHAR(20) COMMENT '排',
    shelf VARCHAR(20) COMMENT '架',
    layer VARCHAR(20) COMMENT '层',
    qr_code VARCHAR(100) COMMENT '货位二维码',
    status SMALLINT DEFAULT 1 COMMENT '状态：1-可用，0-禁用',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_locations_tenant ON locations(tenant_id);
CREATE INDEX idx_locations_code ON locations(code);
CREATE UNIQUE INDEX uk_locations_tenant_code ON locations(tenant_id, code);

-- 库存表
CREATE TABLE stocks (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL COMMENT '租户 ID',
    material_id BIGINT NOT NULL COMMENT '物料 ID',
    location_id BIGINT NOT NULL COMMENT '货位 ID',
    quantity INT NOT NULL DEFAULT 0 COMMENT '当前库存数量',
    locked_quantity INT DEFAULT 0 COMMENT '锁定数量',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_stocks_tenant ON stocks(tenant_id);
CREATE INDEX idx_stocks_material ON stocks(material_id);
CREATE INDEX idx_stocks_location ON stocks(location_id);
CREATE UNIQUE INDEX uk_stocks_tenant_material_location ON stocks(tenant_id, material_id, location_id);

-- 入库单表
CREATE TABLE stock_in_records (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL COMMENT '租户 ID',
    record_no VARCHAR(50) UNIQUE NOT NULL COMMENT '入库单号',
    material_id BIGINT NOT NULL COMMENT '物料 ID',
    location_id BIGINT NOT NULL COMMENT '货位 ID',
    quantity INT NOT NULL COMMENT '入库数量',
    supplier_id BIGINT COMMENT '供应商 ID',
    purchase_order_no VARCHAR(50) COMMENT '采购单号',
    operator_id BIGINT COMMENT '操作人 ID',
    remarks TEXT COMMENT '备注',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_stock_in_tenant ON stock_in_records(tenant_id);
CREATE INDEX idx_stock_in_material ON stock_in_records(material_id);

-- 出库单表
CREATE TABLE stock_out_records (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL COMMENT '租户 ID',
    record_no VARCHAR(50) UNIQUE NOT NULL COMMENT '出库单号',
    material_id BIGINT NOT NULL COMMENT '物料 ID',
    location_id BIGINT NOT NULL COMMENT '货位 ID',
    quantity INT NOT NULL COMMENT '出库数量',
    work_order_id BIGINT COMMENT '关联工单 ID',
    operator_id BIGINT COMMENT '操作人 ID',
    remarks TEXT COMMENT '备注',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_stock_out_tenant ON stock_out_records(tenant_id);
CREATE INDEX idx_stock_out_material ON stock_out_records(material_id);

-- ==================== 初始化数据 ====================

-- 插入默认租户
INSERT INTO tenants (name, code, status) VALUES 
('演示租户', 'demo', 1);

-- 插入默认角色
INSERT INTO roles (tenant_id, name, code, description, status) VALUES
(1, '超级管理员', 'SUPER_ADMIN', '系统超级管理员', 1),
(1, '销售经理', 'SALES_MANAGER', '销售管理角色', 1),
(1, '生产主管', 'PRODUCTION_MANAGER', '生产管理角色', 1),
(1, '仓库管理员', 'WAREHOUSE_MANAGER', '仓储管理角色', 1),
(1, '工人', 'WORKER', '普通工人角色', 1),
(1, '财务', 'FINANCE', '财务管理角色', 1);

-- 插入默认管理员用户 (密码：admin123)
INSERT INTO users (tenant_id, username, password, real_name, role_id, status) VALUES
(1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '系统管理员', 1, 1);

