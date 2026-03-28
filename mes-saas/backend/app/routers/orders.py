from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from datetime import datetime
from ..core.database import get_db
from ..models import SalesOrder, ProductionTask, ProcessRoute, Product
from ..schemas import (
    SalesOrderCreate, SalesOrderUpdate, SalesOrderResponse,
    ProductionTaskCreate, ProductionTaskResponse,
    ResponseBase
)

router = APIRouter(prefix="/orders", tags=["销售订单"])


@router.get("", response_model=ResponseBase)
def list_orders(
    skip: int = 0,
    limit: int = 100,
    status_filter: str = None,
    tenant_id: int = 1,
    db: Session = Depends(get_db)
):
    """获取销售订单列表"""
    query = db.query(SalesOrder).filter(
        SalesOrder.tenant_id == tenant_id
    )
    
    if status_filter:
        query = query.filter(SalesOrder.status == status_filter)
    
    orders = query.order_by(SalesOrder.created_at.desc()).offset(skip).limit(limit).all()
    total = query.count()
    
    return ResponseBase(
        message="获取成功",
        data={"items": orders, "total": total}
    )


@router.post("", response_model=ResponseBase)
def create_order(
    order_data: SalesOrderCreate,
    db: Session = Depends(get_db)
):
    """创建销售订单"""
    # 生成订单号
    order_no = f"SO{datetime.now().strftime('%Y%m%d%H%M%S')}"
    
    # 计算总金额
    total_amount = order_data.quantity * order_data.unit_price
    
    db_order = SalesOrder(
        **order_data.model_dump(),
        order_no=order_no,
        total_amount=total_amount
    )
    db.add(db_order)
    db.commit()
    db.refresh(db_order)
    
    # 自动创建生产任务
    product = db.query(Product).filter(Product.id == order_data.product_id).first()
    if product:
        task_no = f"PT{datetime.now().strftime('%Y%m%d%H%M%S')}"
        db_task = ProductionTask(
            tenant_id=order_data.tenant_id,
            sales_order_id=db_order.id,
            task_no=task_no,
            product_id=order_data.product_id,
            quantity=order_data.quantity,
            status="pending"
        )
        db.add(db_task)
        db.commit()
    
    return ResponseBase(
        message="创建成功",
        data={"order_id": db_order.id, "order_no": db_order.order_no}
    )


@router.get("/{order_id}", response_model=ResponseBase)
def get_order(order_id: int, db: Session = Depends(get_db)):
    """获取订单详情"""
    order = db.query(SalesOrder).filter(SalesOrder.id == order_id).first()
    if not order:
        raise HTTPException(status_code=404, message="订单不存在")
    
    return ResponseBase(message="获取成功", data=order)


@router.put("/{order_id}/status", response_model=ResponseBase)
def update_order_status(
    order_id: int,
    status: str,
    db: Session = Depends(get_db)
):
    """更新订单状态"""
    order = db.query(SalesOrder).filter(SalesOrder.id == order_id).first()
    if not order:
        raise HTTPException(status_code=404, message="订单不存在")
    
    order.status = status
    db.commit()
    db.refresh(order)
    
    return ResponseBase(message="更新成功")


@router.post("/{order_id}/convert-to-quote", response_model=ResponseBase)
def convert_quote_to_order(
    quote_id: int,
    tenant_id: int,
    created_by: int,
    db: Session = Depends(get_db)
):
    """报价单转订单"""
    from ..models import Quote
    
    quote = db.query(Quote).filter(Quote.id == quote_id).first()
    if not quote:
        raise HTTPException(status_code=404, message="报价单不存在")
    
    if quote.status != "accepted":
        raise HTTPException(status_code=400, message="报价单未接受")
    
    # 创建订单逻辑...
    
    return ResponseBase(message="转换成功")
