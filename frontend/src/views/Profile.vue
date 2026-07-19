<template>
  <div class="profile-page">
    <AppHeader />
    <div class="profile-content">
      <h1>👤 个人资料</h1>
      <div class="profile-layout">
        <div class="glass-card profile-sidebar">
          <div class="avatar-section">
            <el-avatar :size="80" :src="form.avatar || undefined">{{ form.nickname?.[0] || userStore.currentUser?.username?.[0] }}</el-avatar>
            <h3>{{ form.nickname || userStore.currentUser?.username }}</h3>
            <p class="role-tag">
              <el-tag :type="userStore.currentUser?.role === 'ADMIN' ? 'danger' : 'info'">
                {{ userStore.currentUser?.role }}
              </el-tag>
            </p>
          </div>
        </div>
        <div class="glass-card profile-form">
          <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
            <el-form-item label="用户名">
              <el-input :model-value="userStore.currentUser?.username" disabled />
            </el-form-item>
            <el-form-item label="昵称" prop="nickname">
              <el-input v-model="form.nickname" placeholder="设置昵称" />
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" placeholder="邮箱地址" />
            </el-form-item>
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="form.phone" placeholder="手机号码" />
            </el-form-item>
            <el-form-item label="头像URL" prop="avatar">
              <el-input v-model="form.avatar" placeholder="头像图片链接" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :loading="saving" @click="saveProfile">保存修改</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { userApi } from '@/api'
import AppHeader from '@/components/AppHeader.vue'

const userStore = useUserStore()
const formRef = ref()
const saving = ref(false)

const form = reactive({
  nickname: '',
  email: '',
  phone: '',
  avatar: '',
})

const rules = {
  email: [{ type: 'email', message: '请输入有效邮箱', trigger: 'blur' }],
}

onMounted(() => {
  const user = userStore.currentUser
  if (user) {
    form.nickname = user.nickname || ''
    form.email = user.email || ''
    form.phone = user.phone || ''
    form.avatar = user.avatar || ''
  }
})

async function saveProfile() {
  if (!formRef.value) return
  await formRef.value.validate(async (valid: boolean) => {
    if (!valid) return
    saving.value = true
    try {
      const res = await userApi.updateProfile(form)
      userStore.currentUser = res.data
      localStorage.setItem('user', JSON.stringify(res.data))
      ElMessage.success('资料更新成功')
    } catch (err: any) {
      ElMessage.error(err.response?.data?.message || '更新失败')
    } finally {
      saving.value = false
    }
  })
}
</script>

<style scoped>
.profile-content { padding: 32px 48px; max-width: 1000px; margin: 0 auto; }
.profile-layout { display: grid; grid-template-columns: 280px 1fr; gap: 24px; margin-top: 24px; }
.profile-sidebar { text-align: center; padding: 32px; }
.avatar-section { display: flex; flex-direction: column; align-items: center; gap: 12px; }
.role-tag { margin-top: 4px; }
.profile-form { padding: 32px; }
</style>