# ============================================================
# 芒得很职 开发命令 (Makefile)
# 使用: make <target>
# ============================================================

.PHONY: help backend-dev backend-build frontend-dev frontend-build docker-up docker-down docker-dev test test-backend test-frontend clean

help: ## 显示帮助
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | \
		awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-20s\033[0m %s\n", $$1, $$2}'

# ---- 后端 ----

backend-dev: ## 启动后端开发模式 (H2 内存数据库，端口 8080)
	cd backend && ./mvnw.cmd spring-boot:run

backend-build: ## 编译后端
	cd backend && ./mvnw.cmd clean package -DskipTests

backend-test: ## 运行后端测试
	cd backend && ./mvnw.cmd test

# ---- 前端 ----

frontend-dev: ## 启动前端开发模式 (Vite Dev Server，端口 5173)
	cd frontend && npm run dev

frontend-build: ## 构建前端
	cd frontend && npm run build

frontend-test: ## 运行前端测试
	cd frontend && npm run test

frontend-lint: ## 前端类型检查
	cd frontend && npx vue-tsc --noEmit

# ---- Docker ----

docker-up: ## 启动生产环境 (Docker Compose)
	docker-compose up -d --build

docker-down: ## 停止生产环境
	docker-compose down

docker-dev: ## 启动开发环境 (Docker Compose + 热重载)
	docker-compose -f docker-compose.dev.yml up -d --build

docker-dev-down: ## 停止开发环境
	docker-compose -f docker-compose.dev.yml down

docker-logs: ## 查看日志
	docker-compose logs -f

# ---- 测试 ----

test: test-backend test-frontend ## 运行全部测试

# ---- 清理 ----

clean: ## 清理构建产物
	cd backend && ./mvnw.cmd clean
	cd frontend && rm -rf dist
	rm -rf backend/target frontend/dist

# ---- 初始化 ----

init: ## 初始化项目 (安装依赖)
	cd backend && ./mvnw.cmd dependency:go-offline
	cd frontend && npm install