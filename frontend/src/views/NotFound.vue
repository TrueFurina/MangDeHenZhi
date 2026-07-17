<template>
  <div class="not-found-page">
    <div class="not-found-content">
      <div class="nf-code">404</div>
      <div class="nf-icon">🔍</div>
      <h1>页面未找到</h1>
      <p>你访问的页面不存在，可能已被移除或地址输入有误</p>
      <div class="nf-actions">
        <router-link to="/">
          <el-button type="primary" size="large" round>
            <el-icon><home-filled /></el-icon> 返回首页
          </el-button>
        </router-link>
        <router-link to="/courses">
          <el-button size="large" round>浏览课程</el-button>
        </router-link>
      </div>
      <div class="nf-links">
        <p>你可能想找：</p>
        <div class="nf-suggestions">
          <router-link to="/assessments">📝 技能测评</router-link>
          <router-link to="/metaverse">🌐 元宇宙训练</router-link>
          <router-link to="/certifications">🏆 我的证书</router-link>
          <router-link to="/login">🔑 登录</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.not-found-page {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #0a0b1a 0%, #1a1b3e 50%, #0a0b1a 100%);
  padding: 48px;
}
.not-found-content {
  text-align: center;
  max-width: 500px;
}
.nf-code {
  font-size: 120px;
  font-weight: 800;
  background: linear-gradient(135deg, #667eea, #764ba2);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  line-height: 1;
  margin-bottom: 16px;
  opacity: 0.8;
}
.nf-icon {
  font-size: 64px;
  margin-bottom: 24px;
  animation: float 3s ease-in-out infinite;
}
@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-10px); }
}
.not-found-content h1 {
  font-size: 28px;
  margin-bottom: 12px;
}
.not-found-content p {
  color: #a0a0a0;
  font-size: 16px;
  line-height: 1.6;
  margin-bottom: 32px;
}
.nf-actions {
  display: flex;
  gap: 16px;
  justify-content: center;
  margin-bottom: 48px;
}
.nf-links p {
  font-size: 14px;
  color: #666;
  margin-bottom: 16px;
}
.nf-suggestions {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  justify-content: center;
}
.nf-suggestions a {
  padding: 8px 20px;
  border-radius: 20px;
  background: rgba(255,255,255,0.04);
  color: #a0a0a0;
  text-decoration: none;
  font-size: 14px;
  transition: all 0.2s;
}
.nf-suggestions a:hover {
  background: rgba(64,158,255,0.1);
  color: #409eff;
}
</style>