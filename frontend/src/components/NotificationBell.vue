<template>
  <el-dropdown trigger="click" @command="handleCommand">
    <el-badge :value="unreadCount" :hidden="unreadCount === 0" class="notif-badge">
      <el-button text size="large">
        <el-icon :size="20"><bell /></el-icon>
      </el-button>
    </el-badge>
    <template #dropdown>
      <el-dropdown-menu class="notif-menu">
        <div class="notif-header">
          <span>消息通知</span>
          <el-button text size="small" @click="markAllRead" v-if="unreadCount > 0">全部已读</el-button>
        </div>
        <div v-if="notifications.length === 0" class="notif-empty">暂无通知</div>
        <el-dropdown-item v-for="n in notifications" :key="n.id" :command="n.id" class="notif-item">
          <div class="notif-content">
            <div class="notif-title">
              <span v-if="!n.read" class="notif-dot" />
              {{ n.title }}
            </div>
            <div class="notif-time">{{ formatTime(n.createdAt) }}</div>
          </div>
        </el-dropdown-item>
        <el-dropdown-item v-if="notifications.length > 0" divided>
          <router-link to="/notifications" style="color:var(--primary-color);text-decoration:none">查看全部</router-link>
        </el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { notificationApi } from '@/api'

const notifications = ref<any[]>([])
const unreadCount = ref(0)
let timer: ReturnType<typeof setInterval> | null = null

async function fetchNotifications() {
  try {
    const [listRes, countRes] = await Promise.all([
      notificationApi.getUnread(),
      notificationApi.getUnreadCount(),
    ])
    notifications.value = listRes.data || []
    unreadCount.value = countRes.data || 0
  } catch (_) {}
}

async function handleCommand(id: number) {
  await notificationApi.markAsRead(id)
  notifications.value = notifications.value.filter(n => n.id !== id)
  unreadCount.value = Math.max(0, unreadCount.value - 1)
}

async function markAllRead() {
  await notificationApi.markAllAsRead()
  notifications.value = []
  unreadCount.value = 0
}

function formatTime(t: string) {
  const d = new Date(t)
  const now = new Date()
  const diff = now.getTime() - d.getTime()
  if (diff < 60000) return '刚刚'
  if (diff < 3600000) return `${Math.floor(diff / 60000)}分钟前`
  if (diff < 86400000) return `${Math.floor(diff / 3600000)}小时前`
  return d.toLocaleDateString('zh-CN')
}

onMounted(() => {
  fetchNotifications()
  timer = setInterval(fetchNotifications, 30000)
})

onUnmounted(() => { if (timer) clearInterval(timer) })
</script>

<style scoped>
.notif-badge { line-height: 1; }
.notif-menu { width: 320px; max-height: 400px; overflow-y: auto; }
.notif-header { display: flex; justify-content: space-between; align-items: center; padding: 8px 16px; border-bottom: 1px solid var(--border-color); }
.notif-empty { text-align: center; padding: 24px; color: #a0a0a0; font-size: 13px; }
.notif-item { white-space: normal; height: auto !important; padding: 8px 16px !important; }
.notif-content { width: 100%; }
.notif-title { display: flex; align-items: center; gap: 6px; font-size: 13px; }
.notif-dot { width: 6px; height: 6px; border-radius: 50%; background: #409eff; flex-shrink: 0; }
.notif-time { font-size: 11px; color: #666; margin-top: 4px; }
</style>