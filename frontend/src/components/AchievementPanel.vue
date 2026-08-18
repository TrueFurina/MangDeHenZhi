<template>
  <div class="achievement-panel">
    <div class="panel-header">
      <h3>🏆 成就系统</h3>
      <span class="badge-count">{{ achievements.length }} 个成就</span>
    </div>
    <div class="achievement-grid">
      <div v-for="a in achievements" :key="a.id" class="achievement-card" :class="{ unlocked: a.unlocked }">
        <div class="ach-icon">{{ a.unlocked ? a.icon : '🔒' }}</div>
        <div class="ach-info">
          <span class="ach-name">{{ a.name }}</span>
          <span class="ach-desc">{{ a.description }}</span>
        </div>
        <div v-if="a.unlocked" class="ach-check">✅</div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'

interface Achievement {
  id: string
  icon: string
  name: string
  description: string
  unlocked: boolean
}

const achievements = ref<Achievement[]>([])

onMounted(() => {
  achievements.value = [
    { id: 'first_assessment', icon: '📝', name: '初次测评', description: '完成第一次技能测评', unlocked: true },
    { id: 'passed_exam', icon: '🎯', name: '通过考核', description: '通过任意测评考核', unlocked: true },
    { id: 'learning_path', icon: '🗺️', name: '规划之路', description: '生成个性化学习路径', unlocked: false },
    { id: 'ai_tutor', icon: '🤖', name: 'AI对话', description: '与AI导师对话10次', unlocked: false },
    { id: 'skill_master', icon: '💎', name: '技能大师', description: '掌握5项高需求技能', unlocked: false },
    { id: 'certificate', icon: '🏆', name: '首次认证', description: '获得第一张区块链证书', unlocked: false },
    { id: 'metaverse', icon: '🌐', name: '元宇宙探索者', description: '进入元宇宙场景', unlocked: false },
    { id: 'persistent', icon: '🔥', name: '坚持不懈', description: '连续学习7天', unlocked: false },
  ]
})
</script>

<style scoped>
.panel-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.badge-count { font-size: 13px; color: #a0a0a0; }
.achievement-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 12px; }
.achievement-card { display: flex; align-items: center; gap: 12px; padding: 12px; border-radius: 8px; background: rgba(255,255,255,0.03); border: 1px solid rgba(255,255,255,0.06); }
.achievement-card.unlocked { border-color: rgba(103,194,58,0.2); background: rgba(103,194,58,0.04); }
.ach-icon { font-size: 24px; }
.ach-info { flex: 1; }
.ach-name { display: block; font-size: 14px; font-weight: 500; }
.ach-desc { font-size: 12px; color: #666; }
.ach-check { color: #67c23a; }
</style>