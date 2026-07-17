<template>
  <div class="home">
    <NavBar variant="home" />

    <!-- Hero 区域 -->
    <section class="hero">
      <div class="hero-content">
        <div class="hero-badge">🚀 下一代职业技能培训平台</div>
        <h1 class="hero-title">
          <span class="gradient-text">AI + 元宇宙</span><br />
          重新定义技能培训
        </h1>
        <p class="hero-subtitle">
          沉浸式3D虚拟面试训练 · AI智能测评分析 · 区块链证书存证
        </p>
        <div class="hero-actions">
          <router-link to="/register">
            <el-button type="primary" size="large" round class="hero-btn-primary">
              免费开始学习 <el-icon><arrow-right /></el-icon>
            </el-button>
          </router-link>
          <router-link to="/courses">
            <el-button size="large" round class="hero-btn-secondary">
              浏览课程
            </el-button>
          </router-link>
        </div>
        <div class="hero-stats">
          <div class="hero-stat">
            <span class="hs-number">12+</span>
            <span class="hs-label">技能课程</span>
          </div>
          <div class="hero-stat">
            <span class="hs-number">3</span>
            <span class="hs-label">测评维度</span>
          </div>
          <div class="hero-stat">
            <span class="hs-number">4</span>
            <span class="hs-label">元宇宙场景</span>
          </div>
          <div class="hero-stat">
            <span class="hs-number">100%</span>
            <span class="hs-label">区块链存证</span>
          </div>
        </div>
      </div>
      <div class="hero-visual">
        <div class="floating-cube"></div>
        <div class="floating-cube-2"></div>
        <div class="floating-cube-3"></div>
        <div class="glow-dot dot-1"></div>
        <div class="glow-dot dot-2"></div>
        <div class="glow-dot dot-3"></div>
      </div>
    </section>

    <!-- 核心功能 -->
    <section class="features">
      <div class="section-header">
        <h2 class="section-title">三大核心能力</h2>
        <p class="section-desc">AI 驱动 · 元宇宙沉浸 · 区块链可信</p>
      </div>
      <div class="feature-grid">
        <div class="glass-card feature-card" @click="router.push('/assessments')">
          <div class="fc-icon" style="background:rgba(64,158,255,0.1)">🧠</div>
          <h3>AI 智能测评</h3>
          <p>多维度能力评估，AI 生成个性化分析报告与学习建议，精准定位你的技能短板</p>
          <div class="fc-features">
            <span>✅ 12 道专业测评题</span>
            <span>✅ 3 大能力维度</span>
            <span>✅ AI 智能分析报告</span>
          </div>
          <el-button text type="primary" class="fc-link">了解更多 <el-icon><arrow-right /></el-icon></el-button>
        </div>
        <div class="glass-card feature-card" @click="router.push('/metaverse')">
          <div class="fc-icon" style="background:rgba(103,194,58,0.1)">🌐</div>
          <h3>元宇宙 3D 场景</h3>
          <p>沉浸式虚拟面试、实训环境，与 AI 角色实时互动，获得真实场景训练体验</p>
          <div class="fc-features">
            <span>✅ 4 种训练场景</span>
            <span>✅ AI 角色实时互动</span>
            <span>✅ 3D 沉浸式体验</span>
          </div>
          <el-button text type="primary" class="fc-link">了解更多 <el-icon><arrow-right /></el-icon></el-button>
        </div>
        <div class="glass-card feature-card" @click="router.push('/certifications')">
          <div class="fc-icon" style="background:rgba(230,162,60,0.1)">🔗</div>
          <h3>区块链认证</h3>
          <p>技能证书上链存证，不可篡改，全球可验证，让每一份努力都有据可查</p>
          <div class="fc-features">
            <span>✅ SHA-256 哈希存证</span>
            <span>✅ 全球可验证</span>
            <span>✅ 不可篡改</span>
          </div>
          <el-button text type="primary" class="fc-link">了解更多 <el-icon><arrow-right /></el-icon></el-button>
        </div>
      </div>
    </section>

    <!-- 流程 -->
    <section class="how-it-works">
      <div class="section-header">
        <h2 class="section-title">如何开始</h2>
        <p class="section-desc">三步开启你的技能提升之旅</p>
      </div>
      <div class="steps-grid">
        <div class="step-card">
          <div class="step-number">1</div>
          <div class="step-icon">📝</div>
          <h3>注册账号</h3>
          <p>免费注册，立即开始学习</p>
        </div>
        <div class="step-connector">
          <div class="connector-line" />
          <el-icon color="#409eff" :size="24"><arrow-right /></el-icon>
        </div>
        <div class="step-card">
          <div class="step-number">2</div>
          <div class="step-icon">🧠</div>
          <h3>完成测评</h3>
          <p>AI 智能分析你的能力水平</p>
        </div>
        <div class="step-connector">
          <div class="connector-line" />
          <el-icon color="#409eff" :size="24"><arrow-right /></el-icon>
        </div>
        <div class="step-card">
          <div class="step-number">3</div>
          <div class="step-icon">🏆</div>
          <h3>获得认证</h3>
          <p>区块链存证，全球可验证</p>
        </div>
      </div>
    </section>

    <!-- CTA 区域 -->
    <section class="cta">
      <div class="glass-card cta-card">
        <h2>准备好提升你的技能了吗？</h2>
        <p>加入芒得很职，开启 AI 驱动的职业技能提升之旅</p>
        <router-link to="/register">
          <el-button type="primary" size="large" round>免费开始学习</el-button>
        </router-link>
      </div>
    </section>

    <!-- 页脚 -->
    <footer class="footer">
      <div class="footer-content">
        <div class="footer-brand">
          <span class="footer-logo">◆ 芒得很职</span>
          <p>AI + 元宇宙 + 区块链 职业技能培训认证平台</p>
        </div>
        <div class="footer-links">
          <div class="footer-col">
            <h4>平台</h4>
            <router-link to="/courses">课程中心</router-link>
            <router-link to="/assessments">技能测评</router-link>
            <router-link to="/metaverse">元宇宙</router-link>
          </div>
          <div class="footer-col">
            <h4>支持</h4>
            <a href="#">帮助中心</a>
            <a href="#">联系我们</a>
            <a href="#">服务条款</a>
          </div>
        </div>
      </div>
      <div class="footer-bottom">
        <p>© 2026 芒得很职 · 技能培训认证平台 · MIT License</p>
      </div>
    </footer>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import NavBar from '@/components/NavBar.vue'

