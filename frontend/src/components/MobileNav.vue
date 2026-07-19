<template>
  <div class="mobile-nav" v-if="visible">
    <router-link to="/" class="nav-item" active-class="active">
      <el-icon><home-filled /></el-icon>
      <span>首页</span>
    </router-link>
    <router-link to="/courses" class="nav-item" active-class="active">
      <el-icon><reading /></el-icon>
      <span>课程</span>
    </router-link>
    <router-link to="/assessments" class="nav-item" active-class="active">
      <el-icon><collection /></el-icon>
      <span>测评</span>
    </router-link>
    <router-link to="/dashboard" class="nav-item" active-class="active">
      <el-icon><user /></el-icon>
      <span>我的</span>
    </router-link>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'

const visible = ref(window.innerWidth < 768)

function onResize() {
  visible.value = window.innerWidth < 768
}

onMounted(() => window.addEventListener('resize', onResize))
onUnmounted(() => window.removeEventListener('resize', onResize))
</script>

<style scoped>
.mobile-nav {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  background: rgba(10, 11, 26, 0.95);
  backdrop-filter: blur(10px);
  border-top: 1px solid rgba(255, 255, 255, 0.05);
  z-index: 1000;
  padding-bottom: env(safe-area-inset-bottom, 0);
}

.nav-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2px;
  padding: 8px 0 6px;
  color: #666;
  text-decoration: none;
  font-size: 11px;
  transition: color 0.2s;
}

.nav-item.active {
  color: #409eff;
}

.nav-item .el-icon {
  font-size: 20px;
}
</style>