# 功能规格 (PRD): Q3 启动 Sprint 1 — 可信度对齐 + 度量地基 + 轻量督学 v0

**状态:** 草稿（待主理人评审） | **作者:** 析客（requirement-analyst） | **日期:** 2026-07-17
**Sprint 周期:** 2026-07-20 ~ 2026-08-02（W1–W2，2 周） | **团队:** 产品战略 / 工程
**关联基线:** `roadmap-update-mangdehenzhi-2026-07-17.md`、全员代码交叉审计（析客 + 4 成员实证）

> 本文严格按产品战略团队「工作流 1：需求文档」模板撰写，并融合全员代码实证结论。判定口径沿用审计：**已实现 / 部分 / 桩代码 / 缺失**。

---

## TL;DR（执行摘要）

本 Sprint 是一次**从 over-claim 回归可信**的纠偏 Sprint——不追求功能增量，而追求三件地基事：

1. **消除误导表述（P0）**：证书页"全球可验证"文案改为**条件渲染**（当前 100% 未上链，须显式标注"研发中"）；修复推荐维度键中英不匹配 Bug（当前个性化恒返回技术类课程）。
2. **铺设度量地基（P0）**：建立埋点事件表 + 4 节点落点（登录活跃 / 测评完成 / 实训完成 / 闭环完成），解锁 MCL 与部分 AARRR 可测。
3. **交付首个真实用户价值（P0）**：轻量督学 v0（每日 nudge / 打卡 / 课程进度 / 提醒），直击瑞思结论中"学得完"#1 痛点，目标拉升降课率。

并行启动 **D5（C 网信办备案前置）** 与 **D6（B 立项）**，但**本 Sprint 不实现真实上链 MVP、不工程化 B 交互、不新增内容库**。

---

## 核心结论卡片

| 项 | 内容 |
|----|------|
| 当前可信度风险 | C 模块 100% mock（`blockchainTxId` 恒 null），前端 `Certifications.vue:8` 却宣称"区块链存证的技能认证证书，全球可验证" → 对外硬称"区块链可信"会被三节课官方证反噬（竞析） |
| 度量现状 | 埋点 = 0；MCL 仅"测评→证书"子闭环可算；3/5 AARRR 不可测 |
| 用户第一痛点 | "学得完 + 学得准"（瑞思）；督学为 roadmap Q3 P0，但代码现实为零实现 |
| 本 Sprint 北极星代理 | 督学 v0 覆盖活跃学员比例、埋点四事件落地率、误导文案清零率 |
| 风险排序 | 继续 over-claim 的合规/品牌风险 **>** 功能延迟风险 |

---

## 问题陈述

**我们在解决什么：** 芒得很职对外叙事（前端文案、README、营销）宣称"区块链存证·全球可验证""AI 角色实时互动"等能力，但代码实证显示——C 区块链 100% 为模拟桩、A 的 DeepSeek 默认关闭走硬编码 Mock、推荐因维度键 Bug 个性化失效、B 仅有 3D 渲染壳无训练/互动、且全平台**零埋点**导致 MCL 与 AARRR 不可测。在竞品（三节课）已联合工信部推"官网可查、全国通用"官方认证、抢占"可验证信任"心智的窗口期，我方用 mock 硬称"区块链可信"会被反噬。

**为什么现在做：** 路线图中 C 备案为 Q3 硬前置、督学为 Q3 P0，且"功能上了数不清"会直接拖垮后续所有健康度度量。Sprint 1 必须先把"可信基调 + 度量地基"打牢，否则 Q4 的 C-MVP 与 B-标杆都无从评估。

---

## 目标和非目标

### 目标（3 个正交）
- **目标① 消除误导表述，建立可信基调**：区块链证书页文案条件渲染/降级 + 推荐维度键统一修复。不新增功能，只修正陈述，是后续一切对外叙事（含 C 备案、雇主生态）的前提。
- **目标② 铺设度量地基**：埋点 P0 事件表 + 4 节点（登录活跃 / 测评完成 / 实训完成 / 闭环完成）落点。独立于功能交付，为 MCL 与 AARRR 补地基。
- **目标③ 交付首个真实用户价值（轻量督学 v0）**：打卡 / 提醒 / 课程进度 / 每日 nudge。在 A 基本盘上直接对冲"学得完"痛点，是唯一被验证可拉升完课率（+15~20pp）的手段，当前为零代码。

