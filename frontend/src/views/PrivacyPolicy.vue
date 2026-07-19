<template>
  <div class="legal-page">
    <AppHeader />
    <div class="legal-content">
      <h1>🔒 隐私政策</h1>
      <p class="last-updated">最后更新日期：2026年7月18日</p>

      <section>
        <h2>1. 信息收集</h2>
        <p>我们收集以下信息：</p>
        <ul>
          <li><strong>注册信息</strong>：用户名、邮箱地址、密码（加密存储）</li>
          <li><strong>个人资料</strong>：昵称、手机号、头像（您自愿提供）</li>
          <li><strong>测评数据</strong>：您的测评答案、得分、AI分析报告</li>
          <li><strong>使用数据</strong>：课程学习记录、元宇宙场景交互数据</li>
          <li><strong>技术数据</strong>：IP地址、浏览器类型、设备信息</li>
        </ul>
      </section>

      <section>
        <h2>2. 信息使用</h2>
        <p>我们使用您的信息用于：</p>
        <ul>
          <li>提供和维护平台服务</li>
          <li>生成个性化测评分析和学习推荐</li>
          <li>签发区块链认证证书</li>
          <li>改善用户体验和平台功能</li>
          <li>发送服务通知和更新</li>
        </ul>
      </section>

      <section>
        <h2>3. 信息保护</h2>
        <p>我们采取以下安全措施保护您的信息：</p>
        <ul>
          <li>密码使用 BCrypt 加密存储</li>
          <li>通信使用 HTTPS 加密传输</li>
          <li>JWT Token 认证，限时有效</li>
          <li>API 接口限流防暴力破解</li>
          <li>服务器安全防护与监控</li>
        </ul>
      </section>

      <section>
        <h2>4. 信息共享</h2>
        <p>我们不会将您的个人信息出售给第三方。在以下情况下可能共享：</p>
        <ul>
          <li>获得您的明确同意</li>
          <li>法律要求或政府机构要求</li>
          <li>保护平台权利和安全</li>
        </ul>
      </section>

      <section>
        <h2>5. 区块链存证</h2>
        <p>当您通过测评并获得证书时，证书哈希值将被记录在区块链上。区块链上的数据是公开且不可篡改的，但仅包含证书哈希，不包含您的个人身份信息。</p>
      </section>

      <section>
        <h2>6. 您的权利</h2>
        <p>您有权：</p>
        <ul>
          <li>访问和修改您的个人信息</li>
          <li>删除您的账号和相关数据</li>
          <li>导出您的测评数据</li>
          <li>撤回同意（可能影响部分服务）</li>
        </ul>
      </section>

      <section>
        <h2>7. 联系我们</h2>
        <p>如有隐私相关问题，请联系：<a href="mailto:privacy@mangdehenzhi.com">privacy@mangdehenzhi.com</a></p>
      </section>
    </div>
  </div>
</template>

<script setup lang="ts">
import AppHeader from '@/components/AppHeader.vue'
</script>

<style scoped>
.legal-content {
  max-width: 800px;
  margin: 0 auto;
  padding: 32px 48px;
}
.legal-content h1 { font-size: 28px; margin-bottom: 8px; }
.last-updated { color: #666; font-size: 14px; margin-bottom: 32px; }
.legal-content section { margin-bottom: 24px; }
.legal-content h2 { font-size: 18px; margin-bottom: 8px; color: var(--primary-color); }
.legal-content p { line-height: 1.8; color: #a0a0a0; }
.legal-content ul { padding-left: 20px; }
.legal-content li { line-height: 2; color: #a0a0a0; }
.legal-content a { color: var(--primary-color); }
</style>