<template>
  <div class="page">
    <AppHeader />
    <div class="page-content">
      <div class="page-header">
        <h1>🏪 技能市场</h1>
        <p class="page-desc">基于行业大数据的技能需求趋势，发现高价值技能</p>
      </div>

      <!-- 搜索与筛选 -->
      <div class="market-toolbar">
        <el-input v-model="searchQuery" placeholder="搜索技能..." size="large" clearable
          @input="debouncedSearch" class="search-input">
          <template #prefix><el-icon><search /></el-icon></template>
        </el-input>
        <el-select v-model="selectedCategory" placeholder="全部分类" size="large" @change="doSearch">
          <el-option label="全部分类" value="" />
          <el-option v-for="c in categories" :key="c.value" :label="c.label" :value="c.value" />
        </el-select>
        <el-select v-model="sortBy" size="large" @change="doSearch">
          <el-option label="需求最高" value="demand" />
          <el-option label="最稀缺" value="scarcity" />
          <el-option label="增长最快" value="growth" />
        </el-select>
      </div>

      <!-- 统计卡片 -->
      <div class="market-stats">
        <div class="stat-card glass-card">
          <span class="stat-num">{{ skills.length }}</span>
          <span class="stat-lbl">技能总数</span>
        </div>
        <div class="stat-card glass-card">
          <span class="stat-num" style="color:#f56c6c">{{ highDemandCount }}</span>
          <span class="stat-lbl">高需求技能</span>
        </div>
        <div class="stat-card glass-card">
          <span class="stat-num" style="color:#e6a23c">{{ highScarcityCount }}</span>
          <span class="stat-lbl">稀缺技能</span>
        </div>
        <div class="stat-card glass-card">
          <span class="stat-num" style="color:#67c23a">{{ trendingCount }}</span>
          <span class="stat-lbl">快速增长</span>
        </div>
      </div>

      <!-- 技能网格 -->
      <div v-if="loading" class="loading-grid">
        <SkeletonCard v-for="i in 6" :key="i" variant="card" />
      </div>
      <div v-else-if="filteredSkills.length > 0" class="skill-grid">
        <div v-for="skill in filteredSkills" :key="skill.id || skill.skillName" class="glass-card skill-card">
          <div class="skill-top">
            <div class="skill-icon">{{ getCategoryIcon(skill.category) }}</div>
            <div class="skill-info">
              <h3>{{ skill.skillName }}</h3>
              <span class="skill-category">{{ categoryLabel(skill.category) }}</span>
            </div>
            <el-tag v-if="skill.trend === 'UP'" size="small" type="success" effect="dark">🔥 热门</el-tag>
          </div>
          <div class="skill-bars">
            <div class="bar-row">
              <span class="bar-label">需求</span>
              <div class="bar-track"><div class="bar-fill demand" :style="{ width: skill.demandIndex + '%' }" /></div>
              <span class="bar-val">{{ skill.demandIndex }}%</span>
            </div>
            <div class="bar-row">
              <span class="bar-label">稀缺</span>
              <div class="bar-track"><div class="bar-fill scarcity" :style="{ width: skill.scarcityIndex + '%' }" /></div>
              <span class="bar-val">{{ skill.scarcityIndex }}%</span>
            </div>
            <div class="bar-row">
              <span class="bar-label">增长</span>
              <div class="bar-track"><div class="bar-fill growth" :style="{ width: Math.min(skill.growthRate * 2, 100) + '%' }" /></div>
              <span class="bar-val">+{{ skill.growthRate }}%</span>
            </div>
          </div>
          <p class="skill-desc">{{ skill.description }}</p>
          <div class="skill-courses" v-if="skill.relatedCourses">
            <span v-for="c in parseCourses(skill.relatedCourses)" :key="c" class="course-chip" @click="router.push('/courses')">{{ c }}</span>
          </div>
        </div>
      </div>
      <div v-else class="empty-state glass-card">
        <el-icon :size="48" color="#a0a0a0"><search /></el-icon>
        <p>没有找到匹配的技能</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import http from '@/api'
