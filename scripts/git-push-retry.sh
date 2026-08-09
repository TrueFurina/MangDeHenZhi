#!/bin/bash
# ============================================================
# Git 推送重试脚本 — 抓取网络间歇性连通窗口
# 网络波动时（如 GitHub 直连不稳定），多次重试 pull+push
# 用法: bash scripts/git-push-retry.sh
# 默认最多重试 8 次，间隔 5 秒
# ============================================================
set -uo pipefail

MAX_ATTEMPTS="${1:-8}"
INTERVAL="${2:-5}"

echo "=========================================="
echo "  🔁 Git 推送重试脚本"
echo "  最多尝试: ${MAX_ATTEMPTS} 次，间隔 ${INTERVAL}s"
echo "=========================================="

for ((i = 1; i <= MAX_ATTEMPTS; i++)); do
  echo ""
  echo "--- 第 $i/${MAX_ATTEMPTS} 次尝试 ---"

  # 拉取合并远程（忽略失败，网络波动时可能拉取失败）
  if timeout 40 git pull --rebase origin main >/dev/null 2>&1; then
    echo "  ✅ pull --rebase 成功"
  else
    echo "  ⚠️ pull 失败（网络波动），继续尝试 push"
  fi

  # 尝试推送
  if timeout 40 git push origin main 2>&1; then
    echo ""
    echo "=========================================="
    echo "  ✅ 推送成功！循环闭环完成"
    echo "=========================================="
    exit 0
  fi
  echo "  ❌ push 失败，${INTERVAL}s 后重试..."

  sleep "${INTERVAL}"
done

echo ""
echo "=========================================="
echo "  ⏳ 已达最大尝试次数，推送仍未成功"
echo "  网络恢复后手动执行:"
echo "  git pull --rebase origin main && git push origin main"
echo "=========================================="
exit 1