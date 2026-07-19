<template>
  <div class="error-page">
    <div class="error-content">
      <div class="error-icon">🌐</div>
      <h1>网络连接异常</h1>
      <p>请检查网络连接后重试</p>
      <div class="error-actions">
        <el-button type="primary" @click="refresh">刷新页面</el-button>
        <el-button @click="router.push('/')">返回首页</el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
const router = useRouter()
function refresh() { window.location.reload() }
</script>

<style scoped>
.error-page {
  display: flex; align-items: center; justify-content: center;
  min-height: 100vh; background: var(--bg-dark);
}
.error-content { text-align: center; }
.error-icon { font-size: 80px; margin-bottom: 24px; }
h1 { font-size: 28px; margin-bottom: 12px; }
p { color: #a0a0a0; margin-bottom: 32px; font-size: 16px; }
.error-actions { display: flex; gap: 16px; justify-content: center; }
</style>