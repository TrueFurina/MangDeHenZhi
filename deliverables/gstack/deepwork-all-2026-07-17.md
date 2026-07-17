# GStack 全员深度工作 · 综合收口报告

**日期**：2026-07-17
**场景**：多成员协作深度工作（调试修复 + 安全审计 + QA 验收 + 设计 + 产品评审）
**参与成员**：排障手(investigator) + 安全卫士(security-officer) + 质量门神(qa-lead) + 设计师(designer) + 产品官(product-reviewer)

---

## 📌 TL;DR（执行摘要）

- **整体结论**：🟡 有条件通过（MARL Dashboard 核心缺陷已修复、可演示；当前工作区 `E:\Code\Mangdehenzhi` 安全为 **D 级高风险**，需上线前整改）
- **阻塞项数量**：Dashboard 侧 **0**（P0 已清零）；工作区安全侧 **3 个 Critical（P0）**
- **下一步**：① 优先整改工作区 3 个 Critical 安全项；② 补齐 BC 模式 `bc_scores_history` 并加 noData 占位；③ PPT 制作前统一口径披露

---

## 🎯 核心结论卡片

| 项目 | 内容 |
|------|------|
| Go / No-Go | 🟡 条件 Go（Dashboard 可演示；工作区需安全整改后上线） |
| 严重度分布（开放项） | 🔴 5 / 🟠 5+ / 🟡 10+ / 🟢 8+ |
| 关键行动项 | 6 条 |
| 建议负责人 | 后端/安全（工作区）、排障手+设计师（MARL Dashboard） |

> 开放 🔴 = C1 配色双轨 + C2 空状态图表（设计侧）+ F-001/F-002/F-003（安全侧）。Dashboard 前端 `toFixed` 崩溃已修复，不计入开放项。

---

## 1. 各成员核心结论

### 🔧 排障手（调试与根因）
- **核心判断**：Dashboard 两大问题根因已定位并**实际修复**。前端崩溃真凶是 Canvas 权重图 `values[i].toFixed(2)` 缺空值保护（实际崩溃点在 `templates/dashboard.html:2192`，用户所述"1582 行"为旧版本行号偏移）；数据缺失根因是 `scripts/generate_complete_data.py` 的 `BASE_DIR` 指向 `scripts/` 子目录，而 Dashboard 从**项目根**读取，路径错位使根目录 `training_results_selfish.json` 一直是 278 字节桩文件。
- **关键建议**：已修正导出路径 + 加写入前校验 + 支持 `--mode` 单独重生成；selfish 已补为 1000 回合完整数据；pure/bc 本就完整（各 1000 条）。提示"pure/bc 仅 100 回合 / episode_rewards 空"为旧缓存态误判，真正缺口只有 selfish。

### 🛡️ 安全卫士（OWASP + STRIDE 审计）
- **核心判断**：`E:\Code\Mangdehenzhi` 全栈整体安全评级 **D（高风险）**，共 26 项发现。身份认证与授权信任边界被完全击穿是最核心风险（提交的 JWT 密钥可伪造令牌、默认口令、方法级鉴权从未启用）。
- **关键建议**：上线前必须轮换提交的 JWT 密钥、移除默认种子口令、给匿名课程创建加鉴权、启用 `@EnableMethodSecurity` 并填充 `authorities`；全链路加 TLS、不暴露 MySQL/后端端口。完整 26 项见 `deliverables/SECURITY_AUDIT_REPORT.md`。

### ✅ 质量门神（QA 测试与发布）
- **核心判断**：🟡 **有条件通过**。`toFixed` 崩溃与 selfish 数据缺失已独立复现验证通过，无 P0。残留 1 项非阻塞缺陷：**bc（BC-MARL 模式）的 `bc_scores_history` 仍为空**，导致 monitor / consensus 两张 BC 积分图空状态（不崩溃，但国赛演示像 Bug）。
- **关键建议**：重新生成 `training_results_bc.json` 补齐 `bc_scores_history`；前端对空图加 noData 占位；回滚依赖文件备份（仓库无 git）。验证采用"静态源码 + 数据完整性 + 接口可达性"三重替代（无头浏览器不可用）。

