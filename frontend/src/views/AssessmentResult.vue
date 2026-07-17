<template>
  <div class="page">
    <AppHeader />
    <div class="page-content">
      <el-button link @click="router.push('/assessments')" style="margin-bottom:16px">
        <el-icon><arrow-left /></el-icon> 返回测评列表
      </el-button>

      <!-- 加载态 -->
      <div v-if="!result" class="loading-state">
        <SkeletonCard variant="card" />
        <SkeletonCard variant="card" style="margin-top:16px" />
      </div>

      <div v-if="result" class="result-container">
        <!-- ===== 结果头部 ===== -->
        <div class="glass-card result-header" :class="{ passed: result.passed }">
          <div class="score-section">
            <div class="score-circle" :class="{ passed: result.passed }">
              <svg viewBox="0 0 120 120">
                <circle cx="60" cy="60" r="54" fill="none" stroke="#2d2d4a" stroke-width="8" />
                <circle cx="60" cy="60" r="54" fill="none"
                  :stroke="result.passed ? '#67c23a' : '#f56c6c'"
                  stroke-width="8"
                  :stroke-dasharray="339.292"
                  :stroke-dashoffset="calcOffset(result.score, result.dimensionScores ? Object.keys(result.dimensionScores).length * 100 : 300)"
                  stroke-linecap="round" transform="rotate(-90, 60, 60)" />
              </svg>
              <div class="score-text">
                <span class="score-number">{{ result.score }}</span>
                <span class="score-label">总分</span>
              </div>
            </div>
            <div class="score-badge">
              <el-tag :type="result.passed ? 'success' : 'danger'" size="large" effect="dark">
                {{ result.passed ? '✅ 通过' : '❌ 未通过' }}
              </el-tag>
            </div>
          </div>
          <div class="result-info">
            <h2>{{ result.assessmentTitle }}</h2>
            <div class="result-meta">
              <span>📅 {{ formatDate(result.completedAt) }}</span>
              <span>👤 {{ result.username }}</span>
            </div>
          </div>
        </div>

        <!-- ===== 维度雷达图 ===== -->
        <div class="glass-card chart-card">
          <h3>📊 能力维度分析</h3>
          <div class="radar-wrapper">
            <canvas ref="radarCanvas" width="400" height="400"></canvas>
          </div>
          <div class="dimension-bars">
            <div v-for="(score, dim) in result.dimensionScores" :key="dim" class="dimension-bar">
              <div class="dim-header">
                <span class="dim-label">{{ dimLabels[dim] || dim }}</span>
                <span class="dim-score" :class="score >= 80 ? 'high' : score >= 60 ? 'mid' : 'low'">{{ score }}分</span>
              </div>
              <div class="dim-track">
                <div class="dim-fill" :style="{ width: score + '%', background: score >= 80 ? '#67c23a' : score >= 60 ? '#e6a23c' : '#f56c6c' }" />
              </div>
            </div>
          </div>
        </div>

        <!-- ===== AI 分析报告 ===== -->
        <div class="glass-card report-card" v-if="parsedAnalysis">
          <div class="report-header">
            <span class="report-icon">🤖</span>
            <h3>AI 智能分析报告</h3>
          </div>
          <div class="report-body">
            <div class="report-overall">
              <span class="label">总体评价</span>
              <p class="overall-text">{{ parsedAnalysis.overallAssessment }}</p>
            </div>
            <div class="report-grid">
              <div v-if="parsedAnalysis.strengthDimensions && Object.keys(parsedAnalysis.strengthDimensions).length" class="report-section strength">
                <h4>💪 优势维度</h4>
                <div class="tag-list">
                  <span v-for="(s, d) in parsedAnalysis.strengthDimensions" :key="d" class="strength-tag">
                    {{ dimLabels[d] || d }} <em>{{ s }}分</em>
                  </span>
                </div>
              </div>
              <div v-if="parsedAnalysis.weaknessDimensions && Object.keys(parsedAnalysis.weaknessDimensions).length" class="report-section weakness">
                <h4>📈 待提升维度</h4>
                <div class="tag-list">
                  <span v-for="(s, d) in parsedAnalysis.weaknessDimensions" :key="d" class="weakness-tag">
                    {{ dimLabels[d] || d }} <em>{{ s }}分</em>
                  </span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- ===== 学习建议 ===== -->
        <div class="glass-card" v-if="parsedRecommendations">
          <div class="report-header">
            <span class="report-icon">🎯</span>
            <h3>个性化学习建议</h3>
          </div>
          <div class="recommend-content">
            <div v-if="parsedRecommendations.recommendedCourses?.length" class="rec-section">
              <h4>📚 推荐课程</h4>
              <div class="course-chips">
                <span v-for="(course, i) in parsedRecommendations.recommendedCourses" :key="i" class="course-chip">
                  {{ course }}
                </span>
              </div>
            </div>
            <div v-if="parsedRecommendations.learningPath?.length" class="rec-section">
              <h4>🗺️ 学习路径</h4>
              <div class="path-steps">
                <div v-for="(step, i) in parsedRecommendations.learningPath" :key="i" class="path-step">
                  <span class="step-num">{{ i + 1 }}</span>
                  <span>{{ step }}</span>
                </div>
              </div>
            </div>
            <div v-if="parsedRecommendations.estimatedImprovementTime" class="rec-section">
              <h4>⏱ 预计提升时间</h4>
              <p class="estimate-time">{{ parsedRecommendations.estimatedImprovementTime }}</p>
            </div>
          </div>
        </div>

        <!-- ===== 操作按钮 ===== -->
        <div class="action-buttons">
          <el-button type="primary" @click="router.push('/assessments')">
            <el-icon><collection /></el-icon> 继续测评
          </el-button>
          <el-button @click="router.push('/courses')">
            <el-icon><reading /></el-icon> 浏览推荐课程
          </el-button>
          <el-button @click="router.push('/certifications')" v-if="result.passed">
            <el-icon><trophy /></el-icon> 查看证书
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, nextTick } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { assessmentApi } from '@/api'
import AppHeader from '@/components/AppHeader.vue'
import SkeletonCard from '@/components/SkeletonCard.vue'
import type { AssessmentResult } from '@/types'

