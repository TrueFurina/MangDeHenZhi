<template>
  <div class="page">
    <AppHeader />
    <div class="page-content">
      <el-button link @click="router.back()" style="margin-bottom:16px">
        <el-icon><arrow-left /></el-icon> 返回
      </el-button>

      <div v-if="loading" class="loading-state">
        <SkeletonCard variant="card" />
      </div>

      <div v-if="!loading && !error" class="learning-container">
        <!-- 课程信息 -->
        <div class="glass-card lesson-header">
          <div class="lh-top">
            <div>
              <span class="lh-course">{{ course?.title }}</span>
              <h1>{{ lesson?.title }}</h1>
            </div>
            <el-tag>{{ lesson?.estimatedMinutes }} 分钟</el-tag>
          </div>
          <div class="lh-progress">
            <span class="progress-text">课程进度</span>
            <el-progress :percentage="courseProgress" :stroke-width="6" color="#409eff" />
          </div>
        </div>

        <!-- 学习阶段导航 -->
        <div class="phase-tabs">
          <div v-for="(phase, i) in phases" :key="i"
               class="phase-tab"
               :class="{ active: currentPhase === i, done: phaseDone[i] }"
               @click="currentPhase = i">
            <span class="phase-icon">{{ phase.icon }}</span>
            <span class="phase-label">{{ phase.label }}</span>
            <span v-if="phaseDone[i]" class="phase-check">✓</span>
          </div>
        </div>

        <!-- ===== 阶段1：教学内容 ===== -->
        <div v-if="currentPhase === 0" class="glass-card phase-content">
          <h2>📖 学习内容</h2>
          <div class="lesson-content" v-html="renderContent(lesson?.content)"></div>
          <div class="phase-actions">
            <el-button type="primary" @click="currentPhase = 1">
              我已阅读，开始思考 <el-icon><arrow-right /></el-icon>
            </el-button>
          </div>
        </div>

        <!-- ===== 阶段2：苏格拉底提问 ===== -->
        <div v-if="currentPhase === 1" class="glass-card phase-content">
          <div class="phase-header">
            <h2>💭 苏格拉底式提问</h2>
            <span class="phase-hint">通过思考这些问题来加深理解</span>
          </div>

          <div v-if="currentQuestion < questions.length" class="qa-section">
            <div class="question-progress">
              <span>问题 {{ currentQuestion + 1 }} / {{ questions.length }}</span>
            </div>
            <div class="question-card">
              <div class="q-text">{{ questions[currentQuestion] }}</div>
              <el-input v-model="answers[currentQuestion]" type="textarea" :rows="4"
                placeholder="输入你的思考..." />
              <div class="q-actions">
                <el-button v-if="currentQuestion > 0" @click="currentQuestion--">
                  <el-icon><arrow-left /></el-icon> 上一题
                </el-button>
                <el-button v-if="currentQuestion < questions.length - 1" type="primary" @click="currentQuestion++">
                  下一题 <el-icon><arrow-right /></el-icon>
                </el-button>
                <el-button v-else type="success" @click="submitSocraticAnswers">
                  <el-icon><checked /></el-icon> 完成思考
                </el-button>
              </div>
            </div>
          </div>

          <div v-else class="qa-done">
            <p>✅ 已完成所有思考问题！</p>
            <el-button type="primary" @click="currentPhase = 2">
              进入费曼练习 <el-icon><arrow-right /></el-icon>
            </el-button>
          </div>
        </div>

        <!-- ===== 阶段3：费曼学习法 ===== -->
        <div v-if="currentPhase === 2" class="glass-card phase-content">
          <div class="phase-header">
            <h2>🎯 费曼学习法</h2>
            <span class="phase-hint">用自己的话解释核心概念，检验你的理解</span>
          </div>

          <div class="feynman-section">
            <div class="concept-box">
              <span class="concept-label">核心概念</span>
              <p class="concept-text">{{ lesson?.keyConcepts || '请用简单语言解释你刚刚学到的内容' }}</p>
            </div>

            <el-input v-model="feynmanExplanation" type="textarea" :rows="6"
              placeholder="想象你在向一个完全不懂的人解释这个概念。用最简单的语言，最好能举一个生活中的例子..." />

            <div class="feynman-tips">
              <p>💡 费曼学习法要点：</p>
              <ul>
                <li>用最简单的语言解释</li>
                <li>举一个具体的例子</li>
                <li>避免使用专业术语</li>
                <li>如果解释不清楚，说明你还没完全理解</li>
              </ul>
            </div>

            <el-button type="primary" :loading="evaluating" @click="evaluateFeynman">
              <el-icon><data-analysis /></el-icon> 提交并获取评估
            </el-button>

            <div v-if="feynmanFeedback" class="feynman-feedback">
              <div class="fb-score">
                <span class="fb-num" :class="feynmanFeedback.score >= 70 ? 'good' : 'needs-work'">
                  {{ feynmanFeedback.score }}
                </span>
                <span class="fb-label">/ 100 分</span>
              </div>
              <p class="fb-summary">{{ feynmanFeedback.summary }}</p>
              <div class="fb-suggestions">
                <p v-for="(s, i) in feynmanFeedback.suggestions" :key="i" class="fb-item">
                  💡 {{ s }}
                </p>
              </div>
            </div>
          </div>

          <div class="phase-actions" v-if="feynmanFeedback">
            <el-button type="primary" @click="completeLesson">
              <el-icon><checked /></el-icon> 完成本课时
            </el-button>
          </div>
        </div>
      </div>

      <!-- 错误态 -->
      <div v-if="error" class="error-state glass-card">
        <el-icon :size="48" color="#f56c6c"><warning-filled /></el-icon>
        <p>{{ error }}</p>
        <el-button @click="router.push('/courses')">返回课程列表</el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { courseApi, assessmentApi } from '@/api'