const router = useRouter()
</script>

<style scoped>
.home {
  min-height: 100vh;
  background: linear-gradient(135deg, #0a0b1a 0%, #1a1b3e 50%, #0a0b1a 100%);
}

/* ===== Hero ===== */
.hero {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 80px 48px;
  max-width: 1200px;
  margin: 0 auto;
  min-height: 80vh;
  position: relative;
}
.hero-content { flex: 1; max-width: 600px; }
.hero-badge { display: inline-block; padding: 6px 16px; border-radius: 20px; font-size: 13px; background: rgba(64,158,255,0.1); color: #409eff; margin-bottom: 20px; }
.hero-title { font-size: 52px; line-height: 1.15; margin-bottom: 24px; }
.hero-subtitle { font-size: 18px; color: #a0a0a0; margin-bottom: 40px; line-height: 1.6; }
.hero-actions { display: flex; gap: 16px; margin-bottom: 48px; }
.hero-btn-primary { height: 52px; font-size: 16px; padding: 0 32px; }
.hero-btn-secondary { height: 52px; font-size: 16px; padding: 0 32px; }
.hero-stats { display: flex; gap: 40px; }
.hero-stat { display: flex; flex-direction: column; }
.hs-number { font-size: 28px; font-weight: 700; color: #409eff; }
.hs-label { font-size: 13px; color: #666; margin-top: 2px; }

.hero-visual {
  width: 400px;
  height: 400px;
  position: relative;
  flex-shrink: 0;
}
.floating-cube {
  width: 200px; height: 200px;
  background: linear-gradient(135deg, #667eea44, #764ba244);
  border: 1px solid #667eea33;
  border-radius: 24px;
  animation: float 6s ease-in-out infinite;
  position: absolute;
  top: 50%; left: 50%;
  transform: translate(-50%, -50%);
}
.floating-cube-2 {
  width: 120px; height: 120px;
  background: linear-gradient(135deg, #409eff33, #67c23a33);
  border: 1px solid rgba(64,158,255,0.2);
  border-radius: 30px;
  animation: float 8s ease-in-out infinite reverse;
  position: absolute;
  top: 20%; right: 10%;
}
.floating-cube-3 {
  width: 80px; height: 80px;
  background: linear-gradient(135deg, #e6a23c33, #f56c6c33);
  border: 1px solid rgba(230,162,60,0.2);
  border-radius: 16px;
  animation: float 5s ease-in-out infinite;
  position: absolute;
  bottom: 20%; left: 10%;
}
.glow-dot { position: absolute; width: 6px; height: 6px; border-radius: 50%; animation: pulse 3s ease-in-out infinite; }
.dot-1 { top: 30%; right: 20%; background: #409eff; box-shadow: 0 0 20px #409eff; }
.dot-2 { top: 60%; right: 40%; background: #67c23a; box-shadow: 0 0 20px #67c23a; animation-delay: 1s; }
.dot-3 { top: 40%; left: 20%; background: #e6a23c; box-shadow: 0 0 20px #e6a23c; animation-delay: 2s; }

@keyframes float {
  0%, 100% { transform: translate(-50%, -50%) rotate(0deg); }
  33% { transform: translate(-50%, -60%) rotate(5deg); }
  66% { transform: translate(-50%, -40%) rotate(-5deg); }
}
@keyframes pulse {
  0%, 100% { opacity: 0.3; transform: scale(1); }
  50% { opacity: 1; transform: scale(1.5); }
}

/* ===== Features ===== */
.features { padding: 80px 48px; max-width: 1200px; margin: 0 auto; }
.section-header { text-align: center; margin-bottom: 48px; }
.section-title { font-size: 32px; margin-bottom: 12px; }
.section-desc { color: #a0a0a0; font-size: 16px; }
.feature-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 32px; }
.feature-card { text-align: left; padding: 40px 32px; cursor: pointer; transition: all 0.3s; }
.feature-card:hover { transform: translateY(-4px); box-shadow: 0 12px 30px rgba(64,158,255,0.15); }
.fc-icon { width: 56px; height: 56px; border-radius: 16px; display: flex; align-items: center; justify-content: center; font-size: 28px; margin-bottom: 20px; }
.feature-card h3 { font-size: 22px; margin-bottom: 12px; }
.feature-card p { color: #a0a0a0; line-height: 1.7; margin-bottom: 20px; font-size: 14px; }
.fc-features { display: flex; flex-direction: column; gap: 6px; margin-bottom: 16px; }
.fc-features span { font-size: 13px; color: #666; }
.fc-link { font-size: 14px; }

/* ===== How it works ===== */
.how-it-works { padding: 80px 48px; background: rgba(255,255,255,0.02); }
.steps-grid { display: flex; align-items: center; justify-content: center; gap: 0; max-width: 900px; margin: 0 auto; }
.step-card { text-align: center; padding: 32px; position: relative; width: 220px; }
.step-number { width: 36px; height: 36px; border-radius: 50%; background: linear-gradient(135deg, #667eea, #764ba2); display: flex; align-items: center; justify-content: center; font-size: 16px; font-weight: 700; margin: 0 auto 16px; }
.step-icon { font-size: 40px; margin-bottom: 12px; }
.step-card h3 { margin-bottom: 8px; }
.step-card p { color: #a0a0a0; font-size: 14px; }
.step-connector { display: flex; align-items: center; gap: 8px; }
.connector-line { width: 60px; height: 1px; background: rgba(255,255,255,0.1); }

/* ===== CTA ===== */
.cta { padding: 80px 48px; text-align: center; }
.cta-card { padding: 64px; max-width: 700px; margin: 0 auto; }
.cta-card h2 { font-size: 28px; margin-bottom: 12px; }
.cta-card p { color: #a0a0a0; margin-bottom: 32px; }

/* ===== Footer ===== */
.footer { border-top: 1px solid rgba(255,255,255,0.05); padding: 48px 48px 24px; }
.footer-content { display: flex; justify-content: space-between; max-width: 1200px; margin: 0 auto; }
.footer-brand { max-width: 300px; }
.footer-logo { font-size: 20px; font-weight: 700; background: linear-gradient(135deg, #667eea, #764ba2); -webkit-background-clip: text; -webkit-text-fill-color: transparent; }
.footer-brand p { color: #666; font-size: 13px; margin-top: 8px; }
.footer-links { display: flex; gap: 48px; }
.footer-col { display: flex; flex-direction: column; gap: 8px; }
.footer-col h4 { font-size: 14px; margin-bottom: 8px; }
.footer-col a { color: #a0a0a0; text-decoration: none; font-size: 13px; transition: color 0.2s; }
.footer-col a:hover { color: #409eff; }
.footer-bottom { text-align: center; padding-top: 24px; margin-top: 24px; border-top: 1px solid rgba(255,255,255,0.05); }
.footer-bottom p { color: #666; font-size: 12px; }

/* ===== Responsive ===== */
@media (max-width: 900px) {
  .hero { flex-direction: column; text-align: center; padding: 48px 24px; }
  .hero-title { font-size: 36px; }
  .hero-stats { justify-content: center; }
  .hero-visual { display: none; }
  .feature-grid { grid-template-columns: 1fr; }
  .steps-grid { flex-direction: column; gap: 16px; }
  .step-connector { transform: rotate(90deg); }
  .footer-content { flex-direction: column; gap: 32px; }
}
</style>