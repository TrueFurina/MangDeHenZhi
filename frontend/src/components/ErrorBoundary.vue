<template>
  <div v-if="hasError" class="error-boundary">
    <div class="error-content">
      <div class="error-icon">⚠️</div>
      <h2>页面出现异常</h2>
      <p>{{ errorMessage }}</p>
      <div class="error-actions">
        <el-button type="primary" @click="reload">重新加载</el-button>
        <el-button @click="goHome">返回首页</el-button>
      </div>
    </div>
  </div>
  <slot v-else />
</template>

<script setup lang="ts">
import { ref, onErrorCaptured } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const hasError = ref(false)
const errorMessage = ref('页面渲染异常，请尝试重新加载')

onErrorCaptured((err: Error) => {
  hasError.value = true
  errorMessage.value = err.message || '页面渲染异常'
  console.error('[ErrorBoundary]', err)
  return false
})

function reload() {
  hasError.value = false
  window.location.reload()
}

function goHome() {
  hasError.value = false
  router.push('/')
}
</script>

<style scoped>
.error-boundary {
  display: flex; align-items: center; justify-content: center;
  min-height: 60vh;
}
.error-content { text-align: center; padding: 48px; }
.error-icon { font-size: 64px; margin-bottom: 16px; }
h2 { margin-bottom: 8px; }
p { color: #a0a0a0; margin-bottom: 24px; }
.error-actions { display: flex; gap: 12px; justify-content: center; }
</style>