import AppHeader from '@/components/AppHeader.vue'
import SkeletonCard from '@/components/SkeletonCard.vue'

const router = useRouter()
const skills = ref<any[]>([])
const loading = ref(true)
const searchQuery = ref('')
const selectedCategory = ref('')
const sortBy = ref('demand')
let searchTimer: any = null

const categories = [
  { value: 'AI', label: '🤖 AI' },
  { value: 'TECHNOLOGY', label: '💻 技术' },
  { value: 'SOFT_SKILLS', label: '💬 软技能' },
  { value: 'BUSINESS', label: '💼 商业' },
  { value: 'DESIGN', label: '🎨 设计' },
  { value: 'LANGUAGE', label: '🌍 语言' },
]

const highDemandCount = computed(() => skills.value.filter(s => s.demandIndex >= 85).length)
const highScarcityCount = computed(() => skills.value.filter(s => s.scarcityIndex >= 80).length)
const trendingCount = computed(() => skills.value.filter(s => s.growthRate >= 20).length)

const filteredSkills = computed(() => {
  let result = [...skills.value]

  if (searchQuery.value) {
    const q = searchQuery.value.toLowerCase()
    result = result.filter(s => s.skillName.toLowerCase().includes(q))
  }
  if (selectedCategory.value) {
    result = result.filter(s => s.category === selectedCategory.value)
  }
  if (sortBy.value === 'demand') {
    result.sort((a, b) => b.demandIndex - a.demandIndex)
  } else if (sortBy.value === 'scarcity') {
    result.sort((a, b) => b.scarcityIndex - a.scarcityIndex)
  } else if (sortBy.value === 'growth') {
    result.sort((a, b) => b.growthRate - a.growthRate)
  }
  return result
})

function categoryLabel(cat: string): string {
  const labels: Record<string, string> = { AI: '人工智能', TECHNOLOGY: '技术', SOFT_SKILLS: '软技能', BUSINESS: '商业', DESIGN: '设计', LANGUAGE: '语言' }
  return labels[cat] || cat
}

function getCategoryIcon(cat: string): string {
  const icons: Record<string, string> = { AI: '🤖', TECHNOLOGY: '💻', SOFT_SKILLS: '💬', BUSINESS: '💼', DESIGN: '🎨', LANGUAGE: '🌍' }
  return icons[cat] || '📋'
}

