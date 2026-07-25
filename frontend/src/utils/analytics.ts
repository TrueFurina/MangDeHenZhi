/**
 * 网站统计分析 — Umami 接入模块
 * 
 * Umami 是自建的开源统计分析工具，隐私友好，数据自主可控。
 * 
 * 使用方式：
 * 1. docker compose -f docker-compose.umami.yml up -d 启动 Umami 服务
 * 2. 后台创建站点获取跟踪 ID
 * 3. 设置环境变量 VITE_UMAMI_ID / VITE_UMAMI_URL
 * 
 * 访问地址: http://localhost:3000 (默认管理员 admin/umami)
 */

const UMAMI_URL = import.meta.env.VITE_UMAMI_URL || ''
const UMAMI_WEBSITE_ID = import.meta.env.VITE_UMAMI_ID || ''

/** 是否已启用 Umami */
const enabled = !!UMAMI_URL && !!UMAMI_WEBSITE_ID

/**
 * 记录页面访问
 */
export function trackPageView(path?: string) {
  if (!enabled) return

  const payload = {
    type: 'pageview',
    url: path || window.location.pathname,
    website: UMAMI_WEBSITE_ID,
    hostname: window.location.hostname,
    language: navigator.language,
    referrer: document.referrer || '',
    screen: `${window.screen.width}x${window.screen.height}`,
  }

  send(payload)
}

/**
 * 记录自定义事件
 * @param eventName 事件名称
 * @param eventData 事件数据
 */
export function trackEvent(eventName: string, eventData?: Record<string, string | number>) {
  if (!enabled) return

  const payload: Record<string, any> = {
    type: 'event',
    url: window.location.pathname,
    website: UMAMI_WEBSITE_ID,
    event_name: eventName,
    hostname: window.location.hostname,
  }

  if (eventData) {
    payload.event_data = JSON.stringify(eventData)
  }

  send(payload)
}

function send(payload: Record<string, any>) {
  try {
    const img = new Image()
    img.src = `${UMAMI_URL}/api/collect?${new URLSearchParams(
      Object.fromEntries(
        Object.entries(payload).map(([k, v]) => [k, String(v)])
      )
    )}`
  } catch (e) {
    console.debug('[Analytics] send failed:', e)
  }
}

/**
 * 初始化 Umami 页面跟踪（自动记录路由变化）
 * 在路由 afterEach 中调用
 */
export function initUmamiTracking() {
  if (!enabled) return

  // 记录初始页面
  trackPageView()

  // 监听路由变化
  window.addEventListener('popstate', () => {
    trackPageView()
  })

  console.log(`[Analytics] Umami 已启用: ${UMAMI_URL}`)
}

/**
 * 关键业务事件跟踪
 */
export const BusinessEvents = {
  /** 用户注册 */
  userRegistered: () => trackEvent('user_registered'),

  /** 用户登录 */
  userLoggedIn: () => trackEvent('user_logged_in'),

  /** 提交测评 */
  assessmentSubmitted: (score: number) => trackEvent('assessment_submitted', { score }),

  /** 证书签发 */
  certificateIssued: () => trackEvent('certificate_issued'),

  /** 进入元宇宙 */
  metaverseEntered: (sceneType: string) => trackEvent('metaverse_entered', { sceneType }),

  /** 课程浏览 */
  courseViewed: (courseId: number) => trackEvent('course_viewed', { courseId }),
}