const route = useRoute()
const router = useRouter()
const result = ref<AssessmentResult | null>(null)
const radarCanvas = ref<HTMLCanvasElement>()

const dimLabels: Record<string, string> = {
  communication: '沟通能力',
  collaboration: '协作能力',
  problem_solving: '问题解决能力',
}

const COLORS = ['#409eff', '#67c23a', '#e6a23c', '#f56c6c', '#909399']

function calcOffset(score: number, maxScore: number): number {
  if (maxScore <= 0) return 339.292
  return 339.292 - (339.292 * Math.min(score, maxScore) / maxScore)
}

function formatDate(dateStr: string): string {
  if (!dateStr) return ''
  return dateStr.replace('T', ' ').substring(0, 16)
}

const parsedAnalysis = computed(() => {
  if (!result.value?.aiAnalysis) return null
  try {
    const raw = typeof result.value.aiAnalysis === 'string'
      ? JSON.parse(result.value.aiAnalysis)
      : result.value.aiAnalysis
    return raw
  } catch { return null }
})

const parsedRecommendations = computed(() => {
  if (!result.value?.recommendations) return null
  try {
    return typeof result.value.recommendations === 'string'
      ? JSON.parse(result.value.recommendations)
      : result.value.recommendations
  } catch { return null }
})

