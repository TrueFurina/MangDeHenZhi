<template>
  <div class="legal-page">
    <AppHeader />
    <div class="legal-content">
      <h1>📄 用户服务协议</h1>
      <p class="last-updated">最后更新日期：2026年7月18日</p>

      <section>
        <h2>1. 接受条款</h2>
        <p>欢迎使用芒德矩阵（以下简称"本平台"）。通过注册或使用本平台服务，即表示您同意本协议的全部条款。如果您不同意，请勿注册或使用本平台。</p>
      </section>

      <section>
        <h2>2. 服务说明</h2>
        <p>本平台提供以下服务：</p>
        <ul>
          <li>职业技能在线测评：AI驱动的多维度能力评估</li>
          <li>元宇宙3D虚拟训练：沉浸式面试与技能实训</li>
          <li>课程学习：在线课程浏览与学习</li>
          <li>区块链证书认证：技能认证证书的签发与验证</li>
        </ul>
      </section>

      <section>
        <h2>3. 用户责任</h2>
        <p>您承诺：</p>
        <ul>
          <li>提供真实、准确的注册信息</li>
          <li>妥善保管账号密码，对账号下所有活动负责</li>
          <li>不利用本平台从事任何违法活动</li>
          <li>不干扰平台的正常运行</li>
        </ul>
      </section>

      <section>
        <h2>4. 知识产权</h2>
        <p>本平台所有内容（包括但不限于课程资料、测评题目、软件代码、设计）的知识产权归本平台所有。未经书面许可，不得复制、修改、传播。</p>
      </section>

      <section>
        <h2>5. 免责声明</h2>
        <p>本平台提供的测评结果仅供参考，不构成任何形式的认证或保证。平台不对因使用服务而产生的任何直接或间接损失承担责任。</p>
      </section>

      <section>
        <h2>6. 协议修改</h2>
        <p>本平台保留随时修改本协议的权利。修改后的协议将通过平台公告或站内信通知用户。继续使用服务即表示接受修改后的协议。</p>
      </section>

      <section>
        <h2>7. 联系我们</h2>
        <p>如有任何疑问，请联系：<a href="mailto:support@mangdehenzhi.com">support@mangdehenzhi.com</a></p>
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