### 非目标
- 不做真实上链 MVP（仅备案启动 D5）。
- 不做 B 交互工程化（仅立项 D6，不实现 AI 角色互动/实训内容）。
- 不新增内容库（课程/题量）。
- 不做短信/邮件/外部推送渠道（督学仅站内）。
- 不做重 AI 辅导（督学为轻量 nudge，不实现 AI 陪练/自适应教学）。

---

## 用户故事

- **US-1（学员·可信感知）**：作为一名通过测评的学员，我希望在证书页看到诚实的状态说明，以便我知道证书当前是"本地存证（研发中）"还是"已上链可验证"，而不是被误导以为已全球可验证。
- **US-2（学员·学得准）**：作为一名沟通能力薄弱的学员，我希望推荐课程能针对我的弱项（如沟通/协作类），以便我补的是真短板，而不是每次都收到同样的技术类课程。
- **US-3（学员·学得完）**：作为一名每天仅 15–20 分钟的碎片化转行在职者（>70%），我希望收到每日学习 nudge、看到课程进度与打卡提醒，以便我能坚持学完、不被遗忘拖垮。
- **US-4（产品/数据·可度量）**：作为一名负责北极星指标的产品经理，我希望关键行为（登录/测评完成/实训完成/闭环完成）被埋点记录，以便我能计算 MCL 代理与首测率，而非"功能上了数不清"。
- **US-5（HR/雇主·可验证·远期铺垫）**：作为一名雇主，我希望未来能验证候选人的实操能力档案，以便我信任其技能——本 Sprint 通过"诚实叙事 + 备案启动"为直连雇主铺垫，而非用 mock 硬称"区块链可信"。

---

## 方案设计（关键流程 / UI 文字描述）

**① 督学提醒流（P0-SUP1）**
- 触发：学员登录 → 后端按 `user_id` 查 `study_checkins` 与课程进度 → 计算"距上次活跃天数"。
- 展示：Dashboard 顶部"继续学习"提醒卡（未完成课程名 + 进度条 + "继续"按钮）；若 ≥3 天未活跃，卡片置顶并显示"你已 X 天未学习，今日一步：…"。
- 每日 nudge：基于最近一次测评弱维度 + 未完成课程，生成一句话"今日推荐一步"（如"用 15 分钟补一节《沟通与协作技巧》"）。
- 打卡：课程详情/首页提供"今日打卡"按钮，写入 `study_checkins`（日期 + user_id）。

**② 埋点落点（P0-EVT1）**
- `login`：`AuthController` 登录成功后写事件。
- `assessment_complete`：`AssessmentService.submitAssessment` 成功落库后写事件。
- `training_complete`：`MetaverseService.endSession` 成功后写事件（首次为实训完成语义落地）。
- `loop_complete`：`AssessmentService` 中 `passed` 且 `CertificationService.issueCertification` 成功后写事件（= MCL 子闭环）。
- 统一入口：`MetricsService.log(event, userId, payload)` → `metrics_events` 表。

**③ 证书页条件渲染（P0-C1）**
- `Certifications.vue` 读取 `cert.blockchainTxId`：
  - 为 null（当前全量）→ 顶部状态条显示"📄 本地存证（链上存证研发中）"，移除"全球可验证/不可篡改"硬宣称；`blockchain-info` 块不渲染。
  - 非空（未来真上链后）→ 显示"🔗 区块链存证·可验证"区块与交易号（为 P2 预留）。
- 验证按钮文案："验证证书（本地校验）"，仍调 `/certifications/verify/{hash}`。

**边界/错误场景**：未登录访问埋点落点不写 userId（匿名事件可记，但不计入 MCL）；督学 nudge 在无测评记录时回退为"继续未完成课程"通用提示；埋点写入失败不影响主业务流程（异步/旁路，不阻塞）。

