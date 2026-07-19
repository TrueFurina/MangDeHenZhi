<template>
  <div class="captcha-row" v-if="visible">
    <el-input v-model="answer" placeholder="验证码" size="large" :disabled="loading">
      <template #prefix>
        <el-icon><key /></el-icon>
      </template>
      <template #append>
        <el-button @click="fetchCaptcha" :loading="loading" size="large" style="min-width:140px;font-family:monospace">
          {{ question || '获取验证码' }}
        </el-button>
      </template>
    </el-input>
  </div>
</template>

<script setup lang="ts">
import { ref, watch, onMounted } from 'vue'
import http from '@/api'

const props = withDefaults(defineProps<{ visible?: boolean }>(), { visible: true })

const emit = defineEmits<{ update: [key: string, answer: number] }>()

const loading = ref(false)
const question = ref('')
const captchaKey = ref('')
const answer = ref('')

async function fetchCaptcha() {
  loading.value = true
  try {
    const res = await http.get<any, any>('/captcha')
    captchaKey.value = res.data.key
    question.value = res.data.question
    answer.value = ''
    emitCaptcha()
  } catch (_) { /* ignore */ }
  finally { loading.value = false }
}

function emitCaptcha() {
  if (captchaKey.value && answer.value) {
    emit('update', captchaKey.value, parseInt(answer.value))
  }
}

watch(answer, () => emitCaptcha())

onMounted(() => fetchCaptcha())
</script>