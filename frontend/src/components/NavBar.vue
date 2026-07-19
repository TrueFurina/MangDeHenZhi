<template>
  <header class="app-header" :class="{ 'home-header': variant === 'home' }">
    <div class="header-inner">
      <router-link to="/" class="logo">
        <span class="logo-icon">◆</span>
        <span class="logo-text">芒得很职</span>
      </router-link>

      <nav class="nav">
        <router-link to="/courses">课程中心</router-link>
        <router-link to="/assessments">技能测评</router-link>
        <router-link to="/metaverse">元宇宙</router-link>
        <router-link to="/certifications">证书</router-link>
      </nav>

      <!-- 登录态：用户信息 + 退出 -->
      <div class="user-area" v-if="userStore.isLoggedIn">
        <NotificationBell />
        <router-link to="/dashboard">
          <el-button :text="variant === 'sticky'" :type="variant === 'home' ? 'primary' : undefined" :plain="variant === 'home'">
            <el-icon><home-filled /></el-icon> 控制台
          </el-button>
        </router-link>
        <el-dropdown>
          <el-button :text="variant === 'sticky'" :type="variant === 'home' ? 'primary' : undefined" :plain="variant === 'home'">
            {{ userStore.currentUser?.nickname || userStore.currentUser?.username }}
            <el-icon><arrow-down /></el-icon>
          </el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="router.push('/dashboard')">控制台</el-dropdown-item>
              <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>

      <!-- 未登录：home 页显示注册/登录，其他页仅显示登录 -->
      <div class="user-area" v-else>
        <template v-if="variant === 'home'">
          <router-link to="/login">
            <el-button type="primary" plain size="default">登录</el-button>
          </router-link>
          <router-link to="/register">
            <el-button type="primary" size="default">注册</el-button>
          </router-link>
        </template>
        <router-link v-else to="/login">
          <el-button text>登录</el-button>
        </router-link>
      </div>
    </div>
  </header>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import NotificationBell from '@/components/NotificationBell.vue'

withDefaults(defineProps<{
  variant?: 'sticky' | 'home'
}>(), {
  variant: 'sticky',
})

const router = useRouter()
const userStore = useUserStore()

function handleLogout() {
  userStore.logout()
  ElMessage.success('已退出登录')
  router.push('/')
}
</script>

<style scoped>
.app-header {
  border-bottom: 1px solid rgba(255,255,255,0.05);
  background: rgba(10, 11, 26, 0.9);
  backdrop-filter: blur(10px);
  position: sticky;
  top: 0;
  z-index: 100;
}

.home-header {
  position: static;
  background: transparent;
  border-bottom-color: rgba(255,255,255,0.05);
}

.header-inner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 48px;
  max-width: 1400px;
  margin: 0 auto;
}

.logo {
  display: flex;
  align-items: center;
  gap: 8px;
  text-decoration: none;
}

.logo-icon { font-size: 20px; color: #409eff; }
.logo-text {
  font-size: 18px;
  font-weight: 700;
  background: linear-gradient(135deg, #667eea, #764ba2);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.nav { display: flex; gap: 24px; }
.nav a {
  color: #a0a0a0;
  text-decoration: none;
  transition: color 0.2s;
  font-size: 14px;
}
.nav a:hover,
.nav a.router-link-active { color: #409eff; }

.user-area { display: flex; align-items: center; gap: 8px; }
</style>