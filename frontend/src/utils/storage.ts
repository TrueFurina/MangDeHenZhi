/**
 * 安全 Storage 包装器
 *
 * 当浏览器因隐私设置（如"阻止所有Cookie"）拒绝 DOM Storage 访问时，
 * 自动降级为内存存储，防止第三方库因 SecurityError 导致整站白屏。
 *
 * 参考: ponyjob.top 的 storage 降级方案
 */

type StorageType = 'localStorage' | 'sessionStorage'

interface SafeStorage {
  getItem(key: string): string | null
  setItem(key: string, value: string): void
  removeItem(key: string): void
  clear(): void
  readonly length: number
}

function createMemoryStorage(): SafeStorage {
  const memory = new Map<string, string>()
  return {
    getItem(key: string) { return memory.get(key) ?? null },
    setItem(key: string, value: string) { memory.set(key, String(value)) },
    removeItem(key: string) { memory.delete(key) },
    clear() { memory.clear() },
    get length() { return memory.size },
  }
}

function createSafeStorage(type: StorageType): SafeStorage {
  try {
    const original = window[type]
    const testKey = '__storage_test__'
    original.setItem(testKey, '1')
    original.removeItem(testKey)
    return original
  } catch {
    console.warn(
      `[SafeStorage] ${type} 被浏览器拒绝访问（可能禁用了Cookie），` +
      '已降级为内存存储。登录状态等数据在刷新后将丢失。'
    )
    return createMemoryStorage()
  }
}

export const safeLocalStorage = createSafeStorage('localStorage')
export const safeSessionStorage = createSafeStorage('sessionStorage')