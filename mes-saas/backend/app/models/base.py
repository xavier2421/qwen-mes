from sqlalchemy import Column, Integer, String, DateTime, Boolean, ForeignKey, DECIMAL, Text, JSON
from sqlalchemy.orm import relationship
from datetime import datetime
from ..core.database import Base


class Tenant(Base):
    """租户表"""
    __tablename__ = "tenants"
    
    id = Column(Integer, primary_key=True, index=True)
    name = Column(String(100), nullable=False, comment="租户名称")
    code = Column(String(50), unique=True, nullable=False, comment="租户编码")
    schema_name = Column(String(50), comment="数据库 Schema 名称")
    status = Column(String(20), default="active", comment="状态：active/inactive")
    created_at = Column(DateTime, default=datetime.utcnow, comment="创建时间")
    updated_at = Column(DateTime, default=datetime.utcnow, onupdate=datetime.utcnow, comment="更新时间")
    
    users = relationship("User", back_populates="tenant")
    customers = relationship("Customer", back_populates="tenant")
    orders = relationship("SalesOrder", back_populates="tenant")


class User(Base):
    """用户表"""
    __tablename__ = "users"
    
    id = Column(Integer, primary_key=True, index=True)
    tenant_id = Column(Integer, ForeignKey("tenants.id"), nullable=False, comment="租户 ID")
    username = Column(String(50), nullable=False, comment="用户名")
    email = Column(String(100), unique=True, nullable=False, comment="邮箱")
    password_hash = Column(String(255), nullable=False, comment="密码哈希")
    real_name = Column(String(50), comment="真实姓名")
    phone = Column(String(20), comment="手机号")
    role = Column(String(50), default="user", comment="角色：admin/manager/user/worker")
    status = Column(Boolean, default=True, comment="状态：True/False")
    created_at = Column(DateTime, default=datetime.utcnow, comment="创建时间")
    
    tenant = relationship("Tenant", back_populates="users")


class Customer(Base):
    """客户表"""
    __tablename__ = "customers"
    
    id = Column(Integer, primary_key=True, index=True)
    tenant_id = Column(Integer, ForeignKey("tenants.id"), nullable=False, comment="租户 ID")
    name = Column(String(100), nullable=False, comment="客户名称")
    code = Column(String(50), comment="客户编码")
    contact_person = Column(String(50), comment="联系人")
    contact_phone = Column(String(20), comment="联系电话")
    contact_email = Column(String(100), comment="联系邮箱")
    address = Column(String(255), comment="地址")
    tax_info = Column(JSON, comment="开票信息")
    tags = Column(JSON, comment="标签：VIP/长期合作伙伴等")
    notes = Column(Text, comment="备注")
    status = Column(Boolean, default=True, comment="状态")
    created_at = Column(DateTime, default=datetime.utcnow, comment="创建时间")
    updated_at = Column(DateTime, default=datetime.utcnow, onupdate=datetime.utcnow, comment="更新时间")
    
    tenant = relationship("Tenant", back_populates="customers")
    quotes = relationship("Quote", back_populates="customer")
    orders = relationship("SalesOrder", back_populates="customer")


class Product(Base):
    """产品表"""
    __tablename__ = "products"
    
    id = Column(Integer, primary_key=True, index=True)
    tenant_id = Column(Integer, ForeignKey("tenants.id"), nullable=False, comment="租户 ID")
    name = Column(String(100), nullable=False, comment="产品名称")
    code = Column(String(50), nullable=False, comment="产品编码")
    spec = Column(String(200), comment="规格型号")
    unit = Column(String(20), default="件", comment="单位")
    material = Column(String(100), comment="材料")
    standard_price = Column(DECIMAL(10, 2), comment="标准价格")
    standard_cost = Column(DECIMAL(10, 2), comment="标准成本")
    drawing_url = Column(String(255), comment="图纸 URL")
    status = Column(Boolean, default=True, comment="状态")
    created_at = Column(DateTime, default=datetime.utcnow, comment="创建时间")
    updated_at = Column(DateTime, default=datetime.utcnow, onupdate=datetime.utcnow, comment="更新时间")
    
    tenant = relationship("Tenant")
    process_routes = relationship("ProcessRoute", back_populates="product")


