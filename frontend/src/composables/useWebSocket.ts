import { ref, onUnmounted, type Ref } from 'vue'

export interface WSMessage {
  type: string
  [key: string]: any
}

export function useWebSocket(roomId: string, userId: string, username: string) {
  const connected = ref(false)
  const messages = ref<WSMessage[]>([])
  let ws: WebSocket | null = null
  let reconnectTimer: ReturnType<typeof setTimeout> | null = null

  const listeners = new Map<string, Set<(msg: WSMessage) => void>>()

  function connect() {
    const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:'
    const host = window.location.host
    const url = `${protocol}//${host}/ws/metaverse`

    ws = new WebSocket(url)

    ws.onopen = () => {
      connected.value = true
      // 发送加入房间消息
      send({ type: 'join', roomId, userId, username })
    }

    ws.onmessage = (event) => {
      try {
        const msg: WSMessage = JSON.parse(event.data)
        messages.value.push(msg)

        // 触发对应类型的监听器
        const typeListeners = listeners.get(msg.type)
        if (typeListeners) {
          typeListeners.forEach(cb => cb(msg))
        }
      } catch (e) {
        console.error('WS消息解析失败:', e)
      }
    }

    ws.onclose = () => {
      connected.value = false
      // 自动重连
      reconnectTimer = setTimeout(() => connect(), 3000)
    }

    ws.onerror = () => {
      ws?.close()
    }
  }

  function send(msg: WSMessage) {
    if (ws?.readyState === WebSocket.OPEN) {
      ws.send(JSON.stringify(msg))
    }
  }

  function on(type: string, cb: (msg: WSMessage) => void) {
    if (!listeners.has(type)) {
      listeners.set(type, new Set())
    }
    listeners.get(type)!.add(cb)
  }

  function off(type: string, cb: (msg: WSMessage) => void) {
    listeners.get(type)?.delete(cb)
  }

  function disconnect() {
    if (reconnectTimer) clearTimeout(reconnectTimer)
    if (ws) {
      send({ type: 'leave' })
      ws.close()
      ws = null
    }
    connected.value = false
  }

  // 组件卸载时自动断开
  onUnmounted(() => disconnect())

  return {
    connected,
    messages,
    connect,
    send,
    on,
    off,
    disconnect,
  }
}