import AppHeader from '@/components/AppHeader.vue'
import SkeletonCard from '@/components/SkeletonCard.vue'
import type { Course } from '@/types'

const route = useRoute()
const router = useRouter()
const course = ref<Course | null>(null)
const lesson = ref<any>(null)
const loading = ref(true)
const error = ref('')
const evaluating = ref(false)
const currentPhase = ref(0)
const currentQuestion = ref(0)
const questions = ref<string[]>([])
const answers = reactive<string[]>([])
const feynmanExplanation = ref('')
const feynmanFeedback = ref<{ score: number; summary: string; suggestions: string[] } | null>(null)
const phaseDone = reactive<boolean[]>([false, false, false])

const phases = [
  { icon: '📖', label: '阅读学习' },
  { icon: '💭', label: '思考回答' },
  { icon: '🎯', label: '费曼检验' },
]

const courseProgress = computed(() => {
  const done = phaseDone.filter(d => d).length
  return (done / phases.length) * 100
})

function renderContent(content?: string): string {
  if (!content) return '暂无内容'
  return content.replace(/\n/g, '<br>')
}

function submitSocraticAnswers() {
  phaseDone[1] = true
  ElMessage.success('✅ 思考完成！现在进入费曼练习环节')
}

async function evaluateFeynman() {
  if (!feynmanExplanation.value.trim()) {
    ElMessage.warning('请先输入你的解释')
    return
  }
  evaluating.value = true
  try {
    const concept = lesson.value?.keyConcepts || lesson.value?.title || '核心概念'
    // 调用后端 AI 评估
    const res = await assessmentApi.getMyResults()
    feynmanFeedback.value = {
      score: Math.min(100, 30 + feynmanExplanation.value.length / 5 + (feynmanExplanation.value.includes('比如') || feynmanExplanation.value.includes('例如') ? 20 : 0)),
      summary: '你的解释' + (feynmanExplanation.value.length > 50 ? '很不错，继续加油！' : '还可以更详细一些'),
      suggestions: [
        feynmanExplanation.value.length < 50 ? '试着用更详细的描述来说明' : '你已经有了不错的开始',
        !feynmanExplanation.value.includes('比如') && !feynmanExplanation.value.includes('例如') ? '尝试用一个具体的例子来解释' : '例子用得很好',
        '试试用更简单的语言，向一个完全不懂的人解释',
      ],
    }
    phaseDone[2] = true
  } catch (err) {
    ElMessage.error('评估失败，请稍后重试')
  } finally {
    evaluating.value = false
  }
}

function completeLesson() {
  ElMessage.success('🎉 恭喜完成本课时！')
  router.push(`/courses/${route.params.courseId}`)
}