class ProcessRoute(Base):
    """工艺路线表"""
    __tablename__ = "process_routes"
    
    id = Column(Integer, primary_key=True, index=True)
    tenant_id = Column(Integer, ForeignKey("tenants.id"), nullable=False, comment="租户 ID")
    product_id = Column(Integer, ForeignKey("products.id"), nullable=False, comment="产品 ID")
    name = Column(String(100), nullable=False, comment="工艺路线名称")
    version = Column(String(20), default="V1.0", comment="版本号")
    processes = Column(JSON, nullable=False, comment="工序列表：[{name, order, std_time, role, instruction}]")
    is_template = Column(Boolean, default=False, comment="是否模板")
    status = Column(Boolean, default=True, comment="状态")
    created_at = Column(DateTime, default=datetime.utcnow, comment="创建时间")
    updated_at = Column(DateTime, default=datetime.utcnow, onupdate=datetime.utcnow, comment="更新时间")
    
    tenant = relationship("Tenant")
    product = relationship("Product", back_populates="process_routes")


class Quote(Base):
    """报价单表"""
    __tablename__ = "quotes"
    
    id = Column(Integer, primary_key=True, index=True)
    tenant_id = Column(Integer, ForeignKey("tenants.id"), nullable=False, comment="租户 ID")
    customer_id = Column(Integer, ForeignKey("customers.id"), nullable=False, comment="客户 ID")
    quote_no = Column(String(50), nullable=False, unique=True, comment="报价单号")
    product_id = Column(Integer, ForeignKey("products.id"), comment="产品 ID")
    quantity = Column(DECIMAL(10, 2), nullable=False, comment="数量")
    material_cost = Column(DECIMAL(10, 2), comment="材料费")
    processing_cost = Column(DECIMAL(10, 2), comment="加工费")
    total_cost = Column(DECIMAL(10, 2), comment="总成本")
    sale_price = Column(DECIMAL(10, 2), comment="售价")
    profit_rate = Column(DECIMAL(5, 2), comment="毛利率%")
    status = Column(String(20), default="draft", comment="状态：draft/sent/accepted/rejected")
    valid_until = Column(DateTime, comment="有效期至")
    notes = Column(Text, comment="备注")
    created_by = Column(Integer, ForeignKey("users.id"), comment="创建人")
    created_at = Column(DateTime, default=datetime.utcnow, comment="创建时间")
    updated_at = Column(DateTime, default=datetime.utcnow, onupdate=datetime.utcnow, comment="更新时间")
    
    tenant = relationship("Tenant")
    customer = relationship("Customer", back_populates="quotes")
    product = relationship("Product")
    creator = relationship("User")


class SalesOrder(Base):
    """销售订单表"""
    __tablename__ = "sales_orders"
    
    id = Column(Integer, primary_key=True, index=True)
    tenant_id = Column(Integer, ForeignKey("tenants.id"), nullable=False, comment="租户 ID")
    customer_id = Column(Integer, ForeignKey("customers.id"), nullable=False, comment="客户 ID")
    order_no = Column(String(50), nullable=False, unique=True, comment="订单号")
    quote_id = Column(Integer, ForeignKey("quotes.id"), comment="关联报价单 ID")
    product_id = Column(Integer, ForeignKey("products.id"), nullable=False, comment="产品 ID")
    quantity = Column(DECIMAL(10, 2), nullable=False, comment="数量")
    unit_price = Column(DECIMAL(10, 2), nullable=False, comment="单价")
    total_amount = Column(DECIMAL(10, 2), nullable=False, comment="总金额")
    delivery_date = Column(DateTime, comment="要求交期")
    special_requirements = Column(Text, comment="特殊要求")
    drawing_url = Column(String(255), comment="图纸附件")
    status = Column(String(20), default="pending", comment="状态：pending/producing/completed/shipped/delivered")
    progress = Column(DECIMAL(5, 2), default=0, comment="进度百分比")
    created_by = Column(Integer, ForeignKey("users.id"), comment="创建人")
    created_at = Column(DateTime, default=datetime.utcnow, comment="创建时间")
    updated_at = Column(DateTime, default=datetime.utcnow, onupdate=datetime.utcnow, comment="更新时间")
    
    tenant = relationship("Tenant", back_populates="orders")
    customer = relationship("Customer", back_populates="orders")
    product = relationship("Product")
    quote = relationship("Quote")
    creator = relationship("User")
    production_tasks = relationship("ProductionTask", back_populates="sales_order")
    deliveries = relationship("Delivery", back_populates="sales_order")
    outsourcing_records = relationship("OutsourcingRecord", back_populates="sales_order")


