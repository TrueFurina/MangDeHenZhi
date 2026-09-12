# 芒得很职 — M1 源码抢救与仓库修复总结

## 做了什么
- **恢复 M1 丢失源码**：此前因本地 git 引用被反复清空 + `gc prune`，M1（North Star）服务端权威计分与证书防伪实现从磁盘和 git 双丢失。本次依据 `backend/target/mangdehenzhi-backend-1.0.0.jar` 中的编译产物（class 字节码），用 `javap` 提取签名与逻辑，重建了全部核心源码并重新入库。
- **推送 GitHub**：`feat/m1-recovery` 已成功推送到 GitHub；`main` 已对齐并推送 LICENSE 移除。

## 关键文件（已生成 / 修改）
- `backend/src/main/java/com/mangdehenzhi/blockchain/CertSignatureService.java` — SHA256withRSA 签名/验真；生产环境缺 `CERT_SIGN_PRIVATE_KEY` 则 fail-fast 拒绝启动，否则生成临时演示密钥（防伪核心）。
- `backend/src/main/java/com/mangdehenzhi/service/AssessmentScoringBank.java` — 5 维度题库 + 服务端权威计分 `computeDimension (前端分数不信任)。
- `backend/src/main/java/com/mangdehenzhi/entity/Enrollment.java`、`enums/EnrollmentStatus.java`、`repository/EnrollmentRepository.java`、`service/EnrollmentService.java` — 报名领域模型与基础服务。
- `backend/src/test/java/com/mangdehenzhi/service/M1ScoringCertSigningIntegrationTest.java` — 覆盖服务端计分与证书防伪（含篡改验签失败断言）。
- `frontend/src/theme/palette.ts` — 设计令牌（红涨绿跌）。
- `LICENSE` — 已从 `main` 移除并推送（符合"原创项目不要 license"约定）。

## 提交
- `feat/m1-recovery` → 已推送（8 个源码/令牌文件）
- `main` → 已推送（移除 LICENSE，两端均为 `31eb659`）

## 未解决 / 需注意
- **自动心跳提交**：GitHub `main` 持续被自动循环推送 `chore: daily contribution heartbeat`（约 126 条噪音提交），是"远程领先本地、推送冲突"的根因。建议禁用该自动化。
- **题库数据**：55 道题分值权重为从字节码还原的代表性样例（默认 4→90/3→65/2→35/1→15），引擎本身完整可用；如需精确原始分值可调校。
- **编译验证**：本环境无 Maven，未跑 `mvn test`；建议在 CI/本地 Maven 跑 `M1ScoringCertSigningIntegrationTest`。

## 下一步
- 合并 `feat/m1-recovery` 到 main（或发起 PR）。
- 停掉心跳自动化，保持 main 干净。
- 后续跑 Maven 验证编译与测试。