### 🎨 设计师（设计系统与视觉）
- **核心判断**：Dashboard 视觉为深色科幻风、令牌统一，但存在 2 个 **Critical**——① 配色双轨（JS 霓虹色 `#00d4ff` 等与 CSS 令牌脱节）；② 空状态图表无占位（`bc_scores_history` 空导致两图只画坐标轴）。已产出 19 页零依赖 HTML 模板（`ppt_template/index.html`）。
- **关键建议**：建 `chartColor()` 统一取色；加 noData placeholder plugin；并同步排障手在数据侧补齐 `bc_scores_history`。另注 Inter 字体未加载、对比页图表拥挤等 Major 项。

### 🔍 产品官（产品评审）
- **核心判断**：19 页大纲骨架扎实，Dashboard 7 整页 + 11 图全部落位，满足"完整纳入"要求。唯一阻断演示的数据风险（selfish 缺失、pure/bc 口径）现已随排障修复消解；并纠正了"episode_rewards 全空"的误判。
- **关键建议**：PPT 内统一 **+40.6%（总奖励口径）vs +9.5%（公平环境口径）** 披露、主动坦诚讲局限；现有 `scripts/generate_ppt.py` 需改读仓库根数据路径（现读 `results/` 错位）。

---

## 2. 综合审查发现（去重合并，按严重度排序）

| # | 严重度 | 类别 | 位置 | 问题描述 | 建议 | 来源成员 |
|---|--------|------|------|---------|------|---------|
| 1 | 🔴 | 安全 | `application.yml:36` / `docker-compose.yml:40` / `.env.example:15` | 提交的 JWT 签名密钥为公开字符串，可伪造 admin 令牌 | prod 缺失即启动失败 + 外部注入强随机密钥 + 立即轮换 | 安全卫士 |
| 2 | 🔴 | 安全 | `DataInitializer.java:43-68` | 默认种子口令 admin/admin123、teacher/teacher123、student/student123 源码提交 | 仅 dev 种子化 + 首登强制改密 | 安全卫士 |
| 3 | 🔴 | 安全 | `SecurityConfig.java:30` / `CourseController.java:35-38` | `/api/courses/**` 设 permitAll，POST 可匿名写课程 | 要求认证 + 限 ADMIN/TEACHER | 安全卫士 |
| 4 | 🔴 | 设计 | Dashboard JS 取色 | 图表/共识/P2P 用并行霓虹色，与 CSS 令牌脱节（两套配色） | 建 `chartColor()` 统一取色 | 设计师 |
| 5 | 🔴 | 设计/数据 | `dashboard.html:1404 / 1694` | `bc_scores_history` 空 → 两张 BC 积分图只画坐标轴、无占位 | 数据侧补齐 bc 模式 + 前端加 noData 占位 | 设计师 / 质量门神 |
| 6 | 🟠 | 安全 | 全局无 `@EnableMethodSecurity` | 方法级鉴权完全缺失，`authorities` 恒为空，角色从未强制（F-001~003 根因） | 启用方法安全 + 填充 authorities | 安全卫士 |
| 7 | 🟠 | 安全 | `GET /api/assessments/results/{id}` | IDOR 读他人测评分数与 AI 报告 | 加 owner 过滤 | 安全卫士 |
| 8 | 🟠 | 安全 | `GET /api/users/{id}` | IDOR/PII 任意登录用户查他人 email/phone/role | 加 owner/角色校验 | 安全卫士 |
| 9 | 🟠 | 安全 | `docker-compose*.yml` | MySQL 3306 与后端 8080 映射宿主机，绕过 Nginx 隔离 | 仅 Nginx 暴露 80/443 | 安全卫士 |
| 10 | 🟠 | 安全 | 全链路 | 无 TLS，JWT/凭据明文传输；HSTS 写在 HTTP 上无效 | 全链路 HTTPS | 安全卫士 |
| 11 | 🟡 | 数据/QA | `training_results_bc.json` | BC-MARL 模式 `bc_scores_history` 缺失（selfish 已补、pure 预期无） | 重新生成 bc 模式补齐该字段 | 质量门神 / 排障手 |
| 12 | 🟡 | 安全 | `axios ^1.7.7` / CI | 组件漏洞（axios SSRF CVE、CI 动作未 SHA 锁定） | `npm audit` 升级 + 锁定 action | 安全卫士 |
| 13 | 🟡 | 设计 | Dashboard 字体/图表 | Inter 未加载、对比页 5 dataset 拥挤、P2P Canvas 尺寸需手工修复 | 加载字体、精简图例、修 Canvas | 设计师 |
| 14 | 🟢 | 安全 | 注册/日志等 | 邮箱枚举、GET 改状态非幂等、HTTP 上声明 HSTS、日志含 URI 等 | 低优先修复 | 安全卫士 |

