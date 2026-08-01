<template>
  <div class="page">
    <AppHeader />
    <div class="page-content">
      <el-button link @click="router.push('/recruitment')" style="margin-bottom:16px">
        <el-icon><arrow-left /></el-icon> 返回职位列表
      </el-button>

      <div v-if="!job" class="loading-state">
        <SkeletonCard variant="card" />
      </div>

      <div v-if="job" class="job-detail">
        <!-- 职位头部 -->
        <div class="glass-card detail-header">
          <div class="header-icon">{{ getIndustryIcon(job.industry) }}</div>
          <div class="header-info">
            <h1>{{ job.title }}</h1>
            <div class="header-company">{{ job.company }}</div>
            <div class="header-meta">
              <span v-if="job.location">📍 {{ job.location }}</span>
              <span v-if="job.salary">💰 {{ job.salary }}</span>
              <span v-if="job.degree">🎓 {{ job.degree }}</span>
              <span v-if="job.major">📚 {{ job.major }}</span>
            </div>
            <div class="header-tags">
              <el-tag>{{ job.industry }}</el-tag>
              <el-tag type="info">{{ job.source || '公开信息' }}</el-tag>
            </div>
          </div>
          <div class="header-actions">
            <el-button type="primary" size="large" @click="startApplication" :loading="applying">
              <el-icon><edit /></el-icon> AI 辅助网申
            </el-button>
            <el-button size="large" @click="analyzeFit" :loading="analyzing">
              <el-icon><data-analysis /></el-icon> 匹配分析
            </el-button>
          </div>
        </div>

        <!-- 匹配度分析 -->
        <div v-if="fitAnalysis" class="glass-card fit-card">
          <h3>📊 匹配度分析</h3>
          <p class="fit-text">{{ fitAnalysis }}</p>
        </div>

        <!-- 职位描述 -->
        <div class="glass-card detail-section">
          <h3>📝 职位描述</h3>
          <div class="section-content" v-html="renderText(job.description)"></div>
        </div>

        <!-- 任职要求 -->
        <div class="glass-card detail-section">
          <h3>📋 任职要求</h3>
          <div class="section-content" v-html="renderText(job.requirements)"></div>
        </div>

        <!-- 操作 -->
        <div class="detail-actions">
          <el-button type="primary" size="large" @click="startApplication" :loading="applying">
            <el-icon><edit /></el-icon> AI 辅助网申
          </el-button>
          <el-button size="large" v-if="job.applyUrl" @click="openApplyUrl">
            <el-icon><link /></el-icon> 官网投递
          </el-button>
          <el-button size="large" @click="router.push('/recruitment')">
            <el-icon><search /></el-icon> 浏览更多
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { recruitmentApi } from '@/api'
import AppHeader from '@/components/AppHeader.vue'
import SkeletonCard from '@/components/SkeletonCard.vue'
import type { Job } from '@/types'

const route = useRoute()
const router = useRouter()
const job = ref<Job | null>(null)
const applying = ref(false)
const analyzing = ref(false)
const fitAnalysis = ref('')

function getIndustryIcon(industry?: string): string {
  const icons: Record<string, string> = {
    '互联网/IT': '💻', '金融': '🏦', '制造': '🏭',
    '教育': '📚', '医疗': '🏥', '房地产': '🏗️',
  }
  return icons[industry || ''] || '🏢'
}

function renderText(text?: string | string[]): string {
  if (!text) return ''
  const raw = Array.isArray(text) ? text.join('\n') : text
  return raw.replace(/\n/g, '<br>')
}

async function startApplication() {
  if (!job.value) return
  applying.value = true
  try {
    const res = await recruitmentApi.createApplication(job.value.id)
    ElMessage.success('🎉 网申已创建，正在跳转填报助手...')
    router.push(`/applications/${res.data.id}/edit`)
  } catch (err: any) {
    ElMessage.error(err.response?.data?.message || '创建失败')
  } finally {
    applying.value = false
  }
}

async function analyzeFit() {
  if (!job.value) return
  analyzing.value = true
  try {
    const desc = (job.value.description || '') + '\n' + (job.value.requirements || '')
    const res = await recruitmentApi.analyzeResume('应届生校招简历', desc)
    fitAnalysis.value = res.data
  } catch (err) {
    ElMessage.warning('匹配分析暂不可用')
  } finally {
    analyzing.value = false
  }
}

function openApplyUrl() {
  if (job.value?.applyUrl) {
    window.open(job.value.applyUrl, '_blank')
  }
}

onMounted(async () => {
  try {
    const res = await recruitmentApi.getJob(Number(route.params.id))
    job.value = res.data
  } catch (err) {
    console.error('加载职位详情失败', err)
    ElMessage.error('职位不存在')
  }
})
</script>

<style scoped>
.page-content { padding: 32px 48px; max-width: 1000px; margin: 0 auto; }
.loading-state { max-width: 800px; margin: 0 auto; }

.detail-header { display: flex; gap: 24px; padding: 32px; align-items: flex-start; }
.header-icon { font-size: 48px; flex-shrink: 0; }
.header-info { flex: 1; }
.header-info h1 { font-size: 24px; margin-bottom: 8px; }
.header-company { font-size: 16px; color: #409eff; margin-bottom: 12px; }
.header-meta { display: flex; gap: 16px; font-size: 13px; color: #a0a0a0; margin-bottom: 12px; flex-wrap: wrap; }
.header-tags { display: flex; gap: 8px; }
.header-actions { display: flex; flex-direction: column; gap: 8px; flex-shrink: 0; }

.fit-card { margin-top: 16px; padding: 24px; }
.fit-text { color: #a0a0a0; line-height: 1.6; margin-top: 12px; }

.detail-section { margin-top: 16px; padding: 32px; }
.section-content { color: #a0a0a0; line-height: 1.8; font-size: 14px; margin-top: 16px; }

.detail-actions { display: flex; gap: 16px; justify-content: center; margin-top: 32px; flex-wrap: wrap; }
</style>