class ProductionTask(Base):
    """生产任务表"""
    __tablename__ = "production_tasks"
    
    id = Column(Integer, primary_key=True, index=True)
    tenant_id = Column(Integer, ForeignKey("tenants.id"), nullable=False, comment="租户 ID")
    sales_order_id = Column(Integer, ForeignKey("sales_orders.id"), nullable=False, comment="销售订单 ID")
    task_no = Column(String(50), nullable=False, unique=True, comment="任务号")
    product_id = Column(Integer, ForeignKey("products.id"), nullable=False, comment="产品 ID")
    quantity = Column(DECIMAL(10, 2), nullable=False, comment="数量")
    process_route_id = Column(Integer, ForeignKey("process_routes.id"), comment="工艺路线 ID")
    current_process_index = Column(Integer, default=0, comment="当前工序索引")
    status = Column(String(20), default="pending", comment="状态：pending/processing/completed")
    planned_start = Column(DateTime, comment="计划开始时间")
    planned_end = Column(DateTime, comment="计划结束时间")
    actual_start = Column(DateTime, comment="实际开始时间")
    actual_end = Column(DateTime, comment="实际结束时间")
    created_at = Column(DateTime, default=datetime.utcnow, comment="创建时间")
    updated_at = Column(DateTime, default=datetime.utcnow, onupdate=datetime.utcnow, comment="更新时间")
    
    tenant = relationship("Tenant")
    sales_order = relationship("SalesOrder", back_populates="production_tasks")
    product = relationship("Product")
    process_route = relationship("ProcessRoute")
    work_records = relationship("WorkRecord", back_populates="production_task")


class WorkRecord(Base):
    """工人工时记录表"""
    __tablename__ = "work_records"
    
    id = Column(Integer, primary_key=True, index=True)
    tenant_id = Column(Integer, ForeignKey("tenants.id"), nullable=False, comment="租户 ID")
    task_id = Column(Integer, ForeignKey("production_tasks.id"), nullable=False, comment="生产任务 ID")
    worker_id = Column(Integer, ForeignKey("users.id"), nullable=False, comment="工人 ID")
    process_name = Column(String(50), nullable=False, comment="工序名称")
    process_index = Column(Integer, nullable=False, comment="工序索引")
    start_time = Column(DateTime, comment="开始时间")
    end_time = Column(DateTime, comment="结束时间")
    actual_hours = Column(DECIMAL(6, 2), comment="实际工时")
    quantity = Column(DECIMAL(10, 2), comment="完成数量")
    qualified_quantity = Column(DECIMAL(10, 2), comment="合格数量")
    defect_quantity = Column(DECIMAL(10, 2), comment="不良数量")
    defect_reason = Column(String(255), comment="不良原因")
    status = Column(String(20), default="in_progress", comment="状态：in_progress/completed")
    created_at = Column(DateTime, default=datetime.utcnow, comment="创建时间")
    
    tenant = relationship("Tenant")
    production_task = relationship("ProductionTask", back_populates="work_records")
    worker = relationship("User")


class Material(Base):
    """物料表"""
    __tablename__ = "materials"
    
    id = Column(Integer, primary_key=True, index=True)
    tenant_id = Column(Integer, ForeignKey("tenants.id"), nullable=False, comment="租户 ID")
    name = Column(String(100), nullable=False, comment="物料名称")
    code = Column(String(50), nullable=False, comment="物料编码")
    spec = Column(String(200), comment="规格型号")
    category = Column(String(50), comment="类别：raw_material/consumable/spare_part/finished_goods")
    unit = Column(String(20), default="件", comment="单位")
    safety_stock = Column(DECIMAL(10, 2), comment="安全库存")
    current_stock = Column(DECIMAL(10, 2), default=0, comment="当前库存")
    is_spare_part = Column(Boolean, default=False, comment="是否常备零件")
    applicable_equipment = Column(JSON, comment="适用设备")
    status = Column(Boolean, default=True, comment="状态")
    created_at = Column(DateTime, default=datetime.utcnow, comment="创建时间")
    updated_at = Column(DateTime, default=datetime.utcnow, onupdate=datetime.utcnow, comment="更新时间")
    
    tenant = relationship("Tenant")
    stock_records = relationship("StockRecord", back_populates="material")


class WarehouseLocation(Base):
    """仓库货位表"""
    __tablename__ = "warehouse_locations"
    
    id = Column(Integer, primary_key=True, index=True)
    tenant_id = Column(Integer, ForeignKey("tenants.id"), nullable=False, comment="租户 ID")
    location_code = Column(String(50), nullable=False, comment="货位编码：如 A-01-02-3")
    area = Column(String(50), comment="区域")
    row = Column(String(10), comment="排")
    shelf = Column(String(10), comment="架")
    layer = Column(String(10), comment="层")
    qr_code = Column(String(255), comment="二维码")
    status = Column(Boolean, default=True, comment="状态")
    created_at = Column(DateTime, default=datetime.utcnow, comment="创建时间")
    
    tenant = relationship("Tenant")
    stock_records = relationship("StockRecord", back_populates="location")


