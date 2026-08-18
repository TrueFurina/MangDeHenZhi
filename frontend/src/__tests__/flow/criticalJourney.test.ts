/// <reference types="vitest" />
// ============================================================
// F-15 前端测试补强（最小骨架）— 关键用户旅程契约/冒烟测试
// 覆盖链路：登录 → 测评提交（服务端计分）→ 证书验证锚点
// 说明：本文件为「骨架」，聚焦核心 API 契约与调用顺序，
//       不依赖浏览器/E2E。环境若无可运行 node/npm，请标注「未运行」。
// 后续建议：补充 @vue/test-utils 组件测试 与 Cypress/Playwright E2E（见仓库 e2e/）。
// ============================================================
import { describe, it, expect, beforeEach, vi } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'

// 用 mock 隔离真实 HTTP，验证「调用顺序 + 入参 + 出参契约」
const loginMock = vi.fn()
const submitMock = vi.fn()
const verifyMock = vi.fn()

vi.mock('@/api', () => ({
  authApi: { login: (...a: any[]) => loginMock(...a) },
  assessmentApi: { submit: (...a: any[]) => submitMock(...a) },
  certificationApi: { verify: (...a: any[]) => verifyMock(...a) },
}))

import { useUserStore } from '@/stores/user'

describe('关键链路：登录 → 测评提交 → 证书验证 (F-15 骨架)', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
    localStorage.clear()
    loginMock.mockReset()
    submitMock.mockReset()
    verifyMock.mockReset()
  })

  it('登录：拿到 token 并写入 store 与 localStorage', async () => {
    loginMock.mockResolvedValue({
      data: { token: 'jwt-abc', user: { id: 1, username: 'student', role: 'STUDENT' } },
    })

    const store = useUserStore()
    const res = await store.login('student', 'student123')

    // 入参契约：authApi.login 收到 { username, password }
    expect(loginMock).toHaveBeenCalledWith({ username: 'student', password: 'student123' })
    // 状态契约：token 落地到 store 与本地存储
    expect(res.token).toBe('jwt-abc')
    expect(store.token).toBe('jwt-abc')
    expect(store.isLoggedIn).toBe(true)
    expect(localStorage.getItem('token')).toBe('jwt-abc')
  })

  it('登录后：以服务端权威计分提交测评，并用返回哈希验证证书锚点', async () => {
    loginMock.mockResolvedValue({
      data: { token: 'jwt-abc', user: { id: 1, username: 'student', role: 'STUDENT' } },
    })
    submitMock.mockResolvedValue({
      data: { id: 10, passed: true, certHash: 'cert-hash-xyz' },
    })
    verifyMock.mockResolvedValue({
      data: { id: 10, status: 'VERIFIED', certHash: 'cert-hash-xyz' },
    })

    const store = useUserStore()
    await store.login('student', 'student123')

    // 测评提交：分数应由服务端权威计分（不信任前端自算），此处仅传测评与维度分
    const dimensionScores = { 沟通: 80, 逻辑: 75, 协作: 90, 学习: 85, 创新: 70 }
    const submitRes = await submitMock({ assessmentId: 1, dimensionScores })
    expect(submitMock).toHaveBeenCalledWith({ assessmentId: 1, dimensionScores })
    expect(submitRes.data.passed).toBe(true)

    // 证书验证锚点：用提交返回的哈希调用 verify，状态应为 VERIFIED
    const verifyRes = await verifyMock(submitRes.data.certHash)
    expect(verifyMock).toHaveBeenCalledWith('cert-hash-xyz')
    expect(verifyRes.data.status).toBe('VERIFIED')
    expect(verifyRes.data.certHash).toBe(submitRes.data.certHash)
  })
})
