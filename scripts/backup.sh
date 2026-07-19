#!/bin/sh
# ============================================================
# 芒得很职 (Mangdehenzhi) — MySQL 备份脚本
# 设计目标（对应 SRE R2）：
#   1. 每日全量逻辑备份（mysqldump --single-transaction）
#   2. 备份时 flush-logs，使后续 binlog 形成 PITR 连续链
#   3. 归档 binlog 文件，提供时间点恢复物料
#   4. 本地保留策略（默认 7 天），并提示异地/对象存储增强
# 由 docker-compose 中 backup 服务以 "启动即备 + 每日轮询" 方式调用。
# ============================================================
set -eu

BACKUP_DIR="${BACKUP_DIR:-/backups}"
BINLOG_DIR="${BINLOG_DIR:-/var/log/mysql}"
RETENTION_DAYS="${RETENTION_DAYS:-7}"
DB="${MYSQL_DATABASE:-mangdehenzhi}"
HOST="${MYSQL_HOST:-db}"

# 凭证选择：优先 root（具备 RELOAD/PROCESS，可做一致性快照并取 binlog 位点）
if [ -n "${MYSQL_ROOT_PASSWORD:-}" ]; then
  DB_USER=root
  DB_PASS="${MYSQL_ROOT_PASSWORD}"
elif [ -n "${MYSQL_PASSWORD:-}" ]; then
  DB_USER="${MYSQL_USER:-mangdehenzhi}"
  DB_PASS="${MYSQL_PASSWORD}"
else
  echo "[$(date)] ERROR: 未提供 MYSQL_ROOT_PASSWORD / MYSQL_PASSWORD，无法备份" >&2
  exit 1
fi

TS="$(date +%Y%m%d-%H%M%S)"
OUT="$BACKUP_DIR/${DB}-${TS}.sql.gz"
mkdir -p "$BACKUP_DIR"

echo "[$(date)] 开始备份 ${DB}@${HOST} -> $OUT"
MYSQL_PWD="$DB_PASS" mysqldump \
  -h "$HOST" -u "$DB_USER" \
  --single-transaction \
  --routines --events --triggers \
  --flush-logs --hex-blob \
  "$DB" | gzip -9 > "$OUT"
echo "[$(date)] 备份完成: $(ls -lh "$OUT" | awk '{print $5}')"

# 归档 binlog（PITR 物料，best-effort）
if [ -d "$BINLOG_DIR" ]; then
  mkdir -p "$BACKUP_DIR/binlogs"
  cp -u "$BINLOG_DIR"/mysql-bin.* "$BACKUP_DIR/binlogs/" 2>/dev/null || true
  echo "[$(date)] binlog 归档: $(ls -1 "$BACKUP_DIR/binlogs" 2>/dev/null | wc -l) 个文件"
fi

# 本地保留策略
find "$BACKUP_DIR" -maxdepth 1 -name "${DB}-*.sql.gz" -mtime +"$RETENTION_DAYS" -delete
echo "[$(date)] 保留最近 ${RETENTION_DAYS} 天"

# 异地增强提示（对象存储/远端挂载请挂载到 /backups 或自行 rsync）
echo "[$(date)] 提示: 本地备份仅防单盘故障，建议将 /backups 同步至对象存储 (COS/S3) 或异地主机以实现容灾"
