# MARL Dashboard 收尾修复（P1 数据 + P2 前端空状态占位）

**日期**：2026-07-17
**场景**：Dashboard 缺陷收尾（调试修复 + 视觉验收）
**执行方式**：原定排障手+质量门神二人协作，但 agent 执行后端连续 502 网络故障（ENOTFOUND copilot.tencent.com），子 Agent 拉起失败；主理人改用自有工具（Read/Edit/Bash）直接落地，QA 验证由主理人代执行。

---

## 📌 TL;DR
- **整体结论**：🟢 已完成（Dashboard C2 空状态缺陷 + P1 bc 数据缺口均消除）
- **阻塞项**：0
- **下一步**：国赛演示前用真实浏览器跑一遍 console 复核（环境无无头浏览器，本次以静态+数据+接口三重验证）

---

## 1. P1 · 补齐 BC 模式 bc_scores_history 数据

**根因**：磁盘上的 `training_results_bc.json` 是**旧版本脚本生成的残缺文件**（仅 `episode_rewards/cooperation_rates/losses/summary` 4 个字段），缺失 `bc_scores_history` 及 `ecdsa_stats/consensus_stats/blockchain_stats` 等一整批字段。当前 `scripts/generate_complete_data.py` 的 bc 分支本就写 `bc_scores_history`（第 258/268 行），无需改脚本。

**修复**：用项目 `.venv`（numpy 2.5.0）重跑 `python scripts/generate_complete_data.py --mode bc`，重新生成完整 bc 数据（13 字段，含 `bc_scores_history` 长度 1000）。旧文件自动备份为 `training_results_bc_backup.json`。

**验证**：`GET /api/compare` → bc 现含 13 字段，`bc_scores_history` 为非空 list（len=1000）。

---

## 2. P2 · 前端空状态占位（emptyStatePlugin）

依据 `DESIGN_REVIEW.md §5` 定稿规范，在 `templates/dashboard.html` 落地 8 处改动：
1. 新增 `emptyStatePlugin`（Chart.js `afterDraw`，数据集为空时居中绘制 ⬡ + 分级文案），并 `Chart.register(emptyStatePlugin)`。
2. `chart-bc`（监控页，约原 1422 行）：传入分级文案，按 `monitorMode` 分级——
   - bc 模式数据缺失 → `BC 积分数据缺失 · 请重新生成 training_results_bc.json`
   - pure/selfish → `本模式未启用区块链激励 · 无链上积分`
3. `chart-bc-history`（区块链页，约原 1707 行）：同样分级（按 `d === bcData` 判定）。
4. 共识页 `loadConsensus`：权重为空时切换 `#consensus-empty` → `本模式未启用区块链 · 共识数据不可用`。
5. 区块浏览器页 `loadBlockExplorer`：区块为空时切换 `#explorer-empty` → `本模式未启用区块链 · 链上数据不可用`。
6. HTML 注入 `#consensus-empty` / `#explorer-empty` 两个 `.empty-state` 容器。
7. `static/dashboard.css` 新增 `.empty-state` 样式（对齐设计令牌 `--accent`/`--text-dim`）。

**验证**：源码 `grep emptyStatePlugin` 命中 6 处（定义+注册+两级文案）；`/healthz` 返回 ok。

---

## 3. 验证总览
| 检查 | 结果 |
|------|------|
| `GET /healthz` | ✅ `{"status":"ok","version":"v3.9"}` |
| `GET /api/compare` bc `bc_scores_history` | ✅ 非空，len=1000 |
| 源码含 `emptyStatePlugin` | ✅ 6 处命中 |
| 前端运行时渲染（无头浏览器） | ⚠️ 环境不可用，以静态+数据+接口三重替代验证 |

---

## ⚠️ 已知局限
- 重新生成 bc 数据为合成随机值（avg_reward≈-55，last50≈-42），与历史演示数值略有漂移，属合成数据正常波动。
- 前端实跑（console）未做，建议国赛演示机用真实浏览器复核空状态占位与图表渲染。
- 原定质量门神回归因 agent 后端故障未独立执行，由主理人代做接口级验证。

---

> 本报告由软件工坊主理人直接执行（agent 后端 502 故障期间），关键决策请由工程负责人复核。
