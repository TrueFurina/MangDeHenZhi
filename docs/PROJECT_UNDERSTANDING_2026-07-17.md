# 芒得很职 (Mangdehenzhi) · 项目深度认知报告（校正版）

> 分析日期：2026-07-17 ｜ 代码库解读（非表格分析）
> **校正说明**：本报告初版曾误判"本仓库是 NetLearn（408 考研）可复用脚手架、核心能力实现度 0%"。经产品战略团队核对代码实证（包名 `com.mangdehenzhi`、三支柱业务域）与 `USER.md` 纠偏，**本 workspace 真实产品 = 芒得很职（AI+元宇宙+区块链 职业技能培训认证平台）**，NetLearn 是另一独立项目。本报告已按真实身份重写，错误结论全部作废。

---

## 0. 一句话定位（真实身份）

`E:\Code\Mangdehenzhi` 是一套 **AI + 元宇宙 + 区块链 职业技能培训认证平台（芒得很职）** 的完整前后端代码。

- 包名 `com.mangdehenzhi`，业务域：A 智能测评 → AI 报告 → 课程 → 区块链证书（模拟上链）→ 元宇宙实训原型。
- 技术栈 Spring Boot 3.3.5 + Vue 3.5，已自带可跑通的「登录 → 测评 → AI 报告 → 课程 → 证书(模拟链)」闭环，**不是脚手架、不是 0% 空壳**。
- 目标演进路线见 `docs/sdd-mangdehenzhi-vocational.md`（口径 A，单一事实源 SSOT）：复用现有 A/C/Metaverse，Lite 闭环跑通+安全基线，Real 增量叠 Fabric 上链 / 元宇宙 AI 实时互动 / 自适应路径+督学 / 埋点 MCL。

> ⚠️ 作废物料：原 408 重写蓝图 `docs/sdd-blueprint-skeleton.md`（已标 VOID）、`archive/target-architecture-blueprint-NETLEARN-OBSOLETE-2026-07-17.md`（A1 408 蓝图，本会话移入归档标注废弃）——均不再作为事实源。

---

## 1. 技术栈（与代码核对一致，为本产品自身栈）

| 层 | 组件 | 版本 | 对本产品的状态 |
|---|---|---|---|
| 后端 | Java 17 + Spring Boot | 3.3.5 | ✅ 主栈，直接复用 |
| 安全 | Spring Security + JWT（jjwt 0.12.6） | — | ✅ 已启用方法级鉴权；A5 本会话追加纵深防御 |
| 持久化 | Spring Data JPA + MySQL 8 / H2 | — | ✅ 关系库即用；无向量库（本产品暂不需要） |
| AI 调用 | RestClient 直连 DeepSeek | deepseek-chat | ✅ 本产品真实 AI 供给（非"与目标背离"）；无 Key 降级本地文案 |
| 前端 | Vue 3.5 + Vite 6 + TS 5 + Element Plus 2.9 + Pinia 2.3 | — | ✅ 主栈，直接复用 |
| 3D | Three.js 0.170 | — | ✅ B 元宇宙实训支柱，保留（非"建议下线"） |
| 测试 | JUnit 5 + Vitest 2.1 | — | ✅ 复用 |
| 部署 | Docker + Compose + Nginx + Makefile + GitHub Actions | — | ✅ 复用；Lite 3 服务拓扑 |

---

## 2. 当前代码真相：真实实现 vs 桩/模拟

**真实可用（即产品 Lite 闭环核心）**：Auth/User、Assessment（提交+计分+AI+自动签发证书）、Course CRUD、Metaverse 会话生命周期、ThreeScene 3D 渲染、JWT/限流/日志/Swagger 配置、6 张实体表与 JPA、全局异常处理、前端 10+ 页面（Home/Dashboard/Assessment*/Courses/Certifications/Metaverse/Login/Register）。

**桩 / 模拟 / 写死**（C 支柱演示态，属 Real 阶段设计替换项，非 bug）：
- `BlockchainService`：上链与验证均为**模拟**（`storeOnChain` 返回伪造 txId，`verifyOnChain` 恒返回 `verified:true`）。前端展示「区块链交易/存证」文案。→ Real 阶段由 **Hyperledger Fabric 真实链上证据** 替换（SDD §6 F-013）。
- `CertificationController`：`GET /api/certifications/{id}` 直接 `return null`（桩）。
- `RecommendationController.learning-path`：返回**写死** 4 段文本，非基于用户数据。
- `DeepSeekService`：真实 API，但无 key 时降级为本地写死文案；URL/model/key 硬编码，无模型抽象层（Real 建议抽 `LLMProvider`）。
- 死代码：`metaverse/AICharacter.java`、`metaverse/SceneConfig.java`、`ai/AnalysisReport.java`、`ai/RecommendationResult.java` 无调用方。
- `service/impl/` 空目录。

