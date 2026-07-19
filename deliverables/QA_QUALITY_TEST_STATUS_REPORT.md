# 质量与测试现状报告（NetLearn / 芒得很职）

> 评估人：QA 工程师 严过关（Yan）
> 评估日期：2026-07-17
> 项目根目录：`E:\Code\Mangdehenzhi`
> 评估性质：**现状评估（只读、不改动代码、不新增测试用例）**
> 评估方法：源码静态走查 + 测试文件清点 + 配置文件核对 + 既有文档（OPTIMIZATION_LOG.md / SECURITY_AUDIT_REPORT.md / README.md / CI / docker-compose）交叉比对。未运行测试、未发起任何攻击性探测。

---

## 0. 一句话结论

项目**已有基础自动化测试与 CI 流水线**，但**测试覆盖极浅、无覆盖率门禁、无 E2E、无安全/鉴权专项测试**；同时存在一份**严重的既有安全审计报告（26 项，评级 D）**尚未闭环。对"软件杯 Lite 版（约 20 天、需稳定可演示）"而言，**最大威胁不是功能缺失，而是"默认配置即不安全"与"核心卖点（区块链存证）为模拟实现"**，演示前若不处理，极易当场翻车。

---

## 1. 测试现状总览

### 1.1 自动化测试（已存在）

| 端 | 框架 | 测试文件 | 实测用例数 | 覆盖内容 |
|----|------|----------|-----------|----------|
| 后端 | JUnit 5 + Spring Boot Test（`@SpringBootTest`+MockMvc） | 5 个（UserServiceTest、AssessmentServiceTest、CertificationServiceTest、AuthControllerTest、IntegrationTests） | **27 个 `@Test` 方法**（文档称 20~21，存在漂移，见 §2） | 注册/登录/角色、测评提交/通过/校验、证书签发/验证/哈希、Auth API、课程/健康检查/搜索集成 |
| 前端 | Vitest + jsdom | 2 个（`__tests__/stores/user.test.ts`、`__tests__/api/index.test.ts`） | **10 个 `it`** | UserStore 初始态/恢复/登出/角色判断；API 函数"存在性"、路由存在性、路由守卫 meta |

> 文档漂移：README 后端测试表列 4 类（20 例）且**漏列 IntegrationTests（5 例）**；OPTIMIZATION_LOG 第四轮称"后端 21 例"。实测 27 例。三者互不一致，**测试清单未与代码同步维护**。

### 1.2 类型检查 / Lint

- **前端**：CI 执行 `npx vue-tsc --noEmit`，每次优化记录均为"零错误"。但源码仍有 **6 处 `as any`/`: any`**（`AssessmentDetail.vue`、`Certifications.vue`、`Login.vue`、`Metaverse.vue`、`Register.vue` 及测试文件），vue-tsc 默认不拒绝显式 `any`，**类型安全未完全达成**。
- **后端**：仅编译期检查，**未配置 Checkstyle/SpotBugs/PMD** 等静态分析。
- **前端 Lint 门禁**：CI 仅跑类型检查，**未见 ESLint/Prettier 在流水线中执行**（仓库未检索到被 CI 调用的 ESLint 配置）。

### 1.3 覆盖率估计（保守、基于清点，因无工具实际采集）

> ⚠️ 项目**未接入任何覆盖率采集**：后端 `pom.xml` 无 JaCoCo；前端 `vitest.config.ts` 虽有 `coverage` 配置（v8 提供器），但 CI 的 `npm run test` = `vitest run` **不带 `--coverage`**，且 `test:coverage` 脚本未接入 CI。以下为按"已测/未测模块"估算的**有效逻辑覆盖率**。

