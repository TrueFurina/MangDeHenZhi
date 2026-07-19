<template>
  <div id="app-container">
    <router-view />
    <MobileNav />
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import MobileNav from '@/components/MobileNav.vue'

const userStore = useUserStore()

onMounted(() => {
  userStore.initFromStorage()
})
</script>

<style>
html, body, #app {
  margin: 0;
  padding: 0;
  height: 100%;
  font-family: 'Microsoft YaHei', 'PingFang SC', sans-serif;
}

#app-container {
  height: 100%;
}

/* 移动端内容区域底部留白给导航栏 */
@media (max-width: 768px) {
  #app-container {
    padding-bottom: 56px;
  }
}
</style>