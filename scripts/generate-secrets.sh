#!/usr/bin/env bash
# ============================================================
# generate-secrets.sh — 生成强随机密钥与口令 (Linux / macOS / CI)
# 用途：为 .env 生成生产级强随机值，避免提交弱口令/固定密钥。
# 用法：bash scripts/generate-secrets.sh
# ============================================================
set -euo pipefail

ENV_FILE="${1:-.env}"

gen() { openssl rand -base64 "$1" | tr -d '\n'; }

JWT=$(gen 48)      # >= 32 字节（JWT HS256 建议）
ROOT_PW=$(gen 24)
APP_PW=$(gen 24)

BLOCK=$(cat <<EOF
# ---- 自动生成的强随机密钥 ----
# 请勿将真实 .env 提交到仓库
MYSQL_ROOT_PASSWORD=$ROOT_PW
MYSQL_PASSWORD=$APP_PW
JWT_SECRET=$JWT
EOF
)

if [ -f "$ENV_FILE" ]; then
  echo "⚠️  $ENV_FILE 已存在，未覆盖。请将以下值手动合并进 $ENV_FILE ：" >&2
  echo "$BLOCK"
else
  [ -f .env.example ] && cp .env.example "$ENV_FILE"
  printf '\n%s\n' "$BLOCK" >> "$ENV_FILE"
  echo "已生成 $ENV_FILE 。请补充 DEEPSEEK_API_KEY 与 CORS_ALLOWED_ORIGINS。"
fi

echo ""
echo "【安全提示】以上为强随机值，请妥善保管；生产/演示环境切勿将 .env 提交到仓库。"
echo "启动前请执行：docker compose up -d --build"
