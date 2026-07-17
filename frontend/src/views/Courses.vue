<template>
  <div class="page">
    <AppHeader />
    <div class="page-content">
      <div class="page-header">
        <div>
          <h1>📚 课程中心</h1>
          <p class="page-desc">精选课程，助你全面提升职业技能</p>
        </div>
        <div class="header-filter">
          <el-select v-model="filterCategory" placeholder="全部类别" clearable size="large" @change="loadCourses">
            <el-option label="全部类别" value="" />
            <el-option v-for="c in categories" :key="c.value" :label="c.label" :value="c.value" />
          </el-select>
        </div>
      </div>

      <!-- 分类快捷入口 -->
      <div class="category-chips">
        <div v-for="cat in categories" :key="cat.value"
             class="chip" :class="{ active: filterCategory === cat.value }"
             @click="filterCategory = cat.value; loadCourses()">
          <span>{{ cat.icon }}</span>
          <span>{{ cat.label }}</span>
        </div>
      </div>

      <!-- 课程网格 -->
      <div v-if="loading" class="loading-grid">
        <SkeletonCard v-for="i in 6" :key="i" variant="card" />
      </div>
      <div v-else class="course-grid">
        <div v-for="course in courses" :key="course.id"
             class="glass-card course-card" @click="router.push(`/courses/${course.id}`)">
          <div class="course-cover">
            <div class="cover-placeholder" :style="{ background: getCoverColor(course.id) }">
              <span class="cover-icon">{{ getCategoryIcon(course.category) }}</span>
            </div>
            <div class="course-badges">
              <el-tag size="small" :type="course.difficulty === 'BEGINNER' ? 'success' : 'warning'">
                {{ difficultyLabels[course.difficulty] || course.difficulty }}
              </el-tag>
            </div>
          </div>
          <div class="course-body">
            <div class="course-category">{{ categoryLabels[course.category] || course.category }}</div>
            <h3>{{ course.title }}</h3>
            <p class="course-desc">{{ course.description }}</p>
            <div class="course-footer">
              <div class="cf-left">
                <span>⏱ {{ course.duration }}分钟</span>
                <span>👥 {{ course.enrollmentCount || 0 }}人</span>
              </div>
              <span class="course-price" :class="{ free: course.price === 0 || !course.price }">
                {{ course.price > 0 ? '¥' + course.price : '免费' }}
              </span>
            </div>
          </div>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-if="!loading && courses.length === 0" class="empty-state glass-card">
        <el-icon :size="48" color="#a0a0a0"><reading /></el-icon>
        <p>暂无课程</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { courseApi } from '@/api'
import AppHeader from '@/components/AppHeader.vue'
import SkeletonCard from '@/components/SkeletonCard.vue'
import type { Course } from '@/types'

const router = useRouter()
const courses = ref<Course[]>([])
const loading = ref(true)
const filterCategory = ref('')

const categories = [
  { value: 'TECHNOLOGY', label: '技术', icon: '💻' },
  { value: 'BUSINESS', label: '商业', icon: '💼' },
  { value: 'DESIGN', label: '设计', icon: '🎨' },
  { value: 'LANGUAGE', label: '语言', icon: '🌍' },
  { value: 'SOFT_SKILLS', label: '软技能', icon: '🤝' },
]

const categoryLabels: Record<string, string> = {
  TECHNOLOGY: '技术', BUSINESS: '商业', DESIGN: '设计', LANGUAGE: '语言', SOFT_SKILLS: '软技能',
}
const difficultyLabels: Record<string, string> = {
  BEGINNER: '入门', INTERMEDIATE: '进阶', ADVANCED: '高级', ADAPTIVE: '自适应',
}

const COVER_COLORS = ['#1a1b3e', '#1a2a1a', '#2a1a2e', '#1a2a2e', '#3e1a1a', '#1a3e3e']

function getCoverColor(id: number): string {
  return COVER_COLORS[id % COVER_COLORS.length]
}
function getCategoryIcon(cat: string): string {
  const icons: Record<string, string> = { TECHNOLOGY: '💻', BUSINESS: '💼', DESIGN: '🎨', LANGUAGE: '🌍', SOFT_SKILLS: '🤝' }
  return icons[cat] || '📖'
}

async function loadCourses() {
  loading.value = true
  try {
    if (filterCategory.value) {
      const res = await courseApi.getByCategory(filterCategory.value)
      courses.value = res.data || []
    } else {
      const res = await courseApi.getAll()
      courses.value = res.data || []
    }
  } catch (err) {
    console.error('加载课程失败', err)
  } finally {
    loading.value = false
  }
}

onMounted(loadCourses)
</script>

<style scoped>
.page-content { padding: 32px 48px; max-width: 1200px; margin: 0 auto; }
.page-header { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 24px; }
.page-header h1 { font-size: 28px; }
.page-desc { color: #a0a0a0; margin-top: 4px; }
.header-filter { width: 200px; }

.category-chips { display: flex; gap: 10px; margin-bottom: 32px; flex-wrap: wrap; }
.chip { display: flex; align-items: center; gap: 6px; padding: 8px 18px; border-radius: 20px; background: rgba(255,255,255,0.04); cursor: pointer; font-size: 14px; color: #a0a0a0; transition: all 0.2s; }
.chip:hover { background: rgba(64,158,255,0.08); color: #409eff; }
.chip.active { background: rgba(64,158,255,0.15); color: #409eff; }

.loading-grid, .course-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 24px; }
.course-card { padding: 0; overflow: hidden; cursor: pointer; transition: all 0.3s; }
.course-card:hover { transform: translateY(-4px); box-shadow: 0 12px 30px rgba(64,158,255,0.15); }

.course-cover { position: relative; height: 140px; }
.cover-placeholder { height: 100%; display: flex; align-items: center; justify-content: center; }
.cover-icon { font-size: 48px; opacity: 0.6; }
.course-badges { position: absolute; top: 12px; right: 12px; }

.course-body { padding: 20px; }
.course-category { font-size: 11px; color: #409eff; text-transform: uppercase; letter-spacing: 1px; margin-bottom: 4px; }
.course-body h3 { font-size: 17px; margin-bottom: 8px; }
.course-desc { color: #a0a0a0; font-size: 13px; line-height: 1.5; margin-bottom: 16px; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }
.course-footer { display: flex; justify-content: space-between; align-items: center; }
.cf-left { display: flex; gap: 12px; font-size: 12px; color: #666; }
.course-price { font-size: 20px; font-weight: 700; color: #f56c6c; }
.course-price.free { color: #67c23a; }

.empty-state { text-align: center; padding: 64px; }
.empty-state p { margin: 16px 0; color: #a0a0a0; }

@media (max-width: 900px) {
  .loading-grid, .course-grid { grid-template-columns: 1fr; }
  .page-header { flex-direction: column; gap: 12px; }
}
</style>