<template>
  <div class="page">
    <AppHeader />
    <div class="page-content">
      <el-button v-if="step === 'result'" link @click="router.push('/assessments')" style="margin-bottom:16px">
        <el-icon><arrow-left /></el-icon> 返回测评列表
      </el-button>

      <!-- ===== 测评介绍页 ===== -->
      <div v-if="step === 'intro' && assessment" class="intro-container">
        <div class="glass-card intro-card">
          <div class="intro-icon">🧠</div>
          <h1>{{ assessment.title }}</h1>
          <p class="intro-desc">{{ assessment.description }}</p>

          <div class="intro-stats">
            <div class="stat-item">
              <span class="stat-icon">📝</span>
              <span class="stat-label">题目数量</span>
              <span class="stat-value">{{ totalQuestions }} 题</span>
            </div>
            <div class="stat-item">
              <span class="stat-icon">⏱</span>
              <span class="stat-label">预估时长</span>
              <span class="stat-value">{{ assessment.duration }} 分钟</span>
            </div>
            <div class="stat-item">
              <span class="stat-icon">🎯</span>
              <span class="stat-label">测评维度</span>
              <span class="stat-value">{{ questionGroups.length }} 个</span>
            </div>
          </div>

          <div class="dimension-previews">
            <div v-for="g in questionGroups" :key="g.dimension" class="dimension-preview">
              <span class="dp-icon">{{ g.icon }}</span>
              <div>
                <strong>{{ g.label }}</strong>
                <span class="dp-count">{{ g.questions.length }} 题</span>
              </div>
            </div>
          </div>

          <el-button type="primary" size="large" round @click="startAssessment" style="width:100%;margin-top:24px">
            <el-icon><video-play /></el-icon> 开始测评
          </el-button>
        </div>
      </div>

      <!-- ===== 答题页 ===== -->
      <div v-if="step === 'answering'" class="answering-container">
        <!-- 顶部进度条 -->
        <div class="progress-bar-container">
          <div class="progress-info">
            <span class="progress-text">第 {{ currentIndex + 1 }} / {{ totalQuestions }} 题</span>
            <span class="progress-dim">{{ currentGroup.label }} · {{ currentGroup.icon }}</span>
          </div>
          <el-progress :percentage="progressPercent" :stroke-width="6" color="#409eff" />
        </div>

        <!-- 维度切换标签 -->
        <div class="dimension-tabs">
          <div v-for="(g, gi) in questionGroups" :key="g.dimension"
               class="dim-tab"
               :class="{ active: gi === currentGroupIndex, done: groupDone(gi) }"
               @click="switchGroup(gi)">
            <span>{{ g.icon }}</span>
            <span>{{ g.label }}</span>
            <span v-if="groupDone(gi)" class="done-badge">✓</span>
          </div>
        </div>

        <!-- 题目卡片 -->
        <transition name="slide-fade" mode="out-in">
          <div class="glass-card question-card" :key="currentQuestion?.id">
            <div class="question-number">Q{{ currentIndex + 1 }}</div>
            <h3 class="question-text">{{ currentQuestion?.text }}</h3>

            <div class="options-list">
              <div v-for="opt in currentQuestion?.options" :key="opt.value"
                   class="option-item"
                   :class="{ selected: answers[currentQuestion?.id || ''] === opt.value }"
                   @click="selectOption(currentQuestion?.id || '', opt.value)">
                <div class="option-radio">
                  <div v-if="answers[currentQuestion?.id || ''] === opt.value" class="radio-dot" />
                </div>
                <span class="option-label">{{ opt.label }}</span>
              </div>
            </div>
          </div>
        </transition>

        <!-- 导航按钮 -->
        <div class="nav-buttons">
          <el-button :disabled="currentIndex === 0" @click="prevQuestion">
            <el-icon><arrow-left /></el-icon> 上一题
          </el-button>
          <el-button v-if="currentIndex < totalQuestions - 1" type="primary" @click="nextQuestion">
            下一题 <el-icon><arrow-right /></el-icon>
          </el-button>
          <el-button v-else type="success" :loading="submitting" @click="confirmSubmit">
            <el-icon><checked /></el-icon> 提交测评
          </el-button>
        </div>

        <!-- 答题进度 -->
        <div class="question-dots">
          <div v-for="(_, idx) in totalQuestions" :key="idx"
               class="dot"
               :class="{
                 active: idx === currentIndex,
                 answered: answers[questionAt(idx)?.id || ''] !== undefined,
                 current: idx === currentIndex
               }"
               @click="goToQuestion(idx)" />
        </div>
      </div>

      <!-- ===== 提交确认弹窗 ===== -->
      <el-dialog v-model="showConfirm" title="确认提交测评" width="420px">
        <div class="confirm-content">
          <div class="confirm-icon">📊</div>
          <p>你已作答 <strong>{{ answeredCount }}</strong> / {{ totalQuestions }} 题</p>
          <div class="confirm-stats">
            <div v-for="g in questionGroups" :key="g.dimension" class="confirm-dim">
              <span>{{ g.icon }} {{ g.label }}</span>
              <span>{{ groupAnsweredCount(g.dimension) }} / {{ g.questions.length }}</span>
            </div>
          </div>
          <p v-if="answeredCount < totalQuestions" style="color:#e6a23c;margin-top:8px">
            ⚠️ 还有 {{ totalQuestions - answeredCount }} 题未作答
          </p>
        </div>
        <template #footer>
          <el-button @click="showConfirm = false">继续答题</el-button>
          <el-button type="primary" :loading="submitting" @click="doSubmit">
            确认提交
          </el-button>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, reactive } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { assessmentApi } from '@/api'
