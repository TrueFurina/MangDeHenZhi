# 芒得很职（Mangdehenzhi）安全审计报告

> 审计范围：OWASP Top 10 + STRIDE 威胁建模
> 审计对象：`E:\Code\Mangdehenzhi`（全栈应用：Spring Boot 3.3.5 / Java 17 后端 + Vue3 前端 + Docker 编排）
> 审计方式：源码静态审计（实际读取配置、入口、路由、关键逻辑与依赖清单），未执行任何外部 LLM API 调用，未修改任何代码。
> 审计日期：2026-07-17

---

## 一、目录可访问性与说明

| 目录 / 文件 | 状态 | 说明 |
|---|---|---|
| `backend/` | ✅ 已审计 | Spring Boot 源码、配置、`pom.xml`、`sql/`、`Dockerfile` |
| `frontend/` | ✅ 已审计 | Vue3 + Vite 工程、`package.json`、`vite.config.ts`、`nginx.conf`、源码 |
| `docker-compose.yml` / `docker-compose.dev.yml` | ✅ 已审计 | 容器编排与密钥默认值 |
| `后台管理系统/` | ⚠️ 参考代码，未编译 | 两个 `.java` 文件含**重复类定义**（同一文件内 `BlockchainCertificationService` 定义了 4 次），**不在 Maven 构建路径内**（包名非 `com.mangdehenzhi`），不会参与编译/运行，视为设计参考片段，不直接构成运行时风险，但集成时需补齐鉴权 |
| `index/` | ✅ 已审计 | `元宇宙场景交互核心代码.js` 为独立 Three.js 片段，无密钥/注入问题 |
| `JSMO-PAGE/` | ✅ 已审计 | 前端页面资源（html/js/css/lib），未发现硬编码密钥 |
| `.env` | ✅ 不存在 | 仓库仅含 `.env.example`（已被 `.gitignore` 忽略 `.env`，无真实密钥入库，良好） |
| `target/`、`node_modules/`、`dist/` | ⚠️ 构建产物 | 已存在但被 gitignore，其中 `target/classes/application.yml` 含提交的 JWT 密钥（镜像内生效） |

**结论前提**：默认开箱部署（`docker compose up`）即使用提交的 JWT 密钥与弱口令，因此多数高危项在"默认配置"下即可被利用。

---

## 二、总体安全态势评分

| 等级 | 数量 |
|---|---|
| 🔴 Critical | 3 |
| 🟠 High | 5 |
| 🟡 Medium | 10 |
| 🟢 Low / Info | 8 |
| **合计** | **26** |

**总体评级：D（高风险）**。存在可被直接利用的身份鉴权绕过（提交式 JWT 密钥 + 默认管理员口令）与未授权写操作，建议在上线前必须修复全部 🔴 项与大部分 🟠 项。

---

## 三、发现表（按严重度排序）

> 每条含：位置 / 问题描述 / 修复建议 / 关联 OWASP / STRIDE / 置信度(0-10)

### 🔴 CRITICAL

#### F-001 提交的 JWT 签名密钥（可被伪造任意令牌 → 完全认证绕过 / 提权）
- **位置**：
  - `backend/src/main/resources/application.yml:36`（默认 `jwt.secret: mangdehenzhi-jwt-secret-key-2026-very-long-secret-for-security`）
  - `docker-compose.yml:40`（默认 `JWT_SECRET: mangdehenzhi-jwt-secret-key-2026-very-long-secret-for-security`）
  - `.env.example:15`、`backend/Dockerfile:36`（`ENV JWT_SECRET=""` 会被 compose 的默认值覆盖）
- **问题**：生产镜像经 docker-compose 启动时，`JWT_SECRET` 环境变量即为上述**公开字符串**（HS256 对称密钥）。攻击者在 GitHub 上即可读到该密钥，自行签发 `userId=admin` 的合法 JWT，实现任意用户冒充与管理员提权。
- **修复建议**：
  1. 从 `application.yml` 中删除默认密钥，`@Value` 在 prod 下若缺失则**启动失败**（fail-fast）。
  2. 通过密钥管理器/部署机密注入强随机密钥（`openssl rand -base64 48`），长度 ≥ 32 字节。
  3. 立即轮换并让旧令牌失效；考虑升级为非对称算法（RS256/ES256）。