| 端 | 行/逻辑覆盖率估计 | 主要依据 |
|----|------------------|----------|
| 后端 | **< 15%** | 64 个主源码文件中，仅 User/Assessment/Certification 三个 Service + Auth/Course/Health/Search 控制器被触及；**Metaverse、Recommendation、AI/DeepSeek、Security/JWT、RateLimit、Admin、Blockchain 等核心模块零测试** |
| 前端 | **< 5%** | 10 个用例均为"结构断言"（函数/路由*存在*），未做真实行为、组件渲染、网络请求断言；所有业务视图组件（Login/Register/Dashboard/Assessment/Courses/Metaverse）**零覆盖** |

### 1.4 CI/CD 现状（`.github/workflows/ci.yml`）

三段式流水线：`backend`（JDK17 编译→`./mvnw test`→打包）→ `frontend`（Node20 `npm ci`→`vue-tsc`→`npm run test`→`vite build`）→ `docker`（依赖前两者，`docker compose build`）。

**缺口**：
- 后端测试**不采集覆盖率**；前端测试**不传 `--coverage`**，无任何覆盖率门槛（threshold）。
- `docker` job **仅构建镜像**，不启动容器、不做 `/api/health` 或首页冒烟验证 → 镜像"能 build"≠"能跑"。
- CI 动作仅固定到主版本标签（`actions/checkout@v4`），**无 SHA 锁定、无镜像/依赖漏洞扫描**（对应 F-019）。
- 触发条件 `push: [main, develop]` + `pull_request: [main]`，合理。

### 1.5 测试数据 / 配置

- 集成测试用 `@ActiveProfiles("test")`，默认回退 `application.yml` 的 **H2 内存库**（`ddl-auto: create-drop`），无需外部 MySQL，CI 可独立运行（✅ 这点做得对）。
- 种子数据（`DataInitializer`）写入 `admin/admin123` 等弱口令（见 §2，F-002）。

---

## 2. 已知问题清单（来源 + 描述 + 严重度）

### 2.1 安全类（来自 `deliverables/SECURITY_AUDIT_REPORT.md`，2026-07-17，总体评级 **D / 高风险**，共 26 项）

| 编号 | 来源 | 描述 | 严重度 |
|------|------|------|--------|
| F-001 | 审计报告 | **提交式 JWT 对称密钥**写在 `application.yml:36` 与 `docker-compose.yml:40`，任何人可伪造 admin 令牌 → 完全认证绕过/提权 | 🔴 Critical |
| F-002 | 审计报告 | **默认种子口令** `admin/admin123`、`teacher/teacher123`、`student/student123` 明文写入源码 | 🔴 Critical |
| F-003 | 审计报告 | **匿名可建课**（`/api/courses/**` 设为 `permitAll`，`createCourse` 无鉴权）→ 未授权写 | 🔴 Critical |
| F-004 | 审计报告 | **方法级鉴权完全缺失**：无 `@EnableMethodSecurity`；`JwtAuthenticationFilter` 的 `authorities` 始终为空 → 角色形同虚设 | 🟠 High |
| F-005 | 审计报告 | **IDOR**：任意登录用户可读取他人测评结果与 AI 报告（含分数/分析） | 🟠 High |
| F-006 | 审计报告 | **IDOR/PII**：任意登录用户可枚举他人邮箱、手机号、角色 | 🟠 High |
| F-007 | 审计报告 | MySQL 3306 / 后端 8080 **直接映射宿主机**，绕过 Nginx 与内网隔离 | 🟠 High |
| F-008 | 审计报告 | **全链路无 TLS**（仅 HTTP），JWT/凭据明文传输，HSTS 在 HTTP 上无效 | 🟠 High |
| F-009 | 审计报告 | 通配 CORS（`*`）+ `allowCredentials`，默认即 `*` | 🟡 Medium |
| F-010 | 审计报告 | 限流器信任可伪造的 `X-Forwarded-For`，登录爆破限流可被绕过；内存 Map 无淘汰 | 🟡 Medium |
| F-011 | 审计报告 | dev H2 控制台免认证 + 全局 `frameOptions.disable()` | 🟡 Medium |
| F-012 | 审计报告 | Swagger/OpenAPI 文档免认证暴露全 API 契约 | 🟡 Medium |
| F-013 | 审计报告 | **"区块链认证"校验为模拟实现**，恒返回 `verified:true`（违背"不可篡改"卖点） | 🟡 Medium |
| F-014 | 审计报告 | JWT 存 `localStorage`，XSS 可窃取 | 🟡 Medium |
| F-015 | 审计报告 | 前端 axios `^1.7.7` 存在已知 SSRF CVE，需升级 | 🟡 Medium |
| F-016 | 审计报告 | AI 提示词注入（`String.format` 拼入不可信字段） | 🟡 Medium |
| F-017 | 审计报告 | **DeepSeek 出站调用无超时**，上游慢/不可达致线程耗尽 DoS | 🟡 Medium |
| F-018 | 审计报告 | 提交式弱数据库口令（`root123`/`mangdehenzhi123`） | 🟡 Medium |
| F-019 | 审计报告 | CI 动作未 SHA 锁定、无镜像扫描 | 🟢 Low |
| F-020 | 审计报告 | **Vite 代理端口 9997 与后端 8080 不一致**，本地联调失败 | 🟢 Low |
| F-021 | 审计报告 | 注册接口用户/邮箱枚举 | 🟢 Low |
| F-022 | 审计报告 | `后台管理系统/` 代码未编译、重复类定义、缺鉴权（参考片段） | 🟢 Low |
| F-023 | 审计报告 | 证书公开验证接口反而要求登录（设计瑕疵） | 🟢 Low |
| F-024 | 审计报告 | 场景类型错误回显用户输入（信息泄露） | 🟢 Low |
| F-025 | 审计报告 | 证书验证 GET 产生写副作用（非幂等） | 🟢 Low |
| F-026 | 审计报告 | HTTP 上声明 HSTS 无效 + 日志含请求 URI | 🟢 Low |

