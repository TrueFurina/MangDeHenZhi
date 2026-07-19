# 备份与恢复手册 (R2)

> 对应 SRE 巡检项：**R2（P0 可靠性）— 原仅 named volume，无 dump / 异地 / binlog(PITR) / 演练**。
> 本手册说明当前已落地的备份体系、恢复步骤，以及**季度恢复演练** checklist。

---

## 1. 体系架构

```
┌─────────────┐   每日全量 dump     ┌──────────────────────┐
│  db (mysql) │ ────(mysqldump)───▶ │  backup 容器          │
│  binlog ON  │ ◀─── binlog 归档 ── │  /backups             │
│ /var/log/   │                    │   ├─ mangdehenzhi-...  │
│  mysql/     │                    │   │    .sql.gz (全量)  │
└─────────────┘                    │   └─ binlogs/          │
                                   │       mysql-bin.* (PITR)│
                                   └──────────────────────┘
                                              │
                                  （建议）rsync / COS 同步 → 异地
```

- **全量备份**：`backup` 服务基于 `mysql:8.0` 镜像，启动即执行一次 `mysqldump --single-transaction`，之后每 24h 轮询一次。
- **PITR 物料**：db 已开启 `log-bin`（ROW 格式，保留 7 天）；备份时 `flush-logs` 切换，并将 binlog 文件复制到 `/backups/binlogs`，形成「全量 + 增量」连续链。
- **保留策略**：本地保留最近 7 天全量 dump（`RETENTION_DAYS` 可配）。
- **容灾增强（待办）**：当前备份卷与数据库同在单宿主机，仅防单盘/误删；**强烈建议**将 `/backups` 同步至对象存储（COS/S3）或异地主机，实现真正的容灾。

---

## 2. 关键文件

| 文件 | 作用 |
|---|---|
| `mysql/conf.d/mysql-bin.cnf` | 开启 binlog、server-id、7 天过期 |
| `scripts/backup.sh` | 全量 dump + binlog 归档 + 保留策略 |
| `scripts/restore.sh` | 全量 / 全量+PITR 恢复 |
| `docker-compose.yml` → `backup` 服务 | 调度备份容器，挂载 `mysql_backups` / `mysql_binlogs` |

---

## 3. 日常运维

### 3.1 查看已生成备份
```bash
docker compose exec backup ls -lhR /backups
```

### 3.2 手动触发一次备份
```bash
docker compose exec backup sh /backup.sh
```

### 3.3 确认 binlog 已开启
```bash
docker compose exec db mysql -u root -p"$MYSQL_ROOT_PASSWORD" -e "SHOW VARIABLES LIKE 'log_bin';"
# 期望: log_bin = ON
```

---

## 4. 恢复流程

### 4.1 全量恢复（最近一份 dump）
```bash
docker compose run --rm \
  -e MYSQL_ROOT_PASSWORD="$MYSQL_ROOT_PASSWORD" \
  backup sh /restore.sh /backups/<最近的>.sql.gz
```

### 4.2 时间点恢复 (PITR)
先恢复全量，再重放 binlog 到故障前时刻：
```bash
docker compose run --rm \
  -e MYSQL_ROOT_PASSWORD="$MYSQL_ROOT_PASSWORD" \
  backup sh /restore.sh /backups/<最近的>.sql.gz "2026-07-17 11:58:00"
```
> 截止时间应**早于**误操作/故障发生时间，避免把坏数据一并重放。

### 4.3 恢复后校验
```bash
docker compose exec db mysql -u root -p"$MYSQL_ROOT_PASSWORD" \
  -e "USE mangdehenzhi; SELECT COUNT(*) FROM assessment_result; SELECT MAX(created_at) FROM certification;"
```

---

## 5. 季度恢复演练 Checklist（R2 验收项）

> 每季度至少一次，记录结果到本文件末尾「演练记录」。

- [ ] 在**隔离环境**（或临时 `backup` 容器 + 临时库）拉起一份干净 MySQL
- [ ] 执行 4.1 全量恢复，确认库表结构完整、行数符合预期
- [ ] 执行 4.2 PITR，确认能恢复到指定时间点
- [ ] 校验关键业务表（assessment_result / certification / user）数据一致性
- [ ] 记录 RTO（恢复耗时）与 RPO（最大数据丢失窗口 ≈ 备份间隔，当前 24h；PITR 可缩至分钟级）
- [ ] 验证 `/backups` 异地同步链路（若已配置）
- [ ] 复盘：恢复脚本、凭证、文档是否仍有效，更新本手册

---

## 6. 演练记录

| 日期 | 执行人 | 全量恢复 | PITR | RTO | RPO | 结论 |
|---|---|---|---|---|---|---|
| （待填） |  |  |  |  |  |  |

---

## 7. 已知限制与后续

- **RPO = 24h（全量）**：未配置实时/近实时同步；若需更小 RPO，PITR（binlog）可将窗口缩至分钟级，但需保证 binlog 异地留存。
- **R3 未做**：仍为单机单实例，无主从/多副本；本备份仅解决「数据可恢复」，不解决「服务高可用」。HA 见 SRE 报告 R3。
- **异地容灾待办**：当前备份卷与 db 同主机，需增加 COS/异地同步以满足真实容灾要求。
