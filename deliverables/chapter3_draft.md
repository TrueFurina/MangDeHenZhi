# 第 3 章 AI 智能测评、元宇宙 3D 训练与区块链存证三大核心能力模块

## 论点

芒得很职平台宣称的"AI + 元宇宙 + 区块链"三大核心能力，经实码核验后呈现**显著的成熟度梯度**：元宇宙 3D 渲染为**已真实交付**的可用能力，AI 智能测评为"真实大模型调用路径 + 阈值规则降级"的**混合态**（默认无 Key 时退化为确定性模拟），区块链存证则为**纯预留/模拟**（既无真实上链，连模拟调用入口亦被注释断开）。在对外表述与产品承诺中，须严格区分"已上线功能"与"路线图/概念验证"，避免将模拟实现表述为生产级能力。

## 论据与事实核验

### 一、AI 智能测评：评分本地确定性，AI 仅生成报告叙事

**事实一（评分非 AI）**：测评的维度得分由前端本地题库逻辑计算得出——`calculateAllScores` 按维度聚合选项分值，`calculateTotalScore` 对各维度分**直接求和**（[questions.ts](file:///E:/Code/Mangdehenzhi/frontend/src/data/questions.ts)，L208–209）；后端 `AssessmentService.submitAssessment` 同样对各维度分求和得总分，并以 `总分 >= passScore` 判定通过（[AssessmentService.java](file:///E:/Code/Mangdehenzhi/backend/src/main/java/com/mangdehenzhi/service/AssessmentService.java)，L59–62）。即"智能测评"的评分环节完全由确定性规则完成，并不经过大模型。

**事实二（真实 API 路径存在但默认不触发）**：`AIService` 优先调用 `DeepSeekService`；后者仅在环境变量 `DEEPSEEK_API_KEY` 存在时启用，向 `https://api.deepseek.com/v1/chat/completions` 发送 `deepseek-chat`、temperature 0.7、max_tokens 2048 的请求（[DeepSeekService.java](file:///E:/Code/Mangdehenzhi/backend/src/main/java/com/mangdehenzhi/service/DeepSeekService.java)，L26、L38、L112–113）。这一真实大模型调用链路是**已交付**的。

**事实三（默认降级为阈值模拟）**：当 Key 缺失或调用异常，`AIService` 降级为本地规则——总分阈值判定（≥90 优秀 / ≥75 良好 / ≥60 及格）、强项（≥80）/弱项（<60）机械划分，推荐课程为固定三项静态数组，且 `aiConfidence` **硬编码为 0.85**（[AIService.java](file:///E:/Code/Mangdehenzhi/backend/src/main/java/com/mangdehenzhi/service/AIService.java)，L50、L84–127）。该常量并非真实置信度估计，属于"看似智能"的拟真输出。

### 二、元宇宙 3D 训练：渲染真实，互动训练未交付

**事实四（渲染能力真实且完成度高）**：`ThreeScene.vue` 是真实的 Three.js 实现——`WebGLRenderer`（antialias）、`shadowMap`（PCFSoftShadowMap）、`ACESFilmicToneMapping` 色调映射、`CSS2DRenderer` 标签渲染、`OrbitControls` 阻尼交互一应俱全（[ThreeScene.vue](file:///E:/Code/Mangdehenzhi/frontend/src/components/ThreeScene.vue)，L56–80）。四类场景 INTERVIEW_ROOM / CLASSROOM / MEETING_ROOM / TRAINING_ROOM 各有专属几何与 AI 角色（圆柱身体 + 球头 + 发光光环 + CSS2D emoji 标签）（L30–35、L136–320）。**此能力确已交付且质量可观**。

**事实五（互动训练为预留）**：`MetaverseService` 仅做会话生命周期管理（生成 roomId、结束置 active=false、返回场景配置 JSON）（[MetaverseService.java](file:///E:/Code/Mangdehenzhi/backend/src/main/java/com/mangdehenzhi/service/MetaverseService.java)，L24–104）。值得警惕的是，`getSceneConfig` 声明的 `features:["voiceInteraction","behaviorTracking","realTimeFeedback"]` 在前端与组件中**均无可对应实现**（L71–98 对比 ThreeScene.vue）。AI 角色为静态几何体，训练中无语音/行为追踪/实时反馈，亦无训练结果持久化。

### 三、区块链存证：纯模拟，连模拟入口亦断开

**事实六（上链为随机值，验证恒为真）**：`BlockchainService.storeOnChain` 返回随机 UUID 拼接的 txId，真实 Hyperledger Fabric 调用**全部注释**（[BlockchainService.java](file:///E:/Code/Mangdehenzhi/backend/src/main/java/com/mangdehenzhi/blockchain/BlockchainService.java)，L22–36）；`verifyOnChain` 硬编码 `verified:true`、`blockNumber:123456`，属纯模拟（L43–56）；`connectToFabricNetwork` 仅占位（L61–66）。

**事实七（存证调用甚至未接入）**：`CertificationService.issueCertification` 中区块链存证调用**被注释掉**（L39–40），证书仅本地存储 SHA-256 哈希于数据库；`verifyCertification` 仅在 DB 标记 VERIFIED，无链上查验（[CertificationService.java](file:///E:/Code/Mangdehenzhi/backend/src/main/java/com/mangdehenzhi/service/CertificationService.java)，L45–52）。当前"认证"本质是本地记录 + 哈希，与"区块链存证"承诺不符。

## 分析与外部最佳实践对照

**对照 AI 教育落地**：三节课在 DeepSeek 走红后 2 周内即上线首门相关课程、30 天内建成含 DeepSeek 的学习地图（[三节课](https://www.sanjieke.cn/about/article/1471)）；希沃教学大模型 2.0 将**知识图谱与向量数据库结合**抑制幻觉（[希沃](https://www.seewo.com/article/detail/2733)）。学术前沿上，Abu-Rasheed 等（2025）以知识图谱为 LLM 提供事实上下文以降低推荐解释幻觉（[arXiv:2501.12300](https://arxiv.org/html/2501.12300)）；暨南大学 KnowLP 以 GraphRAG 双知识结构图 + 强化学习做个性化学习路径，AAAI 2026 录用（[arXiv:2506.22303](https://arxiv.org/pdf/2506.22303)）。相较之下，本平台个性化推荐仅为"弱维度 → 课程类别"的启发式映射（[RecommendationService.java](file:///E:/Code/Mangdehenzhi/backend/src/main/java/com/mangdehenzhi/service/RecommendationService.java)，L70–85），未引入领域知识模型，真实性与可解释性弱于上述实践。

**对照 VR/3D 实训价值**：PwC VR 研究显示 VR 学员完成速度快 4 倍、信心提升 275%、知识留存达 80%（[ArborXR](https://arborxr.com/blog/vr-training-statistics)）；Safety Science 2024 年 52 项研究元分析表明 VR 安全训练在知识留存上具大效应（Hedges' g = 0.838, p < 0.001）（[Skillsive](https://www.skillsive.com/blog/why-vr-training-is-more-effective-than-traditional-training)）。这**佐证了真实可交互 3D 训练的价值假设**，但本平台目前仅到"可视化"层，尚未产生可度量的训练成效数据，亦未实现交互闭环。

**对照区块链凭证标准与价值**：W3C 于 2025-05-15 将 Verifiable Credentials 2.0 发布为 W3C 标准，提供密码学安全、机器可验证的凭证表达（[W3C](https://www.w3.org/press-releases/2025/verifiable-credentials-2-0/)）；VerifyEd 指出 Open Badges 3.0 与 W3C VC 2.0 对齐、以区块链锚定密码学证明实现防篡改（[VerifyEd](https://www.verifyed.io/blog/digital-credentialing-2025)）。本平台若要兑现"区块链凭证"，应落到 VC 2.0 数据模型 + 真实链上锚定 + 可验证证明，而非随机 txId 与恒真校验。

## 小结

- **已交付**：元宇宙 3D 渲染（真实可运行）；AI 测评的"报告生成"存在真实 DeepSeek 调用路径（需配置 API Key）。
- **部分/降级交付**：AI 测评默认态为规则模拟，评分环节非 AI；个性化推荐为启发式映射，缺乏知识图谱增强。
- **预留/模拟（未交付）**：区块链上链与验证（连模拟入口亦断开）；元宇宙场景的语音交互、行为追踪、实时反馈与训练结果落库。
- **建议**：对外沟通须区分"已上线功能"与"概念验证/路线图"，修正 `getSceneConfig` 中对未实现功能的声明；对照 W3C VC 2.0、知识图谱增强推荐等最佳实践补齐真实性短板，方能匹配"AI + 元宇宙 + 区块链"的平台叙事。

---

## 新增来源清单（供主理人更新来源池）

1. [三节课：企业大学的AI机遇 / 自研AI学习智能体](https://www.sanjieke.cn/about/article/1471) — 三节课 DeepSeek 课程 2 周上线、30 天建成学习地图，AI 教育落地实例（企业官方）。
2. [希沃：教学大模型"1+N+N"AI技术体系，融合DeepSeek](https://www.seewo.com/article/detail/2733) — 希沃教学大模型 2.0 结合知识图谱+向量库抑制幻觉，教育 AI 落地（企业官方/媒体）。
3. [ArborXR: 22 VR Training Statistics](https://arborxr.com/blog/vr-training-statistics) — PwC/UPS/Deloitte 等 VR 培训数据：留存 80%、在岗率提升等（行业报告/媒体）。
4. [Skillsive: Why VR training is more effective](https://www.skillsive.com/blog/why-vr-training-is-more-effective-than-traditional-training) — 引 Safety Science 2024 元分析（52 研究，留存 g=0.838）与 PwC 4x 速度（行业分析）。
5. [W3C: Verifiable Credentials 2.0 成为 W3C 标准（2025-05-15）](https://www.w3.org/press-releases/2025/verifiable-credentials-2-0/) — 权威标准：VC 2.0 密码学安全、机器可验证（标准组织）。
6. [VerifyEd: Digital Credentialing 2025 / Open Badges 3.0 与 W3C VC 对齐](https://www.verifyed.io/blog/digital-credentialing-2025) — 区块链凭证价值与标准对齐（行业平台）。
7. [Abu-Rasheed et al. (2025): LLM-Assisted Knowledge Graph Completion for Curriculum Modelling, EDUCON2025](https://arxiv.org/html/2501.12300) — 知识图谱增强个性化学习路径推荐（学术论文）。
8. [Cheng et al. / 暨南大学 (2026): KnowLP, GraphRAG 双知识结构图学习路径推荐, AAAI 2026](https://arxiv.org/pdf/2506.22303) — 前沿个性化推荐 GraphRAG+强化学习（学术论文）。

## 复用代码来源（已核验，本地）

- [AIService.java](file:///E:/Code/Mangdehenzhi/backend/src/main/java/com/mangdehenzhi/service/AIService.java) — 阈值降级规则、硬编码 aiConfidence 0.85。
- [DeepSeekService.java](file:///E:/Code/Mangdehenzhi/backend/src/main/java/com/mangdehenzhi/service/DeepSeekService.java) — 真实 DeepSeek API 调用，需 DEEPSEEK_API_KEY。
- [BlockchainService.java](file:///E:/Code/Mangdehenzhi/backend/src/main/java/com/mangdehenzhi/blockchain/BlockchainService.java) — 模拟上链/恒真验证。
- [CertificationService.java](file:///E:/Code/Mangdehenzhi/backend/src/main/java/com/mangdehenzhi/service/CertificationService.java) — 存证调用已注释，仅本地哈希。
- [ThreeScene.vue](file:///E:/Code/Mangdehenzhi/frontend/src/components/ThreeScene.vue) — 真实 Three.js 四类场景渲染。
- [MetaverseService.java](file:///E:/Code/Mangdehenzhi/backend/src/main/java/com/mangdehenzhi/service/MetaverseService.java) — 会话管理 + 声明未实现 features。
- [RecommendationService.java](file:///E:/Code/Mangdehenzhi/backend/src/main/java/com/mangdehenzhi/service/RecommendationService.java) — 弱维度→课程类别启发式映射。
- [questions.ts](file:///E:/Code/Mangdehenzhi/frontend/src/data/questions.ts) — 维度分本地计算、总分求和。
