<template>
  <div class="skill-heatmap">
    <div class="skill-controls">
      <div class="filter-tabs">
        <el-button v-for="tab in tabs" :key="tab.key"
          :type="activeTab === tab.key ? 'primary' : 'default'"
          size="small" @click="activeTab = tab.key">
          {{ tab.label }}
        </el-button>
      </div>
      <el-button text type="primary" size="small" @click="loadData" :loading="loading">
        {{ loading ? '加载中...' : '刷新数据' }}
      </el-button>
    </div>

    <!-- 加载态 -->
    <div v-if="loading" class="skeleton-grid">
      <div v-for="i in 8" :key="i" class="skeleton-bar" />
    </div>

    <!-- 技能卡片网格 -->
    <div v-else class="skill-grid">
      <div v-for="skill in displayedSkills" :key="skill.id || skill.skillName"
           class="skill-card" :style="{ borderLeft: `3px solid ${getScoreColor(skill.demandIndex)}` }">
        <div class="skill-top">
          <span class="skill-name">{{ skill.skillName }}</span>
          <el-tag v-if="skill.trend === 'UP'" size="small" type="success" effect="dark">🔥 热门</el-tag>
          <el-tag v-else size="small" type="info">稳定</el-tag>
        </div>
        <div class="skill-bars">
          <div class="skill-bar-row">
            <span class="bar-label">需求指数</span>
            <div class="bar-track">
              <div class="bar-fill demand" :style="{ width: skill.demandIndex + '%' }" />
            </div>
            <span class="bar-value">{{ skill.demandIndex }}%</span>
          </div>
          <div class="skill-bar-row">
            <span class="bar-label">稀缺指数</span>
            <div class="bar-track">
              <div class="bar-fill scarcity" :style="{ width: skill.scarcityIndex + '%' }" />
            </div>
            <span class="bar-value">{{ skill.scarcityIndex }}%</span>
          </div>
          <div class="skill-bar-row">
            <span class="bar-label">增长率</span>
            <div class="bar-track">
              <div class="bar-fill growth" :style="{ width: Math.min(skill.growthRate * 2, 100) + '%' }" />
            </div>
            <span class="bar-value">+{{ skill.growthRate }}%</span>
          </div>
        </div>
        <p class="skill-desc">{{ skill.description }}</p>
        <div class="skill-courses" v-if="skill.relatedCourses">
          <span v-for="c in parseCourses(skill.relatedCourses)" :key="c" class="course-chip"
                @click="router.push('/courses')">
            {{ c }}
          </span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { skillApi } from '@/api'

const router = useRouter()
const loading = ref(true)
const allSkills = ref<any[]>([])
const activeTab = ref('demand')

const tabs = [
  { key: 'demand', label: '🔥 高需求技能' },
  { key: 'scarcity', label: '💎 稀缺技能' },
  { key: 'trending', label: '📈 增长最快' },
  { key: 'all', label: '📋 全部' },
]

const displayedSkills = computed(() => {
  if (activeTab.value === 'demand') {
    return [...allSkills.value].sort((a, b) => b.demandIndex - a.demandIndex).slice(0, 10)
  }
  if (activeTab.value === 'scarcity') {
    return [...allSkills.value].sort((a, b) => b.scarcityIndex - a.scarcityIndex).slice(0, 10)
  }
  if (activeTab.value === 'trending') {
    return [...allSkills.value].sort((a, b) => b.growthRate - a.growthRate).slice(0, 10)
  }
  return allSkills.value
})

function getScoreColor(score: number): string {
  if (score >= 90) return '#f56c6c'
  if (score >= 80) return '#e6a23c'
  if (score >= 70) return '#409eff'
  return '#67c23a'
}

