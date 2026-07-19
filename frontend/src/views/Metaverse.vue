<template>
  <div class="page">
    <AppHeader />
    <div class="page-content">
      <h1>🌐 元宇宙虚拟训练</h1>
      <p class="page-desc">进入沉浸式3D场景，与AI角色进行真实互动训练</p>

      <!-- 场景选择 -->
      <div class="scene-grid">
        <div v-for="scene in scenes" :key="scene.type"
             class="glass-card scene-card"
             :class="{ active: activeScene === scene.type }"
             @click="selectScene(scene.type)">
          <div class="scene-icon">{{ scene.icon }}</div>
          <h3>{{ scene.name }}</h3>
          <p>{{ scene.desc }}</p>
        </div>
      </div>

      <!-- 3D 场景渲染区 -->
      <div class="scene-viewport" v-if="activeScene">
        <div class="scene-toolbar">
          <span class="scene-title">{{ scenes.find(s => s.type === activeScene)?.name }}</span>
          <div class="toolbar-actions">
            <el-button size="small" @click="resetCamera" :disabled="!sceneReady">
              <el-icon><aim /></el-icon> 复位视角
            </el-button>
            <el-button size="small" @click="toggleOrbit" :disabled="!sceneReady">
              <el-icon><refresh-right /></el-icon> {{ orbitEnabled ? '锁定视角' : '自由视角' }}
            </el-button>
            <el-button type="primary" size="small" @click="enterScene(activeScene)" :loading="entering">
              <el-icon><video-camera /></el-icon> 进入场景
            </el-button>
          </div>
        </div>
        <ThreeScene
          v-if="activeScene"
          :scene-type="activeScene"
          :ai-character-count="aiCount"
          @scene-ready="onSceneReady"
        />
      </div>

      <!-- 空状态 -->
      <div v-else class="empty-state glass-card">
        <el-icon :size="48" color="#a0a0a0"><video-camera /></el-icon>
        <p>请选择一个场景开始训练</p>
      </div>

      <!-- 训练记录 -->
      <h2 style="margin-top:48px">训练记录</h2>
      <el-table :data="sessions" style="width:100%;margin-top:16px" stripe v-if="sessions.length > 0">
        <el-table-column prop="sessionName" label="场景" />
        <el-table-column prop="sceneType" label="类型" />
        <el-table-column prop="roomId" label="房间ID" />
        <el-table-column prop="startTime" label="开始时间" />
        <el-table-column prop="active" label="状态">
          <template #default="{ row }">
            <el-tag :type="row.active ? 'success' : 'info'">{{ row.active ? '进行中' : '已结束' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template #default="{ row }">
            <el-button v-if="row.active" type="danger" size="small" @click="endSession(row.id)">
              结束
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <p v-else style="color:#666;text-align:center;padding:24px">暂无训练记录</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { metaverseApi } from '@/api'
import { useWebSocket } from '@/composables/useWebSocket'
import AppHeader from '@/components/AppHeader.vue'
import ThreeScene from '@/components/ThreeScene.vue'
import type { MetaverseSession } from '@/types'

const userStore = useUserStore()
const sessions = ref<MetaverseSession[]>([])
const activeScene = ref<MetaverseSceneType | null>(null)
const currentRoomId = ref('')

type MetaverseSceneType = 'INTERVIEW_ROOM' | 'CLASSROOM' | 'MEETING_ROOM' | 'TRAINING_ROOM'
const sceneReady = ref(false)
const orbitEnabled = ref(true)
const entering = ref(false)
const aiCount = ref(2)

// WebSocket 实时通信
const ws = useWebSocket(
  currentRoomId,
  String(userStore.currentUser?.id || ''),
  userStore.currentUser?.nickname || userStore.currentUser?.username || '访客'
)

const onlineUsers = ref(0)
const chatMessages = ref<{ user: string; msg: string; time: number }[]>([])
const chatInput = ref('')

// 监听 WebSocket 消息
ws.on('room_info', (msg) => { onlineUsers.value = msg.userCount })
ws.on('user_joined', (msg) => { ElMessage.info(`${msg.username} 加入了房间`) })
ws.on('user_left', (msg) => { ElMessage.info(`${msg.username} 离开了房间`) })
ws.on('chat', (msg) => {
  chatMessages.value.push({ user: msg.username, msg: msg.message, time: msg.timestamp })
})

function sendChat() {
  if (!chatInput.value.trim()) return
  ws.send({ type: 'chat', message: chatInput.value.trim() })
  chatInput.value = ''
}

const scenes = [
  { type: 'INTERVIEW_ROOM' as MetaverseSceneType, name: '虚拟面试', desc: '模拟真实面试场景，与AI面试官互动', icon: '🎯' },
  { type: 'CLASSROOM' as MetaverseSceneType, name: '虚拟课堂', desc: '沉浸式在线学习，AI讲师实时授课', icon: '📚' },
  { type: 'MEETING_ROOM' as MetaverseSceneType, name: '协作会议', desc: '团队协作模拟，锻炼沟通能力', icon: '🤝' },
  { type: 'TRAINING_ROOM' as MetaverseSceneType, name: '技能实训', desc: '专项技能训练，实时反馈指导', icon: '⚡' },
]

function selectScene(type: MetaverseSceneType) {
  activeScene.value = type
  sceneReady.value = false
}

function onSceneReady() {
  sceneReady.value = true
}

function resetCamera() {
  const tmp = activeScene.value as MetaverseSceneType | null
  activeScene.value = null
  setTimeout(() => { activeScene.value = tmp }, 50)
}

function toggleOrbit() {
  orbitEnabled.value = !orbitEnabled.value
}

async function enterScene(type: string) {
  entering.value = true
  try {
    const res = await metaverseApi.createSession({
      sessionName: scenes.find(s => s.type === type)?.name || type,
      sceneType: type,
    })
    currentRoomId.value = res.data.roomId
    ws.connect()
    ElMessage.success(`已进入「${res.data.sessionName}」，房间ID: ${res.data.roomId}`)
    sessions.value.unshift(res.data)
    selectScene(type as MetaverseSceneType)
  } catch (err: any) {
    ElMessage.error(err.response?.data?.message || '创建场景失败')
  } finally {
    entering.value = false
  }
}

async function endSession(id: number) {
  try {
    await metaverseApi.endSession(id)
    ElMessage.success('训练已结束')
    const idx = sessions.value.findIndex(s => s.id === id)
    if (idx >= 0) sessions.value[idx].active = false
  } catch (err: any) {
    ElMessage.error(err.response?.data?.message || '结束失败')
  }
}

onMounted(async () => {
  try {
    const res = await metaverseApi.getMySessions()
    sessions.value = res.data || []
  } catch (err: any) {
    console.error('加载训练记录失败', err?.response?.data?.message || err.message)
  }
})
</script>

<style scoped>
.page-content { padding: 32px 48px; max-width: 1200px; margin: 0 auto; }
.page-desc { color: #a0a0a0; margin-bottom: 32px; }
.scene-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 24px; margin-bottom: 32px; }
.scene-card { text-align: center; padding: 32px 24px; cursor: pointer; transition: all 0.3s; }
.scene-card:hover { transform: translateY(-4px); }
.scene-card.active { border-color: #409eff; box-shadow: 0 0 20px rgba(64,158,255,0.3); }
.scene-icon { font-size: 48px; margin-bottom: 16px; }
.scene-card h3 { margin-bottom: 8px; }
.scene-card p { color: #a0a0a0; font-size: 13px; }
.scene-viewport { margin-top: 24px; }
.scene-toolbar {
  display: flex; justify-content: space-between; align-items: center;
  padding: 12px 16px; background: rgba(26,27,46,0.9); border-radius: 12px 12px 0 0;
  border: 1px solid var(--border-color); border-bottom: none;
}
.scene-title { font-size: 16px; font-weight: 600; }
.toolbar-actions { display: flex; gap: 8px; }
.empty-state { text-align: center; padding: 64px; }
.empty-state p { margin: 16px 0; color: #a0a0a0; }
</style>