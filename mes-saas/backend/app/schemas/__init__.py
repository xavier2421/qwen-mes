from pydantic import BaseModel, EmailStr, Field
from typing import Optional, List
from datetime import datetime
from decimal import Decimal


# ============ 通用响应 ============
class ResponseBase(BaseModel):
    code: int = 200
    message: str = "success"
    data: Optional[dict] = None


# ============ 租户相关 ============
class TenantBase(BaseModel):
    name: str
    code: str
    schema_name: Optional[str] = None


class TenantCreate(TenantBase):
    pass


class TenantResponse(TenantBase):
    id: int
    status: str
    created_at: datetime
    
    class Config:
        from_attributes = True


# ============ 用户相关 ============
class UserBase(BaseModel):
    username: str
    email: EmailStr
    real_name: Optional[str] = None
    phone: Optional[str] = None
    role: str = "user"


class UserCreate(UserBase):
    password: str
    tenant_id: int


class UserLogin(BaseModel):
    username: str
    password: str


class UserResponse(UserBase):
    id: int
    tenant_id: int
    status: bool
    created_at: datetime
    
    class Config:
        from_attributes = True


class Token(BaseModel):
    access_token: str
    token_type: str = "bearer"
    user: UserResponse


# ============ 客户相关 ============
class CustomerBase(BaseModel):
    name: str
    code: Optional[str] = None
    contact_person: Optional[str] = None
    contact_phone: Optional[str] = None
    contact_email: Optional[EmailStr] = None
    address: Optional[str] = None
    tax_info: Optional[dict] = None
    tags: Optional[List[str]] = None
    notes: Optional[str] = None


class CustomerCreate(CustomerBase):
    tenant_id: int


class CustomerUpdate(BaseModel):
    name: Optional[str] = None
    contact_person: Optional[str] = None
    contact_phone: Optional[str] = None
    contact_email: Optional[EmailStr] = None
    address: Optional[str] = None
    tax_info: Optional[dict] = None
    tags: Optional[List[str]] = None
    notes: Optional[str] = None
    status: Optional[bool] = None


class CustomerResponse(CustomerBase):
    id: int
    tenant_id: int
    status: bool
    created_at: datetime
    updated_at: datetime
    
    class Config:
        from_attributes = True


# ============ 产品相关 ============
class ProductBase(BaseModel):
    name: str
    code: str
    spec: Optional[str] = None
    unit: str = "件"
    material: Optional[str] = None
    standard_price: Optional[Decimal] = None
    standard_cost: Optional[Decimal] = None
    drawing_url: Optional[str] = None


class ProductCreate(ProductBase):
    tenant_id: int


class ProductUpdate(BaseModel):
    name: Optional[str] = None
    spec: Optional[str] = None
    unit: Optional[str] = None
    material: Optional[str] = None
    standard_price: Optional[Decimal] = None
    standard_cost: Optional[Decimal] = None
    drawing_url: Optional[str] = None
    status: Optional[bool] = None


class ProductResponse(ProductBase):
    id: int
    tenant_id: int
    status: bool
    created_at: datetime
    updated_at: datetime
    
    class Config:
        from_attributes = True


# ============ 工艺路线相关 ============
class ProcessRouteBase(BaseModel):
    name: str
    product_id: int
    version: str = "V1.0"
    processes: List[dict]  # [{name, order, std_time, role, instruction}]
    is_template: bool = False


class ProcessRouteCreate(ProcessRouteBase):
    tenant_id: int


class ProcessRouteResponse(ProcessRouteBase):
    id: int
    tenant_id: int
    status: bool
    created_at: datetime
    updated_at: datetime
    
    class Config:
        from_attributes = True


# ============ 报价单相关 ============
class QuoteBase(BaseModel):
    customer_id: int
    product_id: Optional[int] = None
    quantity: Decimal
    material_cost: Optional[Decimal] = None
    processing_cost: Optional[Decimal] = None
    total_cost: Optional[Decimal] = None
    sale_price: Optional[Decimal] = None
    profit_rate: Optional[Decimal] = None
    valid_until: Optional[datetime] = None
    notes: Optional[str] = None


class QuoteCreate(QuoteBase):
    tenant_id: int
    created_by: int


class QuoteUpdate(BaseModel):
    status: Optional[str] = None
    sale_price: Optional[Decimal] = None
    notes: Optional[str] = None