**运行外散件（已处理）**：
- `后台管理系统/`、`index/`、`JSMO-PAGE/` → 本会话已移入 `archive/`（不在 Maven/Vite 构建路径，零编译影响）。
- `marl_ecdsa_dashboard.html` → **保留在根目录**（用户 2026-07-17 指示"html 先不动"；属外来 NetLearn 研究物料，后续建议迁回 NetLearn 目录）。

---

## 3. 目标架构 vs 现状（按口径 A，非 408）

| 能力（对应路线图 A/B/C） | 目标（口径 A） | 现状 | 缺口 |
|---|---|---|---|
| A 智能测评 | AI 报告 + 能力画像 | 测评提交+计分+DeepSeek 报告闭环已跑通 | 深化能力画像（Real） |
| B 元宇宙实训 | AI 角色实时互动（WebRTC+对话后端） | Three.js 原型 + Metaverse 会话生命周期 | Real 增量（AI 实时互动） |
| C 区块链证书 | 真实 Fabric 上链 + 可验证证据 | 模拟链（演示用） | Real 替换（Fabric） |
| 自适应路径 + 督学 | 学情画像→动态路径 + 轻量督学 | 无（写死推荐文本） | Real 新增（AdaptivePathEngine + SupervisionService） |
| 埋点 MCL/AARRR | 闭环完成数北极星指标 | 无 | Real 新增（InstrumentationService） |
| 安全基线 | 复核 SRE 修复 + 补缺口 | SRE 已编译修复 F-001~018；A5 加纵深防御 | 见 §4 |
| 部署 | Lite 3 服务 / Real +3 服务 | Lite 3 服务可跑 | Real 增量（blockchain-svc/media-svc/analytics-svc） |

检索 `backend/src` 关键词（Fabric/WebRTC/AdaptivePath/Instrumentation/MetaverseAI）**Real 增量项命中 0**（预期内，属待开发）。

---

## 4. 安全态势：原评级 D（26 项，3 Critical / 5 High）→ 当前代码已封死多项

🔴 **原 3 Critical（据 `deliverables/SECURITY_AUDIT_REPORT.md`）**：
- **F-001** 提交 JWT 对称密钥 → 已修复：`application.yml` 占位 `${JWT_SECRET:local-dev-only...}` + `application-prod.yml` `jwt.secret: ${JWT_SECRET}`（无默认，缺失即启动失败）；**A5 本会话追加**：`JwtUtil` 显式拒绝已知坏密钥（`local-dev-only-*` / `mangdehenzhi-jwt-*`，纵深防御）。
- **F-002** 默认种子口令 → 已修复：`DataInitializer` `@Profile("!prod")` 仅非生产种子 + BCrypt；prod/dev compose 与 `.env.example` 均无默认口令值。
- **F-003** 匿名建课 → 已修复：`SecurityConfig` `POST/PUT/DELETE /api/courses/**` 需 `ADMIN/TEACHER`。

🟠 **5 High**：F-004 方法级鉴权（`@EnableMethodSecurity` + `JwtAuthenticationFilter` 填充 ROLE_*）、F-005/F-006 IDOR（owner/role 校验）、F-007 端口暴露、F-008 无 TLS、F-010 限流 XFF 绕过 — 据 SDD/团队确认 F-004/005/006/007/010 已由 SRE 编译修复；**F-008（TLS）仍待补（Real/演示必需）**。

**正向亮点（保留）**：密码 BCrypt ✅、JPA 参数化无注入 ✅、nginx 安全头齐备 ✅、Metaverse owner 校验 ✅、真实 `.env` 未入库 ✅、镜像非 root ✅、SRE 已加 healthcheck + 备份机制（`docs/BACKUP_RECOVERY.md`）。

> ✅ 建议：Lite 上线前做一次 QA/SRE 安全复测，确认 F-001..018 现状，避免重复劳动（SDD §6 已列 spot-check 证据）。

---

## 5. 已确认的待修清单（真实 bug / 技术债）

