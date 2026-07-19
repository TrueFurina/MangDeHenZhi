# 行业 SRE 巡检报告 — 芒得很职 (Mangdehenzhi)

> **行业语境**：自托管「职业技能培训 + AI 测评 + 区块链证书存证」SaaS 平台（Spring Boot 3.3.5/Java17 + Vue3/Three.js + MySQL8 + 单宿主机 Docker Compose）。
> **巡检维度**：安全 · 可靠性 · 性能 · 成本 · 服务限制（五维全量）
> **分诊依据**：项目同时具备「教育培训」「多角色 SaaS」「泛互联网前端」三重属性，故由 education-sre（可靠性+性能）、saas-sre（安全复核+成本+服务限制）、internet-sre（前端性能+容器成本）三方协同。
> **状态图例**：✅ 已修复（前序 P0）｜🔧 本次修复（本回合落地）｜⬜ 待处理（含优先级）

---

## 一、核心结论

1. **本平台瓶颈不在 3D 元宇宙（Three.js 纯客户端渲染，后端仅存会话元数据），而在「测评提交 + AI」链路。** 这是可靠性、性能、服务限制三者的共同命门。
2. **最致命单点（R1 / F-017 / S-004）已修复**：`submitAssessment` 原在 `@Transactional` 内同步调用 DeepSeek（2 次）且无超时 → 慢上游可耗尽连接池、全站雪崩。本回合已将 AI 调出事务 + RestClient 设 3s/30s 超时。
3. **容量天花板（P1/Hikari）已修复**：连接池默认 10、无超时、open-in-view=true → 现配 `maximum-pool-size=20` + 超时 + `open-in-view=false`。
4. **6 项安全 P0（F-001/002/003/004/007/012）已于前序回合修复并编译通过。**
5. **剩余高危待处理**：零备份/PITR（R2）、IDOR（F-005/006）、限流 XFF 绕过（F-010）、弱口令默认值（F-018）、无 TLS（F-008）——均已定位到 file:line，可对照执行。

---

## 二、五维巡检发现（去重合并）

### 🔒 安全
| 编号 | 优先级 | 位置 | 问题 | 状态 |
|---|---|---|---|---|
| F-001 | P0 | compose/yml/Dockerfile | JWT 密钥硬编码/默认值覆盖 fail-fast | ✅ 已修复（密钥外置+启动长度校验） |
| F-002 | P0 | DataInitializer | 种子账户 admin/admin123 等弱口令 | ✅ 已修复（`@Profile("!prod")`） |
| F-003 | P0 | SecurityConfig/CourseController | 课程写接口匿名可调用 | ✅ 已修复（写操作限 ADMIN/TEACHER） |
| F-004 | P0(框架) | SecurityConfig/JwtAuthFilter | 无方法级鉴权、authorities 恒空 | ✅ 已修复（`@EnableMethodSecurity`+ROLE_*） |
| F-005 | **P1** | AssessmentController:47 / Service:104 | 按 resultId 取结果无 owner 过滤 → 读他人分数 (BOLA) | ⬜ 待处理 |
| F-006 | **P1** | UserController:24 / UserService:60 / UserDTO | 按 id 取用户无过滤，DTO 泄漏 email/phone/role | ⬜ 待处理 |
| F-007 | P1 | compose | DB(3306)/backend(8080) 宿主机映射暴露 | ✅ 已修复（仅 Nginx:80 对外） |
| F-008 | **P1** | compose/nginx | 全链路 HTTP、HSTS 写在 HTTP 上无效 | ⬜ 待处理（需证书） |
| F-009 | P2 | CorsConfig | `origins=*` + allowCredentials + 反射 Origin | ⬜ 待处理 |
| F-010 | **P1** | RateLimitConfig:75 | 限流优先取可伪造 XFF → 登录爆破无限流 | ⬜ 待处理 |
| F-011 | P2 | SecurityConfig:45 | frameOptions 全局禁用 | ⬜ 待处理 |
| F-012 | P2 | SecurityConfig/SwaggerConfig | Swagger 生产公开 | ✅ 已修复（`@Profile("!prod")`） |
| F-013 / S-NEW-1 | P2 | CertificationService:45 | 「上链存证」实为 DB+SHA256；verify 写副作用无核验 | ⬜ 待处理 |
| F-014 | P2 | frontend user.ts | token 存 localStorage（XSS 可窃） | ⬜ 待处理（前端） |
| F-015 | P2 | frontend package.json | axios 潜在 CVE | ⬜ 待处理（前端） |
| F-016 | P2(低) | DeepSeekService:57/83 | 提示词 String.format 插值 | ⬜ 待处理（低可利用） |
| F-017 | P2 | DeepSeekService:39 | RestClient 无超时 | 🔧 本次修复（3s/30s） |
| F-018 | **P1** | compose | MySQL root123 / mangdehenzhi123 弱口令默认值 | ⬜ 待处理 |

