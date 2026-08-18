<template>
  <div class="page">
    <AppHeader />
    <div class="page-content">
      <div class="page-header">
        <h1>📊 学习分析</h1>
        <p class="page-desc">追踪你的学习效果、技能成长与学习行为数据</p>
      </div>

      <div v-if="loading" class="loading-grid">
        <SkeletonCard v-for="i in 4" :key="i" variant="card" />
      </div>

      <template v-else-if="analytics">
        <!-- 综合评分 -->
        <div class="stats-grid">
          <div class="glass-card stat-card">
            <div class="stat-icon" style="background:rgba(64,158,255,0.1)">
              <el-icon :size="24" color="#409eff"><data-analysis /></el-icon>
            </div>
            <div class="stat-info">
              <span class="stat-number">{{ Math.round(analytics.overallScore) }}</span>
              <span class="stat-label">综合评分</span>
            </div>
          </div>
          <div class="glass-card stat-card">
            <div class="stat-icon" style="background:rgba(103,194,58,0.1)">
              <el-icon :size="24" color="#67c23a"><document /></el-icon>
            </div>
            <div class="stat-info">
              <span class="stat-number">{{ analytics.totalAssessments }}</span>
              <span class="stat-label">完成测评</span>
            </div>
          </div>
          <div class="glass-card stat-card">
            <div class="stat-icon" style="background:rgba(230,162,60,0.1)">
              <el-icon :size="24" color="#e6a23c"><reading /></el-icon>
            </div>
            <div class="stat-info">
              <span class="stat-number">{{ analytics.behavior.completedLessons }}</span>
              <span class="stat-label">完成课时</span>
            </div>
          </div>
          <div class="glass-card stat-card">
            <div class="stat-icon" style="background:rgba(245,108,108,0.1)">
              <el-icon :size="24" color="#f56c6c"><trend-chart /></el-icon>
            </div>
            <div class="stat-info">
              <span class="stat-number">{{ analytics.behavior.recentActivity }}</span>
              <span class="stat-label">近7天活动</span>
            </div>
          </div>
        </div>

        <!-- 技能成长趋势 -->
        <div class="glass-card section-card" v-if="analytics.skillTrends.length > 0">
          <h3>📈 技能成长趋势</h3>
          <div class="trend-list">
            <div v-for="trend in analytics.skillTrends" :key="trend.dimension" class="trend-item">
              <div class="trend-header">
                <span class="trend-dim">{{ dimLabels[trend.dimension] || trend.dimension }}</span>
                <span class="trend-change" :class="trend.change >= 0 ? 'up' : 'down'">
                  {{ trend.change >= 0 ? '+' : '' }}{{ trend.change }}
                </span>
              </div>
              <div class="trend-bar-container">
                <div class="trend-bar">
                  <div class="trend-fill start" :style="{ width: (trend.firstScore / 100) * 100 + '%' }" />
                </div>
                <div class="trend-arrow">→</div>
                <div class="trend-bar">
                  <div class="trend-fill end" :style="{ width: (trend.lastScore / 100) * 100 + '%' }" />
                </div>
              </div>
              <div class="trend-values">
                <span>首次: {{ trend.firstScore }}</span>
                <span>当前: {{ trend.lastScore }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 薄弱环节 -->
        <div class="glass-card section-card" v-if="analytics.weakAreas.length > 0">
          <h3>⚠️ 待提升维度</h3>
          <div class="weak-list">
            <div v-for="area in analytics.weakAreas" :key="area.dimension" class="weak-item">
              <span class="weak-dim">{{ dimLabels[area.dimension] || area.dimension }}</span>
              <el-progress :percentage="area.score" :color="area.severity === 'critical' ? '#f56c6c' : '#e6a23c'" />
            </div>
          </div>
        </div>

        <!-- 学习建议 -->
        <div class="glass-card section-card" v-if="analytics.recommendations.length > 0">
          <h3>💡 学习建议</h3>
          <div class="rec-list">
            <div v-for="(rec, i) in analytics.recommendations" :key="i" class="rec-item">
              {{ rec }}
            </div>
          </div>
        </div>
      </template>

      <div v-else class="empty-state glass-card">
        <el-icon :size="48" color="#a0a0a0"><data-board /></el-icon>
        <p>暂无学习数据，完成测评后即可查看分析</p>
        <el-button type="primary" @click="router.push('/diagnostic')">去诊断</el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import http from '@/api'
import AppHeader from '@/components/AppHeader.vue'
import SkeletonCard from '@/components/SkeletonCard.vue'

const router = useRouter()
const analytics = ref<any>(null)
const loading = ref(true)

const dimLabels: Record<string, string> = {
  communication: '沟通能力',
  collaboration: '团队协作',
  problem_solving: '问题解决能力',
  computer_basics: '计算机基础',
  data_analysis: '数据分析',
  team_collaboration: '团队协作',
}

onMounted(async () => {
  try {
    const res = await http.get('/analytics/dashboard')
    analytics.value = res.data
  } catch (err) {
    console.error('加载分析数据失败', err)
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.page-content { padding: 32px 48px; max-width: 1000px; margin: 0 auto; }
.page-header { margin-bottom: 24px; }
.page-header h1 { font-size: 28px; }
.page-desc { color: #a0a0a0; margin-top: 4px; }
.loading-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 16px; }

.stats-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 16px; margin-bottom: 24px; }
.stat-card { display: flex; align-items: center; gap: 16px; padding: 24px; }
.stat-icon { width: 48px; height: 48px; border-radius: 12px; display: flex; align-items: center; justify-content: center; }
.stat-info { display: flex; flex-direction: column; }
.stat-number { font-size: 28px; font-weight: 700; }
.stat-label { font-size: 13px; color: #a0a0a0; }

.section-card { padding: 32px; margin-bottom: 20px; }
.section-card h3 { margin-bottom: 20px; }

.trend-list { display: flex; flex-direction: column; gap: 16px; }
.trend-item { padding: 12px; background: rgba(255,255,255,0.03); border-radius: 8px; }
.trend-header { display: flex; justify-content: space-between; margin-bottom: 8px; }
.trend-dim { font-size: 14px; font-weight: 500; }
.trend-change { font-size: 14px; font-weight: 700; }
.trend-change.up { color: #67c23a; }
.trend-change.down { color: #f56c6c; }
.trend-bar-container { display: flex; align-items: center; gap: 8px; }
.trend-bar { flex: 1; height: 8px; background: rgba(255,255,255,0.06); border-radius: 4px; overflow: hidden; }
.trend-fill { height: 100%; border-radius: 4px; }
.trend-fill.start { background: #409eff; }
.trend-fill.end { background: #67c23a; }
.trend-arrow { color: #a0a0a0; font-size: 12px; }
.trend-values { display: flex; justify-content: space-between; font-size: 11px; color: #666; margin-top: 4px; }

.weak-list { display: flex; flex-direction: column; gap: 12px; }
.weak-item { }
.weak-dim { display: block; font-size: 14px; margin-bottom: 6px; }

.rec-list { display: flex; flex-direction: column; gap: 8px; }
.rec-item { padding: 12px 16px; background: rgba(64,158,255,0.06); border-radius: 8px; border-left: 3px solid #409eff; font-size: 14px; color: #a0a0a0; }

.empty-state { text-align: center; padding: 64px; }
.empty-state p { margin: 16px 0; color: #a0a0a0; }
</style>