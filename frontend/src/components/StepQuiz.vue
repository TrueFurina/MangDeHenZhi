<template>
  <div class="step-quiz">
    <div class="quiz-header">
      <h3>📝 分步测验</h3>
      <span class="quiz-progress">{{ currentStep + 1 }} / {{ steps.length }}</span>
    </div>
    <el-progress :percentage="progressPercent" :stroke-width="6" color="#409eff" style="margin-bottom:16px" />

    <div class="step-card glass-card" v-for="(step, i) in filteredSteps" :key="i">
      <div class="step-number">第 {{ i + 1 }} 步</div>
      <div class="step-content">
        <p>{{ step.question }}</p>
        <div class="step-options">
          <div v-for="opt in step.options" :key="opt"
               class="step-option"
               :class="{ selected: answers[step.id] === opt }"
               @click="selectAnswer(step.id, opt)">
            {{ opt }}
          </div>
        </div>
      </div>
    </div>

    <div class="quiz-actions">
      <el-button v-if="currentStep > 0" @click="prevStep">上一步</el-button>
      <el-button v-if="currentStep < steps.length - 1" type="primary" @click="nextStep">下一步</el-button>
      <el-button v-else type="success" @click="submitQuiz">完成</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, reactive } from 'vue'
import { ElMessage } from 'element-plus'

const currentStep = ref(0)
const answers = reactive<Record<string, string>>({})

const steps = [
  { id: 'q1', question: 'TCP/IP协议分为几层？', options: ['4层', '5层', '7层', '6层'] },
  { id: 'q2', question: '以下哪个是面向连接的协议？', options: ['UDP', 'TCP', 'IP', 'ICMP'] },
  { id: 'q3', question: 'HTTP协议默认使用哪个端口？', options: ['21', '80', '443', '8080'] },
  { id: 'q4', question: 'IP地址192.168.1.1属于哪类地址？', options: ['A类', 'B类', 'C类', 'D类'] },
  { id: 'q5', question: 'DNS的主要功能是什么？', options: ['IP分配', '域名解析', '路由选择', '数据加密'] },
]

const filteredSteps = computed(() => steps.slice(currentStep.value, currentStep.value + 1))
const progressPercent = computed(() => ((currentStep.value + 1) / steps.length) * 100)

function selectAnswer(id: string, opt: string) { answers[id] = opt }
function nextStep() { if (currentStep.value < steps.length - 1) currentStep.value++ }
function prevStep() { if (currentStep.value > 0) currentStep.value-- }
function submitQuiz() {
  const answered = Object.keys(answers).length
  ElMessage.success(`✅ 完成！共回答 ${answered}/${steps.length} 题`)
}
</script>

<style scoped>
.quiz-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px; }
.quiz-progress { font-size: 13px; color: #409eff; }
.step-card { padding: 24px; margin-bottom: 16px; }
.step-number { font-size: 12px; color: #666; margin-bottom: 8px; }
.step-content p { font-size: 16px; margin-bottom: 16px; }
.step-options { display: flex; flex-direction: column; gap: 8px; }
.step-option { padding: 12px 16px; border-radius: 8px; border: 1px solid rgba(255,255,255,0.08); cursor: pointer; font-size: 14px; }
.step-option:hover { border-color: #409eff; }
.step-option.selected { border-color: #409eff; background: rgba(64,158,255,0.08); }
.quiz-actions { display: flex; justify-content: space-between; margin-top: 16px; }
</style>