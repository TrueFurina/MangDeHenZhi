import axios from 'axios'
import type { ApiResponse, LoginResponse, Assessment, AssessmentResult, Course, Certification, MetaverseSession, User } from '@/types'
import router from '@/router'

const http = axios.create({
  baseURL: '/api',
  timeout: 30000,
  headers: { 'Content-Type': 'application/json' },
})

// 请求拦截器 - 自动添加Token
http.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// 响应拦截器 - 统一处理错误
http.interceptors.response.use(
  (response) => response.data,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      router.push('/login')
    }
    return Promise.reject(error)
  },
)

export default http

// ===== Auth API =====
export const authApi = {
  login: (data: { username: string; password: string }) =>
    http.post<any, ApiResponse<LoginResponse>>('/auth/login', data),
  register: (data: {
    username: string
    password: string
    email: string
    nickname?: string
  }) => http.post<any, ApiResponse<LoginResponse>>('/auth/register', data),
}

// ===== Assessment API =====
export const assessmentApi = {
  getAll: () => http.get<any, ApiResponse<Assessment[]>>('/assessments'),
  getById: (id: number) => http.get<any, ApiResponse<Assessment>>(`/assessments/${id}`),
  submit: (data: { assessmentId: number; dimensionScores: Record<string, number> }) =>
    http.post<any, ApiResponse<AssessmentResult>>('/assessments/submit', data),
  getMyResults: () => http.get<any, ApiResponse<AssessmentResult[]>>('/assessments/results'),
  getResultById: (resultId: number) =>
    http.get<any, ApiResponse<AssessmentResult>>(`/assessments/results/${resultId}`),
}

// ===== Course API =====
export const courseApi = {
  getAll: () => http.get<any, ApiResponse<Course[]>>('/courses'),
  getById: (id: number) => http.get<any, ApiResponse<Course>>(`/courses/${id}`),
  getByCategory: (category: string) =>
    http.get<any, ApiResponse<Course[]>>(`/courses/category/${category}`),
}

// ===== Certification API =====
export const certificationApi = {
  getMy: () => http.get<any, ApiResponse<Certification[]>>('/certifications/my'),
  verify: (certHash: string) =>
    http.get<any, ApiResponse<Certification>>(`/certifications/verify/${certHash}`),
}

// ===== Metaverse API =====
export const metaverseApi = {
  getMySessions: () => http.get<any, ApiResponse<MetaverseSession[]>>('/metaverse/sessions'),
  createSession: (data: { sessionName: string; sceneType: string; sceneConfig?: string }) =>
    http.post<any, ApiResponse<MetaverseSession>>('/metaverse/sessions', data),
  endSession: (id: number) =>
    http.post<any, ApiResponse<MetaverseSession>>(`/metaverse/sessions/${id}/end`),
  getSceneConfig: (sceneType: string) =>
    http.get<any, ApiResponse<string>>(`/metaverse/scene-config/${sceneType}`),
}

// ===== Search API =====
export const searchApi = {
  courses: (q: string) =>
    http.get<any, ApiResponse<Course[]>>(`/search/courses?q=${encodeURIComponent(q)}`),
}

// ===== Admin API =====
export const adminApi = {
  getUsers: () => http.get<any, ApiResponse<User[]>>('/admin/users'),
  getUser: (id: number) => http.get<any, ApiResponse<User>>(`/admin/users/${id}`),
  updateUserRole: (id: number, role: string) =>
    http.put<any, ApiResponse<User>>(`/admin/users/${id}/role`, { role }),
  toggleUserStatus: (id: number) =>
    http.put<any, ApiResponse<User>>(`/admin/users/${id}/toggle-status`),
  getCourses: (page = 0, size = 20) =>
    http.get<any, ApiResponse<Course[]>>(`/admin/courses?page=${page}&size=${size}`),
  createCourse: (data: Partial<Course>) =>
    http.post<any, ApiResponse<Course>>('/admin/courses', data),
  updateCourse: (id: number, data: Partial<Course>) =>
    http.put<any, ApiResponse<Course>>(`/admin/courses/${id}`, data),
  deleteCourse: (id: number) =>
    http.delete<any, ApiResponse<void>>(`/admin/courses/${id}`),
  getStats: () => http.get<any, ApiResponse<{ userCount: number; courseCount: number }>>('/admin/stats'),
}