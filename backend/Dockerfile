# ============================================================
# 后端 Dockerfile — 多阶段构建 (Maven + JRE)
# ============================================================
# ---- Stage 1: 编译 ----
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# 先复制 pom.xml 和本地仓库，利用 Docker 缓存加速依赖下载
COPY pom.xml ./
COPY .mvn .mvn
RUN mvn dependency:go-offline -B || true

# 复制源码并编译打包
COPY src ./src
RUN mvn clean package -DskipTests -B

# ---- Stage 2: 运行 ----
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# 时区
RUN apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone

# 非 root 用户运行
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# 通过环境变量注入配置
ENV SPRING_PROFILES_ACTIVE=prod
ENV JWT_SECRET=""
ENV DEEPSEEK_API_KEY=""
ENV MYSQL_HOST=db
ENV MYSQL_USER=mangdehenzhi
ENV MYSQL_PASSWORD=""

# 容器感知型 JVM 参数：堆随 cgroup 内存上限浮动，OOM 时直接退出（而非挂死）
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-XX:InitialRAMPercentage=50", "-XX:+UseG1GC", "-XX:+ExitOnOutOfMemoryError", "-jar", "app.jar"]