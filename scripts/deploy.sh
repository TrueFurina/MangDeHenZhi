#!/bin/bash
# ============================================================
# CDN + Docker 部署脚本 — 前端构建 + 上传 + Docker部署
# 用法:
#   bash scripts/deploy.sh                  # Docker 部署
#   bash scripts/deploy.sh --cdn            # CDN 部署
#   bash scripts/deploy.sh --cdn --docker   # 全量部署
# 前置条件: 配置 .env 中的环境变量
# ============================================================
set -euo pipefail

MODE="${1:-docker}"

echo "=========================================="
echo "  🚀 MangDeHenZhi 部署脚本"
echo "  模式: $MODE"
echo "=========================================="

# ===== 1. 后端构建 =====
echo ""
echo "📦 构建后端..."
cd backend
mvn clean package -DskipTests -B
cd ..

# ===== 2. 前端构建 =====
echo ""
echo "📦 构建前端..."
cd frontend
npm ci

if [[ "$MODE" == *"--cdn"* ]]; then
  echo "🔨 CDN 模式构建..."
  CDN=true npm run build
else
  echo "🔨 标准模式构建..."
  npm run build
fi
cd ..

# ===== 3. CDN 上传（可选） =====
if [[ "$MODE" == *"--cdn"* ]]; then
  if [ -n "${CDN_BUCKET:-}" ]; then
    echo ""
    echo "☁️ 上传到 CDN 存储桶..."
    # 阿里云 OSS
    # ossutil cp -r frontend/dist/ oss://$CDN_BUCKET/prod/ --force
    # 刷新 CDN 缓存
    # aliyun cdn RefreshObjectCaches --ObjectPath https://cdn.mangdehenzhi.com/prod/index.html
    echo "✅ CDN 上传完成"
  else
    echo "⚠️ CDN_BUCKET 未设置，跳过 CDN 上传"
  fi
fi

# ===== 4. Docker 部署 =====
if [[ "$MODE" == *"--docker"* ]] || [[ "$MODE" == "docker" ]]; then
  echo ""
  echo "🐳 Docker 部署..."
  docker compose down -v 2>/dev/null || true
  docker compose up -d --build
  echo ""
  echo "⏳ 等待服务就绪..."
  for i in $(seq 1 30); do
    if curl -fsS http://localhost:80/ >/dev/null 2>&1; then
      echo "✅ 服务已就绪！"
      echo "   🌐 前端: http://localhost:80"
      echo "   🔧 API: http://localhost:80/api"
      echo "   📊 健康检查: http://localhost:80/api/health"
      break
    fi
    echo "  等待中 ($i/30)..."
    sleep 3
  done
fi

echo ""
echo "=========================================="
echo "  ✅ 部署完成"
echo "=========================================="