---

## 技术考量

- **数据模型新增**：
  - `metrics_events(id, user_id, event VARCHAR, payload JSON, created_at)`。
  - `study_checkins(id, user_id, checkin_date, created_at)`；课程进度复用现有 `course` / 测评完成记录，新增 `user_course_progress(user_id, course_id, last_active, completed_count)`（轻量）。
- **后端改动**：`MetricsService` 新服务 + 4 处落点接入；`RecommendationService.mapDimensionsToCategories` 键映射修正（英文键或双语）；`CertificationController`/`Certifications.vue` 文案条件渲染；`AdminController` 新增 MCL 代理/首测率统计端点。
- **前端改动**：`Certifications.vue` 条件渲染；`Dashboard.vue` 接入提醒卡 + 今日 nudge；新增打卡入口。
- **性能影响**：埋点为旁路写，异步化避免阻塞主请求；`metrics_events` 按 `created_at` 分区/索引。
- **迁移计划**：新增表走 JPA `ddl-auto: validate` 兼容（生产需同步 init.sql）；P0-C1 文案为纯前端条件渲染，零数据迁移。
- **复用既有**：督学 v0 全部复用现有 `course` / `assessment_results` 数据，不新增内容。

---

## 成功指标

| 指标 | 当前 | 目标 | 评估时间 |
|------|------|------|---------|
| 误导文案清零率 | 证书页 100% 硬宣称"全球可验证" | 100% 改为条件渲染（null 时不显示） | Sprint 1 结束 |
| 推荐个性化修复率 | 弱维度恒返 TECHNOLOGY 类 | 英文维度键→正确类目单测通过率 100% | Sprint 1 结束 |
| 埋点四事件落地率 | 0 事件 | login/assessment_complete/training_complete/loop_complete 100% 落表 | Sprint 1 结束 |
| 督学 v0 活跃覆盖 | 0（缺失） | ≥70% 活跃学员登录可见 nudge/进度卡 | Sprint 1 结束 |
| 完课率（远期观测） | 行业 30–50% | 上线督学后 +15~20pp（多 Sprint 观测，本 Sprint 仅采集基线） | Sprint 2+ |
| MCL 可算性 | partial（缺实训完成） | 子闭环可算 + 看板可见代理值 | Sprint 1 结束 |

---

## 验收标准

- **P0-C1（文案条件渲染）** — Given 证书 `blockchainTxId` 为 null（当前全量），When 进入证书页，Then 显示"本地存证（链上存证研发中）"且无"全球可验证/不可篡改"硬宣称；Given `blockchainTxId` 非空（未来），When 渲染，Then 才显示"区块链存证·可验证"区块与交易号。
- **P0-REC1（推荐键统一）** — Given 用户弱维度为英文键 `communication`，When 调用 `recommendCourses`，Then 推荐结果包含 `SOFT_SKILLS` 类课程（而非恒为 `TECHNOLOGY`）；单测覆盖 3 英文维度键→正确类目。
- **P0-EVT1（埋点地基）** — Given 用户完成登录/提交测评/结束实训/闭环发证，When 服务端处理，Then 对应 `metrics_events` 行写入；提供最小查询接口供数析取数。
- **P0-SUP1（督学 v0）** — Given 学员有未完成课程，When 登录首页，Then 看到进度条 + 今日 nudge；Given 学员手动打卡，When 点击"今日打卡"，Then `study_checkins` 写入当日记录；Given ≥3 天未活跃，When 登录，Then "继续学习"卡置顶提示。
- **P1-DASH1（指标看板）** — Given `metrics_events` 有数据，When 管理员查看统计，Then 可见 MCL 代理（月度 `loop_complete` 计数）与首测率。
- **P2-ADAPT1（自适应路径）** — Given 用户有最新测评结果，When 请求 `/recommendations/learning-path`，Then 返回基于该结果的路径（替换当前硬编码静态串）。

---

## 需求池（P0 / P1 / P2）

> 估算单位：人天（pd）。为基于相似任务的粗略估算，需主理人与工程对齐；遵循范围守恒——本 Sprint 新增均为纠偏/地基，不膨胀功能增量。

