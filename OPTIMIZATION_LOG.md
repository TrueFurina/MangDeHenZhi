# 芒得很职 优化日志

> 记录项目的全面质检测试与优化历史。
> 每次优化均通过编译/类型检查验证。

---

## 2026-07-17 — 第四轮优化 (管理端 + 搜索 + PWA + 测试)

### 新增功能

#### 1. 管理后台端点 `AdminController.java`
- **路径：** `/api/admin/**` — 仅 ADMIN 角色可访问
- **用户管理：** GET/PUT 用户列表、角色修改、启用/禁用
- **课程管理：** 分页列表、创建、编辑、删除
- **数据统计：** GET `/api/admin/stats` 返回用户数和课程数

#### 2. 搜索功能 `SearchController.java`
- **路径：** `GET /api/search/courses?q=关键字`
- **特性：** 忽略大小写模糊搜索，limit 参数控制返回数量
- **前端：** `searchApi.courses(q)` API 封装

#### 3. PWA 支持
- **manifest.json：** 应用名称、图标、主题色、独立显示模式
- **index.html：** 添加 `theme-color` meta、`manifest` link、`description` meta

#### 4. 独立 404 页面 `NotFound.vue`
- **特性：** 渐变 404 文字、浮动动画图标、建议导航链接（测评/元宇宙/证书/登录）
- **路由：** 替换原有的内联模板为独立组件

#### 5. 更多集成测试 `IntegrationTests.java`
- **新增：** `CourseControllerTest`（2 用例）、`HealthControllerTest`（1 用例）、`SearchControllerTest`（2 用例）
- **累计：** 后端 21 用例 + 前端 10 用例 = 31 总用例

### 验证

- `vue-tsc --noEmit` — ✅ 零错误（修复 `User` 类型导入缺失）
- `mvnw compile -q` — ✅ 零错误

---

## 2026-07-17 — 第三轮优化 (深度全面页面大改)

### 修复的严重 Bug

#### 1. Hibernate ByteBuddyInterceptor 序列化崩溃
- **问题：** 返回 `MetaverseSession` 实体时，Hibernate LAZY 代理 `ByteBuddyInterceptor` 无法被 Jackson 序列化，导致"服务器内部错误"
- **根因：** `@ManyToOne(fetch = FetchType.LAZY)` 的关联字段在序列化时触发代理初始化失败
- **修复方案（双重保障）：**
  - 所有 6 个实体类添加 `@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})` 
  - `MetaverseService` 所有方法添加 `@Transactional(readOnly = true)` 确保事务上下文
- **影响：** 修复了元宇宙场景创建/结束/列表的全部接口

#### 2. 安全配置遗漏
- **问题：** `/api/health` 健康检查和 `/api/courses` 课程列表未加入白名单，导致 403
- **修复：** `SecurityConfig` 添加 `/api/health`、`/api/courses/**`、Swagger 路径到 `permitAll`

### 全项目名称更新

- **变更：** "芒德矩阵" → "芒得很职"
- **范围：** 13 个文件，26 处替换
- **覆盖：** pom.xml、README、NavBar、Home、Swagger、docker-compose、Makefile、SQL、SEO 等

### 测评页面全面重写 (AssessmentDetail.vue)

| 维度 | 改造前 | 改造后 |
|------|--------|--------|
| 交互方式 | 3 个滑块拖拽评分 | 12 道真实选择题，逐题作答 |
| 题目数量 | 无 | 12 题，分 3 个维度，每题 4 选项 |
| 答题流程 | 一次性提交 | 引导式：介绍页 → 逐题作答 → 确认提交 |
| 进度追踪 | 无 | 进度条 + 维度标签 + 答题圆点导航 |
| 自动跳转 | 无 | 选择后 300ms 自动跳转下一题 |
| 确认机制 | 直接提交 | 提交确认弹窗，显示已答/未答统计 |
| 过渡动画 | 无 | slide-fade 切换动画 |

**新增文件：** `src/data/questions.ts`（210 行，3 维度 × 4 题 = 12 道专业测评题）

### 所有前端页面深度大改