function drawRadar() {
  if (!radarCanvas.value || !result.value?.dimensionScores) return
  const canvas = radarCanvas.value
  const ctx = canvas.getContext('2d')
  if (!ctx) return

  const dims = Object.keys(result.value.dimensionScores)
  const count = dims.length
  if (count === 0) return

  const cx = 200, cy = 200, r = 160
  const angleStep = (Math.PI * 2) / count

  ctx.clearRect(0, 0, 400, 400)

  // 背景网格
  for (let level = 1; level <= 5; level++) {
    const lr = (r * level) / 5
    ctx.beginPath()
    for (let i = 0; i <= count; i++) {
      const angle = -Math.PI / 2 + i * angleStep
      const x = cx + lr * Math.cos(angle)
      const y = cy + lr * Math.sin(angle)
      i === 0 ? ctx.moveTo(x, y) : ctx.lineTo(x, y)
    }
    ctx.strokeStyle = 'rgba(255,255,255,0.06)'
    ctx.lineWidth = 1
    ctx.stroke()

    // 刻度标签
    if (level < 5) {
      const label = level * 20
      ctx.fillStyle = '#666'
      ctx.font = '10px sans-serif'
      ctx.textAlign = 'right'
      ctx.fillText(String(label), cx + 2, cy - lr + 4)
    }
  }

  // 轴线
  for (let i = 0; i < count; i++) {
    const angle = -Math.PI / 2 + i * angleStep
    ctx.beginPath()
    ctx.moveTo(cx, cy)
    ctx.lineTo(cx + r * Math.cos(angle), cy + r * Math.sin(angle))
    ctx.strokeStyle = 'rgba(255,255,255,0.08)'
    ctx.stroke()

    // 维度标签
    const lx = cx + (r + 30) * Math.cos(angle)
    const ly = cy + (r + 30) * Math.sin(angle)
    ctx.fillStyle = '#a0a0a0'
    ctx.font = '13px sans-serif'
    ctx.textAlign = 'center'
    ctx.textBaseline = 'middle'
    ctx.fillText(dimLabels[dims[i]] || dims[i], lx, ly)
  }

  // 数据区域
  ctx.beginPath()
  for (let i = 0; i <= count; i++) {
    const idx = i % count
    const score = result.value.dimensionScores[dims[idx]] || 0
    const lr = (r * score) / 100
    const angle = -Math.PI / 2 + idx * angleStep
    const x = cx + lr * Math.cos(angle)
    const y = cy + lr * Math.sin(angle)
    i === 0 ? ctx.moveTo(x, y) : ctx.lineTo(x, y)
  }
  ctx.closePath()

  const gradient = ctx.createRadialGradient(cx, cy, 0, cx, cy, r)
  gradient.addColorStop(0, 'rgba(64,158,255,0.2)')
  gradient.addColorStop(1, 'rgba(64,158,255,0.05)')
  ctx.fillStyle = gradient
  ctx.fill()

  ctx.strokeStyle = '#409eff'
  ctx.lineWidth = 2
  ctx.stroke()

  // 数据点
  for (let i = 0; i < count; i++) {
    const score = result.value.dimensionScores[dims[i]] || 0
    const lr = (r * score) / 100
    const angle = -Math.PI / 2 + i * angleStep
    const x = cx + lr * Math.cos(angle)
    const y = cy + lr * Math.sin(angle)
    ctx.beginPath()
    ctx.arc(x, y, 4, 0, Math.PI * 2)
    ctx.fillStyle = '#409eff'
    ctx.fill()
  }
}

onMounted(async () => {
  try {
    const res = await assessmentApi.getResultById(Number(route.params.resultId))
    result.value = res.data
    await nextTick()
    drawRadar()
  } catch (err) {
    console.error('加载测评结果失败', err)
    ElMessage.error('加载测评结果失败')
  }
})
</script>

<style scoped>
.page-content { padding: 32px 48px; max-width: 1000px; margin: 0 auto; }
.loading-state { max-width: 800px; margin: 0 auto; }

