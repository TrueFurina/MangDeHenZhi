<template>
  <div class="page">
    <AppHeader />
    <div class="page-content">
      <el-button link @click="router.back()" style="margin-bottom:16px">
        <el-icon><arrow-left /></el-icon> 返回
      </el-button>

      <div v-if="!course" class="loading-state">
        <SkeletonCard variant="card" />
      </div>

      <div v-if="course" class="course-detail">
        <!-- 课程头部 -->
        <div class="glass-card detail-header">
          <div class="header-cover">
            <div class="cover-placeholder" :style="{ background: getCoverColor(course.id) }">
              <span class="cover-icon">{{ getCategoryIcon(course.category) }}</span>
            </div>
          </div>
          <div class="header-info">
            <div class="header-tags">
              <el-tag>{{ categoryLabels[course.category] || course.category }}</el-tag>
              <el-tag :type="course.difficulty === 'BEGINNER' ? 'success' : 'warning'">
                {{ difficultyLabels[course.difficulty] || course.difficulty }}
              </el-tag>
              <el-tag v-if="course.price === 0 || !course.price" type="success">免费</el-tag>
            </div>
            <h1>{{ course.title }}</h1>
            <p class="header-desc">{{ course.description }}</p>
            <div class="header-meta">
              <div class="meta-item">
                <span class="meta-icon">⏱</span>
                <div>
                  <span class="meta-label">时长</span>
                  <span class="meta-value">{{ course.duration }} 分钟</span>
                </div>
              </div>
              <div class="meta-item">
                <span class="meta-icon">👥</span>
                <div>
                  <span class="meta-label">已报名</span>
                  <span class="meta-value">{{ course.enrollmentCount || 0 }} 人</span>
                </div>
              </div>
              <div class="meta-item">
                <span class="meta-icon">💰</span>
                <div>
                  <span class="meta-label">价格</span>
                  <span class="meta-value" :class="{ free: !course.price }">
                    {{ course.price > 0 ? '¥' + course.price : '免费' }}
                  </span>
                </div>
              </div>
            </div>
            <el-button type="primary" size="large" class="enroll-btn" @click="handleEnroll">
              <el-icon><plus /></el-icon> 立即报名
            </el-button>
          </div>
        </div>

        <!-- 课程详情 -->
        <div class="glass-card detail-body">
          <h3>📖 课程详情</h3>
          <div class="body-content">
            <p>{{ course.description }}</p>
          </div>
          <div v-if="course.prerequisites" class="prerequisites">
            <h4>📋 前置要求</h4>
            <p>{{ course.prerequisites }}</p>
          </div>
        </div>

        <!-- 相关推荐 -->
        <div class="glass-card detail-related">
          <h3>🔗 相关推荐</h3>
          <p style="color:#a0a0a0;font-size:14px">完成本课程后，推荐继续学习进阶课程</p>
          <div class="related-actions">
            <el-button @click="router.push('/courses')">
              <el-icon><reading /></el-icon> 浏览更多课程
            </el-button>
            <el-button type="primary" @click="router.push('/assessments')">
              <el-icon><collection /></el-icon> 测试你的水平
            </el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { courseApi } from '@/api'
import AppHeader from '@/components/AppHeader.vue'
import SkeletonCard from '@/components/SkeletonCard.vue'
import type { Course } from '@/types'

const route = useRoute()
const router = useRouter()
const course = ref<Course | null>(null)

const categoryLabels: Record<string, string> = {
  TECHNOLOGY: '技术', BUSINESS: '商业', DESIGN: '设计', LANGUAGE: '语言', SOFT_SKILLS: '软技能',
}
const difficultyLabels: Record<string, string> = {
  BEGINNER: '入门', INTERMEDIATE: '进阶', ADVANCED: '高级', ADAPTIVE: '自适应',
}
const COVER_COLORS = ['#1a1b3e', '#1a2a1a', '#2a1a2e', '#1a2a2e', '#3e1a1a', '#1a3e3e']

function getCoverColor(id: number): string { return COVER_COLORS[id % COVER_COLORS.length] }
function getCategoryIcon(cat: string): string {
  const icons: Record<string, string> = { TECHNOLOGY: '💻', BUSINESS: '💼', DESIGN: '🎨', LANGUAGE: '🌍', SOFT_SKILLS: '🤝' }
  return icons[cat] || '📖'
}

function handleEnroll() {
  ElMessage.success('🎉 报名成功！开始你的学习之旅')
}

onMounted(async () => {
  try {
    const res = await courseApi.getById(Number(route.params.id))
    course.value = res.data
  } catch (err) {
    console.error('加载课程详情失败', err)
    ElMessage.error('加载课程失败')
  }
})
</script>

<style scoped>
.page-content { padding: 32px 48px; max-width: 1000px; margin: 0 auto; }
.loading-state { max-width: 800px; margin: 0 auto; }

.detail-header { display: flex; gap: 32px; padding: 0; overflow: hidden; }
.header-cover { width: 280px; min-height: 280px; flex-shrink: 0; }
.cover-placeholder { width: 100%; height: 100%; display: flex; align-items: center; justify-content: center; }
.cover-icon { font-size: 80px; opacity: 0.6; }
.header-info { flex: 1; padding: 32px 32px 32px 0; display: flex; flex-direction: column; }
.header-tags { display: flex; gap: 8px; margin-bottom: 16px; }
.header-info h1 { font-size: 28px; margin-bottom: 12px; }
.header-desc { color: #a0a0a0; line-height: 1.6; margin-bottom: 24px; flex: 1; }
.header-meta { display: grid; grid-template-columns: repeat(3, 1fr); gap: 16px; margin-bottom: 24px; }
.meta-item { display: flex; align-items: center; gap: 12px; padding: 12px; background: rgba(255,255,255,0.03); border-radius: 8px; }
.meta-icon { font-size: 24px; }
.meta-label { display: block; font-size: 11px; color: #666; }
.meta-value { display: block; font-size: 16px; font-weight: 600; }
.meta-value.free { color: #67c23a; }
.enroll-btn { width: 100%; height: 48px; font-size: 16px; }

.detail-body { margin-top: 24px; padding: 32px; }
.body-content { line-height: 1.8; color: #a0a0a0; }
.prerequisites { margin-top: 24px; padding: 16px; background: rgba(230,162,60,0.06); border-radius: 8px; border-left: 3px solid #e6a23c; }
.prerequisites h4 { margin-bottom: 8px; }
.prerequisites p { color: #a0a0a0; font-size: 14px; }

.detail-related { margin-top: 24px; padding: 32px; text-align: center; }
.related-actions { display: flex; gap: 16px; justify-content: center; margin-top: 16px; }

@media (max-width: 768px) {
  .detail-header { flex-direction: column; }
  .header-cover { width: 100%; min-height: 200px; }
  .header-info { padding: 24px; }
  .header-meta { grid-template-columns: 1fr; }
}
</style>