<template>
  <div class="page">
    <AppHeader />
    <div class="page-content">
      <div class="page-header">
        <h1>🗺️ 个性化学习路径</h1>
        <p class="page-desc">基于你的能力画像和测评结果，AI 自动生成的学习路线图</p>
      </div>

      <!-- 进度总览 -->
      <div class="glass-card progress-card">
        <div class="progress-circle">
          <svg viewBox="0 0 120 120" width="120" height="120">
            <circle cx="60" cy="60" r="54" fill="none" stroke="#2d2d4a" stroke-width="8" />
            <circle cx="60" cy="60" r="54" fill="none" stroke="#409eff" stroke-width="8"
              :stroke-dasharray="339.292"
              :stroke-dashoffset="339.292 - (339.292 * pct / 100)"
              stroke-linecap="round" transform="rotate(-90, 60, 60)" />
          </svg>
          <div class="progress-text">
            <span class="pct-num">{{ pct }}%</span>
            <span class="pct-label">已完成</span>
          </div>
        </div>
        <div class="progress-info">
          <h3>学习进度</h3>
          <span>已完成 {{ completed }}/{{ total }} 阶段</span>
          <div class="progress-bar">
            <div class="progress-fill" :style="{ width: pct + '%' }" />
          </div>
        </div>
      </div>

      <!-- 加载态 -->
      <div v-if="loading" class="loading-state">
        <SkeletonCard v-for="i in 4" :key="i" variant="card" style="margin-bottom:12px" />
      </div>

      <!-- 路径列表 -->
      <div v-else-if="nodes.length > 0" class="path-list">
        <!-- 建议下一步 -->
        <div v-if="nextStep" class="next-step-banner glass-card" @click="startLearning(nextStep)">
          <div class="ns-icon">🎯</div>
          <div class="ns-body">
            <span class="ns-label">建议下一步</span>
            <span class="ns-title">{{ nextStep.chapter }}</span>
          </div>
          <el-button type="primary" size="small">开始学习 →</el-button>
        </div>

        <div v-for="(node, idx) in nodes" :key="node.chapter" class="path-card" :class="node.status">
          <div v-if="idx > 0" class="path-connector" />
          <div class="glass-card path-card-inner">
            <div class="path-icon" :class="node.status">
              <span v-if="node.status === 'completed'">✅</span>
              <span v-else-if="node.status === 'current'">📖</span>
              <span v-else-if="node.status === 'locked'">🔒</span>
              <span v-else>📋</span>
            </div>
            <div class="path-content">
              <div class="path-header">
                <span class="path-chapter">阶段 {{ node.order }}</span>
                <el-tag size="small" :type="node.status === 'completed' ? 'success' : node.status === 'current' ? 'primary' : 'info'">
                  {{ statusText(node.status) }}
                </el-tag>
              </div>
              <h3 class="path-title">{{ node.chapter }}</h3>
              <div class="path-topics">
                <span v-for="t in node.topics" :key="t" class="topic-tag">{{ t }}</span>
              </div>
              <div class="path-meta">
                <span>⏱ {{ node.estimated_hours }} 小时</span>
                <span v-if="node.weak_focus" class="weak-tag">⚠️ 薄弱重点</span>
              </div>
              <button v-if="node.status === 'ready' || node.status === 'current'"
                      class="start-btn" @click="startLearning(node)">
                开始学习 →
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- 空态 -->
      <div v-else class="empty-state glass-card">
        <el-icon :size="48" color="#a0a0a0"><map-location /></el-icon>
        <p>还没有学习路径，先完成诊断测评吧</p>
        <el-button type="primary" @click="router.push('/diagnostic')">去诊断</el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import http from '@/api'
import AppHeader from '@/components/AppHeader.vue'
import SkeletonCard from '@/components/SkeletonCard.vue'

const router = useRouter()
const nodes = ref<any[]>([])
const total = ref(0)
const completed = ref(0)
const pct = ref(0)
const loading = ref(true)

const nextStep = computed(() => {
  return nodes.value.find(n => n.status === 'current' || n.status === 'ready') || null
})

function statusText(status: string): string {
  const map: Record<string, string> = { completed: '已完成', current: '学习中', ready: '可开始', locked: '需先学前置' }
  return map[status] || ''
}

function startLearning(node: any) {
  router.push({ path: '/courses', query: { focus: node.chapter } })
}

