// ===== 类型定义 =====

export interface User {
  id: number
  username: string
  email: string
  phone?: string
  nickname?: string
  avatar?: string
  role: 'ADMIN' | 'TEACHER' | 'STUDENT'
  enabled: boolean
}

export interface LoginResponse {
  token: string
  tokenType: string
  user: User
}

export interface Assessment {
  id: number
  title: string
  description: string
  difficulty: string
  dimensions: string[]
  duration: number
  totalScore: number
  passScore: number
  status: string
  createdAt: string
}

// 测评题目
export interface Question {
  id: string
  dimension: string
  text: string
  type: 'single' | 'multiple' | 'scale'
  options: QuestionOption[]
}

export interface QuestionOption {
  value: number
  label: string
  score: number
}

// 测评题目组（按维度分组）
export interface QuestionGroup {
  dimension: string
  label: string
  icon: string
  questions: Question[]
}

export interface AssessmentResult {
  id: number
  assessmentId: number
  assessmentTitle: string
  userId: number
  username: string
  score: number
  passed: boolean
  dimensionScores: Record<string, number>
  aiAnalysis: string
  recommendations: string
  completedAt: string
}

// AI 推荐结果。AssessmentResult.recommendations 是这份结构序列化后的 JSON 字符串，
// 前端 JSON.parse 后使用；字段与后端
// backend/src/main/java/com/mangdehenzhi/ai/RecommendationResult.java 一一对应。
//
// 为什么要显式声明：JSON.parse 返回 any，any 会传染到模板里。vue-tsc 3 对 any 上的
// v-for 只能给出索引类型 string | number，于是 `{{ i + 1 }}` 被误判为 TS2365。
// 声明成 string[] 后索引才是 number——这是修根因，不是把类型断言塞进模板。
export interface Recommendations {
  recommendedCourses?: string[]
  learningPath?: string[]
  estimatedImprovementTime?: string
  practiceSuggestions?: string[]
}

export interface Course {
  id: number
  title: string
  description: string
  coverImage?: string
  category: string
  difficulty: string
  duration: number
  price: number
  published: boolean
  instructor?: User
  enrollmentCount: number
  prerequisites?: string
}

export interface Certification {
  id: number
  certHash: string
  title: string
  description: string
  blockchainTxId?: string
  status: string
  issuedAt: string
}

export interface MetaverseSession {
  id: number
  sessionName: string
  sceneType: string
  roomId: string
  active: boolean
  startTime: string
  endTime?: string
  sceneConfig?: string
}

export interface ApiResponse<T> {
  code: number
  message: string
  data: T
}

// ===== 招聘 / 职位 =====

export interface Job {
  id: number
  title: string
  company: string
  location?: string
  industry?: string
  degree?: string
  salary?: string
  major?: string
  description?: string
  requirements?: string | string[]
  source?: string
  applyUrl?: string
  createdAt?: string
}

export interface JobMatchResult {
  job: Job
  matchScore: number
  reason?: string
}

export type ApplicationStatus = 'DRAFT' | 'SUBMITTED' | 'REJECTED' | 'ACCEPTED'

export interface Application {
  id: number
  companyName: string
  positionName: string
  status: ApplicationStatus
  createdAt: string
  updatedAt?: string
  formData?: Record<string, string>
}