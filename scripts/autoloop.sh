#!/bin/bash
# ============================================================
# AutoLoop 自动循环编排器 — 可执行的 RalphLoop 框架
#
# 自动执行：验证 → 记录 → 推送，多轮迭代产出功能更新
# 用法:
#   bash scripts/autoloop.sh                  # 跑 1 轮验证循环
#   bash scripts/autoloop.sh 3                # 跑 3 轮验证循环
#   bash scripts/autoloop.sh 3 --push         # 3 轮 + 每轮尝试推送
# ============================================================
set -uo pipefail

ROUNDS="${1:-1}"
PUSH_MODE="${2:-}"

# 定位 Maven（支持环境变量 MVN；默认 Windows Git Bash 完整路径）
MVN="${MVN:-/c/Users/Lenovo/tools/apache-maven-3.9.9/bin/mvn}"
if ! command -v "$MVN" >/dev/null 2>&1 && [ ! -x "$MVN" ]; then
  MVN="mvn"
fi
command -v "$MVN" >/dev/null 2>&1 || [ -x "$MVN" ] || { echo "❌ 未找到 Maven（设置 MVN 环境变量指向 mvn）"; exit 1; }
echo "  使用 Maven: ${MVN}"

LOOP_LOG=".github/loop-log.md"
VERIFY_LOG="docs/verification.md"
TIMESTAMP="$(date '+%Y-%m-%d %H:%M')"

echo "=========================================="
echo "  🔄 AutoLoop 自动循环编排器启动"
echo "  轮数: ${ROUNDS} | 推送模式: ${PUSH_MODE:-off}"
echo "  时间: ${TIMESTAMP}"
echo "=========================================="

# 确保循环日志存在
mkdir -p "$(dirname "$LOOP_LOG")"
[ -f "$LOOP_LOG" ] || echo "# AutoLoop 循环日志" > "$LOOP_LOG"

pass_count=0
fail_count=0

for ((round = 1; round <= ROUNDS; round++)); do
  echo ""
  echo "----- 第 ${round}/${ROUNDS} 轮 -----"

  # ===== Step 1: 后端测试 =====
  echo "▶ 后端测试..."
  if (cd backend && "$MVN" test -q >/tmp/autoloop-backend.log 2>&1); then
    backend_result="✅ 通过"
    echo "  ✅ 后端 BUILD SUCCESS"
  else
    backend_result="❌ 失败"
    echo "  ❌ 后端测试失败（见 /tmp/autoloop-backend.log）"
    fail_count=$((fail_count + 1))
  fi

  # ===== Step 2: 前端类型检查 =====
  echo "▶ 前端类型检查..."
  if (cd frontend && npx vue-tsc --noEmit >/tmp/autoloop-ts.log 2>&1); then
    frontend_result="✅ 通过"
    echo "  ✅ vue-tsc 0 错误"
  else
    frontend_result="❌ 失败"
    echo "  ❌ vue-tsc 有错误"
    fail_count=$((fail_count + 1))
  fi

  # ===== Step 3: 前端构建 =====
  echo "▶ 前端构建..."
  if (cd frontend && npx vite build >/tmp/autoloop-build.log 2>&1); then
    build_result="✅ 通过"
    echo "  ✅ 构建成功"
  else
    build_result="❌ 失败"
    echo "  ❌ 构建失败"
    fail_count=$((fail_count + 1))
  fi

  # ===== Step 4: 记录本轮到循环日志 =====
  overall="✅ 通过"
  [ "$backend_result" = "✅ 通过" ] && [ "$frontend_result" = "✅ 通过" ] && [ "$build_result" = "✅ 通过" ] || overall="❌ 失败"
  [ "$overall" = "✅ 通过" ] && pass_count=$((pass_count + 1))

  echo "| ${TIMESTAMP} | 循环第${round}轮 | ${overall} | 后端:${backend_result} 前端:${frontend_result} 构建:${build_result} | AutoLoop 自动编排 |" >> "$LOOP_LOG"
  echo "  📝 已记录到 ${LOOP_LOG}"

  # ===== Step 5: 推送（可选） =====
  if [ "${PUSH_MODE}" = "--push" ] && [ "$overall" = "✅ 通过" ]; then
    echo "▶ 尝试推送..."
    git add -A
    git commit -m "loop: AutoLoop round ${round} - verified by autoloop.sh" >/dev/null 2>&1 || true
    if timeout 40 git push origin main >/dev/null 2>&1; then
      echo "  ✅ 推送成功"
    else
      echo "  ⚠️ 推送失败（网络），继续下一轮"
    fi
  fi

  # ===== Step 6: 轮间间隔 =====
  [ "$round" -lt "$ROUNDS" ] && sleep 2
done

echo ""
echo "=========================================="
echo "  📊 AutoLoop 循环报告"
echo "  总轮数: ${ROUNDS} | 通过: ${pass_count} | 失败: ${fail_count}"
echo "  循环日志: ${LOOP_LOG}"
echo "=========================================="

# 退出码：全通过返回 0
[ "$fail_count" -eq 0 ] && exit 0 || exit 1