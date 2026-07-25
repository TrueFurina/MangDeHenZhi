# CDN 部署配置
# 构建时将静态资源上传到 CDN，提升全球加载速度

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { VitePWA } from 'vite-plugin-pwa'
import { fileURLToPath, URL } from 'node:url'

const isCDN = process.env.CDN === 'true'
const cdnBase = process.env.CDN_BASE || 'https://assets.mangdehenzhi.com/prod'

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
        manualChunks: {
          'vendor-vue': ['vue', 'vue-router', 'pinia'],
          'vendor-element': ['element-plus', '@element-plus/icons-vue'],
          'vendor-three': ['three'],
          'vendor-charts': ['echarts', 'vue-echarts'],
          'vendor-pwa': ['vite-plugin-pwa'],
          'vendor-utils': ['axios'],
        },
      },
    },
    chunkSizeWarningLimit: 500,
    cssCodeSplit: true,
  },
}).then(config => {
  if (isCDN && cdnBase) {
    config.base = cdnBase
  }
  return config
})