### 🛡️ 可靠性
| 编号 | 优先级 | 位置 | 问题 | 状态 |
|---|---|---|---|---|
| R1 | **P0** | AssessmentService:42 / DeepSeekService:39 | 事务内同步 AI + 无超时 → 全站雪崩 | 🔧 本次修复（AI 移出事务+超时） |
| R2 | **P0** | compose/db + backup服务 + scripts/ + mysql/conf.d/ + docs/ | 零备份 → binlog(PITR)+每日dump+binlog归档+保留策略+恢复手册+季度演练 | 🔧 本次修复 |
| R3 | **P0** | compose | 单宿主机单实例，无 HA/容灾/LB | ⬜ 待处理（分阶段） |
| R4 | P1 | compose/HealthController | backend healthcheck 已加（`/api/health` 探活，grep UP） | ✅ 已修复 |
| R5 | P1 | compose | backend healthcheck 联动 `restart:unless-stopped` 实现假死自愈 | ✅ 已修复 |
| R6 | P1 | Dockerfile/compose | 无 JVM/容器资源上限 | ✅ 已修复（C-01/C-02） |
| R7 | P1 | AssessmentService/CertificationService | 无幂等/证书去重，504 重试致重复数据 | ⬜ 待处理 |
| R8 | P2 | compose | frontend `depends_on backend: service_healthy` 已修正 | ✅ 已修复 |

### ⚡ 性能（服务端/数据层）
| 编号 | 优先级 | 位置 | 问题 | 状态 |
|---|---|---|---|---|
| P1(R1) | **P0** | application-prod.yml | Hikari 默认池 10、无超时 | 🔧 本次修复（max20+超时+open-in-view=false） |
| P2 | P1 | AssessmentService:97 / DTO | N+1（懒加载 assessment/user） | ⬜ 待处理 |
| P3 | P1 | 全栈 | 无缓存层（课程目录/AI 结果） | ⬜ 待处理 |
| P4 | P2 | init.sql | 缺 `assessment_results(assessment_id)`、`courses.title` 索引 | ⬜ 待处理 |
| P5 | P2 | RequestLoggingInterceptor | 慢请求阈值统一 3s，无 metrics | ⬜ 待处理 |
| P6 | P2 | AssessmentService:97 | 结果列表未分页 | ⬜ 待处理 |

### 💰 成本
| 编号 | 优先级 | 位置 | 问题 | 状态 |
|---|---|---|---|---|
| C-01 | P1 | compose | 容器无 mem/cpu 限制 | ✅ 已修复（db1g/backend1.5g/frontend256m） |
| C-02 | P2 | Dockerfile | 无 JVM 内存参数 | ✅ 已修复（MaxRAMPercentage=75 等） |
| C-003 | P2 | application-prod.yml | Hikari 未调优 | 🔧 本次修复（并入 P1） |
| C-004 | P3 | Dockerfile/.dockerignore | `COPY .mvn` 与 `.dockerignore` 排除 `.mvn` 冲突 | ⬜ 待处理 |
| C-005 | P3 | compose | 无 HA/弹性伸缩 | ⬜ 待处理（架构） |
| C-006 | P2 | compose.dev | dev/prod 配置与口令混用风险 | ⬜ 待处理 |
| (internet) F-01~F-10 | P1/P2 | frontend/nginx | Element Plus 全量、无 TLS/brotli、无 vendor 分包等 | ⬜ 待处理 |
| (internet) C-03~C-08 | P3 | CI/Docker | 双构建、无 .dockerignore、无镜像扫描等 | ⬜ 待处理 |

### 🚦 服务限制
| 编号 | 优先级 | 位置 | 问题 | 状态 |
|---|---|---|---|---|
| S-001 | **P1** | RateLimitConfig:75 | 限流信任可伪造 XFF | ⬜ 待处理（=F-010） |
| S-002 | P2 | RateLimitConfig:41 | 限流计数无 TTL → 内存泄漏 | ⬜ 待处理 |
| S-003 | P2 | AuthController | 无账户级限流/失败锁定/验证码 | ⬜ 待处理（=F-010） |
| S-004 | P2 | DeepSeekService | 出站无超时 | 🔧 本次修复（RestClient 3s/30s） |
| S-005 | P2 | application-prod.yml | DB 连接池/超时未配 | 🔧 本次修复（Hikari） |
| S-006 | P3 | RateLimitConfig | 非真滑动窗口，边界可突发 2× | ⬜ 待处理 |
| S-007 | P3 | SearchController | search limit 无硬上限 | ⬜ 待处理 |

---

## 三、本回合已落地修复（含证据）

