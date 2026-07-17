<template>
  <div class="dashboard">
    <AppHeader />
    <div class="dashboard-content">
      <!-- 欢迎横幅 -->
      <div class="welcome-banner glass-card">
        <div class="welcome-text">
          <h1>👋 欢迎回来，{{ userStore.currentUser?.nickname }}</h1>
          <p>{{ todayTip }}</p>
        </div>
        <div class="welcome-actions">
          <el-button type="primary" round @click="router.push('/assessments')">
            <el-icon><collection /></el-icon> 开始测评
          </el-button>
          <el-button round @click="router.push('/metaverse')">
            <el-icon><video-camera /></el-icon> 元宇宙训练
          </el-button>
        </div>
      </div>

      <!-- 统计卡片 -->
      <div class="stats-grid">
        <div class="glass-card stat-card" v-for="stat in stats" :key="stat.label">
          <div class="stat-icon" :style="{ background: stat.bg }">
            <el-icon :size="24" :color="stat.color"><component :is="stat.icon" /></el-icon>
          </div>
          <div class="stat-info">
            <span class="stat-number">{{ stat.value }}</span>
            <span class="stat-label">{{ stat.label }}</span>
          </div>
          <div class="stat-trend" :class="stat.trend > 0 ? 'up' : 'down'">
            <span>{{ stat.trend > 0 ? '+' : '' }}{{ stat.trend }}%</span>
          </div>
        </div>
      </div>

      <div class="dashboard-grid">
        <!-- 最近测评 -->
        <div class="glass-card">
          <div class="card-header">
            <h3>📝 最近测评结果</h3>
            <el-button link type="primary" @click="router.push('/assessments')">查看全部</el-button>
          </div>
          <div v-if="results.length === 0" class="empty-state">
            <p>还没有完成测评，<router-link to="/assessments">去试试</router-link></p>
          </div>
          <div v-else class="result-list">
            <div v-for="r in results.slice(0, 5)" :key="r.id"
                 class="result-item" @click="router.push(`/assessments/results/${r.id}`)">
              <div class="result-left">
                <span class="result-title">{{ r.assessmentTitle }}</span>
                <span class="result-date">{{ formatDate(r.completedAt) }}</span>
              </div>
              <div class="result-right">
                <span class="result-score" :class="r.passed ? 'pass' : 'fail'">{{ r.score }}分</span>
                <el-tag :type="r.passed ? 'success' : 'danger'" size="small">
                  {{ r.passed ? '通过' : '未通过' }}
                </el-tag>
              </div>
            </div>
          </div>
        </div>

        <!-- 快速入口 -->
        <div class="glass-card">
          <div class="card-header">
            <h3>🚀 快速入口</h3>
          </div>
          <div class="quick-grid">
            <div class="quick-item" @click="router.push('/assessments')">
              <div class="qi-icon" style="background:rgba(64,158,255,0.1)">🧠</div>
              <span>技能测评</span>
            </div>
            <div class="quick-item" @click="router.push('/courses')">
              <div class="qi-icon" style="background:rgba(103,194,58,0.1)">📚</div>
              <span>课程中心</span>
            </div>
            <div class="quick-item" @click="router.push('/metaverse')">
              <div class="qi-icon" style="background:rgba(230,162,60,0.1)">🌐</div>
              <span>元宇宙</span>
            </div>
            <div class="quick-item" @click="router.push('/certifications')">
              <div class="qi-icon" style="background:rgba(245,108,108,0.1)">🏆</div>
              <span>我的证书</span>
            </div>
            <div class="quick-item" @click="router.push('/assessments/results')">
              <div class="qi-icon" style="background:rgba(144,147,153,0.1)">📊</div>
              <span>历史报告</span>
            </div>
            <div class="quick-item" @click="router.push('/courses')">
              <div class="qi-icon" style="background:rgba(64,158,255,0.1)">🎯</div>
              <span>推荐课程</span>
            </div>
          </div>
        </div>

        <!-- 最近课程 -->
        <div class="glass-card">
          <div class="card-header">
            <h3>📖 推荐课程</h3>
            <el-button link type="primary" @click="router.push('/courses')">查看全部</el-button>
          </div>
          <div v-if="courses.length === 0" class="empty-state">
            <p>暂无推荐课程</p>
          </div>
          <div v-else class="course-list">
            <div v-for="c in courses.slice(0, 4)" :key="c.id"
                 class="course-item" @click="router.push(`/courses/${c.id}`)">
              <div class="course-icon">{{ getCategoryIcon(c.category) }}</div>
              <div class="course-info">
                <span class="course-title">{{ c.title }}</span>
                <span class="course-meta">{{ c.duration }}分钟 · {{ c.difficulty }}</span>
              </div>
              <span class="course-price">{{ c.price > 0 ? '¥' + c.price : '免费' }}</span>
            </div>
          </div>
        </div>

        <!-- 元宇亩训练 -->
        <div class="glass-card">
          <div class="card-header">
            <h3>🌐 元宇宙训练</h3>
            <el-button link type="primary" @click="router.push('/metaverse')">查看全部</el-button>
          </div>
          <div v-if="sessions.length === 0" class="empty-state">
            <p>还没有训练记录，<router-link to="/metaverse">进入元宇宙</router-link></p>
          </div>
          <div v-else class="session-list">
            <div v-for="s in sessions.slice(0, 4)" :key="s.id" class="session-item">
              <div class="session-icon">{{ getSceneIcon(s.sceneType) }}</div>
              <div class="session-info">
                <span class="session-title">{{ s.sessionName }}</span>
                <span class="session-meta">{{ formatDate(s.startTime) }}</span>
              </div>
              <el-tag :type="s.active ? 'success' : 'info'" size="small">
                {{ s.active ? '进行中' : '已结束' }}
              </el-tag>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { assessmentApi, courseApi, metaverseApi } from '@/api'