import { questionBank, calculateAllScores, calculateTotalScore } from '@/data/questions'
import AppHeader from '@/components/AppHeader.vue'
import type { Assessment, QuestionGroup } from '@/types'

const route = useRoute()
const router = useRouter()
const assessment = ref<Assessment | null>(null)
const step = ref<'intro' | 'answering' | 'result'>('intro')
const currentGroupIndex = ref(0)
const currentIndex = ref(0)
const answers = reactive<Record<string, number>>({})
const submitting = ref(false)
const showConfirm = ref(false)

// 按维度分组题目
const questionGroups = computed<QuestionGroup[]>(() => {
  return questionBank
})

const totalQuestions = computed(() => {
  return questionGroups.value.reduce((sum, g) => sum + g.questions.length, 0)
})

const currentGroup = computed(() => {
  return questionGroups.value[currentGroupIndex.value]
})

const currentQuestion = computed(() => {
  const group = currentGroup.value
  if (!group) return null
  const localIdx = currentIndex.value - groupStartIndex.value
  return group.questions[localIdx] || null
})

const groupStartIndex = computed(() => {
  let idx = 0
  for (let i = 0; i < currentGroupIndex.value; i++) {
    idx += questionGroups.value[i].questions.length
  }
  return idx
})

const progressPercent = computed(() => {
  return totalQuestions.value > 0 ? (answeredCount.value / totalQuestions.value) * 100 : 0
})

const answeredCount = computed(() => {
  return Object.keys(answers).length
})

function questionAt(idx: number): (typeof questionGroups.value)[0]['questions'][0] | null {
  let count = 0
  for (const g of questionGroups.value) {
    if (idx < count + g.questions.length) {
      return g.questions[idx - count]
    }
    count += g.questions.length
  }
  return null
}

function groupDone(gi: number): boolean {
  const group = questionGroups.value[gi]
  return group.questions.every(q => answers[q.id] !== undefined)
}

function groupAnsweredCount(dim: string): number {
  const group = questionGroups.value.find(g => g.dimension === dim)
  if (!group) return 0
  return group.questions.filter(q => answers[q.id] !== undefined).length
}

function selectOption(qId: string, value: number) {
  answers[qId] = value
  // 自动跳转到下一题
  setTimeout(() => {
    if (currentIndex.value < totalQuestions.value - 1) {
      nextQuestion()
    }
  }, 300)
}

function startAssessment() {
  step.value = 'answering'
  currentGroupIndex.value = 0
  currentIndex.value = 0
}

function nextQuestion() {
  if (currentIndex.value < totalQuestions.value - 1) {
    currentIndex.value++
    updateGroupIndex()
  }
}

function prevQuestion() {
  if (currentIndex.value > 0) {
    currentIndex.value--
    updateGroupIndex()
  }
}

function goToQuestion(idx: number) {
  currentIndex.value = idx
  updateGroupIndex()
}

function switchGroup(gi: number) {
  currentGroupIndex.value = gi
  let idx = 0
  for (let i = 0; i < gi; i++) {
    idx += questionGroups.value[i].questions.length
  }
  currentIndex.value = idx
}

function updateGroupIndex() {
  let idx = 0
  for (let i = 0; i < questionGroups.value.length; i++) {
    idx += questionGroups.value[i].questions.length
    if (currentIndex.value < idx) {
      currentGroupIndex.value = i
      return
    }
  }
}

function confirmSubmit() {
  showConfirm.value = true
}

async function doSubmit() {
  if (!assessment.value) return
  submitting.value = true
  try {
    const dimensionScores = calculateAllScores(questionGroups.value, answers)
    const totalScore = calculateTotalScore(dimensionScores)

    const res = await assessmentApi.submit({
      assessmentId: assessment.value.id,
      dimensionScores,
    })
    ElMessage.success(`🎉 测评完成！得分 ${res.data.score} 分`)
    router.push(`/assessments/results/${res.data.id}`)
  } catch (err: any) {
    ElMessage.error(err.response?.data?.message || '提交失败')
  } finally {
    submitting.value = false
    showConfirm.value = false
  }
}