class QuoteResponse(QuoteBase):
    id: int
    tenant_id: int
    quote_no: str
    status: str
    created_by: int
    created_at: datetime
    updated_at: datetime
    
    class Config:
        from_attributes = True


# ============ 销售订单相关 ============
class SalesOrderBase(BaseModel):
    customer_id: int
    product_id: int
    quantity: Decimal
    unit_price: Decimal
    delivery_date: Optional[datetime] = None
    special_requirements: Optional[str] = None
    drawing_url: Optional[str] = None


class SalesOrderCreate(SalesOrderBase):
    tenant_id: int
    quote_id: Optional[int] = None
    created_by: int


class SalesOrderUpdate(BaseModel):
    status: Optional[str] = None
    special_requirements: Optional[str] = None
    delivery_date: Optional[datetime] = None


class SalesOrderResponse(SalesOrderBase):
    id: int
    tenant_id: int
    order_no: str
    quote_id: Optional[int]
    total_amount: Decimal
    status: str
    progress: Decimal
    created_by: int
    created_at: datetime
    updated_at: datetime
    
    class Config:
        from_attributes = True


# ============ 生产任务相关 ============
class ProductionTaskBase(BaseModel):
    sales_order_id: int
    product_id: int
    quantity: Decimal
    process_route_id: Optional[int] = None
    planned_start: Optional[datetime] = None
    planned_end: Optional[datetime] = None


class ProductionTaskCreate(ProductionTaskBase):
    tenant_id: int


class ProductionTaskResponse(ProductionTaskBase):
    id: int
    tenant_id: int
    task_no: str
    current_process_index: int
    status: str
    actual_start: Optional[datetime] = None
    actual_end: Optional[datetime] = None
    created_at: datetime
    updated_at: datetime
    
    class Config:
        from_attributes = True


# ============ 工时记录相关 ============
class WorkRecordBase(BaseModel):
    task_id: int
    worker_id: int
    process_name: str
    process_index: int
    start_time: Optional[datetime] = None
    end_time: Optional[datetime] = None
    actual_hours: Optional[Decimal] = None
    quantity: Optional[Decimal] = None
    qualified_quantity: Optional[Decimal] = None
    defect_quantity: Optional[Decimal] = None
    defect_reason: Optional[str] = None


class WorkRecordCreate(WorkRecordBase):
    tenant_id: int


class WorkRecordStart(BaseModel):
    task_id: int
    worker_id: int
    process_name: str
    process_index: int


class WorkRecordEnd(BaseModel):
    quantity: Decimal
    qualified_quantity: Optional[Decimal] = None
    defect_quantity: Optional[Decimal] = None
    defect_reason: Optional[str] = None


class WorkRecordResponse(WorkRecordBase):
    id: int
    tenant_id: int
    status: str
    created_at: datetime
    
    class Config:
        from_attributes = True


# ============ 物料相关 ============
class MaterialBase(BaseModel):
    name: str
    code: str
    spec: Optional[str] = None
    category: Optional[str] = None
    unit: str = "件"
    safety_stock: Optional[Decimal] = None
    is_spare_part: bool = False
    applicable_equipment: Optional[List[str]] = None


class MaterialCreate(MaterialBase):
    tenant_id: int


class MaterialUpdate(BaseModel):
    name: Optional[str] = None
    spec: Optional[str] = None
    category: Optional[str] = None
    unit: Optional[str] = None
    safety_stock: Optional[Decimal] = None
    current_stock: Optional[Decimal] = None
    is_spare_part: Optional[bool] = None
    status: Optional[bool] = None


class MaterialResponse(MaterialBase):
    id: int
    tenant_id: int
    current_stock: Decimal
    status: bool
    created_at: datetime
    updated_at: datetime
    
    class Config:
        from_attributes = True


# ============ 仓库货位相关 ============
class WarehouseLocationBase(BaseModel):
    location_code: str
    area: Optional[str] = None
    row: Optional[str] = None
    shelf: Optional[str] = None
    layer: Optional[str] = None


class WarehouseLocationCreate(WarehouseLocationBase):
    tenant_id: int


class WarehouseLocationResponse(WarehouseLocationBase):
    id: int
    tenant_id: int
    qr_code: Optional[str]
    status: bool
    created_at: datetime
    
    class Config:
        from_attributes = True


