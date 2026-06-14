from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from typing import List
from ..core.database import get_db
from ..models import Customer, Tenant
from ..schemas import CustomerCreate, CustomerUpdate, CustomerResponse, ResponseBase

router = APIRouter(prefix="/customers", tags=["客户管理"])


@router.get("", response_model=ResponseBase)
def list_customers(
    skip: int = 0,
    limit: int = 100,
    tenant_id: int = 1,  # TODO: 从 token 获取
    db: Session = Depends(get_db)
):
    """获取客户列表"""
    customers = db.query(Customer).filter(
        Customer.tenant_id == tenant_id,
        Customer.status == True
    ).offset(skip).limit(limit).all()
    
    total = db.query(Customer).filter(
        Customer.tenant_id == tenant_id,
        Customer.status == True
    ).count()
    
    return ResponseBase(
        message="获取成功",
        data={"items": customers, "total": total}
    )


@router.post("", response_model=ResponseBase)
def create_customer(
    customer_data: CustomerCreate,
    db: Session = Depends(get_db)
):
    """创建客户"""
    # 检查客户编码是否已存在
    if customer_data.code:
        existing = db.query(Customer).filter(
            Customer.code == customer_data.code,
            Customer.tenant_id == customer_data.tenant_id
        ).first()
        if existing:
            raise HTTPException(status_code=400, message="客户编码已存在")
    
    db_customer = Customer(**customer_data.model_dump())
    db.add(db_customer)
    db.commit()
    db.refresh(db_customer)
    
    return ResponseBase(message="创建成功", data={"customer_id": db_customer.id})


@router.get("/{customer_id}", response_model=ResponseBase)
def get_customer(
    customer_id: int,
    db: Session = Depends(get_db)
):
    """获取客户详情"""
    customer = db.query(Customer).filter(Customer.id == customer_id).first()
    if not customer:
        raise HTTPException(status_code=404, message="客户不存在")
    
    return ResponseBase(message="获取成功", data=customer)


@router.put("/{customer_id}", response_model=ResponseBase)
def update_customer(
    customer_id: int,
    customer_data: CustomerUpdate,
    db: Session = Depends(get_db)
):
    """更新客户"""
    customer = db.query(Customer).filter(Customer.id == customer_id).first()
    if not customer:
        raise HTTPException(status_code=404, message="客户不存在")
    
    update_data = customer_data.model_dump(exclude_unset=True)
    for key, value in update_data.items():
        setattr(customer, key, value)
    
    db.commit()
    db.refresh(customer)
    
    return ResponseBase(message="更新成功")


@router.delete("/{customer_id}", response_model=ResponseBase)
def delete_customer(
    customer_id: int,
    db: Session = Depends(get_db)
):
    """删除客户（软删除）"""
    customer = db.query(Customer).filter(Customer.id == customer_id).first()
    if not customer:
        raise HTTPException(status_code=404, message="客户不存在")
    
    customer.status = False
    db.commit()
    
    return ResponseBase(message="删除成功")