function parseCourses(courses: string): string[] {
  try {
    return JSON.parse(courses.replace(/'/g, '"'))
  } catch {
    return []
  }
}

async function loadData() {
  loading.value = true
  try {
    const res = await skillApi.getAll()
    allSkills.value = res.data || []
  } catch (err) {
    console.error('加载技能趋势数据失败', err)
    // 降级：使用本地模拟数据
    allSkills.value = getFallbackData()
  } finally {
    loading.value = false
  }
}

function getFallbackData() {
  return [
    { skillName: '深度学习', demandIndex: 98.5, scarcityIndex: 92.0, growthRate: 45.2, trend: 'UP', description: '深度神经网络、模型训练与调优', relatedCourses: "['AI 与机器学习入门']" },
    { skillName: '大语言模型(LLM)', demandIndex: 97.2, scarcityIndex: 95.0, growthRate: 52.8, trend: 'UP', description: 'GPT、BERT等大模型应用', relatedCourses: "['AI 与机器学习入门']" },
    { skillName: 'Python', demandIndex: 95.0, scarcityIndex: 80.0, growthRate: 25.0, trend: 'UP', description: 'Python编程、数据分析', relatedCourses: "['AI 与机器学习入门']" },
    { skillName: '自然语言处理', demandIndex: 92.0, scarcityIndex: 88.5, growthRate: 38.6, trend: 'UP', description: '文本分类、情感分析', relatedCourses: "['AI 与机器学习入门']" },
    { skillName: '沟通能力', demandIndex: 92.0, scarcityIndex: 70.0, growthRate: 15.0, trend: 'STABLE', description: '有效表达、跨部门沟通', relatedCourses: "['沟通与协作技巧']" },
    { skillName: '数据科学', demandIndex: 90.0, scarcityIndex: 78.0, growthRate: 22.0, trend: 'UP', description: 'SQL、数据可视化', relatedCourses: "['Python数据科学']" },
    { skillName: '团队协作', demandIndex: 90.0, scarcityIndex: 68.0, growthRate: 12.0, trend: 'STABLE', description: '敏捷协作、跨职能团队', relatedCourses: "['沟通与协作技巧']" },
    { skillName: 'Java企业级开发', demandIndex: 88.0, scarcityIndex: 72.0, growthRate: 12.5, trend: 'STABLE', description: 'Spring Boot、微服务', relatedCourses: "['Java 企业级开发实战']" },
    { skillName: '计算机视觉', demandIndex: 88.5, scarcityIndex: 85.0, growthRate: 30.4, trend: 'UP', description: '图像识别、目标检测', relatedCourses: "['AI 与机器学习入门']" },
    { skillName: '数据分析思维', demandIndex: 88.0, scarcityIndex: 76.0, growthRate: 22.0, trend: 'UP', description: '数据驱动决策', relatedCourses: "['商业思维与创新']" },
    { skillName: '问题解决能力', demandIndex: 88.0, scarcityIndex: 75.0, growthRate: 18.0, trend: 'UP', description: '结构化思维、根因分析', relatedCourses: "['沟通与协作技巧']" },
    { skillName: '云原生技术', demandIndex: 85.0, scarcityIndex: 82.0, growthRate: 35.0, trend: 'UP', description: 'Docker、Kubernetes', relatedCourses: "['Java 企业级开发实战']" },
    { skillName: '批判性思维', demandIndex: 85.0, scarcityIndex: 72.0, growthRate: 20.0, trend: 'UP', description: '逻辑推理、论证分析', relatedCourses: "['商业思维与创新']" },
    { skillName: '项目管理', demandIndex: 85.0, scarcityIndex: 70.0, growthRate: 15.0, trend: 'STABLE', description: '敏捷管理、Scrum', relatedCourses: "['商业思维与创新']" },
    { skillName: '前端开发', demandIndex: 82.0, scarcityIndex: 65.0, growthRate: 8.5, trend: 'STABLE', description: 'Vue.js、React', relatedCourses: "['Web 前端进阶教程']" },
    { skillName: '产品思维', demandIndex: 82.0, scarcityIndex: 72.0, growthRate: 18.0, trend: 'UP', description: '用户需求分析、产品规划', relatedCourses: "['商业思维与创新']" },
    { skillName: '网络安全', demandIndex: 80.0, scarcityIndex: 85.0, growthRate: 28.0, trend: 'UP', description: '渗透测试、安全审计', relatedCourses: "['Java 企业级开发实战']" },
    { skillName: '学习能力', demandIndex: 82.0, scarcityIndex: 65.0, growthRate: 10.0, trend: 'STABLE', description: '快速学习新技术', relatedCourses: "['AI 与机器学习入门']" },
  ]
}

onMounted(loadData)
</script>

<style scoped>
.skill-controls { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; flex-wrap: wrap; gap: 12px; }
.filter-tabs { display: flex; gap: 6px; flex-wrap: wrap; }

.skeleton-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 16px; }
.skeleton-bar { height: 120px; border-radius: 12px; background: linear-gradient(90deg, rgba(255,255,255,0.04) 25%, rgba(255,255,255,0.08) 50%, rgba(255,255,255,0.04) 75%); background-size: 200% 100%; animation: shimmer 1.5s ease-in-out infinite; }
@keyframes shimmer { 0% { background-position: 200% 0; } 100% { background-position: -200% 0; } }

.skill-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 16px; }
.skill-card { padding: 20px; background: rgba(255,255,255,0.03); border-radius: 12px; border: 1px solid rgba(255,255,255,0.06); transition: all 0.2s; }
.skill-card:hover { background: rgba(255,255,255,0.06); }
.skill-top { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.skill-name { font-size: 16px; font-weight: 600; }
.skill-bars { display: flex; flex-direction: column; gap: 8px; margin-bottom: 12px; }
.skill-bar-row { display: flex; align-items: center; gap: 8px; }
.bar-label { font-size: 11px; color: #666; width: 56px; flex-shrink: 0; }
.bar-track { flex: 1; height: 6px; background: rgba(255,255,255,0.06); border-radius: 3px; overflow: hidden; }
.bar-fill { height: 100%; border-radius: 3px; transition: width 0.6s ease; }
.bar-fill.demand { background: linear-gradient(90deg, #409eff, #667eea); }
.bar-fill.scarcity { background: linear-gradient(90deg, #e6a23c, #f56c6c); }
.bar-fill.growth { background: linear-gradient(90deg, #67c23a, #409eff); }
.bar-value { font-size: 11px; color: #a0a0a0; width: 48px; text-align: right; flex-shrink: 0; }
.skill-desc { font-size: 13px; color: #a0a0a0; line-height: 1.5; margin-bottom: 12px; }
.skill-courses { display: flex; flex-wrap: wrap; gap: 6px; }
.course-chip { padding: 3px 10px; border-radius: 12px; font-size: 11px; background: rgba(64,158,255,0.08); color: #409eff; cursor: pointer; transition: all 0.2s; }
.course-chip:hover { background: rgba(64,158,255,0.15); }

@media (max-width: 768px) {
  .skill-grid { grid-template-columns: 1fr; }
}
</style>