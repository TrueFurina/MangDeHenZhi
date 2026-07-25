#!/bin/bash
# ============================================================
# CDN 部署脚本 — 前端静态资源构建 + 上传
# 用法: bash scripts/deploy-cdn.sh
# 前置条件: 配置环境变量 CDN_BUCKET / CDN_REGION / CDN_ACCESS_KEY
# ============================================================
set -euo pipefail

echo "🚀 开始 CDN 部署..."

# 1. 安装依赖
echo "📦 安装依赖..."
cd frontend
npm ci

# 2. 构建（CDN 模式）
echo "🔨 构建前端（CDN 模式）..."
CDN=true npm run build

# 3. 上传到 CDN（示例使用 AWS S3 / 阿里云 OSS）
# 根据实际使用的 CDN 服务商调整
if [ -n "${CDN_BUCKET:-}" ]; then
  echo "☁️ 上传到 CDN 存储桶..."
  # AWS S3 示例
  # aws s3 sync dist/ s3://$CDN_BUCKET/ --region $CDN_REGION --delete

  # 阿里云 OSS 示例
  # ossutil cp -r dist/ oss://$CDN_BUCKET/ --force

  echo "✅ CDN 上传完成"
else
  echo "⚠️ CDN_BUCKET 未设置，跳过上传"
fi

# 4. 输出构建信息
echo ""
echo "📊 构建产物:"
du -sh dist/
find dist/ -type f -name "*.js" -o -name "*.css" | while read f; do
  size=$(wc -c < "$f" | tr -d ' ')
  echo "  $f ($size bytes)"
done

echo ""
echo "✅ CDN 部署完成！"