import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/Home.vue'),
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue'),
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('@/views/Dashboard.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/assessments',
    name: 'Assessments',
    component: () => import('@/views/AssessmentList.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/assessments/:id',
    name: 'AssessmentDetail',
    component: () => import('@/views/AssessmentDetail.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/assessments/results/:resultId',
    name: 'AssessmentResult',
    component: () => import('@/views/AssessmentResult.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/courses',
    name: 'Courses',
    component: () => import('@/views/Courses.vue'),
  },
  {
    path: '/courses/:id',
    name: 'CourseDetail',
    component: () => import('@/views/CourseDetail.vue'),
  },
  {
    path: '/metaverse',
    name: 'Metaverse',
    component: () => import('@/views/Metaverse.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/certifications',
    name: 'Certifications',
    component: () => import('@/views/Certifications.vue'),
    meta: { requiresAuth: true },
  },
  // 404 页面
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/NotFound.vue'),
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

// 路由守卫
router.beforeEach((to, _from, next) => {
  const token = localStorage.getItem('token')
  if (to.meta.requiresAuth && !token) {
    next({ name: 'Login', query: { redirect: to.fullPath } })
  } else {
    next()
  }
})

export default router