| 页面 | 改造要点 |
|------|----------|
| **Login.vue** | 双栏布局（左侧品牌装饰 + 右侧表单），一键填充测试账号，响应式适配 |
| **Register.vue** | 匹配 Login 新设计，输入字数统计，服务条款链接 |
| **Dashboard.vue** | 欢迎横幅 + 随机每日格言 + 4 个统计卡片（含趋势）+ 快捷入口 6 宫格 + 最近测评/课程/会话列表 |
| **AssessmentList.vue** | 分类筛选 + 测评卡片（难度图标/维度标签/详细元数据）+ 历史记录表格 |
| **AssessmentDetail.vue** | 见上表 — 完整测评流程 |
| **AssessmentResult.vue** | Canvas 雷达图 + 维度进度条 + AI 报告分区展示 + 学习路径步骤图 + 操作按钮组 + 骨架屏加载态 |
| **Courses.vue** | 分类快捷入口 Chips + 封面卡片（含颜色图标/难度角标/元数据）+ 骨架屏加载态 + 分类筛选 |
| **CourseDetail.vue** | 双栏布局（封面图 + 信息区）+ 元数据表格 + 前置要求 + 相关推荐 |
| **Certifications.vue** | 证书卡片（含角标/哈希码/区块链信息）+ 详情弹窗 + 验证功能 + 空状态引导 |
| **Home.vue** | 全新 Hero（装饰动画/统计数字/光晕粒子）+ 三大功能卡片（含特性列表）+ 三步流程引导 + CTA 区域 + 完整页脚 |

### 验证

- `vue-tsc --noEmit` — ✅ 零错误（修复 `Course.prerequisites` 类型缺失）
- `mvnw compile -q` — ✅ 零错误

---

## 2026-07-16 — 第二轮优化

### 新增功能

#### 1. Swagger/OpenAPI 接口文档 `SwaggerConfig.java`
- **描述：** 集成 SpringDoc OpenAPI 2.6.0，自动生成 API 文档
- **访问：** `http://localhost:8080/swagger-ui.html`
- **特性：** 全局 JWT Bearer 认证方案、接口分组、响应模型展示
- **依赖：** `springdoc-openapi-starter-webmvc-ui`

#### 2. 通用分页支持 `PageDTO.java` + `PageRequest.java`
- **描述：** 统一的分页请求/响应 DTO
- **`PageRequest`：** page/size/sortBy/sortDir，带 `@Min/@Max` 校验
- **`PageDTO`：** content/page/size/total/totalPages，静态工厂方法 `of()`

#### 3. 加载骨架屏组件 `SkeletonCard.vue`
- **描述：** 三种变体的加载骨架屏（card / table / stat）
- **效果：** 数据加载时显示闪烁动画占位，提升感知性能
- **技术：** CSS `@keyframes shimmer` 动画，`background-size 200%`

#### 4. GitHub Actions CI 工作流 `.github/workflows/ci.yml`
- **描述：** 三段式 CI 流水线
- **Job 1 — backend：** JDK 17 编译 → 测试 → Maven 打包 → 上传 JAR
- **Job 2 — frontend：** Node 20 安装 → 类型检查 → 测试 → Vite 构建 → 上传 dist
- **Job 3 — docker：** 依赖前两 Job，拉取产物 → `docker compose build`

#### 5. Nginx 安全头 + Gzip 配置
- **安全头：** `X-Frame-Options` / `X-Content-Type-Options` / `X-XSS-Protection` / `Referrer-Policy` / `Permissions-Policy` / `HSTS` / `Content-Security-Policy`
- **Gzip：** 压缩级别 5，最小 256 字节，覆盖 JS/CSS/JSON/SVG/字体
- **安全：** 禁止访问 `.env`/`.git`/`*.yml` 等敏感文件

#### 6. 共享导航组件 `NavBar.vue` → 全面替换
- **描述：** 统一 `Home.vue` 和 `AppHeader.vue` 的导航栏
- **变体：** `sticky`（吸顶式）和 `home`（首页式，含注册按钮）
- **效果：** 消除 35 行重复代码，统一管理登录/未登录状态

