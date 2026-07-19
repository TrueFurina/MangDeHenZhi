# 芒得很职 (Mangdehenzhi)

> **AI + 元宇宙 + 区块链** 职业技能培训认证平台

[![CI](https://github.com/mangdehenzhi/platform/actions/workflows/ci.yml/badge.svg)](https://github.com/mangdehenzhi/platform/actions/workflows/ci.yml)
[![Java 17](https://img.shields.io/badge/Java-17-blue)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-brightgreen)](https://spring.io/projects/spring-boot)
[![Vue 3](https://img.shields.io/badge/Vue-3.5-4FC08D)](https://vuejs.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow)](LICENSE)

---

## 📋 目录

- [项目概述](#-项目概述)
- [技术栈](#-技术栈)
- [快速开始](#-快速开始)
- [Docker 部署](#-docker-部署)
- [DeepSeek AI 集成](#-deepseek-ai-集成)
- [3D 元宇宙场景](#-3d-元宇宙场景)
- [API 接口文档](#-api-接口文档)
- [项目结构](#-项目结构)
- [测试](#-测试)
- [开发命令](#-开发命令)
- [环境变量](#-环境变量)
- [优化日志](#-优化日志)

---

## 🎯 项目概述

芒得很职是一个集 AI 智能测评、元宇宙 3D 虚拟训练和区块链证书存证于一体的综合性技能培训认证平台。

### 核心功能

| 模块 | 技术 | 说明 |
|------|------|------|
| 🧠 **AI 测评** | Spring Boot + DeepSeek API | 多维度能力评估，AI 生成分析报告与学习建议 |
| 🌐 **元宇宙训练** | Three.js | 沉浸式 3D 虚拟面试/培训场景，AI 角色实时互动 |
| 🔗 **区块链认证** | Hyperledger Fabric（预留） | 技能证书上链存证，不可篡改，全球可验证 |
| 📚 **课程管理** | Vue 3 + Element Plus | 课程浏览、报名、学习路径推荐 |

### 安全特性

- ✅ JWT 令牌认证（支持环境变量配置密钥）
- ✅ API 限流保护（基于 IP 滑动窗口，默认 60 次/分钟）
- ✅ 请求日志记录（自动记录方法、耗时、状态码，慢请求告警）
- ✅ CORS 按域名限制
- ✅ SQL 注入防护（JPA 参数化查询）
- ✅ XSS/CSRF 防护（安全头 + 内容安全策略）

---

## 🛠 技术栈

### 后端 (backend/)

| 组件 | 版本 | 用途 |
|------|------|------|
| Java | 17 | 运行环境 |
| Spring Boot | 3.3.5 | 框架 |
| Spring Security + JWT | - | 认证授权 |
| Spring Data JPA | - | 数据持久化 |
| H2 / MySQL | - | 数据库（H2 开发 / MySQL 生产） |
| SpringDoc OpenAPI | 2.6.0 | 接口文档（`/swagger-ui.html`） |
| DeepSeek API | - | 大模型 AI 分析 |
| Maven | 3.9+ | 构建工具 |

### 前端 (frontend/)

| 组件 | 版本 | 用途 |
|------|------|------|
| Vue | 3.5+ | UI 框架 |
| Vite | 6+ | 构建工具 |
| TypeScript | 5+ | 类型安全 |
| Element Plus | 2.9+ | UI 组件库 |
| Three.js | 0.170+ | 3D 渲染 |
| Pinia | 2.3+ | 状态管理 |
| Vitest | 2.1+ | 单元测试 |

### DevOps

| 工具 | 用途 |
|------|------|
| Docker + Docker Compose | 容器化部署（生产 + 开发） |
| GitHub Actions | 持续集成（编译 → 测试 → 构建 → Docker） |
| Nginx | 反向代理 + 静态托管 + 安全头 + Gzip |
| Makefile | 开发命令统一入口 |

---

## 🚀 快速开始

### 环境要求

- JDK 17+
- Node.js 18+
- Maven 3.9+（或使用 `mvnw`）
- Docker（可选，用于容器化部署）

### 方式一：本地开发

```bash
# 1. 启动后端（H2 内存数据库，自动初始化测试数据）
cd backend
./mvnw.cmd spring-boot:run

# 2. 新终端，启动前端
cd frontend
npm install
npm run dev
```

前端默认运行在 `http://localhost:5173`，自动代理 API 到后端 `8080` 端口。

### 方式二：Docker 一键部署

```bash
# 复制环境变量配置
cp .env.example .env
# 编辑 .env 填入实际值（特别是 DEEPSEEK_API_KEY）
# 启动所有服务
docker-compose up -d --build
```

### 测试账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | admin123 |
| 教师 | teacher | teacher123 |
| 学员 | student | student123 |

---

## 🐳 Docker 部署

### 生产环境

```bash
docker-compose up -d --build
```

启动 3 个服务容器：

| 服务 | 镜像 | 端口 | 说明 |
|------|------|------|------|
| `db` | MySQL 8.0 | 3306 | 数据库 |
| `backend` | 自定义 Spring Boot | 8080 | API 服务 |
| `frontend` | 自定义 Nginx | 80 | 前端 SPA |

### 开发环境（热重载）

```bash
docker-compose -f docker-compose.dev.yml up -d --build
```

- 后端：挂载源码，Maven 热重载
- 前端：Vite HMR 热更新
- 数据库：独立 MySQL 容器

### 健康检查

```
GET http://localhost:8080/api/health
```

```json
{"code": 200, "data": {"status": "UP", "version": "1.0.0", "service": "mangdehenzhi-backend"}}
```

---

## 🤖 DeepSeek AI 集成

平台支持通过 DeepSeek 大模型 API 生成智能测评分析和个性化学习推荐。

### 配置

```bash
# 设置环境变量（从 https://platform.deepseek.com 获取 API Key）
export DEEPSEEK_API_KEY=sk-your-key-here
```

### 架构

```
AIService (入口)
  ├── 优先 → DeepSeekService (真实 API)
  │   ├── 模型: deepseek-chat
  │   ├── temperature: 0.7
  │   └── max_tokens: 2048
  └── 降级 → 本地模拟逻辑 (API Key 未设置时自动降级)
```

### 功能

- **AI 分析报告**：总体评价、优势维度、薄弱维度、改进建议
- **学习推荐**：推荐课程、学习路径、预计提升时间、练习建议

---

## 🌐 3D 元宇宙场景

基于 Three.js 构建的沉浸式 3D 训练场景。

### 场景类型

| 场景 | 3D 元素 | AI 角色 | 用途 |
|------|---------|---------|------|
| 🎯 虚拟面试 | 面试桌 + 双人座椅 | 面试官、HR、技术主管 | 模拟真实面试 |
| 📚 虚拟课堂 | 讲台 + 3 排桌椅 | 讲师 | 在线学习 |
| 🤝 协作会议 | 会议桌 + 6 座椅 | 多名参会者 | 团队协作 |
| ⚡ 技能实训 | 训练台 + 设备 | 培训师、助教 | 技能训练 |

### 技术特性

- WebGL 渲染器 + CSS2D 标签
- OrbitControls 自由/锁定视角切换
- 方向光 + 补光 + 轮廓光，ACES 色调映射
- 阴影映射（2048²）、抗锯齿、自适应像素比
- AI 角色发光光环 + 悬浮标签
- 组件销毁自动内存清理

---

## 📖 API 接口文档

### Swagger UI

启动后端后访问：**http://localhost:8080/swagger-ui.html**

所有接口已配置 JWT Bearer 认证，可直接在页面中测试。

### 接口列表

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| POST | `/api/auth/register` | 用户注册 | ❌ |
| POST | `/api/auth/login` | 用户登录 | ❌ |
| GET | `/api/health` | 健康检查 | ❌ |
| GET | `/api/users/me` | 获取当前用户 | ✅ |
| GET | `/api/assessments` | 获取所有测评 | ✅ |
| GET | `/api/assessments/{id}` | 获取测评详情 | ✅ |
| POST | `/api/assessments/submit` | 提交测评 | ✅ |
| GET | `/api/assessments/results` | 获取测评历史 | ✅ |
| GET | `/api/assessments/results/{id}` | 获取测评结果 | ✅ |
| GET | `/api/courses` | 获取课程列表 | ❌ |
| GET | `/api/courses/{id}` | 获取课程详情 | ❌ |
| GET | `/api/courses/category/{cat}` | 按分类获取课程 | ❌ |
| GET | `/api/certifications/my` | 获取我的证书 | ✅ |
| GET | `/api/certifications/verify/{hash}` | 验证证书 | ✅ |
| POST | `/api/metaverse/sessions` | 创建场景 | ✅ |
| GET | `/api/metaverse/sessions` | 获取会话列表 | ✅ |
| GET | `/api/metaverse/sessions/{id}` | 获取会话详情 | ✅ |
| POST | `/api/metaverse/sessions/{id}/end` | 结束会话 | ✅ |
| GET | `/api/metaverse/scene-config/{type}` | 获取场景配置 | ✅ |
| GET | `/api/recommendations/courses` | 推荐课程 | ✅ |
| GET | `/api/recommendations/learning-path` | 学习路径 | ✅ |

---

## 📁 项目结构

```
Mangdehenzhi/
├── .env.example                   # 环境变量模板
├── .github/workflows/ci.yml       # GitHub Actions CI
├── Makefile                        # 开发命令
├── OPTIMIZATION_LOG.md             # 优化日志
├── docker-compose.yml              # 生产部署
├── docker-compose.dev.yml          # 开发部署（热重载）
│
├── backend/                        # Spring Boot 后端
│   ├── Dockerfile
│   ├── sql/init.sql               # 数据库初始化 DDL
│   └── src/main/java/com/mangdehenzhi/
│       ├── MangdehenzhiApplication.java
│       ├── ai/                     # AI 分析模型
│       ├── blockchain/             # 区块链服务
│       ├── config/                 # 配置
│       │   ├── CorsConfig.java
│       │   ├── JwtUtil.java
│       │   ├── RateLimitConfig.java    ← 新增
│       │   ├── RequestLoggingInterceptor.java ← 新增
│       │   ├── SecurityConfig.java
│       │   └── SwaggerConfig.java     ← 新增
│       ├── controller/             # REST 控制器
│       │   ├── HealthController.java  ← 新增
│       │   └── ...
│       ├── dto/                    # 数据传输对象
│       │   ├── PageDTO.java           ← 新增
│       │   └── ...
│       ├── service/                # 业务逻辑
│       │   ├── DeepSeekService.java   ← 新增
│       │   └── ...
│       └── ...
│
├── frontend/                       # Vue 3 前端
│   ├── Dockerfile
│   ├── nginx.conf                  # 安全头 + Gzip
│   ├── vitest.config.ts
│   └── src/
│       ├── components/
│       │   ├── NavBar.vue             ← 新增（统一导航）
│       │   ├── SkeletonCard.vue       ← 新增（骨架屏）
│       │   ├── ThreeScene.vue         ← 新增（3D场景）
│       │   └── ...
│       ├── views/
│       │   └── Metaverse.vue          ← 重写（集成3D）
│       └── __tests__/                 ← 新增
│
├── JSMO-PAGE/                       # 营销页面（保留）
└── docs/
```

---

## 🧪 测试

### 后端测试（JUnit 5 + Spring Boot Test）

```bash
cd backend
./mvnw.cmd test
```

| 测试类 | 用例数 | 覆盖内容 |
|--------|--------|----------|
| `UserServiceTest` | 6 | 注册/登录/重复校验/角色 |
| `AssessmentServiceTest` | 6 | 测评列表/提交/通过/失败/校验 |
| `CertificationServiceTest` | 4 | 证书签发/验证/哈希唯一性 |
| `AuthControllerTest` | 4 | 注册API/登录API/参数校验 |

### 前端测试（Vitest + jsdom）

```bash
cd frontend
npm run test
```

| 测试文件 | 用例数 | 覆盖内容 |
|----------|--------|----------|
| `stores/user.test.ts` | 5 | 状态初始化/恢复/登出/角色判断 |
| `api/index.test.ts` | 5 | API 函数/路由配置/守卫 |

### CI 自动化

每次 Push/Pull Request 到 main 分支，GitHub Actions 自动执行：

1. ✅ 后端编译 + 测试 + 打包
2. ✅ 前端类型检查 + 测试 + 构建
3. ✅ Docker 镜像构建

---

## 🔧 开发命令

```bash
# 查看所有命令
make help

# 后端开发（H2 内存数据库）
make backend-dev

# 前端开发（Vite HMR）
make frontend-dev

# 运行全部测试
make test

# 前端类型检查
make frontend-lint

# Docker 生产部署
make docker-up

# Docker 开发部署（热重载）
make docker-dev

# 查看日志
make docker-logs

# 初始化项目
make init

# 清理构建产物
make clean
```

---

## 🔐 环境变量

参见 `.env.example`：

| 变量 | 默认值 | 说明 |
|------|--------|------|
| `MYSQL_HOST` | `localhost` | MySQL 主机 |
| `MYSQL_USER` | `mangdehenzhi` | 数据库用户 |
| `MYSQL_PASSWORD` | - | 数据库密码 |
| `JWT_SECRET` | - | JWT 签名密钥（至少 32 字符） |
| `DEEPSEEK_API_KEY` | - | DeepSeek API Key |
| `CORS_ALLOWED_ORIGINS` | `*` | 允许的跨域域名 |
| `SPRING_PROFILES_ACTIVE` | `dev` | Spring 激活配置 |

---

## 🔒 演示安全须知（Security Baseline）

> 面向"软件杯 Lite 版（约 20 天、需稳定可演示）"的安全底线。详细审计见 `deliverables/SECURITY_AUDIT_REPORT.md`（D 级，26 项）。
> 最近一轮（SRE）已闭环以下高危/中危项：**F-001 提交式 JWT 密钥、F-002 默认弱口令、F-003 匿名建课、F-004 方法级鉴权/角色、F-005 IDOR 测评、F-006 IDOR 用户 PII、F-007 端口暴露、F-010 限流 XFF 绕过、F-012 Swagger 暴露、F-018 弱库口令、F-020 Vite 代理端口**（均经 QA 复核确认已修复）。
> **以下仍 open，演示/上线前须知悉：**

| 编号 | 风险 | 等级 | 演示应对 |
|------|------|------|----------|
| F-008 | 全链路无 TLS（仅 HTTP，JWT/凭据明文） | 🟠 | 仅本机/内网演示；公网必须 TLS 终止（443）+ HTTP→HTTPS 重定向 |
| F-009 | `CORS_ALLOWED_ORIGINS` 默认 `*` | 🟡 | 将 `.env` 中设为具体域名，勿用 `*` |
| F-011 | 全局 `frameOptions.disable()`（H2 控制台残留） | 🟡 | prod 已禁用 H2 控制台，实际风险低；后续收紧到 H2 路径 |
| F-013 | "区块链存证/验证"为模拟（verify 恒返回 `true`） | 🟡 | **演示时明确标注"预留/模拟"，勿宣称为已上链**，否则评审验证即穿帮 |
| F-014 | JWT 存 `localStorage`（XSS 可窃取） | 🟡 | 演示环境可信前端可接受；长期改 HttpOnly Cookie |
| F-015 | 前端 axios 存在 SSRF CVE | 🟡 | `npm install axios@latest` 升级至 ≥1.8.0 |
| F-017 | DeepSeek 出站无超时（API 慢→线程耗尽） | 🟡 | 演示前确认 `DEEPSEEK_API_KEY` 有效或依赖本地降级；勿在弱网下演示 AI 报告 |
| F-019 | CI 动作未 SHA 锁定、无镜像扫描 | 🟢 | 非阻塞；后续加 Trivy + 动作 SHA 锁定 |
| F-021/022/024/025/026 | 注册枚举 / 遗留重复代码 / 场景回显 / GET 写副作用 / HSTS-on-HTTP | 🟢 | 低危，后续清理 |

### 密钥与口令：一律强随机、绝不入库

生产/演示必须将以下变量设为**强随机值**（仓库不提交真实 `.env`）：`JWT_SECRET`、`MYSQL_ROOT_PASSWORD`、`MYSQL_PASSWORD`。
当前 `application-prod.yml` 与 `docker-compose.yml` 已设为**缺失即启动失败（fail-fast）**，不会再有默认密钥/弱口令。

生成方式（二选一）：

```bash
# Windows (PowerShell)
powershell -ExecutionPolicy Bypass -File scripts/generate-secrets.ps1
# Linux / macOS / CI
bash scripts/generate-secrets.sh
```

脚本会基于 `.env.example` 生成 `.env` 并注入强随机密钥（已存在则只打印，不覆盖）。

### 部署红线（务必遵守）

- **绝不将 MySQL(3306) / 后端(8080) 映射到公网宿主机端口**。生产 `docker-compose.yml` 已默认**不映射**这两个端口（仅 `frontend:80` 对外）；后端经 Nginx 反代对内通信。
- `docker-compose.dev.yml` 的 3306/8080/5173 端口**仅供本机调试**，切勿把 dev 机器暴露到公网。
- 任何公网暴露都必须**先终止 TLS**（443）；当前仅 HTTP（F-008 仍 open）。
- 将 `CORS_ALLOWED_ORIGINS` 设为前端具体域名，勿保留 `*`。

### 冒烟验证

`docker compose up -d --build` 后，后端 `/api/health` 与前端首页均有健康检查探针；CI 的 `docker` job 会在构建后自动启动容器并校验 `http://localhost:80/`（首页）与 `http://localhost:80/api/health`（经 Nginx 反代到后端）返回 200。

---

## 📝 优化日志

详见 [OPTIMIZATION_LOG.md](./OPTIMIZATION_LOG.md)。

### 优化摘要

| 轮次 | 日期 | 优化项 | 修复问题 |
|------|------|--------|----------|
| 第一轮 | 2026-07-16 | 7 项关键修复 + Docker + DeepSeek + Three.js + 测试 | 安全漏洞、逻辑缺陷、类型安全 |
| 第二轮 | 2026-07-16 | 7 项优化：NavBar/限流/日志/健康检查/Makefile/Dev Docker | 代码重复、API 防护、开发体验 |

---

## 📄 许可证

MIT License