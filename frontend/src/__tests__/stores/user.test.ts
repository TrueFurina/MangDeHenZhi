/// <reference types="vitest" />
import { describe, it, expect, beforeEach, vi } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useUserStore } from '@/stores/user'

// Mock the API module
vi.mock('@/api', () => ({
  authApi: {
    login: vi.fn(),
    register: vi.fn(),
  },
}))

describe('UserStore', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
    localStorage.clear()
  })

  it('should initialize with null token and user', () => {
    const store = useUserStore()
    expect(store.token).toBeNull()
    expect(store.currentUser).toBeNull()
    expect(store.isLoggedIn).toBe(false)
  })

  it('should restore from localStorage', () => {
    const mockUser = { id: 1, username: 'testuser', role: 'STUDENT' }
    localStorage.setItem('token', 'test-token')
    localStorage.setItem('user', JSON.stringify(mockUser))

    const store = useUserStore()
    store.initFromStorage()

    expect(store.token).toBe('test-token')
    expect(store.currentUser).toEqual(mockUser)
    expect(store.isLoggedIn).toBe(true)
  })

  it('should clear state on logout', () => {
    const mockUser = { id: 1, username: 'testuser', role: 'STUDENT' }
    localStorage.setItem('token', 'test-token')
    localStorage.setItem('user', JSON.stringify(mockUser))

    const store = useUserStore()
    store.initFromStorage()
    store.logout()

    expect(store.token).toBeNull()
    expect(store.currentUser).toBeNull()
    expect(store.isLoggedIn).toBe(false)
    expect(localStorage.getItem('token')).toBeNull()
    expect(localStorage.getItem('user')).toBeNull()
  })

  it('isAdmin should be true for ADMIN role', () => {
    const store = useUserStore()
    store.currentUser = { id: 1, username: 'admin', role: 'ADMIN' } as any
    store.token = 'admin-token'

    expect(store.isAdmin).toBe(true)
  })

  it('isAdmin should be false for STUDENT role', () => {
    const store = useUserStore()
    store.currentUser = { id: 2, username: 'student', role: 'STUDENT' } as any
    store.token = 'student-token'

    expect(store.isAdmin).toBe(false)
  })
})