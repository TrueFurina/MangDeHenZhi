<template>
  <div class="auth-page">
    <div class="auth-decoration">
      <div class="decoration-content">
        <div class="deco-icon">◆</div>
        <h1 class="deco-title">芒得很职</h1>
        <p class="deco-desc">开启你的职业技能提升之旅<br/>AI 智能测评 · 元宇宙训练 · 区块链认证</p>
        <div class="deco-quote">
          <p>"学无止境，技无止境"</p>
        </div>
      </div>
    </div>

    <div class="auth-form-area">
      <div class="auth-card glass-card">
        <div class="auth-header">
          <h2 class="auth-title">创建账号</h2>
          <p class="auth-subtitle">注册成为会员，开始你的学习之旅</p>
        </div>

        <el-form :model="form" :rules="rules" ref="formRef" @submit.prevent="handleRegister" class="auth-form">
          <el-form-item prop="username">
            <el-input v-model="form.username" placeholder="用户名" size="large" clearable maxlength="30" show-word-limit>
              <template #prefix><el-icon><user /></el-icon></template>
            </el-input>
          </el-form-item>
          <el-form-item prop="email">
            <el-input v-model="form.email" placeholder="邮箱" size="large" clearable>
              <template #prefix><el-icon><message /></el-icon></template>
            </el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="form.password" type="password" placeholder="密码（至少6位）" size="large" show-password clearable>
              <template #prefix><el-icon><lock /></el-icon></template>
            </el-input>
          </el-form-item>
          <el-form-item prop="confirmPassword">
            <el-input v-model="form.confirmPassword" type="password" placeholder="确认密码" size="large" show-password clearable>
              <template #prefix><el-icon><lock /></el-icon></template>
            </el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="large" :loading="loading" @click="handleRegister" class="auth-submit-btn">
              {{ loading ? '注册中...' : '注 册' }}
            </el-button>
          </el-form-item>
        </el-form>

        <div class="auth-footer">
          <p class="auth-tip">已有账号？<router-link to="/login">立即登录</router-link></p>
          <div class="terms">
            注册即表示同意 <a href="#">服务条款</a> 和 <a href="#">隐私政策</a>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

const form = reactive({
  username: '',
  email: '',
  password: '',
  confirmPassword: '',
})
const loading = ref(false)
const formRef = ref()

const validatePass2 = (_rule: any, value: string, callback: any) => {
  if (value !== form.password) callback(new Error('两次密码不一致'))
  else callback()
}

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 30, message: '用户名长度3-30位', trigger: 'blur' },
  ],
  email: [
    { required: true, type: 'email', message: '请输入有效邮箱', trigger: 'blur' },
  ],
  password: [
    { required: true, min: 6, message: '密码至少6位', trigger: 'blur' },
  ],
  confirmPassword: [
    { required: true, validator: validatePass2, trigger: 'blur' },
  ],
}

async function handleRegister() {
  if (!formRef.value) return
  await formRef.value.validate(async (valid: boolean) => {
    if (!valid) return
    loading.value = true
    try {
      await userStore.register({
        username: form.username,
        password: form.password,
        email: form.email,
      })
      ElMessage.success('🎉 注册成功！欢迎加入芒得很职')
      router.push('/dashboard')
    } catch (err: any) {
      ElMessage.error(err.response?.data?.message || '注册失败，请稍后重试')
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
  background: radial-gradient(circle, rgba(103,194,58,0.08) 0%, transparent 70%);
  top: 30%;
  right: 10%;
  animation: pulse 4s ease-in-out infinite;
}
@keyframes pulse {
  0%, 100% { transform: scale(1); opacity: 0.5; }
  50% { transform: scale(1.2); opacity: 0.8; }
}
.decoration-content { text-align: center; position: relative; z-index: 1; }
.deco-icon { font-size: 64px; color: #67c23a; margin-bottom: 16px; }
.deco-title { font-size: 36px; font-weight: 700; background: linear-gradient(135deg, #667eea, #764ba2); -webkit-background-clip: text; -webkit-text-fill-color: transparent; margin-bottom: 12px; }
.deco-desc { color: #a0a0a0; font-size: 16px; line-height: 1.8; margin-bottom: 40px; }
.deco-quote { padding: 20px 32px; background: rgba(255,255,255,0.03); border-radius: 16px; border: 1px solid rgba(255,255,255,0.06); }
.deco-quote p { font-size: 20px; color: #667eea; font-style: italic; }

.auth-form-area { width: 480px; display: flex; align-items: center; padding: 48px; }
.auth-card { width: 100%; padding: 48px; }
.auth-header { text-align: center; margin-bottom: 32px; }
.auth-title { font-size: 28px; font-weight: 700; }
.auth-subtitle { color: #a0a0a0; margin-top: 8px; font-size: 14px; }
.auth-form { margin-bottom: 24px; }
.auth-submit-btn { width: 100%; height: 48px; font-size: 16px; letter-spacing: 4px; }
.auth-footer { text-align: center; }
.auth-tip { color: #a0a0a0; font-size: 14px; }
.auth-tip a { color: #409eff; text-decoration: none; font-weight: 500; }
.terms { font-size: 12px; color: #666; margin-top: 16px; }
.terms a { color: #409eff; text-decoration: none; }

@media (max-width: 900px) {
  .auth-decoration { display: none; }
  .auth-form-area { width: 100%; }
}
</style>