# ============ 库存记录相关 ============
class StockRecordBase(BaseModel):
    material_id: int
    location_id: Optional[int] = None
    batch_no: Optional[str] = None
    quantity: Decimal
    record_type: str  # in/out/adjust
    related_order_no: Optional[str] = None
    remarks: Optional[str] = None


class StockRecordIn(StockRecordBase):
    tenant_id: int
    operator_id: int


class StockRecordOut(StockRecordBase):
    id: int
    tenant_id: int
    operator_id: Optional[int]
    created_at: datetime
    
    class Config:
        from_attributes = True


# ============ 供应商相关 ============
class SupplierBase(BaseModel):
    name: str
    code: Optional[str] = None
    contact_person: Optional[str] = None
    contact_phone: Optional[str] = None
    contact_email: Optional[EmailStr] = None
    address: Optional[str] = None
    supply_range: Optional[List[str]] = None
    payment_terms: Optional[str] = None


class SupplierCreate(SupplierBase):
    tenant_id: int


class SupplierUpdate(BaseModel):
    name: Optional[str] = None
    contact_person: Optional[str] = None
    contact_phone: Optional[str] = None
    rating: Optional[str] = None
    performance_score: Optional[Decimal] = None
    status: Optional[bool] = None


class SupplierResponse(SupplierBase):
    id: int
    tenant_id: int
    rating: str
    performance_score: Optional[Decimal]
    status: bool
    created_at: datetime
    updated_at: datetime
    
    class Config:
        from_attributes = True


# ============ 采购订单相关 ============
class PurchaseOrderBase(BaseModel):
    supplier_id: int
    material_id: int
    quantity: Decimal
    unit_price: Decimal
    expected_date: Optional[datetime] = None


class PurchaseOrderCreate(PurchaseOrderBase):
    tenant_id: int
    created_by: int


class PurchaseOrderResponse(PurchaseOrderBase):
    id: int
    tenant_id: int
    order_no: str
    total_amount: Decimal
    status: str
    created_by: int
    created_at: datetime
    updated_at: datetime
    
    class Config:
        from_attributes = True


# ============ 发货单相关 ============
class DeliveryBase(BaseModel):
    sales_order_id: int
    customer_id: int
    product_id: int
    quantity: Decimal
    delivery_date: Optional[datetime] = None
    logistics_company: Optional[str] = None
    tracking_no: Optional[str] = None
    receiver_address: Optional[str] = None


class DeliveryCreate(DeliveryBase):
    tenant_id: int
    created_by: int


class DeliveryResponse(DeliveryBase):
    id: int
    tenant_id: int
    delivery_no: str
    status: str
    share_qr_code: Optional[str]
    created_by: int
    created_at: datetime
    updated_at: datetime
    
    class Config:
        from_attributes = True


# ============ 运费记录相关 ============
class FreightRecordBase(BaseModel):
    delivery_id: int
    carrier: Optional[str] = None
    freight_amount: Decimal
    billing_method: Optional[str] = None
    payment_status: str = "unpaid"
    remarks: Optional[str] = None


class FreightRecordCreate(FreightRecordBase):
    tenant_id: int


class FreightRecordResponse(FreightRecordBase):
    id: int
    tenant_id: int
    created_at: datetime
    
    class Config:
        from_attributes = True


# ============ 外协记录相关 ============
class OutsourcingRecordBase(BaseModel):
    sales_order_id: int
    supplier_id: int
    contract_amount: Decimal
    start_date: Optional[datetime] = None
    end_date: Optional[datetime] = None
    payment_nodes: Optional[List[dict]] = None


class OutsourcingRecordCreate(OutsourcingRecordBase):
    tenant_id: int


class OutsourcingRecordResponse(OutsourcingRecordBase):
    id: int
    tenant_id: int
    status: str
    created_at: datetime
    updated_at: datetime
    
    class Config:
        from_attributes = True


# ============ 发票记录相关 ============
class InvoiceRecordBase(BaseModel):
    sales_order_id: int
    invoice_no: str
    invoice_categories: dict
    invoice_amount: Decimal
    invoice_date: Optional[datetime] = None


class InvoiceRecordCreate(InvoiceRecordBase):
    tenant_id: int
    created_by: int


class InvoiceRecordResponse(InvoiceRecordBase):
    id: int
    tenant_id: int
    created_by: int
    created_at: datetime
    
    class Config:
        from_attributes = True