### 2.2 功能 / 一致性类（来自 OPTIMIZATION_LOG、README、源码核对）

| 编号 | 来源 | 描述 | 严重度 |
|------|------|------|--------|
| Q-01 | 源码核对 | **区块链证书"上链存证"为模拟**（F-013 同）：产品核心卖点"不可篡改"当前不成立，演示若被要求验证即穿帮 | 🔴（演示风险） |
| Q-02 | 源码核对 | `application.yml` 默认启用 H2 控制台、`ddl-auto: create-drop`；JWT 密钥硬编码默认 | 🟠 |
| Q-03 | `.env.example` / README | `CORS_ALLOWED_ORIGINS=*` 作为默认交付值（即便已"可配置"，出厂即通配） | 🟡 |
| Q-04 | 测试文档 | **测试清单与代码漂移**：README 列 20、日志称 21、实测 27；IntegrationTests 漏列 | 🟡 |
| Q-05 | `后台管理系统/`、`index/` | 存在**脱离构建路径的遗留/重复代码**（区块链认证系统.java 同一文件重复定义 4 次类；元宇宙场景交互核心代码.js），易误导、制造维护债 | 🟡 |
| Q-06 | 类型检查 | 前端 6 处 `any`，类型安全未完全达成 | 🟢 |

### 2.3 已修复但需"防回归"的问题（OPTIMIZATION_LOG 记录，应有测试固化）

第一轮修复的 7 项关键问题（AIService 手写 JSON 损坏、MySQL 密码硬编码、CORS 全开、评分未校验、证书未自动签发、vite `__dirname`、前端 `any`）与若干逻辑/安全问题。**其中多数仅经"编译/类型检查验证"，缺乏针对性回归测试**——这正是下一阶段测试策略要补的。

---

## 3. 代码 TODO/FIXME 统计（按目录/模块）

使用 Grep 检索 `TODO|FIXME|XXX|HACK|BUG|DEPRECATED`（已排除 `node_modules/`、`dist/`、`.git/`、`JSMO-PAGE/lib/` 等三方/构建产物）。

