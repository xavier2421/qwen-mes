from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session
from typing import List
from ..core.database import get_db
from ..core.config import settings
from ..models import User, Tenant
from ..schemas import UserCreate, UserLogin, UserResponse, Token, ResponseBase
from ..utils.security import get_password_hash, verify_password, create_access_token

router = APIRouter(prefix="/auth", tags=["认证"])


@router.post("/register", response_model=ResponseBase)
def register(user_data: UserCreate, db: Session = Depends(get_db)):
    """用户注册"""
    # 检查邮箱是否已存在
    existing_user = db.query(User).filter(User.email == user_data.email).first()
    if existing_user:
        raise HTTPException(status_code=400, message="邮箱已被注册")
    
    # 检查用户名是否已存在
    existing_username = db.query(User).filter(User.username == user_data.username).first()
    if existing_username:
        raise HTTPException(status_code=400, message="用户名已被使用")
    
    # 创建用户
    hashed_password = get_password_hash(user_data.password)
    db_user = User(
        username=user_data.username,
        email=user_data.email,
        password_hash=hashed_password,
        real_name=user_data.real_name,
        phone=user_data.phone,
        role=user_data.role,
        tenant_id=user_data.tenant_id
    )
    db.add(db_user)
    db.commit()
    db.refresh(db_user)
    
    return ResponseBase(message="注册成功", data={"user_id": db_user.id})


@router.post("/login", response_model=ResponseBase)
def login(login_data: UserLogin, db: Session = Depends(get_db)):
    """用户登录"""
    user = db.query(User).filter(User.username == login_data.username).first()
    if not user or not verify_password(login_data.password, user.password_hash):
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            message="用户名或密码错误"
        )
    
    if not user.status:
        raise HTTPException(status_code=400, message="账号已被禁用")
    
    # 生成 token
    access_token = create_access_token(data={"sub": user.username, "user_id": user.id})
    
    return ResponseBase(
        message="登录成功",
        data={
            "access_token": access_token,
            "token_type": "bearer",
            "user": {
                "id": user.id,
                "username": user.username,
                "email": user.email,
                "real_name": user.real_name,
                "role": user.role
            }
        }
    )


@router.get("/me", response_model=ResponseBase)
def get_current_user_info(
    db: Session = Depends(get_db),
    current_user: User = Depends(lambda: {"id": 1})  # TODO: 实现 JWT 依赖
):
    """获取当前用户信息"""
    user = db.query(User).filter(User.id == current_user["id"]).first()
    if not user:
        raise HTTPException(status_code=404, message="用户不存在")
    
    return ResponseBase(
        message="获取成功",
        data={
            "id": user.id,
            "username": user.username,
            "email": user.email,
            "real_name": user.real_name,
            "phone": user.phone,
            "role": user.role
        }
    )
