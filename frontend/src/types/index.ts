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