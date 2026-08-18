<template>
  <div class="page">
    <AppHeader />
    <div class="page-content">
      <div class="page-header">
        <h1>🕸️ 知识图谱</h1>
        <p class="page-desc">可视化探索技能之间的关系，发现你的学习路径</p>
      </div>

      <div class="view-mode-tabs">
        <el-button v-for="mode in viewModes" :key="mode.value"
          :type="viewMode === mode.value ? 'primary' : 'default'"
          size="small" @click="viewMode = mode.value">
          {{ mode.icon }} {{ mode.label }}
        </el-button>
      </div>

      <!-- 加载态 -->
      <div v-if="loading" class="loading-state" style="text-align:center;padding:60px">
        <SkeletonCard variant="card" />
      </div>

      <!-- 空态 -->
      <div v-else-if="skills.length === 0" class="empty-state glass-card" style="text-align:center;padding:64px">
        <el-icon :size="48" color="#a0a0a0"><data-board /></el-icon>
        <p style="margin:16px 0;color:#a0a0a0">暂无技能数据</p>
      </div>

      <!-- 图谱模式 -->
      <div v-if="viewMode === 'graph'" class="glass-card graph-container">
        <div class="graph-toolbar">
          <el-input v-model="searchQuery" placeholder="搜索节点..." size="small" clearable class="graph-search" />
          <div class="graph-legend">
            <span><span class="dot high" /> 高需求</span>
            <span><span class="dot mid" /> 中等</span>
            <span><span class="dot low" /> 低需求</span>
          </div>
        </div>
        <canvas ref="canvasRef" width="800" height="500" class="graph-canvas" />
      </div>

      <!-- 列表模式 -->
      <div v-if="viewMode === 'list'" class="skill-list">
        <div v-for="skill in skills" :key="skill.id" class="glass-card skill-item">
          <div class="skill-dot" :style="{ background: getScoreColor(skill.demandIndex) }" />
          <div class="skill-info">
            <span class="skill-name">{{ skill.skillName }}</span>
            <span class="skill-cat">{{ categoryLabel(skill.category) }}</span>
          </div>
          <div class="skill-metrics">
            <span>需求 {{ skill.demandIndex }}%</span>
            <span>稀缺 {{ skill.scarcityIndex }}%</span>
          </div>
          <el-tag v-if="skill.trend === 'UP'" size="small" type="success">🔥 热门</el-tag>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import http from '@/api'
import AppHeader from '@/components/AppHeader.vue'
import SkeletonCard from '@/components/SkeletonCard.vue'

const viewMode = ref('graph')
const searchQuery = ref('')
const skills = ref<any[]>([])
const loading = ref(true)
const canvasRef = ref<HTMLCanvasElement | null>(null)

const viewModes = [
  { value: 'graph', label: '图谱', icon: '🕸️' },
  { value: 'list', label: '列表', icon: '📋' },
]

function categoryLabel(cat: string): string {
  const labels: Record<string, string> = { AI: '人工智能', TECHNOLOGY: '技术', SOFT_SKILLS: '软技能', BUSINESS: '商业', DESIGN: '设计', LANGUAGE: '语言' }
  return labels[cat] || cat
}

function getScoreColor(score: number): string {
  if (score >= 85) return '#f56c6c'
  if (score >= 70) return '#e6a23c'
  return '#409eff'
}

function drawGraph() {
  if (!canvasRef.value || skills.value.length === 0) return
  const canvas = canvasRef.value
  const ctx = canvas.getContext('2d')
  if (!ctx) return

  const w = canvas.width, h = canvas.height
  ctx.clearRect(0, 0, w, h)

  // 绘制节点
  const centerX = w / 2, centerY = h / 2
  const radius = Math.min(w, h) * 0.35
  const count = Math.min(skills.value.length, 20)

  skills.value.slice(0, count).forEach((skill, i) => {
    const angle = (Math.PI * 2 * i) / count - Math.PI / 2
    const x = centerX + radius * Math.cos(angle)
    const y = centerY + radius * Math.sin(angle)
    const nodeRadius = 20 + (skill.demandIndex / 100) * 25
    const color = getScoreColor(skill.demandIndex)

    // 连线到中心
    ctx.beginPath()
    ctx.moveTo(centerX, centerY)
    ctx.lineTo(x, y)
    ctx.strokeStyle = 'rgba(64,158,255,0.15)'
    ctx.lineWidth = 1
    ctx.stroke()

    // 节点
    ctx.beginPath()
    ctx.arc(x, y, nodeRadius, 0, Math.PI * 2)
    ctx.fillStyle = color + '30'
    ctx.fill()
    ctx.strokeStyle = color
    ctx.lineWidth = 2
    ctx.stroke()

    // 标签
    ctx.fillStyle = '#e0e0e0'
    ctx.font = '11px sans-serif'
    ctx.textAlign = 'center'
    ctx.fillText(skill.skillName, x, y + nodeRadius + 14)
  })
}

onMounted(async () => {
  try {
    const res = await http.get('/skills/all')
    skills.value = res.data || []
  } catch { skills.value = [] }
  setTimeout(drawGraph, 100)
})
</script>

<style scoped>
.page-content { padding: 32px 48px; max-width: 1000px; margin: 0 auto; }
.page-header { margin-bottom: 24px; }
.page-header h1 { font-size: 28px; }
.page-desc { color: #a0a0a0; margin-top: 4px; }

.view-mode-tabs { display: flex; gap: 8px; margin-bottom: 24px; }

.graph-container { padding: 20px; }
.graph-toolbar { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.graph-search { width: 240px; }
.graph-legend { display: flex; gap: 16px; font-size: 12px; color: #a0a0a0; }
.graph-legend .dot { display: inline-block; width: 8px; height: 8px; border-radius: 50%; margin-right: 4px; }
.dot.high { background: #f56c6c; }
.dot.mid { background: #e6a23c; }
.dot.low { background: #409eff; }
.graph-canvas { width: 100%; height: auto; border-radius: 8px; }

.skill-list { display: flex; flex-direction: column; gap: 8px; }
.skill-item { display: flex; align-items: center; gap: 12px; padding: 16px 20px; }
.skill-dot { width: 10px; height: 10px; border-radius: 50%; flex-shrink: 0; }
.skill-info { flex: 1; }
.skill-name { display: block; font-size: 14px; font-weight: 500; }
.skill-cat { font-size: 12px; color: #666; }
.skill-metrics { display: flex; gap: 12px; font-size: 12px; color: #a0a0a0; }
</style>