- **OWASP**：A01 失效的访问控制 / A02 加密失败 — **STRIDE**：Spoofing、Tampering — **置信度 10**

#### F-002 默认种子管理员口令（admin/admin123、teacher/teacher123、student/student123）
- **位置**：`backend/src/main/java/com/mangdehenzhi/config/DataInitializer.java:43-68`
- **问题**：首次启动向数据库写入三个弱口令账户，口令明文写在源码中且一并提交。任何部署实例均可用 `admin/admin123` 直接登录。
- **修复建议**：
  1. 仅在 `dev` Profile 下种子化测试数据；生产禁用。
  2. 生产首个管理员口令随机生成并仅打印到安全日志，强制首次登录改密。
  3. 不在源码中保留任何默认口令。
- **OWASP**：A07 认证与标识失败 — **STRIDE**：Spoofing — **置信度 10**

#### F-003 未授权课程创建（匿名可写 → 数据完整性破坏）
- **位置**：
  - `backend/.../config/SecurityConfig.java:30`（`/api/courses/**` 设为 `permitAll()`）
  - `backend/.../controller/CourseController.java:35-38`（`createCourse` 无任何鉴权/角色校验）
- **问题**：`POST /api/courses` 对匿名用户开放，任何人可往课程目录注入/篡改内容，属典型的失效访问控制（未授权写操作）。
- **修复建议**：
  1. 将 `/api/courses/**` 从 `permitAll` 移除，要求认证。
  2. 创建/修改/删除仅限 `ADMIN`/`TEACHER`（见 F-004 启用方法级鉴权）。
  3. 保留只读列表（`GET`）对公开用户开放即可。
- **OWASP**：A01 失效的访问控制 — **STRIDE**：Elevation of Privilege、Tampering — **置信度 10**

---

### 🟠 HIGH

#### F-004 完全缺失方法级鉴权，角色（ADMIN/TEACHER）从未被强制
- **位置**：
  - `SecurityConfig.java`（无 `@EnableMethodSecurity`，无任何 `@PreAuthorize`/`hasRole`——全局 grep 无匹配）
  - `JwtAuthenticationFilter.java:39-42`（构造 `Authentication` 时 `authorities = new ArrayList<>()`，**始终为空**）
- **问题**：用户角色仅作为数据库字段存在，安全上下文不含任何权限；即便开启 `@PreAuthorize` 也会因无 authorities 全部拒绝。结果是"管理员"概念形同虚设，所有写接口要么匿名开放（F-003），要么只要有任意合法令牌即可调用。
- **修复建议**：
  1. 在 `SecurityConfig` 增加 `@EnableMethodSecurity`。
  2. `JwtAuthenticationFilter` 中依据 `user.getRole()` 填充 `SimpleGrantedAuthority("ROLE_"+role)`。
  3. 对管理类端点加 `@PreAuthorize("hasRole('ADMIN')")`。
- **OWASP**：A01 失效的访问控制 — **STRIDE**：Elevation of Privilege — **置信度 10**

#### F-005 IDOR：任意用户可读取他人测评结果（含分数与 AI 分析）
- **位置**：
  - `AssessmentController.java:47-50`（`GET /api/assessments/results/{resultId}`）
  - `AssessmentService.getResultById`（仅按 `resultId` 查，无 owner 过滤）
  - `AssessmentResultDTO` 含 `userId`、`username`、`dimensionScores`、`aiAnalysis`、`recommendations`
- **问题**：登录用户传入任意 `resultId` 即可读取其他用户的完整测评成绩与 AI 报告，构成隐私泄露（BOLA/IDOR）。
- **修复建议**：查询时强制 `WHERE user_id = 当前用户ID`；非本人返回 403；公开验证场景改用证书哈希而非顺序 ID。
- **OWASP**：A01 失效的访问控制（BOLA）— **STRIDE**：Information Disclosure — **置信度 10**