| 模块/目录 | 命中 | 性质 |
|-----------|------|------|
| `backend/src/**` | 0 | **源码零技术债标记** |
| `frontend/src/**` | 1（`data/questions.ts:138`） | 命中词为测评题**文案**"严重 BUG"（内容数据，非代码标记） |
| `backend/mvnw.cmd:162` | 1 | 误报（`^` 续行符） |
| `JSMO-PAGE/lib/*.js`、`frontend/dist/**` | 若干 | 三方压缩库 / 构建产物，已排除 |

**结论**：**生产源码几乎不存在 TODO/FIXME/XXX/HACK 类显式技术债标记**。这具有两面性：
- 正面：代码表面整洁、命名规范。
- 负面：技术债**未以标记形式暴露**，而是隐藏在"未测试路径"与"安全审计的 26 项"中——尤其鉴权、区块链、AI、限流等高风险模块**零 TODO 却零测试**，债被"静默"积压。

> 补充：真正的"负债清单"不在 `grep TODO`，而在 `SECURITY_AUDIT_REPORT.md`（26 项）与"未覆盖模块"中。

---

## 4. 质量风险（对双线目标，尤其"软件杯 Lite 可演示"）

> 双线：① 软件杯 Lite 版（≈20 天，需稳定可演示）；② 大创真版（2026-11）。

1. **【演示翻车·高危】默认配置即"可被攻破"**：提交式 JWT 密钥 + 默认 `admin/admin123` + 匿名可建课（F-001/002/003）。演示机若联网或被评审扫描，权威性与安全性直接崩盘；即便不联网，"软件杯"评审常关注安全基线，上台前未换密钥 = 重大减分项。
2. **【功能露馅·高危】认证/授权形同虚设**（F-004）：角色 `authorities` 始终为空、无任何 `@PreAuthorize`。所有"管理员/教师"功能无真正权限隔离——若演示"管理端/角色演示"会当场露馅。
3. **【卖点穿帮·高危】区块链证书校验为模拟**（F-013/Q-01）：产品核心卖点"区块链不可篡改存证"当前恒返回 `verified:true`。评审若要求验证即穿帮。**演示前必须明确标注"预留/模拟"或下线该声明**。
4. **【演示稳定性·中高】测试极浅 + 无 E2E + Docker 无健康检查**：约 20 天窗口内任何改动（AI/元宇宙/课程）回归无保护，"手抖改一处→全站崩"风险高；`docker-compose.yml` 仅 `db` 有 healthcheck，**backend/frontend 无健康检查**，编排无法感知服务就绪/崩溃，演示启动稳定性无保障。
5. **【部署一致性·中】配置缺陷**：Vite 代理端口 9997 ≠ 后端 8080（F-020，本地联调失败）；MySQL 3306 / 后端 8080 直曝宿主机（F-007）。演示部署易因配置不一致或暴露端口翻车。
6. **【现场卡死·中】AI 依赖无超时且无真实测试**（F-017 + 无 AIService 测试）：演示核心"个性化 AI 报告"依赖 DeepSeek，出站无超时（API 慢→线程耗尽），且降级路径（Key 缺失/异常）**未被任何测试覆盖**——演示现场可能卡死或报错。

> 对**大创真版**的连带风险：上述 26 项安全债 + 缺失的测试策略，将使 2026-11 真版的迭代速度与可信度严重受限；缺覆盖率门禁与 E2E，每轮重构都伴随"不敢改"的心理负担。

---

## 5. 缺失的测试策略（能力缺口清单）