### P0（Sprint 1 必须完成）

| 编号 | 需求 | 优先级 | 验收标准（要点） | 估算 |
|------|------|--------|------------------|------|
| P0-C1 | 区块链证书页可信文案修正（条件渲染/降级） | P0 | 见上「验收标准 P0-C1」；代码点 `Certifications.vue:8,34-37,120-131` | 1.5 pd |
| P0-REC1 | 推荐维度键统一（英文键或双语映射） | P0 | 见上「验收标准 P0-REC1」；修复 `RecommendationService.mapDimensionsToCategories`(L70-85)，补单测，校验 `questions.ts` 与 `DataInitializer` 键一致 | 1.5 pd |
| P0-EVT1 | 埋点 P0 事件表 + 4 节点落点 | P0 | 见上「验收标准 P0-EVT1」；建 `metrics_events` 表 + `MetricsService` + 4 落点；最小查询接口 | 3 pd |
| P0-SUP1 | 轻量督学 v0（打卡/提醒/课程进度/每日 nudge） | P0 | 见上「验收标准 P0-SUP1」；Dashboard 提醒卡 + nudge + 打卡；仅站内 | 4 pd |

### P1（紧接 Sprint 1 / 依赖 P0）

| 编号 | 需求 | 优先级 | 验收标准（要点） | 估算 |
|------|------|--------|------------------|------|
| P1-RET1 | 登录事件解锁留存度量 | P1 | 基于 P0-EVT1 `login` 事件，可计算次日/月留存率（代理） | 1 pd |
| P1-DASH1 | admin 指标看板（MCL 代理 / 首测率） | P1 | 新增统计端点：MCL 代理=`loop_complete` 月度计数；首测率=有 `assessment_complete` 活跃用户/活跃用户；admin 页可见 | 2 pd |
| P1-PROF1 | A 画像结构化 | P1 | `AssessmentResult.aiAnalysis` JSON 解析入结构化字段（strength/weakness），向后兼容旧字符串，供后续推荐/画像 | 1.5 pd |

### P2（后续 Sprint / 本 Sprint 不实现）

| 编号 | 需求 | 优先级 | 验收标准（要点） | 估算 |
|------|------|--------|------------------|------|
| P2-FAB1 | 真实 Fabric 上链 PoC | P2 | 接入 Hyperledger Fabric（或合规联盟链）`Gateway`，`storeOnChain` 真写入返 `blockchainTxId`；`verifyOnChain` 真查链；`pom.xml` 加依赖。需 D5 备案完成后启动 | 5+ pd（依赖备案） |
| P2-ADAPT1 | 自适应路径（替换硬编码桩） | P2 | `RecommendationController.getLearningPath`(L37-46) 改为调 `generateLearningPath(result)`，基于用户最新测评结果 | 1 pd |

**范围守恒说明**：本 Sprint 四项 P0 均为"纠偏/地基"，不含功能增量；P2 两项明确延后。通过**移除 over-claim 表述**而非新增承诺来守恒范围——这正是"每个 scope 新增须伴随 scope 移除或时间线延长"的落地方式。

---

## 用户研究洞察（瑞思）

- 跨人群 #1 痛点 = "学得完 + 学得准"。
- 转行在职者占比 >70%，每天仅 15–20 分钟，63% 时间碎片化 → 完课率塌方根因"无人盯/问/管"。
- 行业完课率 30–50%（纯录播 <30%），加轻量督学后 +15~20pp。
- 督学为 roadmap Q3 P0，但代码现实为零实现 → 本 Sprint 的督学 v0 是直接承接用户第一痛点的首个真实价值交付。
- **对 PRD 的含义**：督学 v0 必须"低投入高确定性"——只做 nudge/打卡/进度/提醒这类轻量、确定能提升完课率的能力，不碰重 AI 辅导。

---

## 竞品对比（竞析）