function loadPath() {
  loading.value = true
  http.get('/learning-path/recommend')
    .then((res: any) => {
      const data = res.data
      if (data && data.steps) {
        nodes.value = data.steps.map((s: any, i: number) => ({
          chapter: s.phase,
          order: s.order,
          status: i === 0 ? 'current' : (i <= 1 ? 'ready' : 'locked'),
          topics: s.focusSkills || [],
          estimated_hours: 2,
          weak_focus: i === 0 ? '优先' : '',
          resources: { doc: '', quiz: '' },
        }))
        total.value = nodes.value.length
        completed.value = nodes.value.filter((n: any) => n.status === 'completed').length
        pct.value = Math.round((completed.value / total.value) * 100)
      }
    })
    .catch(() => {
      // 降级：使用默认学习路径
      nodes.value = [
        { chapter: '基础巩固', order: 1, status: 'current', topics: ['核心概念', '基础知识'], estimated_hours: 3, weak_focus: '' },
        { chapter: '能力提升', order: 2, status: 'ready', topics: ['进阶技能', '实践应用'], estimated_hours: 4, weak_focus: '优先' },
        { chapter: '实战应用', order: 3, status: 'ready', topics: ['项目实战', '综合训练'], estimated_hours: 5, weak_focus: '' },
        { chapter: '认证冲刺', order: 4, status: 'locked', topics: ['综合测评', '认证准备'], estimated_hours: 3, weak_focus: '' },
      ]
      total.value = 4
      completed.value = 0
      pct.value = 0
    })
    .finally(() => { loading.value = false })
}

onMounted(loadPath)
</script>

<style scoped>
.page-content { padding: 32px 48px; max-width: 800px; margin: 0 auto; }
.page-header { margin-bottom: 32px; }
.page-header h1 { font-size: 28px; }
.page-desc { color: #a0a0a0; margin-top: 4px; }
.loading-state { max-width: 600px; margin: 0 auto; }

.progress-card { display: flex; align-items: center; gap: 32px; padding: 32px; margin-bottom: 32px; }
.progress-circle { position: relative; width: 120px; height: 120px; flex-shrink: 0; }
.progress-text { position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%); text-align: center; }
.pct-num { display: block; font-size: 28px; font-weight: 700; }
.pct-label { font-size: 12px; color: #a0a0a0; }
.progress-info { flex: 1; }
.progress-info h3 { margin-bottom: 4px; }
.progress-info span { font-size: 14px; color: #a0a0a0; }
.progress-bar { height: 8px; background: rgba(255,255,255,0.06); border-radius: 4px; overflow: hidden; margin-top: 12px; }
.progress-fill { height: 100%; background: linear-gradient(90deg, #409eff, #667eea); border-radius: 4px; transition: width 0.6s ease; }

.next-step-banner { display: flex; align-items: center; gap: 16px; padding: 20px 24px; margin-bottom: 20px; cursor: pointer; }
.ns-icon { font-size: 32px; }
.ns-body { flex: 1; }
.ns-label { display: block; font-size: 12px; color: #409eff; font-weight: 600; }
.ns-title { font-size: 16px; font-weight: 600; }

.path-card { margin-bottom: 0; }
.path-connector { height: 24px; display: flex; justify-content: center; }
.path-connector::after { content: ''; width: 2px; height: 100%; background: rgba(64,158,255,0.3); }
.path-card-inner { display: flex; gap: 20px; padding: 24px; margin-bottom: 0; }
.path-card.completed .path-card-inner { opacity: 0.6; }
.path-card.locked .path-card-inner { opacity: 0.4; }
.path-icon { width: 48px; height: 48px; border-radius: 12px; display: flex; align-items: center; justify-content: center; font-size: 24px; flex-shrink: 0; background: rgba(64,158,255,0.1); }
.path-icon.current { background: rgba(64,158,255,0.15); box-shadow: 0 0 12px rgba(64,158,255,0.2); }
.path-content { flex: 1; }
.path-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 4px; }
.path-chapter { font-size: 12px; color: #666; font-weight: 600; }
.path-title { font-size: 18px; font-weight: 600; margin-bottom: 8px; }
.path-topics { display: flex; flex-wrap: wrap; gap: 6px; margin-bottom: 8px; }
.topic-tag { padding: 3px 10px; border-radius: 12px; font-size: 12px; background: rgba(64,158,255,0.08); color: #409eff; }
.path-meta { display: flex; gap: 16px; font-size: 13px; color: #a0a0a0; }
.weak-tag { color: #e6a23c; }
.start-btn { margin-top: 12px; padding: 8px 24px; border: none; border-radius: 20px; background: linear-gradient(135deg, #409eff, #667eea); color: #fff; font-size: 14px; font-weight: 600; cursor: pointer; transition: all 0.2s; }
.start-btn:hover { transform: translateY(-2px); box-shadow: 0 4px 12px rgba(64,158,255,0.3); }
.empty-state { text-align: center; padding: 64px; }
.empty-state p { margin: 16px 0; color: #a0a0a0; }
</style>