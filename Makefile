# ============================================================
# 芒德矩阵 (MangDeHenZhi) — 项目快速参考
# ============================================================
#
# 常用命令速查
# ============================================================

## --- 后端 ---
backend-dev:   ## 启动后端 (H2, 端口 8080)
	cd backend && mvn spring-boot:run

backend-build: ## 编译后端
	cd backend && mvn clean package -DskipTests

backend-test:  ## 运行后端测试
	cd backend && mvn test

## --- 前端 ---
frontend-dev:   ## 启动前端 (Vite, 端口 5173)
	cd frontend && npm run dev

frontend-build: ## 构建前端
	cd frontend && npm run build

frontend-test:  ## 运行前端测试
	cd frontend && npm run test

frontend-typecheck: ## 前端类型检查
	cd frontend && npx vue-tsc --noEmit

## --- Docker ---
docker-up:      ## 启动生产环境
	docker compose up -d

docker-down:    ## 停止生产环境
	docker compose down -v

docker-dev:     ## 启动开发环境
	docker compose -f docker-compose.dev.yml up -d

## --- Git ---
git-push:       ## 推送到 main
	git push origin main

git-log:        ## 查看提交历史
	git log --oneline -10

## --- 工具 ---
help:           ## 显示帮助
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | \
		awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-20s\033[0m %s\n", $$1, $$2}'