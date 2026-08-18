<template>
  <div class="page">
    <AppHeader />
    <div class="page-content">
      <div class="chat-container">
        <div class="chat-header">
          <h1>🤖 AI 导师</h1>
          <p class="chat-desc">问我任何学习问题，AI 导师随时为你解答</p>
          <p class="chat-note">⚠️ 当前为模板演示回复，大模型接入中</p>
        </div>

        <!-- 消息区 -->
        <div class="chat-messages" ref="messagesRef">
          <div v-if="messages.length === 0" class="chat-empty">
            <div class="suggested-prompts">
              <p class="sp-title">💡 试试这些问题：</p>
              <div v-for="prompt in suggestedPrompts" :key="prompt" class="sp-item" @click="sendMessage(prompt)">
                {{ prompt }}
              </div>
            </div>
          </div>

          <div v-for="(msg, i) in messages" :key="i" class="message" :class="msg.role">
            <div class="msg-avatar">{{ msg.role === 'user' ? '👤' : '🤖' }}</div>
            <div class="msg-content">
              <div class="msg-text">{{ msg.content }}</div>
            </div>
          </div>

          <div v-if="isLoading" class="message assistant">
            <div class="msg-avatar">🤖</div>
            <div class="msg-content">
              <div class="typing-dots"><span>.</span><span>.</span><span>.</span></div>
            </div>
          </div>
        </div>

        <!-- 输入区 -->
        <div class="chat-input-area">
          <el-input
            v-model="inputText"
            type="textarea"
            :rows="2"
            placeholder="输入你的学习问题..."
            :disabled="isLoading"
            @keydown.enter.prevent="handleSend"
            resize="none"
          />
          <el-button type="primary" :loading="isLoading" @click="handleSend" class="send-btn">
            <el-icon><promotion /></el-icon>
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { aiApi } from '@/api'
import AppHeader from '@/components/AppHeader.vue'

const messages = ref<{ role: string; content: string }[]>([])
const inputText = ref('')
const isLoading = ref(false)
const messagesRef = ref<HTMLElement | null>(null)

const suggestedPrompts = [
  '我沟通能力较弱，怎么提升？',
  '帮我制定一个学习计划',
  '数据分析需要学什么？',
  '什么是深度学习？用简单的话解释',
]

async function handleSend() {
  const text = inputText.value.trim()
  if (!text || isLoading.value) return
  sendMessage(text)
  inputText.value = ''
}

async function sendMessage(text: string) {
  messages.value.push({ role: 'user', content: text })
  isLoading.value = true
  scrollToBottom()

  try {
    // 构建对话上下文（最近 10 条）
    const context = messages.value.slice(-10).map(m => ({
      role: m.role === 'user' ? 'user' : 'assistant',
      content: m.content
    }))

    // F1: 经 @/api 模块调用后端 /api/ai/chat（自动携带 token，服务端鉴权后转发大模型）
    const resp = await aiApi.chat(context)
    const reply = resp.data
    messages.value.push({ role: 'assistant', content: reply })
  } catch (err) {
    // 默认走真实模型；仅当后端/网络不可用时降级为明确提示（不再伪装成 AI 回复）
    ElMessage.error('AI 导师暂时不可用，请稍后再试')
    messages.value.push({
      role: 'assistant',
      content: '抱歉，AI 导师暂时不可用，请稍后再试。你可以稍后重试，或先浏览课程与测评内容。',
    })
  } finally {
    isLoading.value = false
    scrollToBottom()
  }
}

function scrollToBottom() {
  nextTick(() => {
    if (messagesRef.value) {
      messagesRef.value.scrollTop = messagesRef.value.scrollHeight
    }
  })
}
</script>

<style scoped>
.page-content { padding: 32px 48px; max-width: var(--content-width-narrow); margin: 0 auto; }
.chat-container { display: flex; flex-direction: column; height: calc(100vh - 160px); }
.chat-header { text-align: center; margin-bottom: 24px; }
.chat-header h1 { font-size: 24px; }
.chat-desc { color: var(--text-secondary); font-size: 14px; margin-top: 4px; }
.chat-note { color: var(--color-warning); font-size: 12px; margin-top: 8px; }

.chat-messages { flex: 1; overflow-y: auto; padding: 16px; background: rgba(255,255,255,0.02); border-radius: var(--radius-md); margin-bottom: 16px; }
.chat-empty { display: flex; align-items: center; justify-content: center; height: 100%; }
.suggested-prompts { max-width: 400px; }
.sp-title { font-size: 14px; color: var(--text-secondary); margin-bottom: 12px; text-align: center; }
.sp-item { padding: 12px 16px; margin-bottom: 8px; border-radius: var(--radius-sm); background: rgba(var(--color-primary-rgb), 0.06); border: 1px solid rgba(var(--color-primary-rgb), 0.1); cursor: pointer; font-size: 14px; color: var(--text-secondary); transition: all 0.2s; }
.sp-item:hover { background: rgba(var(--color-primary-rgb), 0.12); color: var(--color-primary); }

.message { display: flex; gap: 12px; margin-bottom: 16px; }
.message.user { flex-direction: row-reverse; }
.msg-avatar { width: 36px; height: 36px; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 18px; flex-shrink: 0; background: rgba(255,255,255,0.04); }
.message.user .msg-content { background: rgba(var(--color-primary-rgb), 0.12); border-radius: var(--radius-md) 4px var(--radius-md) var(--radius-md); }
.message.assistant .msg-content { background: rgba(255,255,255,0.04); border-radius: 4px var(--radius-md) var(--radius-md) var(--radius-md); }
.msg-content { padding: 12px 16px; max-width: 80%; }
.msg-text { font-size: 14px; line-height: 1.6; white-space: pre-wrap; }

.typing-dots { display: flex; gap: 4px; padding: 4px 0; }
.typing-dots span { animation: blink 1.4s infinite; font-size: 24px; color: var(--color-primary); line-height: 1; }
.typing-dots span:nth-child(2) { animation-delay: 0.2s; }
.typing-dots span:nth-child(3) { animation-delay: 0.4s; }
@keyframes blink { 0%, 80%, 100% { opacity: 0; } 40% { opacity: 1; } }

.chat-input-area { display: flex; gap: 12px; align-items: flex-end; }
.chat-input-area .el-textarea { flex: 1; }
.send-btn { height: 48px; width: 48px; }
</style>