<template>
  <div class="page">
    <AppHeader />
    <div class="page-content">
      <h1>🎤 AI 模拟面试</h1>
      <p class="page-desc">AI 面试官生成问题，你作答后获得评分与改进建议</p>

      <!-- 开始面试配置 -->
      <div v-if="step === 'setup'" class="glass-card setup-card">
        <el-form label-width="100px">
          <el-form-item label="目标职位">
            <el-input v-model="jobTitle" placeholder="如：Java 后端开发工程师" />
          </el-form-item>
          <el-form-item label="面试维度">
            <el-select v-model="dimension" placeholder="选择面试维度">
              <el-option label="技术能力" value="技术能力" />
              <el-option label="沟通能力" value="沟通能力" />
              <el-option label="问题解决" value="问题解决" />
              <el-option label="领导力" value="领导力" />
            </el-select>
          </el-form-item>
          <el-form-item label="题目数量">
            <el-radio-group v-model="count">
              <el-radio :value="2">2 题</el-radio>
              <el-radio :value="3">3 题</el-radio>
              <el-radio :value="5">5 题</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :loading="generating" @click="startInterview" style="width:100%">
              {{ generating ? 'AI 面试官生成题目中...' : '开始面试' }}
            </el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 答题 -->
      <div v-if="step === 'answering' && questions.length" class="glass-card answer-card">
        <div class="progress-info">第 {{ current + 1 }} / {{ questions.length }} 题</div>
        <h3 class="question-text">{{ questions[current].question }}</h3>
        <div v-if="questions[current].expectedPoints?.length" class="expected-points">
          <span class="ep-label">考察要点：</span>
          <el-tag v-for="(p, i) in questions[current].expectedPoints" :key="i" size="small" style="margin:2px">{{ p }}</el-tag>
        </div>
        <el-input v-model="answer" type="textarea" :rows="6" placeholder="输入你的回答..." />
        <div class="nav-buttons">
          <el-button :disabled="current === 0" @click="current--">上一题</el-button>
          <el-button v-if="current < questions.length - 1" type="primary" @click="current++">下一题</el-button>
          <el-button v-else type="success" :loading="evaluating" @click="submitAnswer">提交并评估</el-button>
        </div>
      </div>

      <!-- AI 评估结果 -->
      <div v-if="step === 'result' && evaluation" class="glass-card result-card">
        <div class="result-header">
          <span class="score-badge" :class="scoreClass">{{ evaluation.score }} 分</span>
          <h3>🤖 AI 评分官评估</h3>
        </div>
        <div v-if="evaluation.strengths?.length" class="sec">
          <h4>💪 优点</h4>
          <ul><li v-for="(s, i) in evaluation.strengths" :key="i">{{ s }}</li></ul>
        </div>
        <div v-if="evaluation.weaknesses?.length" class="sec">
          <h4>📈 不足</h4>
          <ul><li v-for="(w, i) in evaluation.weaknesses" :key="i">{{ w }}</li></ul>
        </div>
        <div v-if="evaluation.suggestion" class="sec">
          <h4>💡 改进建议</h4>
          <p>{{ evaluation.suggestion }}</p>
        </div>
        <div v-if="evaluation.sampleBetterAnswer" class="sec">
          <h4>🎯 更好的回答方向</h4>
          <p class="better-answer">{{ evaluation.sampleBetterAnswer }}</p>
        </div>
        <div class="nav-buttons">
          <el-button type="primary" @click="reset">再来一次</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { interviewApi } from '@/api'
import AppHeader from '@/components/AppHeader.vue'

const step = ref<'setup' | 'answering' | 'result'>('setup')
const jobTitle = ref('')
const dimension = ref('技术能力')
const count = ref(3)
const generating = ref(false)
const evaluating = ref(false)
const questions = ref<any[]>([])
const current = ref(0)
const answer = ref('')
const evaluation = ref<any>(null)

const scoreClass = computed(() => {
  const s = evaluation.value?.score || 0
  return s >= 80 ? 'high' : s >= 60 ? 'mid' : 'low'
})

async function startInterview() {
  if (!jobTitle.value.trim()) {
    ElMessage.warning('请输入目标职位')
    return
  }
  generating.value = true
  try {
    const res = await interviewApi.generateQuestions(jobTitle.value, dimension.value, count.value)
    const parsed = JSON.parse(res.data)
    questions.value = parsed.questions || []
    if (!questions.value.length) throw new Error('empty')
    step.value = 'answering'
    current.value = 0
    answer.value = ''
  } catch (_) {
    ElMessage.warning('AI 面试官生成题目失败，请稍后再试')
  } finally {
    generating.value = false
  }
}

async function submitAnswer() {
  if (!answer.value.trim()) {
    ElMessage.warning('请先输入你的回答')
    return
  }
  evaluating.value = true
  try {
    const res = await interviewApi.evaluate(questions.value[current.value].question, answer.value)
    evaluation.value = JSON.parse(res.data)
    step.value = 'result'
  } catch (_) {
    ElMessage.warning('AI 评估失败，请稍后再试')
  } finally {
    evaluating.value = false
  }
}

function reset() {
  step.value = 'setup'
  questions.value = []
  evaluation.value = null
  answer.value = ''
}
</script>

<style scoped>
.page-content { padding: 32px 48px; max-width: 800px; margin: 0 auto; }
.page-desc { color: #a0a0a0; margin-bottom: 24px; }
.setup-card, .answer-card, .result-card { padding: 28px; }
.progress-info { color: #a0a0a0; font-size: 14px; margin-bottom: 12px; }
.question-text { font-size: 18px; margin-bottom: 16px; }
.expected-points { margin-bottom: 16px; font-size: 13px; }
.ep-label { color: #a0a0a0; }
.nav-buttons { display: flex; justify-content: space-between; margin-top: 16px; }
.result-header { display: flex; align-items: center; gap: 16px; margin-bottom: 16px; }
.score-badge {
  font-size: 20px; font-weight: 700; padding: 8px 16px; border-radius: 12px;
}
.score-badge.high { background: rgba(103,194,58,0.15); color: #67c23a; }
.score-badge.mid { background: rgba(230,162,60,0.15); color: #e6a23c; }
.score-badge.low { background: rgba(245,108,108,0.15); color: #f56c6c; }
.sec { margin-top: 16px; }
.sec h4 { margin-bottom: 8px; }
.sec ul { padding-left: 20px; }
.sec li { line-height: 1.8; color: #a0a0a0; }
.sec p { color: #a0a0a0; line-height: 1.8; }
.better-answer { background: rgba(64,158,255,0.08); padding: 12px; border-radius: 8px; }
</style>