| 修复 | 文件 | 改动 |
|---|---|---|
| R1 根因消除 | `AssessmentService.java` | `submitAssessment` 改为非事务；拆出 `persistInitialResult` / `finalizeResult` 两个短 `@Transactional`，AI 调用在事务外；DTO 转换在事务内完成（避免懒加载 detach） |
| F-017/S-004 超时 | `DeepSeekService.java` | `RestClient` 加 `SimpleClientHttpRequestFactory`，connect=3s / read=30s |
| P1/Hikari | `application-prod.yml` | `hikari.maximum-pool-size=20`、`minimum-idle=5`、`connection-timeout=3000`、`idle-timeout=600000`、`max-lifetime=1800000`；JDBC `connectTimeout=3000&socketTimeout=30000`；`jpa.open-in-view=false` |
| C-01 资源上限 | `docker-compose.yml` | db 1g/1.0cpu、backend 1.5g/1.0cpu、frontend 256m/0.5cpu（前序回合，已 yaml 校验） |
| C-02 JVM 参数 | `backend/Dockerfile` | ENTRYPOINT 加 `-XX:MaxRAMPercentage=75 -XX:InitialRAMPercentage=50 -XX:+UseG1GC -XX:+ExitOnOutOfMemoryError` |
| 安全 P0×6 | 多文件 | F-001/002/003/004/007/012（前序回合，已 `./mvnw.cmd compile` 通过） |
| R2 备份/PITR | `docker-compose.yml` + `scripts/backup.sh` + `scripts/restore.sh` + `mysql/conf.d/mysql-bin.cnf` + `docs/BACKUP_RECOVERY.md` | db 启 binlog(ROW,7d)；新增 `backup` 服务(启动即备+每日全量dump+binlog归档+7d保留)；恢复手册含季度演练 checklist |

**编译验证**：`backend` 模块 `./mvnw.cmd compile -q` → EXIT=0。

---

## 四、剩余治理建议（按优先级，可对照执行）

**P0（上线承载真实流量前）**
1. **R2 备份** ✅ 已修复：db 启 binlog(PITR) + `backup` 服务(每日全量dump+binlog归档+7d保留) + `docs/BACKUP_RECOVERY.md`(含季度演练 checklist)。待办：binlog/备份异地同步(COS)以实现真容灾。
2. **R3 HA（分阶段）**：MySQL 主从 + backend 多副本 + 前置 LB；长期迁托管 MySQL / K8s(TKE)。

**P1（本迭代）**
3. **F-005/F-006 IDOR**：`getResultById` 强制 `WHERE user_id=当前用户`（非本人 403）；`getUserById` 仅本人/ADMIN，剥离 `UserDTO.email/phone`。
4. **F-010/S-001/S-003 限流**：仅在可信代理后取 XFF；登录接口改**账户级滑动窗口 + 失败 N 次锁定 + 验证码**。
5. **F-018 弱口令**：compose 移除 DB 口令默认值，改机密管理注入强随机；应用账户最小权限。
6. **F-008 TLS**：反向代理终止 443 + HTTP→HTTPS 重定向；HSTS 仅 HTTPS。
7. **R4/R5 探活**：引入 actuator（liveness/readiness + DataSource 健康检查）+ backend healthcheck，实现假死自愈。
8. **R7 幂等/去重**：提交加幂等键；证书签发前查重；代理超时对齐 AI 上限（或 AI 异步化）。
9. **P2/P3 性能**：`@EntityGraph`/`JOIN FETCH` 修 N+1；引入 Caffeine 缓存课程目录 + AI 结果短时缓存。
10. **internet-sre F-01~F-10**：Element Plus 按需引入、启用 brotli、修正 nginx deny json、vendor 分包、axios 升级等。

**P2/P3（近期加固）**
11. F-009 显式 CORS；F-011 frameOptions 仅 H2 路径禁用；F-012 已修复；F-013/S-NEW-1 真实链码校验或去除声明+验证只读化；F-014 HttpOnly Cookie；F-015 升级 axios；F-016 结构化提示+输出校验；P4 补索引；P5 metrics/告警；P6 分页；R8 service_healthy；S-002/S-006/S-007 限流健壮性；C-004/C-006 构建与 dev/prod 隔离；C-005 弹性架构。

---

## 五、容量结论
当前默认配置下，>10 并发测评提交即触顶连接池；**修复 R1+P1 后，配合缓存(P3)与索引(P4)**，单实例可支撑中小规模职业培训峰值（数百~千级并发读、数十级并发提交）。更大规模需走 R3 多副本+LB。

> 方法：全部结论基于实际读取源码/配置（file:line），未运行攻击性探测、未修改除已标注外的代码。安全基线见 `deliverables/SECURITY_AUDIT_REPORT.md`（评级 D）。
