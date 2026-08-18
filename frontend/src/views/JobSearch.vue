<template>
  <div class="page">
    <AppHeader />
    <div class="page-content">
      <div class="page-header">
        <div>
          <h1>🎯 校招选岗</h1>
          <p class="page-desc">基于你的技能画像，AI 智能匹配适合你的校招职位</p>
        </div>
        <div class="header-actions">
          <el-button @click="router.push('/applications')">
            <el-icon><document /></el-icon> 我的网申
          </el-button>
        </div>
      </div>

      <!-- 搜索栏 -->
      <div class="search-bar">
        <el-input v-model="searchQuery" placeholder="搜索公司、职位、行业..." size="large" clearable
          @keyup.enter="doSearch" @clear="searchQuery = ''; doSearch()">
          <template #prefix><el-icon><search /></el-icon></template>
          <template #append>
            <el-button @click="doSearch">搜索</el-button>
          </template>
        </el-input>
      </div>

      <!-- AI 匹配推荐 -->
      <div class="section-label" v-if="userStore.isLoggedIn">
        <span class="label-icon">🤖</span> AI 智能匹配推荐
        <el-button text type="primary" size="small" @click="loadMatchResults" :loading="matching" style="margin-left:12px">
          {{ matching ? '匹配中...' : '刷新匹配' }}
        </el-button>
      </div>
      <div v-if="matchResults.length > 0" class="match-grid">
        <div v-for="(mr, i) in matchResults" :key="mr.job.id"
             class="glass-card match-card" :class="'rank-' + (i < 3 ? i : 3)"
             @click="router.push(`/recruitment/jobs/${mr.job.id}`)">
          <div class="match-rank" v-if="i < 3">
            <span class="rank-badge">{{ ['🥇','🥈','🥉'][i] }}</span>
          </div>
          <div class="match-header">
            <h3>{{ mr.job.title }}</h3>
            <div class="match-score" :class="mr.matchScore >= 80 ? 'high' : mr.matchScore >= 65 ? 'mid' : 'low'">
              <div class="score-ring">
                <svg viewBox="0 0 40 40" width="40" height="40">
                  <circle cx="20" cy="20" r="17" fill="none" stroke="rgba(255,255,255,0.1)" stroke-width="3" />
                  <circle cx="20" cy="20" r="17" fill="none"
                    :stroke="mr.matchScore >= 80 ? '#67c23a' : mr.matchScore >= 65 ? '#e6a23c' : '#f56c6c'"
                    stroke-width="3"
                    :stroke-dasharray="106.8"
                    :stroke-dashoffset="106.8 - (106.8 * mr.matchScore / 100)"
                    stroke-linecap="round" transform="rotate(-90, 20, 20)" />
                </svg>
                <span class="score-text">{{ Math.round(mr.matchScore) }}%</span>
              </div>
            </div>
          </div>
          <div class="match-company">
            <span class="company">{{ mr.job.company }}</span>
            <span class="location">{{ mr.job.location }}</span>
          </div>
          <p class="match-reason">{{ mr.reason }}</p>
          <div class="match-tags">
            <el-tag size="small">{{ mr.job.industry }}</el-tag>
            <el-tag size="small" type="info">{{ mr.job.degree }}</el-tag>
            <el-tag size="small" type="warning" v-if="mr.job.salary">{{ mr.job.salary }}</el-tag>
          </div>
        </div>
      </div>
      <div v-else-if="userStore.isLoggedIn && !matching" class="empty-match glass-card">
        <p>点击「刷新匹配」获取 AI 推荐职位</p>
      </div>

      <!-- 职位列表 -->
      <div class="section-label" style="margin-top:32px">
        <span class="label-icon">📋</span> 全部校招职位
      </div>
      <div v-if="loading" class="loading-grid">
        <SkeletonCard v-for="i in 6" :key="i" variant="card" />
      </div>
      <div v-else class="job-grid">
        <div v-for="job in jobs" :key="job.id"
             class="glass-card job-card" @click="router.push(`/recruitment/jobs/${job.id}`)">
          <div class="job-header">
            <div class="job-icon">{{ getIndustryIcon(job.industry) }}</div>
            <div class="job-info">
              <h3>{{ job.title }}</h3>
              <span class="job-company">{{ job.company }}</span>
            </div>
          </div>
          <div class="job-meta">
            <span v-if="job.location">📍 {{ job.location }}</span>
            <span v-if="job.salary">💰 {{ job.salary }}</span>
            <span v-if="job.degree">🎓 {{ job.degree }}</span>
          </div>
          <p class="job-desc">{{ job.description?.substring(0, 120) }}...</p>
          <div class="job-tags">
            <el-tag v-if="job.industry" size="small">{{ job.industry }}</el-tag>
            <el-tag v-if="job.major" size="small" type="info">{{ job.major }}</el-tag>
          </div>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-if="!loading && jobs.length === 0" class="empty-state glass-card">
        <el-icon :size="48" color="#a0a0a0"><briefcase /></el-icon>
        <p>暂无校招职位数据</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { recruitmentApi, assessmentApi } from '@/api'