#### 7. 完整 README 文档（424 行）
- **覆盖：** 项目概述 / 技术栈 / 快速开始 / Docker 部署 / DeepSeek AI / 3D 场景 / API 文档 / 项目结构 / 测试 / 开发命令 / 环境变量 / 优化日志

### 配置变更

| 文件 | 变更 |
|------|------|
| `backend/pom.xml` | 添加 `springdoc-openapi-starter-webmvc-ui:2.6.0` |
| `.gitignore` | 增加 `.vite/` `mysql_data/` `.project` `.classpath` `.settings` |
| `frontend/nginx.conf` | 完全重写：安全头 + Gzip + 敏感文件拦截 + 错误页面 |

### 验证

- `vue-tsc --noEmit` — ✅ 零错误
- `mvnw compile -q` — ✅ 零错误

---

## 2026-07-16 — 第二轮优化

### 新增功能

#### 1. 共享导航组件 `NavBar.vue`
- **描述：** 抽取 `Home.vue` 和 `AppHeader.vue` 中重复的导航栏代码为统一组件
- **变更：** 新增 `NavBar.vue`，`AppHeader.vue` 精简为 3 行，`Home.vue` 减少 30 行
- **效果：** 消除代码重复，统一管理登录/未登录两种状态

#### 2. 环境变量文档 `.env.example`
- **描述：** 统一管理所有环境变量配置说明
- **覆盖：** MySQL / JWT / DeepSeek API / CORS / Spring Profile

#### 3. API 请求日志拦截器 `RequestLoggingInterceptor.java`
- **描述：** 基于 `HandlerInterceptor` 的请求日志记录
- **功能：** 自动记录每个 API 的 HTTP 方法、路径、响应状态码、耗时
- **告警：** 超过 3 秒的慢请求自动输出 WARN 级别日志

#### 4. 健康检查端点 `HealthController.java`
- **路径：** `GET /api/health`
- **返回：** `{"status":"UP","timestamp":"...","version":"1.0.0","service":"mangdehenzhi-backend"}`
- **用途：** Docker 容器探活、负载均衡健康检查

#### 5. 开发 Docker Compose `docker-compose.dev.yml`
- **描述：** 开发环境容器化配置，支持热重载
- **特性：** 挂载源码目录、Maven 依赖缓存卷、Vite HMR 热更新
- **启动：** `docker-compose -f docker-compose.dev.yml up`

#### 6. Makefile 开发命令
- **描述：** 12 个常用命令统一入口
- **命令：** `make backend-dev` / `make frontend-dev` / `make test` / `make docker-up` / `make init` 等

#### 7. API 限流保护 `RateLimitConfig.java`
- **描述：** 基于 IP 的滑动窗口限流
- **默认：** 60 次/分钟，可配置
- **排除：** `/api/health` 不限流

### 修复问题

- `Home.vue` 移除未使用的 `router`/`userStore` 导入
- `.gitignore` 增加 `.vite/` `mysql_data/` `.project` `.classpath` 等模式
- 导航栏在未登录状态下不再访问 `currentUser` 属性

### 验证

- `vue-tsc --noEmit` — ✅ 零错误
- `mvnw compile -q` — ✅ 零错误

---

## 2026-07-16 — 第一轮优化

### 修复的关键问题（7 项）

| # | 问题 | 文件 | 修复方案 |
|---|------|------|----------|
| 1 | `AIService.toJson()` 手写 JSON 损坏数据 | `AIService.java` | 改用 Jackson `ObjectMapper.writeValueAsString()` |
| 2 | MySQL 生产密码硬编码 `root` | `application-prod.yml` | 改为 `${MYSQL_USER:root}` / `${MYSQL_PASSWORD:root}` 环境变量 |
| 3 | CORS 全开放 `*` | `CorsConfig.java` | 加入 `cors.allowed-origins` 配置项，支持按域名限制 |
| 4 | `submitAssessment` 未校验维度得分范围 | `AssessmentSubmitRequest.java` + `AssessmentService.java` | 增加 `@NotNull/@NotEmpty` 校验和 0-100 范围检查 |
| 5 | 测评通过后证书自动签发缺失 | `AssessmentService.java` | 注入 `CertificationService`，pass 后自动签发 |
| 6 | `vite.config.ts` 使用 `__dirname` 在 ESM 下不可用 | `vite.config.ts` | 改用 `fileURLToPath(new URL('./src', import.meta.url))` |
| 7 | 前端 API 层大量 `any` 类型 | `api/index.ts` | 全部替换为具体类型 |