onMounted(async () => {
  try {
    const res = await assessmentApi.getById(Number(route.params.id))
    assessment.value = res.data
  } catch (err) {
    console.error('加载测评失败', err)
    ElMessage.error('加载测评失败')
  }
})
</script>

<style scoped>
.page-content { padding: 32px 48px; max-width: 900px; margin: 0 auto; }

/* ===== Intro ===== */
.intro-card { text-align: center; padding: 48px; }
.intro-icon { font-size: 64px; margin-bottom: 16px; }
.intro-desc { color: #a0a0a0; margin: 16px 0 32px; font-size: 16px; line-height: 1.6; }
.intro-stats { display: grid; grid-template-columns: repeat(3, 1fr); gap: 16px; margin-bottom: 32px; }
.stat-item { background: rgba(255,255,255,0.03); border-radius: 12px; padding: 16px; display: flex; flex-direction: column; gap: 4px; }
.stat-icon { font-size: 24px; }
.stat-label { font-size: 12px; color: #666; }
.stat-value { font-size: 18px; font-weight: 600; }
.dimension-previews { display: flex; flex-direction: column; gap: 12px; }
.dimension-preview { display: flex; align-items: center; gap: 12px; padding: 12px 16px; background: rgba(255,255,255,0.03); border-radius: 8px; }
.dp-icon { font-size: 24px; }
.dp-count { font-size: 12px; color: #666; margin-left: 8px; }

/* ===== Answering ===== */
.progress-bar-container { margin-bottom: 20px; }
.progress-info { display: flex; justify-content: space-between; margin-bottom: 8px; }
.progress-text { font-size: 14px; color: #a0a0a0; }
.progress-dim { font-size: 14px; color: #409eff; }

.dimension-tabs { display: flex; gap: 8px; margin-bottom: 24px; overflow-x: auto; }
.dim-tab { display: flex; align-items: center; gap: 6px; padding: 8px 16px; border-radius: 20px; background: rgba(255,255,255,0.04); cursor: pointer; font-size: 13px; color: #a0a0a0; white-space: nowrap; transition: all 0.2s; }
.dim-tab.active { background: rgba(64,158,255,0.15); color: #409eff; }
.dim-tab.done { background: rgba(103,194,58,0.1); color: #67c23a; }
.done-badge { background: #67c23a; color: #fff; border-radius: 50%; width: 18px; height: 18px; display: flex; align-items: center; justify-content: center; font-size: 11px; }

.question-card { padding: 32px; }
.question-number { font-size: 12px; color: #409eff; font-weight: 600; margin-bottom: 8px; }
.question-text { font-size: 18px; line-height: 1.6; margin-bottom: 28px; }

.options-list { display: flex; flex-direction: column; gap: 12px; }
.option-item { display: flex; align-items: center; gap: 16px; padding: 16px 20px; border-radius: 12px; border: 1px solid rgba(255,255,255,0.08); cursor: pointer; transition: all 0.2s; }
.option-item:hover { border-color: rgba(64,158,255,0.3); background: rgba(64,158,255,0.04); }
.option-item.selected { border-color: #409eff; background: rgba(64,158,255,0.1); }
.option-radio { width: 20px; height: 20px; border-radius: 50%; border: 2px solid #555; display: flex; align-items: center; justify-content: center; flex-shrink: 0; transition: all 0.2s; }
.option-item.selected .option-radio { border-color: #409eff; }
.radio-dot { width: 10px; height: 10px; border-radius: 50%; background: #409eff; }
.option-label { font-size: 15px; line-height: 1.4; }

.nav-buttons { display: flex; justify-content: space-between; margin-top: 24px; }

.question-dots { display: flex; justify-content: center; gap: 6px; margin-top: 20px; flex-wrap: wrap; }
.dot { width: 10px; height: 10px; border-radius: 50%; background: rgba(255,255,255,0.1); cursor: pointer; transition: all 0.2s; }
.dot.answered { background: #409eff; }
.dot.current { transform: scale(1.3); box-shadow: 0 0 6px rgba(64,158,255,0.5); }

/* ===== Confirm ===== */
.confirm-content { text-align: center; }
.confirm-icon { font-size: 48px; margin-bottom: 12px; }
.confirm-stats { display: flex; flex-direction: column; gap: 8px; margin: 16px 0; }
.confirm-dim { display: flex; justify-content: space-between; padding: 8px 12px; background: rgba(255,255,255,0.03); border-radius: 8px; font-size: 14px; }

/* 过渡动画 */
.slide-fade-enter-active { transition: all 0.25s ease-out; }
.slide-fade-leave-active { transition: all 0.15s ease-in; }
.slide-fade-enter-from { opacity: 0; transform: translateX(20px); }
.slide-fade-leave-to { opacity: 0; transform: translateX(-20px); }
</style>