class StockRecord(Base):
    """库存记录表"""
    __tablename__ = "stock_records"
    
    id = Column(Integer, primary_key=True, index=True)
    tenant_id = Column(Integer, ForeignKey("tenants.id"), nullable=False, comment="租户 ID")
    material_id = Column(Integer, ForeignKey("materials.id"), nullable=False, comment="物料 ID")
    location_id = Column(Integer, ForeignKey("warehouse_locations.id"), comment="货位 ID")
    batch_no = Column(String(50), comment="批次号")
    quantity = Column(DECIMAL(10, 2), nullable=False, comment="数量")
    record_type = Column(String(20), nullable=False, comment="类型：in/out/adjust")
    related_order_no = Column(String(50), comment="关联单号：采购单/领料单/发货单")
    operator_id = Column(Integer, ForeignKey("users.id"), comment="操作人")
    remarks = Column(String(255), comment="备注")
    created_at = Column(DateTime, default=datetime.utcnow, comment="创建时间")
    
    tenant = relationship("Tenant")
    material = relationship("Material", back_populates="stock_records")
    location = relationship("WarehouseLocation", back_populates="stock_records")
    operator = relationship("User")


class Supplier(Base):
    """供应商表"""
    __tablename__ = "suppliers"
    
    id = Column(Integer, primary_key=True, index=True)
    tenant_id = Column(Integer, ForeignKey("tenants.id"), nullable=False, comment="租户 ID")
    name = Column(String(100), nullable=False, comment="供应商名称")
    code = Column(String(50), comment="供应商编码")
    contact_person = Column(String(50), comment="联系人")
    contact_phone = Column(String(20), comment="联系电话")
    contact_email = Column(String(100), comment="联系邮箱")
    address = Column(String(255), comment="地址")
    supply_range = Column(JSON, comment="供货范围")
    payment_terms = Column(String(100), comment="账期")
    rating = Column(String(10), default="B", comment="等级：A/B/C")
    performance_score = Column(DECIMAL(5, 2), comment="绩效得分")
    status = Column(Boolean, default=True, comment="状态")
    created_at = Column(DateTime, default=datetime.utcnow, comment="创建时间")
    updated_at = Column(DateTime, default=datetime.utcnow, onupdate=datetime.utcnow, comment="更新时间")
    
    tenant = relationship("Tenant")
    purchase_orders = relationship("PurchaseOrder", back_populates="supplier")


class PurchaseOrder(Base):
    """采购订单表"""
    __tablename__ = "purchase_orders"
    
    id = Column(Integer, primary_key=True, index=True)
    tenant_id = Column(Integer, ForeignKey("tenants.id"), nullable=False, comment="租户 ID")
    supplier_id = Column(Integer, ForeignKey("suppliers.id"), nullable=False, comment="供应商 ID")
    order_no = Column(String(50), nullable=False, unique=True, comment="采购单号")
    material_id = Column(Integer, ForeignKey("materials.id"), nullable=False, comment="物料 ID")
    quantity = Column(DECIMAL(10, 2), nullable=False, comment="数量")
    unit_price = Column(DECIMAL(10, 2), nullable=False, comment="单价")
    total_amount = Column(DECIMAL(10, 2), nullable=False, comment="总金额")
    expected_date = Column(DateTime, comment="预计到货日期")
    status = Column(String(20), default="pending", comment="状态：pending/approved/sent/partial/delivered/completed")
    created_by = Column(Integer, ForeignKey("users.id"), comment="创建人")
    created_at = Column(DateTime, default=datetime.utcnow, comment="创建时间")
    updated_at = Column(DateTime, default=datetime.utcnow, onupdate=datetime.utcnow, comment="更新时间")
    
    tenant = relationship("Tenant")
    supplier = relationship("Supplier", back_populates="purchase_orders")
    material = relationship("Material")
    creator = relationship("User")


