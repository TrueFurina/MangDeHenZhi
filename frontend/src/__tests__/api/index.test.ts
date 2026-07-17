import { describe, it, expect, vi, beforeEach } from 'vitest'

// Mock axios
vi.mock('axios', () => ({
  default: {
    create: vi.fn(() => ({
      interceptors: {
        request: { use: vi.fn() },
        response: { use: vi.fn() },
      },
      get: vi.fn(),
      post: vi.fn(),
    })),
  },
}))

describe('Auth API', () => {
  beforeEach(() => {
    vi.resetModules()
    localStorage.clear()
  })

  it('should have login function', async () => {
    const { authApi } = await import('@/api')
    expect(authApi.login).toBeDefined()
    expect(typeof authApi.login).toBe('function')
  })

  it('should have register function', async () => {
    const { authApi } = await import('@/api')
    expect(authApi.register).toBeDefined()
    expect(typeof authApi.register).toBe('function')
  })

  it('should have assessment API functions', async () => {
    const { assessmentApi } = await import('@/api')
    expect(assessmentApi.getAll).toBeDefined()
    expect(assessmentApi.submit).toBeDefined()
    expect(assessmentApi.getMyResults).toBeDefined()
  })
})

describe('Router', () => {
  it('should have the correct routes', async () => {
    const { default: router } = await import('@/router')
    const routes = router.getRoutes()

    expect(routes.some(r => r.name === 'Home')).toBe(true)
    expect(routes.some(r => r.name === 'Login')).toBe(true)
    expect(routes.some(r => r.name === 'Register')).toBe(true)
    expect(routes.some(r => r.name === 'Dashboard')).toBe(true)
    expect(routes.some(r => r.name === 'Assessments')).toBe(true)
    expect(routes.some(r => r.name === 'Courses')).toBe(true)
    expect(routes.some(r => r.name === 'Metaverse')).toBe(true)
    expect(routes.some(r => r.name === 'Certifications')).toBe(true)
  })

  it('should have auth guard on protected routes', async () => {
    const { default: router } = await import('@/router')
    const dashboardRoute = router.getRoutes().find(r => r.name === 'Dashboard')
    expect(dashboardRoute?.meta?.requiresAuth).toBe(true)
  })
})