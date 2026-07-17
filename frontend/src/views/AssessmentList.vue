<template>
  <div class="page">
    <AppHeader />
    <div class="page-content">
      <div class="page-header">
        <div>
          <h1>技能测评</h1>
          <p class="page-desc">选择测评，全面评估你的能力水平</p>
        </div>
        <div class="header-stats">
          <span>📊 {{ results.length }} 次完成</span>
          <span>✅ {{ passedCount }} 次通过</span>
        </div>
      </div>

      <!-- 可用测评 -->
      <div class="section-label">
        <span class="label-icon">📋</span> 可参加的测评
      </div>
      <div class="assessment-grid">
        <div v-for="item in assessments" :key="item.id" class="glass-card assessment-card"
             @click="router.push(`/assessments/${item.id}`)">
          <div class="card-top">
            <div class="card-icon">{{ getDifficultyIcon(item.difficulty) }}</div>
            <el-tag size="small" :type="getDifficultyType(item.difficulty)">
              {{ difficultyLabels[item.difficulty] || item.difficulty }}
            </el-tag>
          </div>
          <h3>{{ item.title }}</h3>
          <p class="card-desc">{{ item.description }}</p>
          <div class="card-meta">
            <span>⏱ {{ item.duration }} 分钟</span>
            <span>🎯 {{ item.dimensions?.length || 0 }} 个维度</span>
            <span>📝 {{ item.totalScore }} 分</span>
          </div>
          <div class="card-dimensions">
            <span v-for="dim in item.dimensions" :key="dim" class="dim-tag">
              {{ dimLabels[dim] || dim }}
            </span>
          </div>
          <el-button type="primary" class="card-btn">
            开始测评 <el-icon><arrow-right /></el-icon>
          </el-button>
        </div>
        <div v-if="assessments.length === 0" class="empty-state glass-card">
          <el-icon :size="48" color="#a0a0a0"><collection /></el-icon>
          <p>暂无可用测评</p>
        </div>
      </div>

      <!-- 历史记录 -->
      <div class="section-label" style="margin-top:48px">
        <span class="label-icon">📊</span> 历史测评记录
      </div>
      <div class="glass-card history-card">
        <el-table :data="results" style="width:100%" stripe v-if="results.length > 0">
          <el-table-column prop="assessmentTitle" label="测评" min-width="160" />
          <el-table-column prop="score" label="得分" width="80">
            <template #default="{ row }">
              <span :class="row.passed ? 'score-pass' : 'score-fail'">{{ row.score }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="completedAt" label="完成时间" min-width="160">
            <template #default="{ row }">{{ formatDate(row.completedAt) }}</template>
          </el-table-column>
          <el-table-column prop="passed" label="结果" width="80">
            <template #default="{ row }">
              <el-tag :type="row.passed ? 'success' : 'danger'" size="small">
                {{ row.passed ? '✅ 通过' : '❌ 未通过' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120" fixed="right">
            <template #default="{ row }">
              <el-button link type="primary" @click="router.push(`/assessments/results/${row.id}`)">
                查看报告
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <div v-else class="empty-state">
          <p style="color:#666">暂无测评记录，快去完成第一次测评吧！</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { assessmentApi } from '@/api'
import AppHeader from '@/components/AppHeader.vue'
import type { Assessment, AssessmentResult } from '@/types'

const router = useRouter()
const assessments = ref<Assessment[]>([])
const results = ref<AssessmentResult[]>([])
const passedCount = computed(() => results.value.filter(r => r.passed).length)

const dimLabels: Record<string, string> = {
  communication: '沟通能力', collaboration: '协作能力', problem_solving: '问题解决能力',
}
const difficultyLabels: Record<string, string> = {
  BEGINNER: '入门', INTERMEDIATE: '进阶', ADVANCED: '高级', ADAPTIVE: '自适应',
}

function getDifficultyIcon(diff: string): string {
  const icons: Record<string, string> = { BEGINNER: '🌱', INTERMEDIATE: '🔥', ADVANCED: '💎', ADAPTIVE: '🧠' }
  return icons[diff] || '📝'
}
function getDifficultyType(diff: string): 'success' | 'warning' | 'danger' | 'info' {
  const types: Record<string, any> = { BEGINNER: 'success', INTERMEDIATE: 'warning', ADVANCED: 'danger', ADAPTIVE: 'info' }
  return types[diff] || 'info'
}
function formatDate(dateStr: string): string {
  if (!dateStr) return ''
  return dateStr.replace('T', ' ').substring(0, 16)
}

onMounted(async () => {
  try {
    const [resA, resR] = await Promise.all([
      assessmentApi.getAll(),
      assessmentApi.getMyResults(),
    ])
    assessments.value = resA.data || []
    results.value = resR.data || []
  } catch (err) {
    console.error('加载测评数据失败', err)
  }
})
</script>

<style scoped>
.page-content { padding: 32px 48px; max-width: 1200px; margin: 0 auto; }

.page-header { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 32px; }
.page-header h1 { font-size: 28px; }
.page-desc { color: #a0a0a0; margin-top: 4px; }
.header-stats { display: flex; gap: 16px; font-size: 14px; color: #a0a0a0; }

.section-label { font-size: 18px; font-weight: 600; margin-bottom: 20px; display: flex; align-items: center; gap: 8px; }
.label-icon { font-size: 20px; }

.assessment-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 24px; }
.assessment-card { padding: 24px; cursor: pointer; transition: all 0.3s; display: flex; flex-direction: column; }
.assessment-card:hover { transform: translateY(-4px); box-shadow: 0 12px 30px rgba(64,158,255,0.15); }
.card-top { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 12px; }
.card-icon { font-size: 32px; }
.assessment-card h3 { font-size: 18px; margin-bottom: 8px; }
.card-desc { color: #a0a0a0; font-size: 13px; line-height: 1.5; margin-bottom: 16px; flex: 1; }
.card-meta { display: flex; gap: 16px; font-size: 12px; color: #666; margin-bottom: 12px; }
.card-dimensions { display: flex; flex-wrap: wrap; gap: 6px; margin-bottom: 16px; }
.dim-tag { padding: 3px 10px; border-radius: 12px; font-size: 11px; background: rgba(64,158,255,0.08); color: #409eff; }
.card-btn { width: 100%; }

.score-pass { color: #67c23a; font-weight: 600; }
.score-fail { color: #f56c6c; font-weight: 600; }

.empty-state { text-align: center; padding: 64px; grid-column: 1 / -1; }
.empty-state p { margin: 16px 0; color: #a0a0a0; }

.history-card { padding: 0; overflow: hidden; }

@media (max-width: 900px) {
  .assessment-grid { grid-template-columns: 1fr; }
  .page-header { flex-direction: column; gap: 12px; }
}
</style>