function parseCourses(courses: string): string[] {
  try { return JSON.parse(courses.replace(/'/g, '"')) } catch { return [] }
}

function debouncedSearch() {
  if (searchTimer) clearTimeout(searchTimer)
  searchTimer = setTimeout(doSearch, 300)
}

function doSearch() {}

async function loadSkills() {
  loading.value = true
  try {
    const res = await http.get('/skills/all')
    skills.value = res.data || []
  } catch {
    skills.value = getFallbackData()
  } finally {
    loading.value = false
  }
}

function getFallbackData() {
  return [
    { skillName: '深度学习', category: 'AI', demandIndex: 98.5, scarcityIndex: 92.0, growthRate: 45.2, trend: 'UP', description: '深度神经网络、模型训练与调优', relatedCourses: "['AI 与机器学习入门']" },
    { skillName: '大语言模型(LLM)', category: 'AI', demandIndex: 97.2, scarcityIndex: 95.0, growthRate: 52.8, trend: 'UP', description: 'GPT、BERT等大模型应用', relatedCourses: "['AI 与机器学习入门']" },
    { skillName: 'Python', category: 'TECHNOLOGY', demandIndex: 95.0, scarcityIndex: 80.0, growthRate: 25.0, trend: 'UP', description: 'Python编程、数据分析', relatedCourses: "['AI 与机器学习入门']" },
    { skillName: '沟通能力', category: 'SOFT_SKILLS', demandIndex: 92.0, scarcityIndex: 70.0, growthRate: 15.0, trend: 'STABLE', description: '有效表达、跨部门沟通', relatedCourses: "['沟通与协作技巧']" },
    { skillName: '数据科学', category: 'TECHNOLOGY', demandIndex: 90.0, scarcityIndex: 78.0, growthRate: 22.0, trend: 'UP', description: 'SQL、数据可视化', relatedCourses: "['Python数据科学']" },
    { skillName: '团队协作', category: 'SOFT_SKILLS', demandIndex: 90.0, scarcityIndex: 68.0, growthRate: 12.0, trend: 'STABLE', description: '敏捷协作', relatedCourses: "['沟通与协作技巧']" },
    { skillName: 'Java企业级开发', category: 'TECHNOLOGY', demandIndex: 88.0, scarcityIndex: 72.0, growthRate: 12.5, trend: 'STABLE', description: 'Spring Boot、微服务', relatedCourses: "['Java 企业级开发实战']" },
    { skillName: '数据分析思维', category: 'BUSINESS', demandIndex: 88.0, scarcityIndex: 76.0, growthRate: 22.0, trend: 'UP', description: '数据驱动决策', relatedCourses: "['商业思维与创新']" },
    { skillName: '问题解决能力', category: 'SOFT_SKILLS', demandIndex: 88.0, scarcityIndex: 75.0, growthRate: 18.0, trend: 'UP', description: '结构化思维', relatedCourses: "['沟通与协作技巧']" },
    { skillName: '云原生技术', category: 'TECHNOLOGY', demandIndex: 85.0, scarcityIndex: 82.0, growthRate: 35.0, trend: 'UP', description: 'Docker、Kubernetes', relatedCourses: "['Java 企业级开发实战']" },
  ]
}

onMounted(loadSkills)
</script>

<style scoped>
.page-content { padding: 32px 48px; max-width: 1200px; margin: 0 auto; }
.page-header { margin-bottom: 24px; }
.page-header h1 { font-size: 28px; }
.page-desc { color: #a0a0a0; margin-top: 4px; }

.market-toolbar { display: flex; gap: 12px; margin-bottom: 24px; flex-wrap: wrap; }
.search-input { flex: 1; min-width: 200px; }

.market-stats { display: grid; grid-template-columns: repeat(4, 1fr); gap: 16px; margin-bottom: 32px; }
.stat-card { text-align: center; padding: 20px; }
.stat-num { display: block; font-size: 28px; font-weight: 700; }
.stat-lbl { font-size: 13px; color: #a0a0a0; margin-top: 4px; }

.loading-grid, .skill-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 20px; }
.skill-card { padding: 24px; }
.skill-top { display: flex; gap: 12px; margin-bottom: 16px; align-items: flex-start; }
.skill-icon { font-size: 28px; }
.skill-info { flex: 1; }
.skill-info h3 { font-size: 16px; margin-bottom: 2px; }
.skill-category { font-size: 12px; color: #409eff; }
.skill-bars { display: flex; flex-direction: column; gap: 6px; margin-bottom: 12px; }
.bar-row { display: flex; align-items: center; gap: 8px; }
.bar-label { font-size: 11px; color: #666; width: 32px; }
.bar-track { flex: 1; height: 6px; background: rgba(255,255,255,0.06); border-radius: 3px; overflow: hidden; }
.bar-fill { height: 100%; border-radius: 3px; transition: width 0.6s; }
.bar-fill.demand { background: linear-gradient(90deg, #409eff, #667eea); }
.bar-fill.scarcity { background: linear-gradient(90deg, #e6a23c, #f56c6c); }
.bar-fill.growth { background: linear-gradient(90deg, #67c23a, #409eff); }
.bar-val { font-size: 11px; color: #a0a0a0; width: 40px; text-align: right; }
.skill-desc { font-size: 13px; color: #a0a0a0; line-height: 1.5; margin-bottom: 12px; }
.skill-courses { display: flex; flex-wrap: wrap; gap: 6px; }
.course-chip { padding: 3px 10px; border-radius: 12px; font-size: 11px; background: rgba(64,158,255,0.08); color: #409eff; cursor: pointer; }
.course-chip:hover { background: rgba(64,158,255,0.15); }
.empty-state { text-align: center; padding: 64px; grid-column: 1/-1; }
.empty-state p { margin: 16px 0; color: #a0a0a0; }
</style>