/* 结果头部 */
.result-header { display: flex; align-items: center; gap: 40px; padding: 40px; }
.score-section { display: flex; flex-direction: column; align-items: center; gap: 16px; flex-shrink: 0; }
.score-circle { position: relative; width: 120px; height: 120px; }
.score-circle.passed { filter: drop-shadow(0 0 20px rgba(103,194,58,0.3)); }
.score-text { position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%); text-align: center; }
.score-number { font-size: 36px; font-weight: 700; display: block; line-height: 1; }
.score-label { font-size: 12px; color: #a0a0a0; }
.result-info { flex: 1; }
.result-info h2 { font-size: 24px; margin-bottom: 8px; }
.result-meta { display: flex; gap: 24px; color: #a0a0a0; font-size: 14px; }

/* 雷达图 */
.chart-card { margin-top: 24px; padding: 32px; }
.radar-wrapper { display: flex; justify-content: center; margin: 24px 0; }
.radar-wrapper canvas { max-width: 100%; height: auto; }
.dimension-bars { display: flex; flex-direction: column; gap: 16px; margin-top: 24px; }
.dimension-bar { }
.dim-header { display: flex; justify-content: space-between; margin-bottom: 6px; }
.dim-label { font-size: 14px; color: #a0a0a0; }
.dim-score { font-size: 14px; font-weight: 600; }
.dim-score.high { color: #67c23a; }
.dim-score.mid { color: #e6a23c; }
.dim-score.low { color: #f56c6c; }
.dim-track { height: 8px; background: rgba(255,255,255,0.06); border-radius: 4px; overflow: hidden; }
.dim-fill { height: 100%; border-radius: 4px; transition: width 0.8s ease; }

/* AI报告 */
.report-card { margin-top: 24px; padding: 32px; }
.report-header { display: flex; align-items: center; gap: 12px; margin-bottom: 20px; }
.report-icon { font-size: 28px; }
.report-body { }
.report-overall { margin-bottom: 24px; }
.report-overall .label { font-size: 12px; color: #666; text-transform: uppercase; letter-spacing: 1px; }
.overall-text { font-size: 18px; line-height: 1.8; margin-top: 8px; padding: 16px; background: rgba(64,158,255,0.06); border-radius: 12px; border-left: 3px solid #409eff; }
.report-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 24px; }
.report-section h4 { font-size: 14px; margin-bottom: 12px; }
.tag-list { display: flex; flex-wrap: wrap; gap: 8px; }
.strength-tag, .weakness-tag { padding: 6px 14px; border-radius: 20px; font-size: 13px; display: flex; align-items: center; gap: 4px; }
.strength-tag { background: rgba(103,194,58,0.1); color: #67c23a; }
.weakness-tag { background: rgba(245,108,108,0.1); color: #f56c6c; }
.strength-tag em, .weakness-tag em { font-style: normal; opacity: 0.7; }

/* 推荐 */
.recommend-content { display: flex; flex-direction: column; gap: 24px; }
.rec-section h4 { margin-bottom: 12px; font-size: 14px; }
.course-chips { display: flex; flex-wrap: wrap; gap: 8px; }
.course-chip { padding: 8px 16px; background: rgba(64,158,255,0.08); border-radius: 20px; font-size: 13px; color: #409eff; }
.path-steps { display: flex; flex-direction: column; gap: 8px; }
.path-step { display: flex; align-items: center; gap: 12px; padding: 10px 16px; background: rgba(255,255,255,0.03); border-radius: 8px; font-size: 14px; }
.step-num { width: 24px; height: 24px; border-radius: 50%; background: #409eff; color: #fff; display: flex; align-items: center; justify-content: center; font-size: 12px; font-weight: 600; flex-shrink: 0; }
.estimate-time { font-size: 16px; color: #e6a23c; font-weight: 600; }

/* 操作按钮 */
.action-buttons { display: flex; gap: 16px; margin-top: 32px; justify-content: center; flex-wrap: wrap; }

@media (max-width: 768px) {
  .result-header { flex-direction: column; text-align: center; }
  .report-grid { grid-template-columns: 1fr; }
}
</style>