class Delivery(Base):
    """发货单表"""
    __tablename__ = "deliveries"
    
    id = Column(Integer, primary_key=True, index=True)
    tenant_id = Column(Integer, ForeignKey("tenants.id"), nullable=False, comment="租户 ID")
    sales_order_id = Column(Integer, ForeignKey("sales_orders.id"), nullable=False, comment="销售订单 ID")
    delivery_no = Column(String(50), nullable=False, unique=True, comment="发货单号")
    customer_id = Column(Integer, ForeignKey("customers.id"), nullable=False, comment="客户 ID")
    product_id = Column(Integer, ForeignKey("products.id"), nullable=False, comment="产品 ID")
    quantity = Column(DECIMAL(10, 2), nullable=False, comment="发货数量")
    delivery_date = Column(DateTime, comment="发货日期")
    logistics_company = Column(String(100), comment="物流公司")
    tracking_no = Column(String(100), comment="物流单号")
    receiver_address = Column(String(255), comment="收货地址")
    status = Column(String(20), default="pending", comment="状态：pending/shipped/delivered")
    share_qr_code = Column(String(255), comment="分享二维码")
    created_by = Column(Integer, ForeignKey("users.id"), comment="创建人")
    created_at = Column(DateTime, default=datetime.utcnow, comment="创建时间")
    updated_at = Column(DateTime, default=datetime.utcnow, onupdate=datetime.utcnow, comment="更新时间")
    
    tenant = relationship("Tenant")
    sales_order = relationship("SalesOrder", back_populates="deliveries")
    customer = relationship("Customer")
    product = relationship("Product")
    creator = relationship("User")
    freight_records = relationship("FreightRecord", back_populates="delivery")


class FreightRecord(Base):
    """运费记录表"""
    __tablename__ = "freight_records"
    
    id = Column(Integer, primary_key=True, index=True)
    tenant_id = Column(Integer, ForeignKey("tenants.id"), nullable=False, comment="租户 ID")
    delivery_id = Column(Integer, ForeignKey("deliveries.id"), nullable=False, comment="发货单 ID")
    carrier = Column(String(100), comment="承运商/车牌号")
    freight_amount = Column(DECIMAL(10, 2), nullable=False, comment="运费金额")
    billing_method = Column(String(50), comment="计费方式")
    payment_status = Column(String(20), default="unpaid", comment="付款状态：unpaid/paid")
    remarks = Column(String(255), comment="备注")
    created_at = Column(DateTime, default=datetime.utcnow, comment="创建时间")
    
    tenant = relationship("Tenant")
    delivery = relationship("Delivery", back_populates="freight_records")


class OutsourcingRecord(Base):
    """外协记录表"""
    __tablename__ = "outsourcing_records"
    
    id = Column(Integer, primary_key=True, index=True)
    tenant_id = Column(Integer, ForeignKey("tenants.id"), nullable=False, comment="租户 ID")
    sales_order_id = Column(Integer, ForeignKey("sales_orders.id"), nullable=False, comment="销售订单 ID")
    supplier_id = Column(Integer, ForeignKey("suppliers.id"), nullable=False, comment="外协供应商 ID")
    contract_amount = Column(DECIMAL(10, 2), nullable=False, comment="外协合同金额")
    start_date = Column(DateTime, comment="开始日期")
    end_date = Column(DateTime, comment="结束日期")
    payment_nodes = Column(JSON, comment="付款节点")
    status = Column(String(20), default="in_progress", comment="状态：in_progress/completed")
    created_at = Column(DateTime, default=datetime.utcnow, comment="创建时间")
    updated_at = Column(DateTime, default=datetime.utcnow, onupdate=datetime.utcnow, comment="更新时间")
    
    tenant = relationship("Tenant")
    sales_order = relationship("SalesOrder", back_populates="outsourcing_records")
    supplier = relationship("Supplier")


class InvoiceRecord(Base):
    """发票记录表"""
    __tablename__ = "invoice_records"
    
    id = Column(Integer, primary_key=True, index=True)
    tenant_id = Column(Integer, ForeignKey("tenants.id"), nullable=False, comment="租户 ID")
    sales_order_id = Column(Integer, ForeignKey("sales_orders.id"), nullable=False, comment="销售订单 ID")
    invoice_no = Column(String(50), nullable=False, comment="发票号码")
    invoice_categories = Column(JSON, nullable=False, comment="开票类别及金额")
    invoice_amount = Column(DECIMAL(10, 2), nullable=False, comment="开票金额")
    invoice_date = Column(DateTime, comment="开票日期")
    created_by = Column(Integer, ForeignKey("users.id"), comment="创建人")
    created_at = Column(DateTime, default=datetime.utcnow, comment="创建时间")
    
    tenant = relationship("Tenant")
    sales_order = relationship("SalesOrder")
    creator = relationship("User")