> 安全侧完整 26 项（含 Medium 8 项、Low/Info 8 项）与 STRIDE 表、OWASP Top 10 检查表、P0–P3 路线图见 `deliverables/SECURITY_AUDIT_REPORT.md`。

---

## 交付清单（代码变更 + 测试覆盖 + 回滚预案）

**代码变更（MARL Dashboard）**
- `templates/dashboard.html`：4 处空值保护（行 1463 / 1552 / 2148 / 2192），消除 `toFixed` 崩溃
- `scripts/generate_complete_data.py`：3 处（`BASE_DIR` 指向项目根 + 写入前 `_validate_training_data()` 校验 + `--mode` 参数）
- `training_results_selfish.json`：重生为 1000 回合完整数据（旧桩备份为 `training_results_selfish_backup.json`）

**测试覆盖（QA 独立验收）**
- 启动 Dashboard（8093）→ `/` 返回 200 且含修复代码；`/api/compare` 三模式 `episode_rewards` 均 = 1000；`/healthz` 返回 ok
- 全 9 标签页 DOM 定义齐全，8 个支撑 API 全部 200
- 三重替代验证：静态源码（所有取值带 `|| {}`/`|| []` 守护）+ 数据完整性 + 接口可达性

**回滚预案**
- 仓库无 git，回退只能依赖文件备份 / 重新生成
- 前端：`dashboard.html:2192` 可还原为 `values[i].toFixed(2)`（⚠️ 会重新引入崩溃，仅当该改动本身导致数值异常时回退）
- 数据：`training_results_selfish_backup.json` 为旧桩（不推荐回退，会重现数据缺失）；建议演示前对三份 json 各留 `.bak` 快照

---

## ✅ 行动清单（具体可执行项）

| # | 行动 | 负责方 | 紧急度 | 期望完成 |
|---|------|--------|--------|---------|
| 1 | 轮换提交的 JWT 密钥（外部注入强随机、prod 缺失即启动失败） | 后端/安全 | P0 | 上线前 |
| 2 | 移除默认种子口令 + 首登强制改密 + 启用方法级鉴权并填充 authorities | 后端 | P0 | 上线前 |
| 3 | 重新生成 `training_results_bc.json` 补齐 `bc_scores_history` | 排障手 | P1 | 演示前 |
| 4 | Dashboard 前端加 noData 占位 + `chartColor()` 统一配色（消除 C1/C2） | 设计师 + 排障手 | P1 | 演示前 |
| 5 | 全链路 TLS + 不暴露 MySQL/后端端口（仅 Nginx 出公网） | 运维/安全 | P1 | 上线前 |
| 6 | PPT 统一 +40.6% 口径披露 + 改 `generate_ppt.py` 数据读取路径到仓库根 | 产品官 | P2 | 制作前 |

---

## ⚠️ 待完善 / 已知局限

- **bc_scores_history 仅 bc 模式缺失**（pure 预期无区块链），需补数据或加占位——否则 monitor/consensus 两页空图易被误判为 Bug
- **+40.6% 口径披露**：总奖励口径（BC 含激励）vs 公平环境口径（约 +9.5%），PPT 须同时给出并注明，避免被质疑
- **Dashboard 9 标签页仅 7 张截图**（缺 consensus / explorer 两页），需补截或答辩现场直切
- **BC 3000 回合数据仅 pure 有**，收敛故事无法对比 BC 长程
- **未做浏览器实跑**（无头浏览器不可用），前端运行时错误以静态 + 数据 + 接口三重替代验证，结论可信但非 100% 等价运行时覆盖
- **仓库无 git**，回滚仅能靠文件备份

---

## 📚 成员产出索引

- 排障手（investigator）原始产出：对话内（dashboard.html 4 处 / generate_complete_data.py 3 处 / selfish 数据重生 + 二次核验证据）
- 安全卫士（security-officer）原始产出：`E:\Code\Mangdehenzhi\deliverables\SECURITY_AUDIT_REPORT.md`
- 质量门神（qa-lead）原始产出：对话内 QA 验收报告（🟡 有条件通过）
- 设计师（designer）原始产出：`<MARL项目根>/DESIGN_REVIEW.md` + `<MARL项目根>/ppt_template/index.html`（19 页零依赖模板）
- 产品官（product-reviewer）原始产出：对话内 19 页叙事大纲 + 数据修复清单

---

> 本报告由软件工坊 AI 协作生成，关键决策请由工程负责人复核。
