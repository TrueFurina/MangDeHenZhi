import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { VitePWA } from 'vite-plugin-pwa'
import { fileURLToPath, URL } from 'node:url'

export default defineConfig({
  plugins: [
    vue(),
    VitePWA({
      registerType: 'autoUpdate',
      includeAssets: ['favicon.svg'],
      manifest: {
        name: '芒德矩阵',
        short_name: '芒德矩阵',
        description: 'AI + 元宇宙 + 区块链 职业技能培训认证平台',
        theme_color: '#409eff',
        background_color: '#0a0b1a',
        display: 'standalone',
        orientation: 'portrait-primary',
        scope: '/',
        start_url: '/',
        lang: 'zh-CN',
        categories: ['education', 'technology'],
        icons: [
          {
            src: '/favicon.svg',
            sizes: 'any',
            type: 'image/svg+xml',
            purpose: 'any maskable',
          },
        ],
      },
      workbox: {
        globPatterns: ['**/*.{js,css,html,svg,png,ico,ttf,woff2}'],
        runtimeCaching: [
          {
            urlPattern: /^https?:\/\/.*\/api\/.*/i,
            handler: 'NetworkFirst',
            options: {
              cacheName: 'api-cache',
              expiration: { maxEntries: 50, maxAgeSeconds: 300 },
            },
          },
        ],
      },
    }),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
  server: {
    port: 5173,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
  build: {
    rollupOptions: {
      external: ['fsevents'],
      output: {
        // Vite 8 底层换成 rolldown 后，`manualChunks` 只接受**函数**形式；
        // 原来的对象形式（'vendor-vue': ['vue', ...]）会直接报
        //   TypeError: manualChunks is not a function
        // 并让 `vite build` 失败。这里改写为等价的函数形式。
        manualChunks(id: string) {
          const p = id.replace(/\\/g, '/')
          if (!p.includes('/node_modules/')) return
          if (/\/node_modules\/(vue|vue-router|pinia|@vue)\//.test(p)) return 'vendor-vue'
          if (/\/node_modules\/(element-plus|@element-plus\/icons-vue)\//.test(p)) return 'vendor-element'
          if (/\/node_modules\/three\//.test(p)) return 'vendor-three'
          if (/\/node_modules\/(echarts|vue-echarts)\//.test(p)) return 'vendor-charts'
          if (/\/node_modules\/(vite-plugin-pwa|workbox-)/.test(p)) return 'vendor-pwa'
          if (/\/node_modules\/axios\//.test(p)) return 'vendor-utils'
        },
      },
    },
    chunkSizeWarningLimit: 500,
    cssCodeSplit: true,
  },
})