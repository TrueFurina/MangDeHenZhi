<template>
  <div class="skeleton-card" :class="[`skeleton-${variant}`]">
    <div v-if="variant === 'card'" class="skeleton-inner">
      <div class="skeleton-line skeleton-title" />
      <div class="skeleton-line skeleton-text" />
      <div class="skeleton-line skeleton-text short" />
      <div class="skeleton-line skeleton-meta" />
    </div>
    <div v-else-if="variant === 'table'" class="skeleton-inner">
      <div v-for="i in rows" :key="i" class="skeleton-row">
        <div class="skeleton-line" :style="{ width: `${60 + Math.random() * 30}%` }" />
      </div>
    </div>
    <div v-else class="skeleton-inner">
      <div class="skeleton-circle" />
      <div class="skeleton-line skeleton-title" />
      <div class="skeleton-line skeleton-text" />
    </div>
  </div>
</template>

<script setup lang="ts">
withDefaults(defineProps<{
  variant?: 'card' | 'table' | 'stat'
  rows?: number
}>(), {
  variant: 'card',
  rows: 5,
})
</script>

<style scoped>
.skeleton-card {
  background: rgba(26, 27, 46, 0.6);
  border: 1px solid var(--border-color, #2d2d4a);
  border-radius: 12px;
  overflow: hidden;
}

.skeleton-inner {
  padding: 24px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.skeleton-line {
  height: 14px;
  border-radius: 4px;
  background: linear-gradient(
    90deg,
    rgba(255, 255, 255, 0.04) 25%,
    rgba(255, 255, 255, 0.08) 50%,
    rgba(255, 255, 255, 0.04) 75%
  );
  background-size: 200% 100%;
  animation: shimmer 1.5s ease-in-out infinite;
}

.skeleton-title {
  height: 20px;
  width: 60%;
}

.skeleton-text {
  width: 90%;
}

.skeleton-text.short {
  width: 40%;
}

.skeleton-meta {
  height: 12px;
  width: 30%;
  margin-top: 8px;
}

.skeleton-row {
  display: flex;
  gap: 12px;
  padding: 12px 0;
  border-bottom: 1px solid rgba(255, 255, 255, 0.04);
}

.skeleton-row:last-child {
  border-bottom: none;
}

.skeleton-circle {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background: linear-gradient(
    90deg,
    rgba(255, 255, 255, 0.04) 25%,
    rgba(255, 255, 255, 0.08) 50%,
    rgba(255, 255, 255, 0.04) 75%
  );
  background-size: 200% 100%;
  animation: shimmer 1.5s ease-in-out infinite;
}

@keyframes shimmer {
  0% { background-position: 200% 0; }
  100% { background-position: -200% 0; }
}

.skeleton-stat .skeleton-inner {
  flex-direction: row;
  align-items: center;
  gap: 16px;
}
</style>