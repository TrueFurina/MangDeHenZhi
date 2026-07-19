<template>
  <div class="auth-page">
    <!-- 左侧装饰区 -->
    <div class="auth-decoration">
      <div class="decoration-content">
        <div class="deco-icon">◆</div>
        <h1 class="deco-title">芒得很职</h1>
        <p class="deco-desc">AI + 元宇宙 + 区块链<br/>职业技能培训认证平台</p>
        <div class="deco-features">
          <div class="deco-feature">🧠 AI 智能测评分析</div>
          <div class="deco-feature">🌐 元宇宙3D虚拟训练</div>
          <div class="deco-feature">🔗 区块链证书存证</div>
        </div>
      </div>
    </div>

    <!-- 右侧登录表单 -->
    <div class="auth-form-area">
      <div class="auth-card glass-card">
        <div class="auth-header">
          <h2 class="auth-title">欢迎回来</h2>
          <p class="auth-subtitle">登录你的账号继续学习</p>
        </div>

        <el-form :model="form" :rules="rules" ref="formRef" @submit.prevent="handleLogin" class="auth-form">
          <el-form-item prop="username">
            <el-input v-model="form.username" placeholder="用户名" size="large" clearable>
              <template #prefix><el-icon><user /></el-icon></template>
            </el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="form.password" type="password" placeholder="密码" size="large" show-password clearable>
              <template #prefix><el-icon><lock /></el-icon></template>
            </el-input>
          </el-form-item>
          <el-form-item>
            <CaptchaInput @update="onCaptcha" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="large" :loading="loading" @click="handleLogin" class="auth-submit-btn">
              {{ loading ? '登录中...' : '登 录' }}
            </el-button>
          </el-form-item>
        </el-form>

        <div class="auth-footer">
          <p class="auth-tip">
            还没有账号？<router-link to="/register">立即注册</router-link>
          </p>
          <div class="test-accounts">
            <p class="test-title">测试账号</p>
            <div class="test-account" @click="fillAccount('student', 'student123')">
              <span class="tag student">学员</span>
              <span>student / student123</span>
            </div>
            <div class="test-account" @click="fillAccount('admin', 'admin123')">
              <span class="tag admin">管理</span>
              <span>admin / admin123</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import CaptchaInput from '@/components/CaptchaInput.vue'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const form = reactive({ username: '', password: '' })
const captcha = reactive({ key: '', answer: 0 })
const loading = ref(false)
const formRef = ref()

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
}

function onCaptcha(key: string, answer: number) {
  captcha.key = key
  captcha.answer = answer
}

function fillAccount(username: string, password: string) {
  form.username = username
  form.password = password
}

async function handleLogin() {
  if (!formRef.value) return
  await formRef.value.validate(async (valid: boolean) => {
    if (!valid) return
    loading.value = true
    try {
      await userStore.login(form.username, form.password, captcha.key, captcha.answer)
      ElMessage.success(`🎉 登录成功，欢迎回来！`)
      const redirect = (route.query.redirect as string) || '/dashboard'
      router.push(redirect)
    } catch (err: any) {
      ElMessage.error(err.response?.data?.message || '登录失败，请检查用户名和密码')
    } finally {
      loading.value = false
    }
  })
}
</script>

<style scoped>
.auth-page {
  display: flex;
  min-height: 100vh;
  background: linear-gradient(135deg, #0a0b1a 0%, #1a1b3e 50%, #0a0b1a 100%);
}

/* 左侧装饰 */
.auth-decoration {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 48px;
  position: relative;
  overflow: hidden;
}
.auth-decoration::before {
  content: '';
  position: absolute;
  width: 400px;
  height: 400px;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(64,158,255,0.08) 0%, transparent 70%);
  top: 20%;
  left: 10%;
  animation: pulse 4s ease-in-out infinite;
}
@keyframes pulse {
  0%, 100% { transform: scale(1); opacity: 0.5; }
  50% { transform: scale(1.2); opacity: 0.8; }
}
.decoration-content { text-align: center; position: relative; z-index: 1; }
.deco-icon { font-size: 64px; color: #409eff; margin-bottom: 16px; }
.deco-title { font-size: 36px; font-weight: 700; background: linear-gradient(135deg, #667eea, #764ba2); -webkit-background-clip: text; -webkit-text-fill-color: transparent; margin-bottom: 12px; }
.deco-desc { color: #a0a0a0; font-size: 16px; line-height: 1.8; margin-bottom: 40px; }
.deco-features { display: flex; flex-direction: column; gap: 16px; }
.deco-feature { font-size: 18px; padding: 12px 24px; background: rgba(255,255,255,0.03); border-radius: 12px; border: 1px solid rgba(255,255,255,0.06); }

/* 右侧表单 */
.auth-form-area {
  width: 480px;
  display: flex;
  align-items: center;
  padding: 48px;
}
.auth-card { width: 100%; padding: 48px; }
.auth-header { text-align: center; margin-bottom: 32px; }
.auth-title { font-size: 28px; font-weight: 700; }
.auth-subtitle { color: #a0a0a0; margin-top: 8px; font-size: 14px; }
.auth-form { margin-bottom: 24px; }
.auth-submit-btn { width: 100%; height: 48px; font-size: 16px; letter-spacing: 4px; }
.auth-footer { text-align: center; }
.auth-tip { color: #a0a0a0; font-size: 14px; }
.auth-tip a { color: #409eff; text-decoration: none; font-weight: 500; }
.test-accounts { margin-top: 20px; padding-top: 20px; border-top: 1px solid rgba(255,255,255,0.06); }
.test-title { font-size: 12px; color: #666; margin-bottom: 12px; }
.test-account { display: flex; align-items: center; gap: 12px; padding: 8px 12px; border-radius: 8px; cursor: pointer; transition: background 0.2s; font-size: 13px; color: #a0a0a0; }
.test-account:hover { background: rgba(64,158,255,0.06); }
.tag { font-size: 11px; padding: 2px 8px; border-radius: 4px; font-weight: 600; }
.tag.student { background: rgba(64,158,255,0.15); color: #409eff; }
.tag.admin { background: rgba(245,108,108,0.15); color: #f56c6c; }

@media (max-width: 900px) {
  .auth-decoration { display: none; }
  .auth-form-area { width: 100%; }
}
</style>