import AppHeader from '@/components/AppHeader.vue'
import type { AssessmentResult, Course, MetaverseSession } from '@/types'

const router = useRouter()
const userStore = useUserStore()

const results = ref<AssessmentResult[]>([])
const courses = ref<Course[]>([])
const sessions = ref<MetaverseSession[]>([])

const passedCount = computed(() => results.value.filter(r => r.passed).length)
const avgScore = computed(() => {
  if (results.value.length === 0) return 0
  return Math.round(results.value.reduce((s, r) => s + r.score, 0) / results.value.length)
})

const todayTip = computed(() => {
  const tips = [
    '今天也是进步的一天，加油！💪',
    '技能提升，从每一次测评开始 📈',
    '元宇宙训练场已就绪，等你来挑战 🎯',
    '学习是通往未来的钥匙 🔑',
    '每一次测评都是成长的机会 🌱',
  ]
  return tips[Math.floor(Math.random() * tips.length)]
})

const stats = computed(() => [
  { label: '已完成测评', value: results.value.length, icon: 'Document', color: '#409eff', bg: 'rgba(64,158,255,0.1)', trend: 12 },
  { label: '通过认证', value: passedCount.value, icon: 'SuccessFilled', color: '#67c23a', bg: 'rgba(103,194,58,0.1)', trend: 8 },
  { label: '平均得分', value: avgScore.value, icon: 'DataAnalysis', color: '#e6a23c', bg: 'rgba(230,162,60,0.1)', trend: 5 },
  { label: '推荐课程', value: courses.value.length, icon: 'Reading', color: '#f56c6c', bg: 'rgba(245,108,108,0.1)', trend: 0 },
])

function formatDate(dateStr: string): string {
  if (!dateStr) return ''
  return dateStr.replace('T', ' ').substring(0, 16)
}

function getCategoryIcon(cat: string): string {
  const icons: Record<string, string> = { TECHNOLOGY: '💻', BUSINESS: '💼', DESIGN: '🎨', LANGUAGE: '🌍', SOFT_SKILLS: '🤝' }
  return icons[cat] || '📖'
}

function getSceneIcon(type: string): string {
  const icons: Record<string, string> = { INTERVIEW_ROOM: '🎯', CLASSROOM: '📚', MEETING_ROOM: '🤝', TRAINING_ROOM: '⚡' }
  return icons[type] || '🌐'
}

onMounted(async () => {
  try {
    const [resR, resC, resS] = await Promise.all([
      assessmentApi.getMyResults(),
      courseApi.getAll(),
      metaverseApi.getMySessions(),
    ])
    results.value = resR.data || []
    courses.value = resC.data || []
    sessions.value = resS.data || []
  } catch (err) {
    console.error('加载仪表盘数据失败', err)
  }
})
</script>

