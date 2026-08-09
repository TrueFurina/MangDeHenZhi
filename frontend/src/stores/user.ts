import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authApi, gamificationApi } from '@/api'
import { safeLocalStorage } from '@/utils/storage'
import type { User } from '@/types'

export const useUserStore = defineStore('user', () => {
  const token = ref<string | null>(null)
  const currentUser = ref<User | null>(null)

  const isLoggedIn = computed(() => !!token.value && !!currentUser.value)
  const isAdmin = computed(() => currentUser.value?.role === 'ADMIN')

  function initFromStorage() {
    const savedToken = safeLocalStorage.getItem('token')
    const savedUser = safeLocalStorage.getItem('user')
    if (savedToken) token.value = savedToken
    if (savedUser) {
      try {
        currentUser.value = JSON.parse(savedUser)
      } catch (e) {
        // F7: 本地数据损坏时清空，避免解析失败导致白屏
        safeLocalStorage.removeItem('user')
        console.error('解析本地用户数据失败，已重置', e)
      }
    }
  }

  async function login(username: string, password: string, captchaKey?: string, captchaAnswer?: number) {
    const res = await authApi.login({ username, password, captchaKey, captchaAnswer })
    token.value = res.data.token
    currentUser.value = res.data.user
    safeLocalStorage.setItem('token', res.data.token)
    safeLocalStorage.setItem('user', JSON.stringify(res.data.user))
    // 游戏化：登录成功上报 XP（静默失败，不影响登录）
    gamificationApi.addXp('LOGIN').catch(() => {})
    return res.data
  }

  async function register(data: {
    username: string
    password: string
    email: string
    nickname?: string
  }) {
    const res = await authApi.register(data)
    token.value = res.data.token
    currentUser.value = res.data.user
    safeLocalStorage.setItem('token', res.data.token)
    safeLocalStorage.setItem('user', JSON.stringify(res.data.user))
    return res.data
  }

  function logout() {
    token.value = null
    currentUser.value = null
    safeLocalStorage.removeItem('token')
    safeLocalStorage.removeItem('user')
  }

  return {
    token,
    currentUser,
    isLoggedIn,
    isAdmin,
    initFromStorage,
    login,
    register,
    logout,
  }
})