import { useUserStore } from '@/stores/user'
import AppHeader from '@/components/AppHeader.vue'
import SkeletonCard from '@/components/SkeletonCard.vue'
import type { Job, JobMatchResult, AssessmentResult } from '@/types'

const router = useRouter()
const userStore = useUserStore()
const jobs = ref<Job[]>([])
const matchResults = ref<JobMatchResult[]>([])
const searchQuery = ref('')
const loading = ref(true)
const matching = ref(false)

function getIndustryIcon(industry?: string): string {
  const icons: Record<string, string> = {
    '互联网/IT': '💻', '金融': '🏦', '制造': '🏭',
    '教育': '📚', '医疗': '🏥', '房地产': '🏗️',
    '零售': '🛒', '咨询': '📊', '媒体': '📺',
  }
  return icons[industry || ''] || '🏢'
}

async function loadJobs() {
  loading.value = true
  try {
    const res = await recruitmentApi.getJobs()
    jobs.value = res.data || []
  } catch (err) {
    console.error('加载职位失败', err)
  } finally {
    loading.value = false
  }
}

async function loadMatchResults() {
  if (!userStore.isLoggedIn) return
  matching.value = true
  try {
    // 获取用户最新测评结果作为技能画像
    const res = await assessmentApi.getMyResults()
    const results = res.data || []
    if (results.length > 0) {
      const latest = results[results.length - 1]
      const matchRes = await recruitmentApi.matchJobs(latest.dimensionScores)
      matchResults.value = matchRes.data || []
    }
  } catch (err) {
    console.error('匹配失败', err)
    ElMessage.warning('请先完成一次技能测评，获取能力画像后再进行匹配')
  } finally {
    matching.value = false
  }
}

async function doSearch() {
  if (!searchQuery.value.trim()) {
    await loadJobs()
    return
  }
  loading.value = true
  try {
    const res = await recruitmentApi.searchJobs(searchQuery.value)
    jobs.value = res.data || []
  } catch (err) {
    console.error('搜索失败', err)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadJobs()
  if (userStore.isLoggedIn) {
    loadMatchResults()
  }
})
</script>

<style scoped>
.page-content { padding: 32px 48px; max-width: 1200px; margin: 0 auto; }
.page-header { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 24px; }
.page-header h1 { font-size: 28px; }
.page-desc { color: #a0a0a0; margin-top: 4px; }

.search-bar { margin-bottom: 32px; }

.section-label { font-size: 18px; font-weight: 600; margin-bottom: 20px; display: flex; align-items: center; gap: 8px; }
.label-icon { font-size: 20px; }

/* 匹配推荐 */
.match-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 20px; margin-bottom: 32px; }
.match-card { padding: 24px; cursor: pointer; position: relative; transition: all 0.3s; }
.match-card:hover { transform: translateY(-4px); box-shadow: 0 12px 30px rgba(64,158,255,0.15); }
.match-rank { position: absolute; top: -8px; left: -8px; }
.rank-badge { font-size: 28px; }
.match-header { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 12px; }
.match-header h3 { font-size: 16px; flex: 1; margin-right: 12px; }
.score-ring { position: relative; width: 40px; height: 40px; flex-shrink: 0; }
.score-text { position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%); font-size: 9px; font-weight: 700; }
.match-company { display: flex; justify-content: space-between; font-size: 13px; color: #a0a0a0; margin-bottom: 8px; }
.match-reason { font-size: 13px; color: #666; margin-bottom: 12px; }
.match-tags { display: flex; gap: 6px; flex-wrap: wrap; }

.loading-grid, .job-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 24px; }
.job-card { padding: 24px; cursor: pointer; transition: all 0.3s; }
.job-card:hover { transform: translateY(-4px); box-shadow: 0 12px 30px rgba(64,158,255,0.15); }
.job-header { display: flex; gap: 16px; margin-bottom: 12px; }
.job-icon { font-size: 36px; }
.job-info h3 { font-size: 16px; margin-bottom: 4px; }
.job-company { font-size: 13px; color: #409eff; }
.job-meta { display: flex; gap: 16px; font-size: 12px; color: #666; margin-bottom: 8px; }
.job-desc { font-size: 13px; color: #a0a0a0; line-height: 1.5; margin-bottom: 12px; }
.job-tags { display: flex; gap: 6px; }

.empty-match, .empty-state { text-align: center; padding: 48px; color: #a0a0a0; }

@media (max-width: 900px) {
  .match-grid, .loading-grid, .job-grid { grid-template-columns: 1fr; }
}
</style>