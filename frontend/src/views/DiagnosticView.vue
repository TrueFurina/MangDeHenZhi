<template>
  <div class="page">
    <AppHeader />
    <div class="page-content">
      <div class="diag-header">
        <h1>📋 AI 入学诊断</h1>
        <p class="diag-desc">完成 20 道诊断题，系统将自动生成你的能力画像和推荐学习路径</p>
      </div>

      <!-- 加载中 -->
      <div v-if="loading" class="diag-loading">
        <SkeletonCard variant="card" />
        <SkeletonCard variant="card" style="margin-top:12px" />
      </div>

      <!-- 答题阶段 -->
      <div v-else-if="step === 'doing'" class="diag-body">
        <div class="diag-progress-bar">
          <el-progress :percentage="progressPercent" :stroke-width="8" color="#409eff" />
          <span class="progress-text">{{ answerCount }} / {{ questions.length }} 题</span>
        </div>

        <div class="diag-subjects">
          <span v-for="s in subjects" :key="s.key" class="subject-tag" :class="{ done: subjectDone(s.key) }">
            {{ s.label }}
          </span>
        </div>

        <div v-for="(q, i) in questions" :key="q.id" class="glass-card diag-question-card">
          <div class="q-header">
            <span class="q-number">第 {{ i + 1 }} 题</span>
            <el-tag size="small" v-if="q.difficulty === 'hard'" type="danger">困难</el-tag>
            <el-tag size="small" v-else-if="q.difficulty === 'medium'" type="warning">中等</el-tag>
            <el-tag size="small" v-else type="success">简单</el-tag>
          </div>
          <div class="q-subject">{{ q.subject_name }} · {{ q.topic }}</div>
          <div class="q-text">{{ q.question }}</div>
          <div class="q-options">
            <div v-for="opt in q.options" :key="opt"
                 class="q-option"
                 :class="{ selected: answers[q.id] === opt.charAt(0) }"
                 @click="selectAnswer(q.id, opt.charAt(0))">
              <span class="option-radio">
                <span v-if="answers[q.id] === opt.charAt(0)" class="radio-dot" />
              </span>
              <span>{{ opt }}</span>
            </div>
          </div>
        </div>

        <div class="diag-actions">
          <el-button type="primary" size="large" :disabled="answerCount < questions.length" @click="submit" :loading="submitting">
            {{ submitting ? '分析中...' : `提交诊断 (${answerCount}/${questions.length})` }}
          </el-button>
        </div>
      </div>

      <!-- 结果阶段 -->
      <div v-else-if="step === 'result' && result" class="diag-result">
        <div class="glass-card result-card">
          <div class="result-icon">🎉</div>
          <h2>诊断完成！</h2>
          <div class="result-score">
            <div class="score-ring">
              <svg viewBox="0 0 120 120" width="120" height="120">
                <circle cx="60" cy="60" r="54" fill="none" stroke="#2d2d4a" stroke-width="8" />
                <circle cx="60" cy="60" r="54" fill="none" stroke="#409eff" stroke-width="8"
                  :stroke-dasharray="339.292"
                  :stroke-dashoffset="339.292 - (339.292 * (result.overall_accuracy * 100) / 100)"
                  stroke-linecap="round" transform="rotate(-90, 60, 60)" />
              </svg>
              <div class="score-text">
                <span class="score-num">{{ (result.overall_accuracy * 100).toFixed(0) }}%</span>
                <span class="score-label">正确率</span>
              </div>
            </div>
          </div>

          <div class="result-subjects">
            <div v-for="r in result.results" :key="r.subject" class="result-subject-card">
              <div class="rs-name">{{ r.subject_name }}</div>
              <div class="rs-accuracy" :class="r.accuracy >= 0.7 ? 'high' : r.accuracy >= 0.4 ? 'mid' : 'low'">
                {{ (r.accuracy * 100).toFixed(0) }}%
              </div>
              <div class="rs-bar">
                <div class="rs-fill" :style="{ width: (r.accuracy * 100) + '%', background: r.accuracy >= 0.7 ? '#67c23a' : r.accuracy >= 0.4 ? '#e6a23c' : '#f56c6c' }" />
              </div>
              <div v-if="r.weak_topics && r.weak_topics.length" class="rs-weak">
                薄弱: {{ r.weak_topics.join('、') }}
              </div>
            </div>
          </div>

          <div class="result-recommendation">
            <p>{{ result.overall_recommendation }}</p>
          </div>

          <div class="result-actions">
            <el-button type="primary" size="large" @click="goToLearningPath">
              <el-icon><arrow-right /></el-icon> 查看学习路径
            </el-button>
            <el-button size="large" @click="goToDashboard">
              进入控制台
            </el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { diagnosticApi } from '@/api'
import AppHeader from '@/components/AppHeader.vue'
import SkeletonCard from '@/components/SkeletonCard.vue'

const router = useRouter()
const questions = ref<any[]>([])
const answers = reactive<Record<string, string>>({})
const step = ref<'start' | 'doing' | 'result'>('start')
const result = ref<any>(null)
const loading = ref(true)
const submitting = ref(false)

