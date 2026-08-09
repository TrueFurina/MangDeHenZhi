<template>
  <div class="gamification-panel">
    <!-- 等级 + XP 进度 -->
    <div class="level-header">
      <div class="level-badge" :class="`level-${summary.level || 1}`">
        <span class="level-num">Lv.{{ summary.level || 1 }}</span>
      </div>
      <div class="xp-info">
        <span class="xp-label">累计经验</span>
        <span class="xp-value">{{ summary.xp || 0 }} XP</span>
        <el-progress :percentage="summary.levelProgress || 0" :stroke-width="8" color="#e6a23c" />
        <span v-if="summary.level < 10" class="xp-next">距 Lv.{{ (summary.level || 1) + 1 }} 还需 {{ summary.xpToNextLevel }} XP</span>
        <span v-else class="xp-next">🎉 已达最高等级</span>
      </div>
    </div>

    <!-- 成就 -->
    <div class="achievement-header">
      <h4>🏆 成就徽章 <em>{{ summary.unlockedCount }}/{{ (summary.achievements || []).length }}</em></h4>
    </div>
    <div class="achievement-grid">
      <div v-for="a in summary.achievements" :key="a.id" class="achievement-card" :class="{ unlocked: a.unlocked }">
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
import { ElMessage } from 'element-plus'
import { gamificationApi } from '@/api'

const summary = ref<any>({ achievements: [], level: 1, xp: 0, levelProgress: 0, unlockedCount: 0 })

onMounted(async () => {
  try {
    const res = await gamificationApi.getMySummary()
    summary.value = res.data || summary.value
  } catch (_) {
    // 静默失败，组件展示默认空状态
  }
})
</script>

<style scoped>
.gamification-panel { padding: 20px; }

.level-header {
  display: flex;
  gap: 20px;
  align-items: center;
  padding-bottom: 16px;
  border-bottom: 1px solid rgba(255,255,255,0.06);
}

.level-badge {
  width: 72px;
  height: 72px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  background: conic-gradient(from 180deg, #e6a23c, #f56c6c, #909399, #67c23a, #e6a23c);
  box-shadow: 0 0 20px rgba(230, 162, 60, 0.3);
}

.level-num {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background: #1a1b2e;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  font-weight: 700;
  color: #e6a23c;
}

.xp-info { flex: 1; }
.xp-label { font-size: 12px; color: #a0a0a0; margin-right: 8px; }
.xp-value { font-size: 18px; font-weight: 700; color: #e6a23c; }
.xp-next { font-size: 12px; color: #666; display: block; margin-top: 4px; }

.achievement-header { margin: 16px 0 12px; }
.achievement-header h4 { font-size: 15px; }
.achievement-header em { font-style: normal; color: #a0a0a0; font-size: 12px; }

.achievement-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 10px;
}

.achievement-card {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 12px;
  background: rgba(255,255,255,0.03);
  border-radius: 8px;
  border: 1px solid rgba(255,255,255,0.05);
  opacity: 0.45;
}

.achievement-card.unlocked { opacity: 1; border-color: rgba(230,162,60,0.3); }
.ach-icon { font-size: 22px; flex-shrink: 0; }
.ach-info { flex: 1; min-width: 0; }
.ach-name { display: block; font-size: 13px; font-weight: 600; }
.ach-desc { display: block; font-size: 11px; color: #a0a0a0; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.ach-check { flex-shrink: 0; }

@media (max-width: 768px) {
  .achievement-grid { grid-template-columns: 1fr; }
}
</style>