<style scoped>
.dashboard-content { padding: 32px 48px; max-width: 1200px; margin: 0 auto; }

/* 欢迎横幅 */
.welcome-banner { display: flex; align-items: center; justify-content: space-between; padding: 32px; margin-bottom: 24px; }
.welcome-text h1 { font-size: 24px; margin-bottom: 8px; }
.welcome-text p { color: #a0a0a0; font-size: 14px; }
.welcome-actions { display: flex; gap: 12px; }

/* 统计卡片 */
.stats-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 20px; margin-bottom: 24px; }
.stat-card { display: flex; align-items: center; gap: 16px; padding: 24px; position: relative; }
.stat-icon { width: 48px; height: 48px; border-radius: 12px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; }
.stat-info { display: flex; flex-direction: column; }
.stat-number { font-size: 28px; font-weight: 700; line-height: 1.2; }
.stat-label { color: #a0a0a0; font-size: 13px; }
.stat-trend { position: absolute; top: 12px; right: 16px; font-size: 12px; padding: 2px 8px; border-radius: 4px; }
.stat-trend.up { background: rgba(103,194,58,0.1); color: #67c23a; }
.stat-trend.down { background: rgba(245,108,108,0.1); color: #f56c6c; }

/* 网格布局 */
.dashboard-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 24px; }
.card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.card-header h3 { font-size: 16px; }

/* 空状态 */
.empty-state { text-align: center; padding: 32px; color: #a0a0a0; }
.empty-state a { color: #409eff; text-decoration: none; }

/* 测评列表 */
.result-list { display: flex; flex-direction: column; }
.result-item { display: flex; justify-content: space-between; align-items: center; padding: 12px 0; border-bottom: 1px solid rgba(255,255,255,0.04); cursor: pointer; transition: background 0.2s; }
.result-item:hover { background: rgba(255,255,255,0.02); }
.result-item:last-child { border-bottom: none; }
.result-left { display: flex; flex-direction: column; }
.result-title { font-size: 14px; }
.result-date { font-size: 12px; color: #666; margin-top: 2px; }
.result-right { display: flex; align-items: center; gap: 12px; }
.result-score { font-size: 18px; font-weight: 700; }
.result-score.pass { color: #67c23a; }
.result-score.fail { color: #f56c6c; }

/* 快速入口 */
.quick-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 12px; }
.quick-item { display: flex; flex-direction: column; align-items: center; gap: 8px; padding: 16px; border-radius: 12px; cursor: pointer; transition: all 0.2s; }
.quick-item:hover { background: rgba(255,255,255,0.04); }
.qi-icon { width: 44px; height: 44px; border-radius: 12px; display: flex; align-items: center; justify-content: center; font-size: 22px; }
.quick-item span { font-size: 12px; color: #a0a0a0; }

/* 课程列表 */
.course-list { display: flex; flex-direction: column; }
.course-item { display: flex; align-items: center; gap: 12px; padding: 12px 0; border-bottom: 1px solid rgba(255,255,255,0.04); cursor: pointer; }
.course-item:last-child { border-bottom: none; }
.course-icon { font-size: 24px; }
.course-info { flex: 1; display: flex; flex-direction: column; }
.course-title { font-size: 14px; }
.course-meta { font-size: 12px; color: #666; }
.course-price { font-size: 14px; font-weight: 600; color: #f56c6c; }

/* 会话列表 */
.session-list { display: flex; flex-direction: column; }
.session-item { display: flex; align-items: center; gap: 12px; padding: 12px 0; border-bottom: 1px solid rgba(255,255,255,0.04); }
.session-item:last-child { border-bottom: none; }
.session-icon { font-size: 24px; }
.session-info { flex: 1; display: flex; flex-direction: column; }
.session-title { font-size: 14px; }
.session-meta { font-size: 12px; color: #666; }

@media (max-width: 900px) {
  .stats-grid { grid-template-columns: repeat(2, 1fr); }
  .dashboard-grid { grid-template-columns: 1fr; }
  .welcome-banner { flex-direction: column; text-align: center; gap: 16px; }
}
</style>