| 维度 | 三节课 | 芒得很职（当前） | 风险 |
|------|--------|------------------|------|
| 可信认证 | 联合工信部推"官网可查、全国通用"AI 岗位能力认证，抢"可验证信任"心智 | C 模块 100% mock，前端却宣称"区块链存证·全球可验证" | 硬说"我们有区块链可信证书"会被官方证反噬，损害品牌信任 |
| 叙事建议 | — | 对外叙事从"我们有区块链"转为"用区块链做不可篡改的实操能力档案，并直连雇主" | over-claim 风险高于功能延迟风险 |

**结论**：本 Sprint 的"可信度对齐"不是可选美化，而是防御竞品心智抢占的必需动作；C 备案（D5）启动是叙事转向"实操能力档案 + 直连雇主"的合规前置。

---

## 数据依据（数析）

- 埋点 = 0：无事件表、无 SDK 落点 → MCL 与 3/5 AARRR 不可测。
- MCL（月度闭环认证学员数）：当前仅"测评→证书"子闭环可算（依赖 `assessment_results.passed` + `certifications`），但缺"实训完成"节点，故为 partial。
- 完课率行业基准 30–50%；加轻量督学 +15~20pp（本 Sprint 上线能力并采集基线，效果在后续 Sprint 观测）。
- AARRR 可测性：获客/激活/留存 3 项依赖埋点（登录活跃、首测），当前不可测；变现/推荐依赖后续。

---

## 时间线 & 里程碑（Sprint 1：W1–W2）

> 采用路径重基线：Sprint 1 含 D1 埋点地基 / D4 督学 v0 / D5 C 备案启动 / D6 B 立项。埋点与督学为 P0 前置，并行推进。

| 周 | 并行流 | 交付 |
|----|--------|------|
| W1（D1–D3） | 流A：P0-C1 文案修正 + P0-REC1 推荐键修复（同日可上线，解误导）｜流B：P0-EVT1 埋点事件表 + 4 落点（启动）｜流C：D5 C 备案材料启动、D6 B 立项文档 | 误导文案清零、推荐 Bug 修复、埋点骨架 |
| W2（D4–D5） | 流A：P0-EVT1 落点联调 + P1-DASH1 看板雏形｜流B：P0-SUP1 督学 v0 开发+联调｜流C：D5 备案提交/受理、D6 立项评审 | 埋点四事件可查、督学 v0 上线、备案/B 立项完成 |

**里程碑 M1（Sprint 1 结束）**：
- ① 误导表述清零率 100%（证书页条件渲染上线）
- ② 埋点四事件落地率 100%（`metrics_events` 可查）
- ③ 督学 v0 覆盖活跃学员（站内 nudge 可见）
- ④ D5 备案已启动、D6 B 立项通过

---

## 待确认问题（Open Questions）

1. **DEEPSEEK_API_KEY 默认策略**：是否在 Sprint 1 默认启用（配置 Key 走真实 DeepSeek）？若启用需明确密钥来源/成本；若仍默认关闭，则 P1-PROF1 解析的是 Mock JSON——需确认 Mock 与真实 JSON schema 是否一致，避免结构化解析断裂。
2. **实训完成定义**：`training_complete` 事件当前绑定 `MetaverseService.endSession`。但 B 无"实训内容/完成度语义"，一个空 3D 房间进入即结束是否算"实训完成"？需确认度量口径，否则与完课率叙事失真。
3. **备案主体**：D5 网信办备案的责任主体/资质（公司主体是否具备？）、周期与所需材料预算——决定 P2-FAB1 启动时间与对外"可验证"叙事的合规边界。
4. **（析客追加）死代码与技术债**：`ai/AnalysisReport.java`、`ai/RecommendationResult.java`、`metaverse/SceneConfig.java`、`metaverse/AICharacter.java` 全仓无引用；`CertificationController.getCertification(id)` 返回 `success(null)` 桩；`getSceneConfig` 死接口。建议本 Sprint 一并清理或标注 TODO，是否计入 P0/P1 工作量需主理人裁定。

---

*范围管理记录：本 PRD 所有新增项均为纠偏/地基类，未引入功能增量膨胀；P2 两项（真上链、自适应路径）明确延后至备案/纠偏完成后。v1 = 本 Sprint 交付；v2 = P2 及后续增强。*
