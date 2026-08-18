<template>
  <div class="page">
    <AppHeader />
    <div class="page-content">
      <el-button link @click="router.push('/applications')" style="margin-bottom:16px">
        <el-icon><arrow-left /></el-icon> 返回我的网申
      </el-button>

      <div class="glass-card" v-if="application">
        <div class="app-header">
          <div>
            <h1>{{ application.companyName }}</h1>
            <p class="app-position">{{ application.positionName }}</p>
          </div>
          <el-tag :type="statusType" size="large" effect="dark">{{ statusLabel }}</el-tag>
        </div>

        <!-- 填报表单 -->
        <div class="form-section">
          <h3>📝 网申信息填写</h3>
          <p class="form-desc">AI 智能助手已为你生成填写建议，你可以参考或直接采用</p>

          <div v-for="(field, idx) in formFields" :key="field.key" class="form-item">
            <div class="form-label">
              <span>{{ field.label }}</span>
              <el-button v-if="suggestions[field.key]" text type="primary" size="small"
                @click="applySuggestion(field.key)">
                使用 AI 建议
              </el-button>
            </div>
            <el-input v-if="field.type === 'textarea'"
              v-model="formData[field.key]" type="textarea" :rows="4"
              :placeholder="field.placeholder" />
            <el-input v-else
              v-model="formData[field.key]" :placeholder="field.placeholder" />
            <div v-if="suggestions[field.key]" class="suggestion-box">
              <span class="suggestion-label">🤖 AI 建议：</span>
              <span class="suggestion-text">{{ suggestions[field.key] }}</span>
            </div>
          </div>

          <div class="form-actions">
            <el-button type="primary" size="large" @click="generateSuggestions" :loading="generating">
              <el-icon><magic-stick /></el-icon> 生成 AI 填报建议
            </el-button>
            <el-button size="large" @click="submitApplication" :loading="submitting">
              <el-icon><checked /></el-icon> 标记为已提交
            </el-button>
          </div>
        </div>

        <!-- 简历分析 -->
        <div class="resume-section">
          <h3>📄 简历匹配分析</h3>
          <el-input v-model="resumeText" type="textarea" :rows="4" placeholder="粘贴你的简历或自我介绍..." />
          <el-button style="margin-top:12px" @click="analyzeResume" :loading="analyzing">
            <el-icon><data-analysis /></el-icon> 分析匹配度
          </el-button>
          <div v-if="resumeAnalysis" class="analysis-result">
            <p>{{ resumeAnalysis }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { recruitmentApi, assessmentApi } from '@/api'
import AppHeader from '@/components/AppHeader.vue'
import type { Application } from '@/types'

const route = useRoute()
const router = useRouter()
const application = ref<Application | null>(null)
const generating = ref(false)
const submitting = ref(false)
const analyzing = ref(false)
const resumeText = ref('')
const resumeAnalysis = ref('')
const suggestions = reactive<Record<string, string>>({})

const formFields = [
  { key: 'self_intro', label: '自我介绍', type: 'textarea', placeholder: '简要介绍自己的优势和求职意向...' },
  { key: 'strengths', label: '核心优势', type: 'textarea', placeholder: '列举你的核心竞争力和特长...' },
  { key: 'projects', label: '项目经历', type: 'textarea', placeholder: '描述相关项目经验和成果...' },
  { key: 'career_plan', label: '职业规划', type: 'textarea', placeholder: '你的短期和长期职业规划...' },
  { key: 'expected_city', label: '期望城市', type: 'input', placeholder: '如：北京、上海、深圳...' },
  { key: 'available_date', label: '可到岗时间', type: 'input', placeholder: '如：2026年7月' },
]

const formData = reactive<Record<string, string>>({})

const statusType = computed(() => {
  const map: Record<string, string> = { DRAFT: 'info', SUBMITTED: 'success', REJECTED: 'danger', ACCEPTED: 'success' }
  return map[application.value?.status || 'DRAFT']
})
const statusLabel = computed(() => {
  const map: Record<string, string> = { DRAFT: '草稿', SUBMITTED: '已投递', REJECTED: '未通过', ACCEPTED: '已录取' }
  return map[application.value?.status || 'DRAFT']
})

async function generateSuggestions() {
  generating.value = true
  try {
    const res = await assessmentApi.getMyResults()
    const results = res.data || []
    const scores = results.length > 0 ? results[results.length - 1].dimensionScores : {}

    const result = await recruitmentApi.getSuggestions(Number(route.params.id), {
      jobDescription: application.value?.positionName || '',
      skillScores: scores,
      formFields: formFields.map(f => f.key),
    })
    Object.assign(suggestions, result.data)
    ElMessage.success('✅ AI 建议已生成')
  } catch (err) {
    ElMessage.warning('AI 建议生成失败，使用默认模板')
    formFields.forEach(f => {
      suggestions[f.key] = `建议填写与${application.value?.positionName || '目标职位'}相关的内容，突出你的匹配度。`
    })
  } finally {
    generating.value = false
  }
}

function applySuggestion(key: string) {
  if (suggestions[key]) {
    formData[key] = suggestions[key]
    ElMessage.success(`已应用「${formFields.find(f => f.key === key)?.label}」建议`)
  }
}

async function submitApplication() {
  submitting.value = true
  try {
    await recruitmentApi.updateApplicationStatus(Number(route.params.id), 'SUBMITTED')
    ElMessage.success('🎉 已标记为已投递！')
    router.push('/applications')
  } catch (err) {
    ElMessage.error('更新失败')
  } finally {
    submitting.value = false
  }
}

async function analyzeResume() {
  if (!resumeText.value.trim()) {
    ElMessage.warning('请先输入简历内容')
    return
  }
  analyzing.value = true
  try {
    const res = await recruitmentApi.analyzeResume(
      resumeText.value,
      `${application.value?.companyName} - ${application.value?.positionName}`
    )
    resumeAnalysis.value = res.data
  } catch (err) {
    ElMessage.error('分析失败')
  } finally {
    analyzing.value = false
  }
}

onMounted(async () => {
  try {
    const res = await recruitmentApi.getMyApplications()
    const apps = res.data || []
    application.value = apps.find(a => String(a.id) === route.params.id) || null
    if (!application.value) {
      ElMessage.error('申请不存在')
      router.push('/applications')
    }
  } catch (err) {
    console.error('加载申请失败', err)
  }
})
</script>

<style scoped>
.page-content { padding: 32px 48px; max-width: 900px; margin: 0 auto; }
.app-header { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 32px; }
.app-header h1 { font-size: 24px; }
.app-position { color: #a0a0a0; margin-top: 4px; }
.form-section, .resume-section { margin-top: 32px; padding-top: 24px; border-top: 1px solid rgba(255,255,255,0.06); }
.form-desc { color: #a0a0a0; font-size: 14px; margin: 8px 0 24px; }
.form-item { margin-bottom: 24px; }
.form-label { display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px; font-size: 14px; font-weight: 500; }
.suggestion-box { margin-top: 8px; padding: 10px 14px; background: rgba(64,158,255,0.06); border-radius: 8px; border-left: 3px solid #409eff; }
.suggestion-label { font-size: 12px; color: #409eff; }
.suggestion-text { font-size: 13px; color: #a0a0a0; line-height: 1.5; }
.form-actions { display: flex; gap: 16px; margin-top: 32px; }
.analysis-result { margin-top: 12px; padding: 16px; background: rgba(103,194,58,0.06); border-radius: 8px; border-left: 3px solid #67c23a; color: #a0a0a0; line-height: 1.6; }
</style>