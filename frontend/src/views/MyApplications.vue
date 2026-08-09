<template>
  <div class="page">
    <AppHeader />
    <div class="page-content">
      <div class="page-header">
        <div>
          <h1>📋 我的网申</h1>
          <p class="page-desc">管理你的校招网申进度，AI 辅助填写</p>
        </div>
        <div class="header-actions">
          <el-button type="primary" @click="router.push('/recruitment')">
            <el-icon><search /></el-icon> 浏览职位
          </el-button>
        </div>
      </div>

      <!-- 求职仪表盘统计 -->
      <div class="stats-row">
        <div class="stat-item">
          <span class="stat-num">{{ appStats.total || applications.length }}</span>
          <span class="stat-lbl">总投递</span>
        </div>
        <div class="stat-item">
          <span class="stat-num" style="color:#409eff">{{ appStats.inProgress || submittedCount }}</span>
          <span class="stat-lbl">进行中</span>
        </div>
        <div class="stat-item">
          <span class="stat-num" style="color:#67c23a">{{ appStats.interviewed || 0 }}</span>
          <span class="stat-lbl">获面试</span>
        </div>
        <div class="stat-item">
          <span class="stat-num" style="color:#e6a23c">{{ appStats.accepted || 0 }}</span>
          <span class="stat-lbl">已录用</span>
        </div>
        <div class="stat-item">
          <span class="stat-num" style="color:#f56c6c">{{ appStats.rejected || 0 }}</span>
          <span class="stat-lbl">未通过</span>
        </div>
      </div>

      <!-- 状态筛选 -->
      <div class="filter-tabs">
        <el-button :type="filter === 'ALL' ? 'primary' : 'default'" size="small" @click="filter = 'ALL'">全部</el-button>
        <el-button :type="filter === 'DRAFT' ? 'primary' : 'default'" size="small" @click="filter = 'DRAFT'">待填写</el-button>
        <el-button :type="filter === 'SUBMITTED' ? 'primary' : 'default'" size="small" @click="filter = 'SUBMITTED'">已投递</el-button>
        <el-button :type="filter === 'ACCEPTED' ? 'primary' : 'default'" size="small" @click="filter = 'ACCEPTED'">已录取</el-button>
        <el-button :type="filter === 'REJECTED' ? 'primary' : 'default'" size="small" @click="filter = 'REJECTED'">未通过</el-button>
      </div>

      <!-- 列表 -->
      <div v-if="filteredApps.length > 0" class="app-list">
        <div v-for="app in filteredApps" :key="app.id" class="glass-card app-card"
          @click="router.push(`/applications/${app.id}/edit`)">
          <div class="app-left">
            <div class="app-icon">{{ getIcon(app.companyName) }}</div>
            <div class="app-info">
              <h3>{{ app.companyName }}</h3>
              <span class="app-position">{{ app.positionName }}</span>
              <span class="app-date">{{ formatDate(app.createdAt) }}</span>
            </div>
          </div>
          <div class="app-right">
            <el-tag :type="app.status === 'SUBMITTED' ? 'success' : app.status === 'DRAFT' ? 'info' : 'danger'">
              {{ statusMap[app.status] || app.status }}
            </el-tag>
            <el-icon color="#a0a0a0"><arrow-right /></el-icon>
          </div>
        </div>
      </div>

      <div v-else class="empty-state glass-card">
        <el-icon :size="48" color="#a0a0a0"><document /></el-icon>
        <p v-if="filter === 'ALL'">还没有网申记录，去浏览校招职位吧</p>
        <p v-else>没有符合条件的记录</p>
        <el-button type="primary" @click="router.push('/recruitment')">浏览职位</el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { recruitmentApi } from '@/api'
import AppHeader from '@/components/AppHeader.vue'
import type { Application } from '@/types'

const router = useRouter()
const applications = ref<Application[]>([])
const filter = ref('ALL')
const appStats = ref<any>({})

const statusMap: Record<string, string> = {
  DRAFT: '待填写', SUBMITTED: '已投递', REJECTED: '未通过', ACCEPTED: '已录取',
}

const submittedCount = computed(() => applications.value.filter(a => a.status === 'SUBMITTED' || a.status === 'ACCEPTED').length)
const draftCount = computed(() => applications.value.filter(a => a.status === 'DRAFT').length)
const filteredApps = computed(() => {
  if (filter.value === 'ALL') return applications.value
  return applications.value.filter(a => a.status === filter.value)
})

function getIcon(name: string): string {
  const icons = ['🏢', '💼', '🏦', '🏭', '📚', '🏥', '🛒', '📊']
  let hash = 0
  for (const c of name) hash = (hash * 31 + c.charCodeAt(0)) & 0x7fffffff
  return icons[hash % icons.length]
}

function formatDate(dateStr: string): string {
  if (!dateStr) return ''
  return dateStr.replace('T', ' ').substring(0, 16)
}

onMounted(async () => {
  try {
    const [listRes, statsRes] = await Promise.all([
      recruitmentApi.getMyApplications(),
      recruitmentApi.getApplicationStats(),
    ])
    applications.value = listRes.data || []
    appStats.value = statsRes.data || {}
  } catch (err) {
    console.error('加载网申列表失败', err)
  }
})
</script>

<style scoped>
.page-content { padding: 32px 48px; max-width: 900px; margin: 0 auto; }
.page-header { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 24px; }
.page-header h1 { font-size: 28px; }
.page-desc { color: #a0a0a0; margin-top: 4px; }

.stats-row { display: grid; grid-template-columns: repeat(3, 1fr); gap: 16px; margin-bottom: 24px; }
.stat-item { text-align: center; padding: 20px; background: rgba(255,255,255,0.03); border-radius: 12px; }
.stat-num { display: block; font-size: 32px; font-weight: 700; }
.stat-lbl { font-size: 13px; color: #a0a0a0; margin-top: 4px; }

.filter-tabs { display: flex; gap: 8px; margin-bottom: 20px; }

.app-list { display: flex; flex-direction: column; gap: 12px; }
.app-card { display: flex; justify-content: space-between; align-items: center; padding: 20px 24px; cursor: pointer; transition: all 0.2s; }
.app-card:hover { background: rgba(255,255,255,0.04); }
.app-left { display: flex; align-items: center; gap: 16px; }
.app-icon { font-size: 32px; }
.app-info h3 { font-size: 16px; margin-bottom: 4px; }
.app-position { display: block; font-size: 13px; color: #a0a0a0; }
.app-date { display: block; font-size: 12px; color: #666; margin-top: 2px; }
.app-right { display: flex; align-items: center; gap: 12px; }

.empty-state { text-align: center; padding: 64px; }
.empty-state p { margin: 16px 0; color: #a0a0a0; }
</style>