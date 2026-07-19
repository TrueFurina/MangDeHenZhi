<!-- README-I18N:START -->
[**English**](./README.md) | **汉语**
<!-- README-I18N:END -->

# 芒德矩阵 (MangDeHenZhi)

> **AI + 元宇宙 + 区块链** — 职业技能培训认证平台

[![CI](https://github.com/TrueFurina/MangDeHenZhi/actions/workflows/ci.yml/badge.svg)](https://github.com/TrueFurina/MangDeHenZhi/actions/workflows/ci.yml)
[![Java 17](https://img.shields.io/badge/Java-17-blue)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-brightgreen)](https://spring.io/projects/spring-boot)
[![Vue 3](https://img.shields.io/badge/Vue-3.5-4FC08D)](https://vuejs.org/)
[![Vite](https://img.shields.io/badge/Vite-6-646CFF)](https://vitejs.dev/)
[![Three.js](https://img.shields.io/badge/Three.js-r170-000000)](https://threejs.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow)](LICENSE)

---

## 📋 目录

- [项目概述](#-项目概述)
- [技术栈](#-技术栈)
- [快速开始](#-快速开始)
- [Docker 部署](#-docker-部署)
- [DeepSeek AI 集成](#-deepseek-ai-集成)
- [3D 元宇宙场景](#-3d-元宇宙场景)
- [WebSocket 实时通信](#-websocket-实时通信)
- [API 接口文档](#-api-接口文档)
- [项目结构](#-项目结构)
- [测试](#-测试)
- [开发命令](#-开发命令)
- [环境变量](#-环境变量)
- [贡献指南](#-贡献指南)

---

## 🎯 项目概述

芒德矩阵是一个集 **AI 智能测评**、**元宇宙3D虚拟训练**和**区块链证书存证**于一体的综合性职业技能培训认证平台。

### 核心功能

| 模块 | 技术栈 | 说明 |
|------|--------|------|
| 🧠 **AI 测评** | Spring Boot + 大模型API | 多维度能力评估，AI生成个性化分析报告与学习建议 |
| 🌐 **元宇宙训练** | Three.js + WebSocket | 沉浸式3D虚拟面试/培训场景，AI角色实时互动 |
| 🔗 **区块链认证** | Hyperledger Fabric（预留） | 技能证书上链存证，不可篡改，全球可验证 |
| 📚 **课程管理** | Vue 3 + Element Plus | 课程浏览、报名、个性化学习路径推荐 |
| 💬 **实时聊天** | WebSocket | 房间内文字聊天、位置同步、WebRTC 信令 |

---

## 🛠 技术栈

### 后端 (backend/)

| 组件 | 版本 | 用途 |
|------|------|------|
| Java | 17 | 运行环境 |
| Spring Boot | 3.3.5 | 框架 |
| Spring Security + JWT | — | 认证授权 |
| Spring Data JPA | — | 数据持久化 |
| H2 / MySQL | — | 数据库（H2开发/MySQL生产） |
| Maven | 3.9+ | 构建工具 |
| WebSocket | — | 实时通信 |

### 前端 (frontend/)

| 组件 | 版本 | 用途 |
|------|------|------|
| Vue | 3.5+ | UI框架 |
| Vite | 6+ | 构建工具 |
| TypeScript | 5+ | 类型安全 |
| Element Plus | 2.9+ | UI组件库 |
| Three.js | 0.170+ | 3D渲染 |
| Pinia | 2.3+ | 状态管理 |
| ECharts | 5+ | 数据可视化 |
| PWA | — | 离线支持 |

---

## 🚀 快速开始

### 环境要求

- JDK 17+
- Node.js 18+
- Maven 3.9+（或使用 `mvnw`）

### 启动后端

```bash
cd backend
# 开发模式（H2内存数据库，自动初始化测试数据）
mvn spring-boot:run

# 生产模式（MySQL）
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

### 启动前端

```bash
cd frontend
npm install
npm run dev
```

前端运行在 `http://localhost:5173`，API 自动代理到后端 `8080` 端口。

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
docker compose up -d
```

启动 MySQL 8.0 + Spring Boot 后端 + Nginx 前端。

### 开发环境

```bash
docker compose -f docker-compose.dev.yml up -d
```

启动 MySQL + Maven 开发服务器（热重载）+ Vite 开发服务器（HMR）。

---

## 🤖 DeepSeek AI 集成

平台支持通过 DeepSeek API 进行 AI 驱动的测评分析。设置环境变量即可启用：

```bash
# Windows (PowerShell)
[System.Environment]::SetEnvironmentVariable('DEEPSEEK_API_KEY', 'sk-your-key', 'User')

# Linux/macOS
export DEEPSEEK_API_KEY=sk-your-key
```

配置 API Key 后，DeepSeek 用于：
- **测评分析**：为每个测评维度生成详细评估报告
- **学习推荐**：根据分数推荐个性化课程和学习路径

未配置 API Key 时，平台自动降级为内置模拟分析。

---

## 🌐 3D 元宇宙场景

基于 **Three.js** 构建，提供 4 种沉浸式训练环境：

| 场景 | 描述 | AI角色 |
|------|------|--------|
| 🎯 **虚拟面试** | 模拟真实面试场景，与AI面试官互动 | 面试官、HR、技术主管 |
| 📚 **虚拟课堂** | 沉浸式在线学习，AI讲师实时授课 | 讲师 |
| 🤝 **协作会议** | 团队协作模拟，锻炼沟通能力 | 最多6位参会者 |
| ⚡ **技能实训** | 专项技能训练，实时反馈指导 | 培训师、助教 |

每个场景包含：
- 逼真的3D渲染（阴影、雾效、光照）
- AI角色浮动动画
- 环境粒子效果
- OrbitControls 交互控制
- CSS2D 角色标签

---

## 💬 WebSocket 实时通信

元宇宙场景通过 WebSocket 支持多人实时互动：

| 功能 | 说明 |
|------|------|
| 房间管理 | 加入/离开房间，用户追踪 |
| 位置同步 | 广播用户移动和旋转 |
| 文字聊天 | 房间内实时消息 |
| WebRTC 信令 | 音视频通话信令转发 |
| 自动重连 | 客户端3秒自动重连 |

---

## 📖 API 接口文档

开发模式下访问 Swagger UI：

```
http://localhost:8080/swagger-ui.html
```

### 主要接口

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/auth/register` | 用户注册 |
| POST | `/api/auth/login` | 用户登录 |
| GET | `/api/users/me` | 获取当前用户 |
| GET | `/api/assessments` | 获取所有测评 |
| POST | `/api/assessments/submit` | 提交测评 |
| GET | `/api/assessments/results` | 获取测评历史（分页） |
| GET | `/api/courses` | 获取课程列表（分页、排序） |
| GET | `/api/certifications/my` | 获取我的证书 |
| GET | `/api/certifications/verify/{hash}` | 验证证书 |
| POST | `/api/metaverse/sessions` | 创建元宇宙场景 |
| WS | `/ws/metaverse` | WebSocket 实时通信 |
| GET | `/api/notifications` | 获取通知消息 |

---

## 📁 项目结构

```
MangDeHenZhi/
├── backend/                    # Spring Boot 后端
│   ├── src/main/java/com/mangdehenzhi/
│   │   ├── ai/                 # AI 分析模型
│   │   ├── blockchain/         # 区块链服务
│   │   ├── config/             # Security, JWT, CORS, 限流
│   │   ├── controller/         # REST 控制器（10个）
│   │   ├── dto/                # 数据传输对象
│   │   ├── entity/             # JPA 实体（7个）
│   │   ├── enums/              # 枚举类型
│   │   ├── exception/          # 全局异常处理
│   │   ├── metaverse/          # 元宇宙数据模型
│   │   ├── repository/         # 数据仓库（7个）
│   │   ├── service/            # 业务逻辑（9个）
│   │   └── websocket/          # WebSocket 处理器
│   └── src/main/resources/
│       └── application.yml     # 应用配置
├── frontend/                   # Vue 3 + Vite 前端
│   └── src/
│       ├── api/                # API 客户端（Axios）
│       ├── components/         # 公共组件
│       ├── composables/        # Vue 组合式函数
│       ├── router/             # 路由配置
│       ├── stores/             # Pinia 状态管理
│       ├── styles/             # 全局样式
│       ├── types/              # TypeScript 类型
│       └── views/              # 页面组件（18个）
├── docs/                       # 文档
└── JSMO-PAGE/                  # 原有营销页面（保留）
```

---

## 🧪 测试

### 后端测试（31个用例，全部通过）

```bash
cd backend && mvn test
```

| 测试类 | 用例数 | 覆盖范围 |
|--------|--------|----------|
| `AuthControllerTest` | 4 | 注册与登录 |
| `CourseControllerTest` | 2 | 课程列表 |
| `HealthControllerTest` | 1 | 健康检查 |
| `SearchControllerTest` | 2 | 课程搜索 |
| `FullFlowIntegrationTest` | 4 | 全流程集成测试 |
| `AssessmentServiceTest` | 7 | 测评提交与结果 |
| `CertificationServiceTest` | 4 | 证书签发与验证 |
| `UserServiceTest` | 7 | 用户注册与登录 |

### 前端构建

```bash
cd frontend && npm run build
```

支持 PWA，自动生成 Service Worker。

---

## 💻 开发命令

```bash
make help          # 显示所有命令
make backend-dev   # 启动后端开发服务器
make backend-test  # 运行后端测试
make frontend-dev  # 启动前端开发服务器
make frontend-build # 构建前端
make docker-up     # 启动生产 Docker 环境
make docker-dev    # 启动开发 Docker 环境
```

---

## 🔐 环境变量

复制 `.env.example` 为 `.env` 并填入实际值：

| 变量 | 必填 | 说明 |
|------|------|------|
| `MYSQL_PASSWORD` | 生产环境 | MySQL 用户密码 |
| `MYSQL_ROOT_PASSWORD` | 生产环境 | MySQL root 密码 |
| `JWT_SECRET` | 生产环境 | JWT 签名密钥（≥32字符） |
| `DEEPSEEK_API_KEY` | 可选 | DeepSeek 大模型 API Key |

---

## 🤝 贡献指南

欢迎贡献！请提交 Pull Request。

1. Fork 本仓库
2. 创建功能分支（`git checkout -b feature/amazing-feature`）
3. 提交更改（`git commit -m 'feat: add amazing feature'`）
4. 推送到分支（`git push origin feature/amazing-feature`）
5. 提交 Pull Request

---

## 📄 许可证

本项目基于 MIT 许可证发布 — 详见 [LICENSE](LICENSE) 文件。

---

<p align="center">用 ❤️ 打造</p>