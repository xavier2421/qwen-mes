from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from .core.database import engine, Base
from .models import Base  # 导入所有模型
from .routers import auth, customers, orders, production

# 创建数据库表
Base.metadata.create_all(bind=engine)


def create_app() -> FastAPI:
    """创建 FastAPI 应用"""
    
    app = FastAPI(
        title="MES SaaS System",
        description="制造执行系统 SaaS 版 API",
        version="1.0.0"
    )
    
    # 配置 CORS
    app.add_middleware(
        CORSMiddleware,
        allow_origins=["*"],  # 生产环境应限制具体域名
        allow_credentials=True,
        allow_methods=["*"],
        allow_headers=["*"],
    )
    
    # 注册路由
    app.include_router(auth.router, prefix="/api/v1")
    app.include_router(customers.router, prefix="/api/v1")
    app.include_router(orders.router, prefix="/api/v1")
    app.include_router(production.router, prefix="/api/v1")
    
    @app.get("/")
    def root():
        return {
            "name": "MES SaaS System",
            "version": "1.0.0",
            "status": "running"
        }
    
    @app.get("/health")
    def health_check():
        return {"status": "healthy"}
    
    return app


app = create_app()


if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)
