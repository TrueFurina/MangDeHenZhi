#!/bin/sh
# ============================================================
# 芒得很职 (Mangdehenzhi) — MySQL 恢复脚本
# 用法:
#   全量恢复:   sh restore.sh <dump文件.sql.gz>
#   全量+PITR:  sh restore.sh <dump文件.sql.gz> <截止时间 "YYYY-MM-DD HH:MM:SS">
# 说明:
#   - 全量恢复把最近的 .sql.gz 回填到 db 实例
#   - PITR 模式下，以 dump 时刻为起点，用 /backups/binlogs 的 mysql-bin.*
#     重放到指定时间点（需 backup 服务归档的 binlog 存在）
# 运行环境: 复用 backup 服务的镜像与挂载（mysql_backups + mysql_binlogs）
#   例: docker compose run --rm -e MYSQL_ROOT_PASSWORD=*** backup sh /restore.sh /backups/<file>.sql.gz "2026-07-17 12:00:00"
# ============================================================
set -eu

DB="${MYSQL_DATABASE:-mangdehenzhi}"
HOST="${MYSQL_HOST:-db}"
if [ -z "${MYSQL_ROOT_PASSWORD:-}" ]; then
  echo "需要 MYSQL_ROOT_PASSWORD 环境变量" >&2
  exit 1
fi

DUMP="${1:-}"
if [ -z "$DUMP" ] || [ ! -f "$DUMP" ]; then
  echo "用法: sh restore.sh <dump.sql.gz> [stop_datetime]" >&2
  exit 1
fi

echo "[$(date)] 恢复全量: $DUMP"
MYSQL_PWD="$MYSQL_ROOT_PASSWORD" gunzip -c "$DUMP" | mysql -h "$HOST" -u root "$DB"

if [ -n "${2:-}" ]; then
  STOP="$2"
  echo "[$(date)] PITR 重放 binlog 至 $STOP"
  MYSQL_PWD="$MYSQL_ROOT_PASSWORD" mysqlbinlog --stop-datetime="$STOP" /backups/binlogs/mysql-bin.* \
    | mysql -h "$HOST" -u root "$DB"
fi

echo "[$(date)] 恢复完成"