const subjects = [
  { key: 'computer_basics', label: '💻 计算机基础' },
  { key: 'communication', label: '💬 沟通能力' },
  { key: 'problem_solving', label: '🧩 问题解决' },
  { key: 'data_analysis', label: '📊 数据分析' },
]

const answerCount = computed(() => Object.keys(answers).length)
const progressPercent = computed(() => {
  return questions.value.length > 0 ? (answerCount.value / questions.value.length) * 100 : 0
})

function subjectDone(key: string): boolean {
  return questions.value.filter((q: any) => q.subject === key).every((q: any) => answers[q.id])
}

function selectAnswer(qId: string, option: string) {
  answers[qId] = option
}

function submit() {
  submitting.value = true
  diagnosticApi.submit({ ...answers })
    .then((res: any) => {
      result.value = res.data
      step.value = 'result'
    })
    .catch((err: any) => {
      ElMessage.error('提交失败，请重试')
    })
    .finally(() => {
      submitting.value = false
    })
}

function goToLearningPath() {
  router.push('/dashboard')
}

function goToDashboard() {
  router.push('/dashboard')
}

onMounted(async () => {
  try {
    const res = await diagnosticApi.start()
    questions.value = res.data || []
    step.value = 'doing'
  } catch (err) {
    console.error('获取诊断题失败:', err)
    ElMessage.error('加载诊断题失败')
  } finally {
    loading.value = false
  }
  loading.value = false
})
</script>

<style scoped>
.page-content { padding: 32px 48px; max-width: 800px; margin: 0 auto; }
.diag-header { text-align: center; margin-bottom: 32px; }
.diag-header h1 { font-size: 28px; margin-bottom: 8px; }
.diag-desc { color: #a0a0a0; font-size: 14px; }
.diag-loading { max-width: 600px; margin: 0 auto; }
.diag-progress-bar { display: flex; align-items: center; gap: 16px; margin-bottom: 20px; }
.diag-progress-bar .el-progress { flex: 1; }
.progress-text { font-size: 14px; color: #409eff; white-space: nowrap; }
.diag-subjects { display: flex; gap: 8px; margin-bottom: 24px; flex-wrap: wrap; }
.subject-tag { padding: 6px 14px; border-radius: 16px; font-size: 13px; background: rgba(255,255,255,0.04); color: #a0a0a0; }
.subject-tag.done { background: rgba(103,194,58,0.1); color: #67c23a; }
.diag-question-card { padding: 20px; margin-bottom: 16px; }
.q-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 4px; }
.q-number { font-size: 12px; color: #666; }
.q-subject { font-size: 12px; color: #409eff; margin-bottom: 8px; }
.q-text { font-size: 15px; font-weight: 500; margin-bottom: 16px; line-height: 1.5; }
.q-options { display: flex; flex-direction: column; gap: 8px; }
.q-option { display: flex; align-items: center; gap: 12px; padding: 12px 16px; border-radius: 8px; border: 1px solid rgba(255,255,255,0.08); cursor: pointer; transition: all 0.2s; font-size: 14px; }
.q-option:hover { border-color: #409eff; }
.q-option.selected { border-color: #409eff; background: rgba(64,158,255,0.08); }
.option-radio { width: 18px; height: 18px; border-radius: 50%; border: 2px solid #555; display: flex; align-items: center; justify-content: center; flex-shrink: 0; }
.q-option.selected .option-radio { border-color: #409eff; }
.radio-dot { width: 8px; height: 8px; border-radius: 50%; background: #409eff; }
.diag-actions { text-align: center; margin-top: 24px; }

/* 结果 */
.result-card { text-align: center; padding: 48px; }
.result-icon { font-size: 64px; margin-bottom: 16px; }
.result-card h2 { margin-bottom: 24px; }
.result-score { margin-bottom: 32px; }
.score-ring { position: relative; width: 120px; height: 120px; margin: 0 auto; }
.score-text { position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%); text-align: center; }
.score-num { font-size: 28px; font-weight: 700; display: block; }
.score-label { font-size: 12px; color: #a0a0a0; }
.result-subjects { display: grid; grid-template-columns: 1fr 1fr; gap: 12px; margin-bottom: 24px; }
.result-subject-card { padding: 16px; background: rgba(255,255,255,0.03); border-radius: 8px; }
.rs-name { font-size: 14px; font-weight: 500; margin-bottom: 4px; }
.rs-accuracy { font-size: 20px; font-weight: 700; margin-bottom: 8px; }
.rs-accuracy.high { color: #67c23a; }
.rs-accuracy.mid { color: #e6a23c; }
.rs-accuracy.low { color: #f56c6c; }
.rs-bar { height: 4px; background: rgba(255,255,255,0.06); border-radius: 2px; overflow: hidden; margin-bottom: 8px; }
.rs-fill { height: 100%; border-radius: 2px; }
.rs-weak { font-size: 11px; color: #f56c6c; }
.result-recommendation { padding: 16px; background: rgba(64,158,255,0.06); border-radius: 8px; margin-bottom: 24px; }
.result-recommendation p { color: #a0a0a0; line-height: 1.6; }
.result-actions { display: flex; gap: 12px; justify-content: center; }
</style>