#### F-006 IDOR / PII 泄露：任意用户可查他人邮箱与手机号
- **位置**：`UserController.java:24-27`（`GET /api/users/{id}` → `UserService.getUserById`）；`UserDTO` 暴露 `email`、`phone`、`role`
- **问题**：任何登录用户传任意 `id` 即可枚举全站用户 PII（邮箱/手机号/角色），且无需本人或管理员权限。
- **修复建议**：仅允许查询本人（`=当前用户ID`）或由 `ADMIN` 查询；对外 DTO 剥离 `email`/`phone` 等非必要字段。
- **OWASP**：A01 / A04 不安全设计 — **STRIDE**：Information Disclosure — **置信度 9**

#### F-007 数据库 3306 与后端 8080 直接映射到宿主机
- **位置**：`docker-compose.yml:18`（`3306:3306`）、`:46`（`8080:8080`）；`docker-compose.dev.yml` 同
- **问题**：MySQL 与后端在宿主机端口直接暴露，绕过 Nginx 安全头与内网隔离；若部署机有公网 IP，数据库可被直接暴力/未授权访问。
- **修复建议**：移除 `db` 的宿主机端口映射（仅保留容器间网络）；后端不对外暴露，仅经 Nginx 反向代理；确需远程管理请走 SSH 隧道或 VPN。
- **OWASP**：A05 安全配置错误 — **STRIDE**：Information Disclosure、Tampering — **置信度 9**

#### F-008 无传输层加密（仅 HTTP，JWT/凭据明文传输）
- **位置**：`docker-compose.yml`（frontend `80:80`、backend `8080:8080`）；`frontend/nginx.conf:2`（`listen 80`）；`nginx.conf:11`（HSTS 写在 HTTP 上无效）
- **问题**：全链路无 TLS，JWT 与登录凭据以明文在网络中传输，易被中间人劫持/重放。HSTS 头在 HTTP 下被浏览器忽略，形同虚设。
- **修复建议**：在反向代理/负载均衡处终止 TLS（443），HTTP→HTTPS 重定向；移除 HTTP 上的 HSTS；内部服务间如跨主机也建议 mTLS。
- **OWASP**：A02 加密失败 — **STRIDE**：Information Disclosure、Tampering — **置信度 9**

---

### 🟡 MEDIUM

#### F-009 通配符 CORS 配合凭据（credentials + `*`）
- **位置**：`CorsConfig.java:15-34`（`CORS_ALLOWED_ORIGINS` 默认 `*`，`setAllowCredentials(true)`，`addAllowedOriginPattern("*")`）；`.env.example:23`
- **问题**：默认允许任意来源携带凭据。`addAllowedOriginPattern("*")` 在允许凭据时会反射请求 Origin。当前鉴权走 `Authorization: Bearer`（非 Cookie），实际利用受限，但仍属错误配置，一旦引入 Cookie 鉴权即升级为严重。
- **修复建议**：显式列出受信来源清单；非必要不开启 `allowCredentials`；前端/后端同源部署可去掉 CORS。
- **OWASP**：A05 安全配置错误 — **STRIDE**：Spoofing — **置信度 8**（受 Bearer 机制缓解）

#### F-010 限流器信任可伪造的 X-Forwarded-For（暴力破解保护可被绕过）
- **位置**：`RateLimitConfig.java:75-81`（`getClientIp` 优先取 `X-Forwarded-For` 首段）
- **问题**：客户端可随意伪造 `X-Forwarded-For` 头部轮换 IP，使基于 IP 的限流（含 `/api/auth/login`）完全失效，登录爆破无法被限制。此外为内存 `Map`，条目无主动淘汰，长期运行有内存增长风险。
- **修复建议**：仅在可信代理后从 `X-Forwarded-For` 取 IP；或对登录接口做**按账户**限流 + 失败锁定 + 验证码；限流计数增加过期清理。
- **OWASP**：A07 认证与标识失败 — **STRIDE**：Denial of Service — **置信度 9**