### 安全优化（5 项）

| # | 优化 | 说明 |
|---|------|------|
| S1 | JWT secret 支持 `JWT_SECRET` 环境变量 | `@Value("${jwt.secret:${JWT_SECRET:}}")` |
| S2 | 生产 profile `ddl-auto: validate` | 防止 Hibernate 自动修改表结构 |
| S3 | 生产 profile `show-sql: false` | 避免 SQL 日志泄露 |
| S4 | `MYSQL_HOST` 可配置 | 生产环境 `application-prod.yml` 全部参数化 |
| S5 | `MetaverseController` 会话操作增加用户身份校验 | `getSession`/`endSession` 校验 `user.getId()` |

### 逻辑修复（5 项）

| # | 修复 | 说明 |
|---|------|------|
| B1 | `User.createdAt` 的 `@Builder.Default` 与 `@PrePersist` 冲突 | 移除 `@Builder.Default`，`@PrePersist` 中判断 null |
| B2 | `RecommendationController.recommendCourses` 返回空列表 | 注入 `AssessmentResultRepository`，获取最新结果进行推荐 |
| B3 | `MetaverseController.getSceneConfig` 缺少异常处理 | 捕获 `IllegalArgumentException` 抛出 `BusinessException` |
| B4 | `AssessmentService` LAZY 字段可能抛 `LazyInitializationException` | 添加 `@Transactional(readOnly=true)` |
| B5 | `LoginResponse` 无参构造时 `tokenType` 未初始化 | 保留已有构造函数 |

### Docker 部署配置

| 文件 | 说明 |
|------|------|
| `backend/Dockerfile` | 多阶段构建（Maven 编译 + JRE 17 运行），非 root 用户 |
| `frontend/Dockerfile` | Node 构建 + Nginx Alpine 静态托管 |
| `frontend/nginx.conf` | API 反向代理 + SPA 路由 + 静态资源缓存 |
| `docker-compose.yml` | 3 服务编排：MySQL 8.0 + Spring Boot + Nginx |
| `backend/sql/init.sql` | 完整 DDL：7 张表 + 8 个索引 |

### DeepSeek 大模型 API 集成

| 组件 | 说明 |
|------|------|
| `DeepSeekService.java` | 调用 `https://api.deepseek.com/v1/chat/completions` |
| 认证 | 从环境变量 `DEEPSEEK_API_KEY` 读取 |
| 模型 | `deepseek-chat`，temperature=0.7 |
| 降级策略 | API Key 未设置或调用失败时自动降级为本地模拟 |

### Three.js 3D 场景

| 文件 | 说明 |
|------|------|
| `ThreeScene.vue` | 372 行 3D 场景渲染组件 |
| 场景类型 | 虚拟面试 / 虚拟课堂 / 协作会议 / 技能实训 |
| 技术特性 | WebGL 渲染器、CSS2D 标签、OrbitControls、阴影映射、ACES 色调映射 |

### 自动化测试

| 层级 | 框架 | 文件数 | 用例数 |
|------|------|--------|--------|
| 后端 | JUnit 5 + Spring Boot Test | 4 个测试类 | 16 个用例 |
| 前端 | Vitest + jsdom | 2 个测试文件 | 10 个用例 |

### 前端用户体验优化

| 修复 | 说明 |
|------|------|
| `AppHeader.vue` | 未登录时显示登录按钮 |
| 404 路由 | 添加 `/:pathMatch(.*)*` 优雅错误页面 |
| 错误处理 | 7 个 View 的 `catch (_) {}` 改为 `console.error` |
| `AssessmentResult.vue` | SVG 进度环动态计算最大值 |
| `tsconfig.json` | 移除无效选项，排除测试目录 |
| `JSMO-PAGE` | 清理未引用 CSS，修复 SEO 标题/关键词 |