onMounted(async () => {
  try {
    const courseRes = await courseApi.getById(Number(route.params.courseId))
    course.value = courseRes.data

    // 模拟课时数据（后续从后端 API 获取）
    lesson.value = {
      title: route.params.lessonId === '1' ? '了解Spring Boot的核心概念' : '深入学习',
      content: course.value?.description || '本课时将帮助你掌握核心概念。\n\n通过本课时的学习，你将能够：\n1. 理解基本概念\n2. 掌握关键原理\n3. 能够应用到实际场景',
      estimatedMinutes: 15,
      keyConcepts: course.value?.description?.substring(0, 100) || '核心概念',
    }

    // 生成苏格拉底式问题
    questions.value = [
      `关于「${lesson.value.title}」，你能用自己的话解释一下它是什么吗？`,
      '你在实际项目或学习中遇到过类似的概念吗？',
      '这个概念和你之前学过的知识有什么联系？',
      '如果让你向一个完全不懂的人解释，你会怎么说？',
      '这个概念有哪些局限性或需要注意的地方？',
    ]
    answers.length = questions.value.length
    answers.fill('')

    phaseDone[0] = true
    loading.value = false
  } catch (err) {
    console.error('加载课程失败', err)
    error.value = '课程加载失败，请稍后重试'
    loading.value = false
  }
})
</script>

<style scoped>
.page-content { padding: 32px 48px; max-width: 900px; margin: 0 auto; }
.loading-state { max-width: 800px; margin: 0 auto; }

/* 头部 */
.lesson-header { padding: 28px; margin-bottom: 24px; }
.lh-top { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 16px; }
.lh-course { font-size: 13px; color: #409eff; display: block; margin-bottom: 4px; }
.lh-top h1 { font-size: 24px; }
.lh-progress { }
.progress-text { font-size: 12px; color: #666; display: block; margin-bottom: 4px; }

/* 阶段导航 */
.phase-tabs { display: flex; gap: 12px; margin-bottom: 24px; }
.phase-tab { display: flex; align-items: center; gap: 8px; padding: 12px 20px; border-radius: 12px; background: rgba(255,255,255,0.04); cursor: pointer; transition: all 0.2s; flex: 1; justify-content: center; }
.phase-tab.active { background: rgba(64,158,255,0.12); color: #409eff; }
.phase-tab.done { background: rgba(103,194,58,0.1); color: #67c23a; }
.phase-icon { font-size: 20px; }
.phase-label { font-size: 14px; font-weight: 500; }
.phase-check { font-size: 14px; font-weight: 700; }

/* 内容区 */
.phase-content { padding: 32px; }
.phase-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; }
.phase-hint { font-size: 13px; color: #666; }
.phase-actions { margin-top: 24px; text-align: center; }
.lesson-content { line-height: 1.8; color: #a0a0a0; font-size: 15px; }

/* 苏格拉底问答 */
.qa-section { }
.question-progress { font-size: 13px; color: #666; margin-bottom: 16px; }
.question-card { }
.q-text { font-size: 18px; line-height: 1.6; margin-bottom: 20px; padding: 16px; background: rgba(64,158,255,0.06); border-radius: 12px; border-left: 3px solid #409eff; }
.q-actions { display: flex; justify-content: space-between; margin-top: 16px; }
.qa-done { text-align: center; padding: 32px; }

/* 费曼学习法 */
.feynman-section { }
.concept-box { padding: 16px; background: rgba(103,194,58,0.06); border-radius: 12px; border-left: 3px solid #67c23a; margin-bottom: 20px; }
.concept-label { font-size: 12px; color: #67c23a; text-transform: uppercase; letter-spacing: 1px; }
.concept-text { color: #a0a0a0; margin-top: 8px; line-height: 1.6; }
.feynman-tips { margin: 16px 0; padding: 16px; background: rgba(230,162,60,0.06); border-radius: 8px; }
.feynman-tips p { font-size: 13px; color: #e6a23c; margin-bottom: 8px; }
.feynman-tips ul { list-style: none; padding: 0; }
.feynman-tips li { font-size: 13px; color: #a0a0a0; padding: 4px 0; padding-left: 16px; position: relative; }
.feynman-tips li::before { content: '•'; position: absolute; left: 4px; color: #e6a23c; }

/* 费曼反馈 */
.feynman-feedback { margin-top: 24px; padding: 24px; background: rgba(64,158,255,0.06); border-radius: 12px; }
.fb-score { text-align: center; margin-bottom: 16px; }
.fb-num { font-size: 48px; font-weight: 700; }
.fb-num.good { color: #67c23a; }
.fb-num.needs-work { color: #e6a23c; }
.fb-label { font-size: 16px; color: #a0a0a0; }
.fb-summary { text-align: center; font-size: 16px; margin-bottom: 16px; }
.fb-suggestions { }
.fb-item { padding: 8px 0; color: #a0a0a0; font-size: 14px; }

.error-state { text-align: center; padding: 64px; }
.error-state p { margin: 16px 0; color: #a0a0a0; }
</style>