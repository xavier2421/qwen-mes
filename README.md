# MES 系统 SaaS 版 V2.1

制造执行系统 (MES) SaaS 化解决方案，支持多租户隔离，可承载千级租户。

## 项目结构

```
/workspace
├── backend/                 # 后端微服务
│   ├── pom.xml             # 父工程 POM
│   ├── user-service/       # 用户与权限中心
│   ├── sales-service/      # 销售与订单管理
│   ├── production-service/ # 生产进度管理
│   ├── warehouse-service/  # 智能仓储管理
│   ├── delivery-service/   # 发货与客户分享
│   ├── finance-service/    # 财务管理
│   ├── invoice-service/    # 发票管理
│   ├── logistics-service/  # 物流费用管理
│   ├── subcontract-service/# 外协管理
│   └── bi-service/         # BI 报表中心
├── database/               # 数据库脚本
│   └── schema.sql         # PostgreSQL 初始化脚本
├── docs/                   # 文档
│   └── 开发文档.md         # 详细开发文档
├── frontend-pc/           # PC 管理后台 (Vue3 + Element Plus)
└── frontend-miniprogram/  # 微信小程序 (工人端)
```

## 技术栈

### 后端
- **语言**: Java 17
- **框架**: Spring Boot 3.2.0
- **数据库**: PostgreSQL 15.x
- **缓存**: Redis 7.x
- **安全**: Spring Security + JWT

### 前端
- **PC 端**: Vue 3.x + Element Plus
- **小程序**: 原生微信小程序

## 快速开始

### 1. 数据库初始化

```bash
# 连接到 PostgreSQL
psql -U postgres

# 执行初始化脚本
\i /workspace/database/schema.sql
```

### 2. 启动服务

```bash
# 进入用户服务目录
cd /workspace/backend/user-service

# 使用 Maven 构建并运行
mvn spring-boot:run
```

### 3. 默认账号

- 用户名：`admin`
- 密码：`admin123`

## 模块说明

| 模块 | 服务名 | 端口 | 优先级 |
|------|--------|------|--------|
| 用户与权限中心 | user-service | 8081 | P0 |
| 销售与订单管理 | sales-service | 8082 | P0 |
| 生产进度管理 | production-service | 8083 | P0 |
| 智能仓储管理 | warehouse-service | 8084 | P0 |
| 发货与客户分享 | delivery-service | 8085 | P1 |
| 财务管理 | finance-service | 8086 | P1 |
| BI 报表中心 | bi-service | 8087 | P1 |
| 发票管理 | invoice-service | 8088 | P2 |
| 物流费用管理 | logistics-service | 8089 | P2 |
| 外协管理 | subcontract-service | 8090 | P2 |

## API 接口

### 用户管理
- `POST /api/v1/users` - 创建用户
- `GET /api/v1/users` - 用户列表
- `GET /api/v1/users/{id}` - 获取用户详情
- `PUT /api/v1/users/{id}` - 更新用户
- `DELETE /api/v1/users/{id}` - 删除用户

### 认证
- `POST /api/v1/auth/login` - 登录
- `POST /api/v1/auth/logout` - 登出
- `POST /api/v1/auth/refresh` - 刷新 Token

## 开发文档

详细开发文档请查看 [docs/开发文档.md](./docs/开发文档.md)

## License

Copyright © 2024 MES SaaS