| # | 问题 | 位置 | 状态 |
|---|---|---|---|
| B1 | Vite 代理端口 9997 ≠ 后端 8080 | `frontend/vite.config.ts:16` | ✅ **已修复（本会话）** |
| B2 | 品牌名不一致（`.env.example` 原「芒德矩阵」） | `.env.example` | ✅ **已修复（本会话）** |
| B3 | PWA 记录夸大（OPTIMIZATION_LOG 称新增 manifest，但 frontend 无 manifest.json） | 文档/代码不符 | ⏳ 待确认（建议：真实落实 PWA 或修正日志表述） |
| B4 | 区块链模拟 vs 前端「上链」文案不一致 | 前后端 | 🔧 设计性（非 bug）：Real 由 Fabric 真实证据替换；Lite 可改文案为「演示存证」 |
| B5 | 报名/选课功能缺失（CourseDetail 仅 ElMessage 提示，无后端选课接口） | 前端 | ⏳ 待补（产品需求确认范围） |
| B6 | prod 无初始数据（DataInitializer 仅 `!prod`，生产首启无 admin） | 后端 | ⏳ 待修（建议：prod 安全种子方案，强随机口令 + 首次改密） |
| B7 | 散件未归档（后台管理系统/、index/、JSMO-PAGE/） | 根目录 | ✅ **已归档至 `archive/`（本会话）**；`marl_ecdsa_dashboard.html` 按指示留根目录 |

---

## 6. 立即可开干的计划（对齐口径 A，替换原 A1–A9 408 路线）

**P0 — Lite 闭环稳 + 安全基线（~20 天，软件杯）**
- **A2 散件降噪**：`archive/` 已完成（B7）；`marl_ecdsa_dashboard.html` 待用户后续决定去留。
- **A5 安全纵深**：SRE 已修 F-001~018；本会话 `JwtUtil` 追加已知坏密钥拒绝；**剩余**：F-008 TLS、F-009 CORS 收敛、F-014 JWT 改 HttpOnly Cookie、F-017 出站超时、F-023 证书验证端点鉴权。
- **B1/B2 已修**；**B5 报名功能 / B6 prod 种子** 待补（需 PM 确认范围）。

**P1 — 核心能力工程化（Real，大创 2026.11）**
- **C 区块链真版**：`BlockchainService` 接口换 Hyperledger Fabric 真实上链 + 可验证证据（替换模拟）。
- **B 元宇宙 AI**：`MetaverseAIService`（对话后端 + WebRTC 信令），实时互动。
- **自适应路径 + 督学**：`AdaptivePathEngine` + `SupervisionService`（提完课率）。
- **埋点 MCL/AARRR**：`InstrumentationService`（P0：测评完成/闭环完成先于一切）。

**P2 — 体验与规模化**
- 前端领域层深化（A 能力画像看板 / B 元宇宙互动界面 / C 可验证证书页）。
- 模型抽象层 `LLMProvider`（解耦 DeepSeek 硬编码，预留 Qwen 等）。

> 原 A1（408 重写蓝图）、A3（Qwen2.5 接入）、A4（408 内容建模）、A7/A8（FrugalRAG/GOMARL）均属 NetLearn 路线，**已作废**，详见 `archive/target-architecture-blueprint-NETLEARN-OBSOLETE-2026-07-17.md`。

---

## 7. 需要你提供的输入（决定 Real 能否开工）

当前仓库**已具备 Lite 全闭环**，Real 增量需外部输入：

1. **PM 路线图 / PRD 合成**：A/B/C 闭环范围与 2026 Q3/Q4 里程碑（见 `deliverables/product-strategy/roadmap-update-mangdehenzhi-rebaselined-2026-07-17.md`）。
2. **QA/SRE 安全复测报告**：确认 F-001..018 现状，避免重复修复。
3. **Real 新增改造工作量**：Fabric / 元宇宙 AI / 自适应路径 / 埋点 的工程师 P0/P1 排期。
4. **`marl_ecdsa_dashboard.html` 去留**：外来 NetLearn 物料，是否迁回 NetLearn 目录。

---

## 8. 本会话已完成的动作

- ✅ 修复 `frontend/vite.config.ts` 代理端口 `9997 → 8080`（B1）
- ✅ 修正 `.env.example` 品牌名「芒德矩阵 → 芒得很职」（B2）
- ✅ `JwtUtil` 追加纵深防御：显式拒绝已知坏密钥（A5 加固）
- ✅ 散件归档：移动 `后台管理系统/`、`index/`、`JSMO-PAGE/` 至 `archive/`；`marl_ecdsa_dashboard.html` 按指示保留根目录（B7）
- ✅ 废弃 408 重写蓝图：移动 `docs/target-architecture-blueprint-2026-07-17.md` 至 `archive/` 并更名 `...-NETLEARN-OBSOLETE-2026-07-17.md`
- ✅ **本报告校正**：以 `docs/sdd-mangdehenzhi-vocational.md` 为 SSOT，删除"NetLearn 脚手架 / 核心能力 0%"误读，保留真实代码事实
- ⏳ 待你确认：Real 路线输入（§7）、B3/B5/B6 处置、html 去留