1. **无覆盖率门禁与阈值**（后端无 JaCoCo；前端 coverage 未接入 CI，无 threshold）。
2. **无 E2E 测试**（Playwright/Cypress 缺失）——黄金用户路径无回归闸门。
3. **无 API 契约测试**（虽有 Swagger，但未用于测试）。
4. **无安全/鉴权专项测试**（角色、越权 IDOR、限流、认证）——F-001~F-006 全靠人工审计发现。
5. **无 AI/外部依赖 Mock 测试**（DeepSeek 降级、超时、异常路径未覆盖）。
6. **前端测试停留在"结构断言"**，无组件渲染/用户交互/网络请求行为测试。
7. **无数据库迁移/真实集成测试**（仅 H2 内存；无 MySQL 一致性验证）。
8. **CI 无质量门禁**：无依赖/镜像漏洞扫描、无 CI 动作 SHA 锁定、docker job 无冒烟。

---

## 6. 建议的测试策略与下一步 QA 任务（按优先级）

### P0 — 保障"软件杯 Lite 可演示"（2 周内必须完成）

- **QA-1 演示安全基线**：提供 `.env` 生成脚本（强随机 `JWT_SECRET`、强口令、非 `*` 的 CORS），写入 README"演示须知"；验证默认部署不暴露 3306/8080；上线前轮换密钥。**直接固化 F-001/002/003/007/018。**
- **QA-2 冒烟测试**：为 `docker-compose.yml` 的 backend/frontend 加 `healthcheck`；CI `docker` job 启动容器并 `curl /api/health` + 首页 `200` 后再结束。
- **QA-3 核心链路 E2E（Playwright）**：覆盖 **注册 → 登录 → 测评 → 结果 → 证书 → 课程浏览** 黄金路径，作为演示前回归闸门。
- **QA-4 鉴权专项单测**：补 `JwtAuthenticationFilter`（authorities 填充）、`@PreAuthorize` 角色、`POST /api/courses` 越权 403 等用例，**直接固化 F-003/004/005/006 的修复**。

### P1 — 提升可信度与回归保护（软件杯后、大创前）

- **QA-5 后端覆盖率门禁**：引入 JaCoCo，CI 设关键模块 threshold（建议 ≥ 60%），未达不合并。
- **QA-6 AIService/DeepSeek 单测**：Mock `RestClient` 超时/异常，验证本地降级与超时配置（固化 F-017）。
- **QA-7 前端行为测试**：用 `@vue/test-utils` + Testing Library 替换"函数存在"型弱断言，覆盖 Login/Register/Assessment 关键交互。
- **QA-8 区块链"明确化"**：真实链码校验 or 明确标注"预留/模拟"并下线对外"不可篡改"声明（避免演示穿帮，对应 F-013/Q-01）。

### P2 — 大创真版长期建设

- **QA-9 供应链安全入 CI**：Trivy 镜像扫描 + `npm audit` + OWASP `dependency-check`；CI 动作 SHA 锁定（F-015/019）。
- **QA-10 性能/限流测试**：按账户限流 + 失败锁定（修复 F-010 后补测）。
- **QA-11 API 契约测试**（基于 OpenAPI）。
- **QA-12 清理遗留/重复代码**：`后台管理系统/`、`index/`、`JSMO-PAGE/` 评估移入或删除，消除维护债与误导（F-022/Q-05）。

---

## 附：评估证据索引

- 测试文件：`backend/src/test/java/com/mangdehenzhi/{service,controller}/*Test.java`（5 文件，27 `@Test`）；`frontend/src/__tests__/**/*.test.ts`（2 文件，10 `it`）。
- CI：`.github/workflows/ci.yml`；命令入口 `Makefile`（`make test` / `test-backend` / `test-frontend` / `frontend-lint`）。
- 配置：`docker-compose.yml`（仅 db 有 healthcheck）、`application.yml`（H2 默认 + 硬编码 JWT 密钥 + H2 控制台）、`.env.example`（CORS `*`、默认口令）。
- 文档：`OPTIMIZATION_LOG.md`（优化/修复史）、`README.md`（测试清单，**与代码漂移**）、`deliverables/SECURITY_AUDIT_REPORT.md`（26 项安全发现，评级 D）。
- 覆盖率：后端无 JaCoCo；前端 `vitest.config.ts` 有 coverage 配置但 CI 未启用。
