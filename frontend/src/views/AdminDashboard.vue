<template>
  <div class="admin-page">
    <AppHeader />
    <div class="admin-content">
      <h1>⚙️ 管理后台</h1>

      <!-- 统计卡片 -->
      <div class="stats-grid">
        <div class="glass-card stat-card">
          <el-icon :size="32" color="#409eff"><user /></el-icon>
          <div class="stat-info">
            <span class="stat-number">{{ stats.userCount }}</span>
            <span class="stat-label">用户总数</span>
          </div>
        </div>
        <div class="glass-card stat-card">
          <el-icon :size="32" color="#67c23a"><reading /></el-icon>
          <div class="stat-info">
            <span class="stat-number">{{ stats.courseCount }}</span>
            <span class="stat-label">课程总数</span>
          </div>
        </div>
      </div>

      <!-- 图表区域 -->
      <div class="charts-grid">
        <div class="glass-card chart-card">
          <h3>📊 数据概览</h3>
          <v-chart :option="barOption" style="height: 300px" autoresize />
        </div>
        <div class="glass-card chart-card">
          <h3>🎯 课程分类分布</h3>
          <v-chart :option="pieOption" style="height: 300px" autoresize />
        </div>
      </div>

      <!-- 管理标签页 -->
      <el-tabs v-model="activeTab" style="margin-top: 24px;">
        <el-tab-pane label="用户管理" name="users">
          <el-table :data="users" stripe style="width: 100%">
            <el-table-column prop="id" label="ID" width="60" />
            <el-table-column prop="username" label="用户名" min-width="120" />
            <el-table-column prop="email" label="邮箱" min-width="180" />
            <el-table-column prop="role" label="角色" width="100">
              <template #default="{ row }">
                <el-tag :type="row.role === 'ADMIN' ? 'danger' : row.role === 'TEACHER' ? 'warning' : 'info'">
                  {{ row.role }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="enabled" label="状态" width="80">
              <template #default="{ row }">
                <el-tag :type="row.enabled ? 'success' : 'danger'">{{ row.enabled ? '正常' : '禁用' }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="课程管理" name="courses">
          <el-button type="primary" style="margin-bottom: 16px;">+ 新增课程</el-button>
          <el-table :data="courses" stripe style="width: 100%">
            <el-table-column prop="id" label="ID" width="60" />
            <el-table-column prop="title" label="课程名称" min-width="200" />
            <el-table-column prop="category" label="分类" width="100" />
            <el-table-column prop="duration" label="时长(分)" width="80" />
            <el-table-column prop="published" label="状态" width="80">
              <template #default="{ row }">
                <el-tag :type="row.published ? 'success' : 'info'">{{ row.published ? '已发布' : '草稿' }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="120">
              <template #default="{ row }">
                <el-button link type="primary" @click="router.push(`/courses/${row.id}`)">查看</el-button>
                <el-button link type="danger">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import { BarChart, PieChart } from 'echarts/charts'
import { TooltipComponent, GridComponent } from 'echarts/components'
import { CanvasRenderer } from 'echarts/renderers'
import { adminApi } from '@/api'
import { useUserStore } from '@/stores/user'
import AppHeader from '@/components/AppHeader.vue'
import type { User, Course } from '@/types'

use([BarChart, PieChart, TooltipComponent, GridComponent, CanvasRenderer])

const router = useRouter()
const userStore = useUserStore()

const activeTab = ref('users')
const users = ref<User[]>([])
const courses = ref<Course[]>([])
const stats = reactive({ userCount: 0, courseCount: 0 })

const barOption = computed(() => ({
  tooltip: { trigger: 'axis' },
  grid: { left: 60, right: 20, bottom: 30, top: 20 },
  xAxis: { type: 'category', data: ['用户', '课程'], axisLabel: { color: '#a0a0a0' } },
  yAxis: { type: 'value', axisLabel: { color: '#a0a0a0' } },
  series: [{
    type: 'bar',
    data: [
      { value: stats.userCount, itemStyle: { color: '#409eff' } },
      { value: stats.courseCount, itemStyle: { color: '#67c23a' } },
    ],
    barWidth: 60,
  }],
}))

const pieOption = computed(() => {
  const catCount: Record<string, number> = {}
  courses.value.forEach(c => {
    catCount[c.category] = (catCount[c.category] || 0) + 1
  })
  const colors = ['#409eff', '#67c23a', '#e6a23c', '#f56c6c', '#909399']
  return {
    tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
    series: [{
      type: 'pie',
      radius: ['40%', '70%'],
      center: ['50%', '50%'],
      data: Object.entries(catCount).map(([name, value], i) => ({
        name, value, itemStyle: { color: colors[i % colors.length] },
      })),
      label: { color: '#a0a0a0' },
      emphasis: { label: { fontSize: 16, fontWeight: 'bold' } },
    }],
  }
})

onMounted(async () => {
  if (userStore.currentUser?.role !== 'ADMIN') {
    ElMessage.error('需要管理员权限')
    router.push('/dashboard')
    return
  }
  try {
    const [userRes, courseRes, statsRes] = await Promise.all([
      adminApi.getUsers(),
      adminApi.getCourses(0, 50),
      adminApi.getStats(),
    ])
    users.value = userRes.data || []
    courses.value = courseRes.data?.content || []
    stats.userCount = statsRes.data?.userCount || 0
    stats.courseCount = statsRes.data?.courseCount || 0
  } catch (_) {
    ElMessage.error('加载管理数据失败')
  }
})
</script>

<style scoped>
.admin-content { padding: 32px 48px; max-width: 1200px; margin: 0 auto; }
.stats-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 24px; margin: 24px 0; }
.stat-card { display: flex; align-items: center; gap: 16px; }
.stat-info { display: flex; flex-direction: column; }
.stat-number { font-size: 28px; font-weight: 700; }
.stat-label { color: #a0a0a0; font-size: 14px; }
.charts-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 24px; margin-bottom: 24px; }
.chart-card { padding: 20px; }
.chart-card h3 { margin-bottom: 12px; font-size: 16px; }
</style>