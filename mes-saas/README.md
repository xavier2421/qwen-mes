# MES SaaS 系统

制造执行系统 (MES) SaaS 版 - 云原生、全栈微服务架构

## 技术栈

### 后端
- Python 3.9+
- FastAPI (微服务框架)
- SQLAlchemy (ORM)
- MySQL (数据库)
- Redis (缓存)
- Celery (异步任务)

### 前端
- Vue 3.x
- Element Plus
- Pinia (状态管理)
- Vue Router
- Axios

### 小程序端
- 微信小程序

## 项目结构

```
mes-saas/
├── backend/          # 后端服务
│   ├── app/
│   │   ├── api/      # API 接口
│   │   ├── core/     # 核心配置
│   │   ├── models/   # 数据模型
│   │   ├── schemas/  # Pydantic 模式
│   │   ├── services/ # 业务逻辑
│   │   ├── routers/  # 路由
│   │   └── utils/    # 工具函数
│   └── migrations/   # 数据库迁移
├── frontend/         # PC 管理后台
│   └── src/
│       ├── views/    # 页面视图
│       ├── components/ # 组件
│       ├── store/    # 状态管理
│       ├── api/      # API 调用
│       └── router/   # 路由配置
└── docs/            # 文档
```

## 功能模块

1. **销售与订单管理** - 客户管理、报价、订单全生命周期
2. **生产进度管理** - 工艺路线、扫码报工、进度监控
3. **智能仓储管理** - 物料管理、扫码出入库、智能预警
4. **采购与供应商管理** - 供应商管理、采购流程、到货验收
5. **发货管理与进度分享** - 扫码发货、客户进度分享
6. **财务管理** - 成本核算、资金分析、利润分析
7. **发票管理** - 合同开票额度、开票记录
8. **物流费用管理** - 运费记录、成本分析
9. **外协管理** - 外协成本追踪
10. **BI 报表中心** - 多角色驾驶舱、自助分析

## 快速开始

### 后端启动
```bash
cd backend
pip install -r requirements.txt
uvicorn app.main:app --reload
```

### 前端启动
```bash
cd frontend
npm install
npm run dev
```

## 多租户架构

系统采用逻辑隔离与物理隔离相结合的混合式多租户数据模型，支持：
- 独立数据库 Schema 模式
- 共享数据库按租户字段隔离模式

## 许可证

MIT License