#### F-011 开发环境 H2 控制台免认证开放 + 全局关闭 frameOptions
- **位置**：`application.yml:12-15`（h2 console `enabled: true`）；`SecurityConfig.java:31`（/h2-console/** `permitAll`）、`:36`（`frameOptions(frame -> frame.disable())` 作用于**所有**响应）
- **问题**：dev Profile 下 H2 控制台无认证即可访问，若 dev 服务可达即可直接操作数据库；且 `frameOptions` 全局禁用削弱了全站点击劫持防护。
- **修复建议**：生产/默认关闭 H2 控制台；如保留仅供本地，`permitAll` 必须移除；`frameOptions` 仅对 H2 路径禁用，其余响应保留 `DENY`/`SAMEORIGIN`。
- **OWASP**：A05 安全配置错误 — **STRIDE**：Tampering — **置信度 9**

#### F-012 Swagger/OpenAPI 文档免认证暴露
- **位置**：`SecurityConfig.java:33`（`/swagger-ui/**`、`/v3/api-docs/**` `permitAll`）；`SwaggerConfig.java`
- **问题**：完整 API 契约（端点、参数、结构）对匿名公开，便于攻击者绘制攻击面。
- **修复建议**：仅 `dev` Profile 开放，或要求认证；生产移除 `springdoc` 相关端点。
- **OWASP**：A05 安全配置错误 — **STRIDE**：Information Disclosure — **置信度 9**

#### F-013 "区块链认证"校验为模拟实现，恒返回 `verified:true`（完整性控制缺失）
- **位置**：`BlockchainService.java:43-56`（`verifyOnChain` 直接返回硬编码 `verified:true`）、`:22-36`（`storeOnChain` 返回伪造 `txId`）；`CertificationService.java:39-41`（上链调用被注释）
- **问题**：证书"上链存证/验证"并未真正接入链，`verifyOnChain` 对任意输入均返回已验证，违背了"区块链不可篡改"的完整性承诺（属软件与数据完整性失败）。当前 `verifyOnChain` 未被控制器直接调用，但设计脆弱，一旦接入即产生伪造验证风险。
- **修复建议**：实现真实链码校验或移除"区块链背书"声明；绝不基于未经验证的本地常量返回 `verified`；验证须有可独立核验的证据（链上交易/签名）。
- **OWASP**：A08 软件与数据完整性失败 — **STRIDE**：Tampering、Repudiation — **置信度 8**

#### F-014 JWT 存于 localStorage（XSS 可窃取令牌）
- **位置**：`frontend/src/stores/user.ts:14,26`、`frontend/src/api/index.ts:13,25`
- **问题**：令牌与用户信息存 `localStorage`，任何 XSS（即便 CSP 已缓解）均可读取并外传令牌。
- **修复建议**：改用 `HttpOnly` + `Secure` + `SameSite` Cookie；或短时效令牌 + 刷新机制；保持严格 CSP（`script-src 'self'`，禁用 `unsafe-inline`/`eval`）。
- **OWASP**：A07 / XSS 影响面 — **STRIDE**：Information Disclosure — **置信度 8**

#### F-015 前端依赖 axios ^1.7.7 存在已知 SSRF CVE（需升级）
- **位置**：`frontend/package.json:18`
- **问题**：`axios < 1.8.0`（及后续多个版本）存在 SSRF/代理绕过类 CVE；`^1.7.7` 可能解析到仍受影响的 1.7.x。需以 `npm audit` 确认。
- **修复建议**：升级 axios 至最新 1.12.x（至少 ≥ 1.8.0）；定期 `npm audit` 并锁定 `package-lock.json`。
- **OWASP**：A06 易受攻击的组件 — **STRIDE**：Tampering — **置信度 6**（取决于实际解析版本）

#### F-016 AI 提示词注入（不可信数据经 String.format 拼入 LLM 提示）
- **位置**：`DeepSeekService.java:57-72`、`83-98`（`String.format` 将 `title`、`dimensionScores` 直接插值进提示词）
- **问题**：测评标题/维度名称以字符串插值方式进入系统提示上下文，若将来支持用户自定义测评标题，可由提示注入操纵 AI 输出。当前因标题由种子数据控制，可利用性低，但属设计缺陷。
- **修复建议**：用结构化消息分离 system/user 内容，对用户控制字段做白名单/转义；对 AI 输出做 JSON Schema 校验，禁止直接执行/渲染未净化内容。
- **OWASP**：A03 注入（LLM01）— **STRIDE**：Tampering — **置信度 6**（当前可利用性低）

#### F-017 出站 DeepSeek 调用无超时（资源耗尽/DoS）
- **位置**：`DeepSeekService.java:39-49`（`RestClient.builder()` 未设置 connect/read 超时）
- **问题**：上游不可达/慢响应时线程被无限期占用，可能耗尽连接/线程池导致拒绝服务。
- **修复建议**：为 `RestClient` 配置连接与读取超时（如 5s/10s），并加熔断/降级（当前已有本地降级，但无超时保护）。
- **OWASP**：A04 不安全设计 — **STRIDE**：Denial of Service — **置信度 7**

#### F-018 提交的弱数据库口令默认值（root123 / mangdehenzhi123）
- **位置**：`docker-compose.yml:13,16,44`；`docker-compose.dev.yml`；`.env.example:11`
- **问题**：MySQL root 与应用账户使用弱口令默认值，且提交在仓库中。
- **修复建议**：通过机密管理注入强随机口令，移除所有默认值；应用账户遵循最小权限。
- **OWASP**：A05 / A07 — **STRIDE**：Spoofing — **置信度 9**

---

### 🟢 LOW / INFO

#### F-019 CI 动作仅固定到可变主版本标签，无 SHA 锁定，无镜像扫描
- **位置**：`.github/workflows/ci.yml`（`actions/checkout@v4` 等仅主版本）
- **问题**：标签可被维护者移动，存在供应链投毒风险；流水线无容器镜像漏洞扫描（Trivy 等）。
- **修复建议**：动作按 commit SHA 锁定；增加 `trivy`/`docker scan` 步骤；CI 中无密钥泄露（未发现 `secrets.` 使用，良好）。
- **OWASP**：A06 / A08 — **置信度 7**

#### F-020 Vite 代理目标端口与生产后端不一致（功能/配置问题）
- **位置**：`frontend/vite.config.ts:16`（proxy `/api` → `http://localhost:9997`，但后端/compose 监听 `8080`）
- **问题**：开发态前端代理指向 9997，而后端实际在 8080，导致本地联调失败（非安全漏洞，但属配置缺陷）。
- **修复建议**：统一端口（建议 `5173 → 8080` 或经 Nginx）。
- **置信度 8**

#### F-021 注册接口用户/邮箱枚举
- **位置**：`UserService.java:22-27`（"用户名已存在"/"邮箱已被注册"）
- **问题**：可被用于批量枚举已注册账号。
- **修复建议**：统一返回模糊提示、加限流/验证码。
- **OWASP**：A07 — **置信度 8**

#### F-022 后台管理系统代码未编译且缺失鉴权（参考片段）
- **位置**：`后台管理系统/区块链认证系统.java`、`职业技能测评.java`
- **问题**：同一文件重复定义类、不在构建路径，无法直接运行；若未来集成，当前无任何鉴权与输入校验。
- **修复建议**：如要复用，移入 `backend/src` 并接入 Spring Security + 输入校验；否则从仓库移除，避免误导。
- **置信度 8**（非运行时风险）

#### F-023 证书公开验证接口需鉴权（设计小瑕疵）
- **位置**：`CertificationController.java:28-33`（`/api/certifications/verify/{certHash}` 未被 `permitAll`）
- **问题**：证书验证本应公开，却要求登录令牌，影响公开核验体验（非安全漏洞）。
- **修复建议**：将验证端点加入公开白名单。
- **置信度 8**

#### F-024 场景类型错误回显用户输入（轻微信息泄露）
- **位置**：`MetaverseController.java:67`（"无效的场景类型: " + `sceneType`）
- **问题**：用户输入被原样拼入异常消息，存在反射式回显。
- **修复建议**：使用固定错误信息，不回显原始输入。
- **置信度 7**

#### F-025 验证证书时在 GET 请求中变更状态（非幂等）
- **位置**：`CertificationService.java:45-52`（`verifyCertification` 在 GET 中写 `verifiedAt`/`status`）
- **问题**：GET 产生副作用，缓存/代理可能误判；与 REST 语义不符。
- **修复建议**：改为 POST/PUT 或纯查询，状态变更走独立写接口。
- **置信度 7**

#### F-026 HTTP 上声明 HSTS（无效）+ 日志可能含请求 URI（低危日志注入）
- **位置**：`frontend/nginx.conf:11`；`RequestLoggingInterceptor.java:42-45`
- **问题**：HTTP 下 HSTS 无效；请求 URI 直接写入日志，极端情况下可构造日志伪造（风险低）。
- **修复建议**：HSTS 仅在 HTTPS 启用；日志对 URI 做裁剪/转义。
- **置信度 6**

---

## 四、STRIDE 威胁建模（核心数据流：前端 → Nginx → 后端 → MySQL）

```
[浏览器/Vue SPA] --HTTPS(缺失)--> [Nginx :80] --/api--> [Spring Boot :8080] --JPA--> [MySQL :3306]
                                   (CSP/HSTS)        (JWT Bearer)              (BCrypt)
                                                        |--> [DeepSeek API] (AI分析)
                                                        |--> [区块链(模拟)] (证书存证)
```

| STRIDE | 威胁场景 | 对应发现 | 缓解是否已具备 |
|---|---|---|---|
| **Spoofing（伪装）** | 用提交式 JWT 密钥伪造 admin 令牌；用默认 admin/admin123 登录 | F-001, F-002, F-009 | ❌ 密钥/口令可被知晓；建议密钥外部注入+强口令 |
| **Tampering（篡改）** | 匿名 `POST /api/courses` 注入课程；模拟区块链返回 `verified:true`；XFF 伪造绕过限流后暴力改密 | F-003, F-013, F-010 | ❌ 写操作无鉴权；⚠️ 区块链校验为假 |
| **Repudiation（抵赖）** | 证书"上链"但无真实链上证据，无法独立核验；GET 验证改状态 | F-013, F-025 | ❌ 完整性证据缺失 |
| **Information Disclosure（信息泄露）** | IDOR 读他人测评/用户 PII；Swagger 暴露全 API；DB/后端端口外露；JWT 明文传输；localStorage 令牌 | F-005, F-006, F-012, F-007, F-008, F-014 | ⚠️ 部分（nginx 头）有；核心 IDOR 无 |
| **Denial of Service（拒绝服务）** | XFF 绕过限流后爆破；DeepSeek 无超时致线程耗尽；H2 控制台暴露 | F-010, F-017, F-011 | ⚠️ 有限流但可被绕过；无超时 |
| **Elevation of Privilege（提权）** | 角色从未强制（无方法级鉴权、authorities 为空）；匿名即能写 | F-004, F-003 | ❌ 角色为装饰字段 |

**信任边界**：浏览器↔Nginx（无 TLS）、Nginx↔后端（内部但后端也直暴露）、后端↔MySQL（口令弱且端口外露）、后端↔DeepSeek（密钥来自环境变量，未落库，良好）。最大风险集中在"身份认证与授权"信任边界被完全击穿（F-001/F-002/F-004）。

---

## 五、OWASP Top 10 检查表

| # | 类别 | 结论 | 关键发现 |
|---|---|---|---|
| A01 | 失效的访问控制 | 🔴 严重 | F-003 匿名建课、F-004 无方法鉴权、F-005/F-006 IDOR、F-001 令牌伪造 |
| A02 | 加密失败 | 🔴 严重 | F-001 提交式对称密钥、F-008 无 TLS、F-018 弱口令 |
| A03 | 注入 | 🟢 低风险 | 后端全程 JPA 参数化（无 `@Query`/原生 SQL/Statement），**未发现 SQL 注入**；F-016 AI 提示词注入（设计缺陷，当前低可利用） |
| A04 | 不安全设计 | 🟠 较高 | F-006 PII 暴露设计、F-017 无超时、F-008 无传输安全 |
| A05 | 安全配置错误 | 🟠 较高 | F-007 端口暴露、F-009 通配 CORS、F-011 H2 控制台、F-012 Swagger 暴露 |
| A06 | 易受攻击组件 | 🟡 中 | F-015 axios CVE、F-019 CI 动作未锁 SHA；Spring Boot 3.3.5/pom 依赖建议做 `mvn dependency-check` |
| A07 | 认证与标识失败 | 🔴 严重 | F-002 默认口令、F-010 限流绕过、F-014 令牌存储、F-021 枚举 |
| A08 | 软件与数据完整性失败 | 🟠 较高 | F-013 模拟区块链校验、F-019 无镜像签名/扫描 |
| A09 | 安全日志与监控失败 | 🟡 中 | 有请求日志与慢请求告警，但无安全事件（登录失败/越权）专门审计与告警；日志含 URI（F-026） |
| A10 | SSRF | 🟢 低风险 | 后端仅对固定 `https://api.deepseek.com` 发起请求，无用户可控 URL；需关注 Hutool `HttpUtil` 若后续被引入（当前未使用） |

---

## 六、正向控制（亮点，予以保留）

- 密码使用 **BCrypt** 哈希存储（`SecurityConfig` / `UserService`）。✅
- 数据访问**全程 JPA 参数化**，无手写 SQL/命令执行，SQL 注入风险低。✅
- 登录失败返回**统一模糊错误**，不泄露用户是否存在（登录路径）。✅
- 前端 **CSP `script-src 'self'`、X-Frame-Options、X-Content-Type-Options、Referrer-Policy、Permissions-Policy** 等安全头齐备（nginx）。✅
- `Metaverse` 相关端点**正确做了 owner 校验**（403 越权保护）。✅
- 真实 `.env` 未被提交，密钥经环境变量注入的思路正确（仅默认值不安全）。✅
- CI 使用 `npm ci` + 锁文件，后端以**非 root 用户**运行镜像。✅
- 存在基于 IP 的限流与请求日志（虽可被绕过，但方向正确）。✅

---

## 七、修复路线图（优先级）

**P0 — 上线前必须修复（🔴）**
1. F-001：移除提交式 JWT 密钥，prod 缺失即启动失败，外部注入强随机密钥并轮换。
2. F-002：生产禁止种子弱口令，强制首登改密。
3. F-003 + F-004：启用方法级鉴权，填充 authorities，课程写操作限 ADMIN/TEACHER。

**P1 — 本迭代修复（🟠）**
4. F-005 / F-006：测评结果与用户查询加 owner/角色过滤，剥离 PII。
5. F-007：移除 DB/后端宿主机端口映射，仅经 Nginx 暴露。
6. F-008：启用 TLS（443）+ HTTP→HTTPS 重定向，HSTS 仅 HTTPS。

**P2 — 近期加固（🟡）**
7. F-009 显式 CORS 来源；F-010 按账户限流+失败锁定；F-011 关 H2 控制台；F-012 收起 Swagger；F-013 真实区块链校验或去除声明；F-014 令牌改 HttpOnly Cookie；F-015 升级 axios；F-016 结构化提示+输出校验；F-017 出站超时；F-018 强随机口令。

**P3 — 持续改进（🟢）**
8. F-019 SHA 锁定 CI 动作 + 镜像扫描；F-020 统一端口；F-021 防枚举；F-022 清理/集成后台代码；F-023~F-026 设计小修。

---

> 说明：本报告所有结论均基于实际源码读取与配置核对（置信度见每条）。未对运行实例发起任何攻击性探测，未触碰生产数据，符合"仅审计+给建议、不改代码"的委托范围。建议补充一次 `mvn org.owasp:dependency-check-maven:check` 与 `npm audit` 以补全 A06 组件级证据。
