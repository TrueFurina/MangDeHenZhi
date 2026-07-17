<template>
  <div class="page">
    <AppHeader />
    <div class="page-content">
      <div class="page-header">
        <div>
          <h1>🏆 我的证书</h1>
          <p class="page-desc">区块链存证的技能认证证书，全球可验证</p>
        </div>
        <div class="header-stats" v-if="certifications.length > 0">
          <span>📜 {{ certifications.length }} 张证书</span>
        </div>
      </div>

      <!-- 证书列表 -->
      <div v-if="certifications.length > 0" class="cert-grid">
        <div v-for="cert in certifications" :key="cert.id" class="glass-card cert-card"
             @click="showCertDetail(cert)">
          <div class="cert-ribbon" :class="cert.status === 'VERIFIED' ? 'verified' : 'issued'">
            {{ cert.status === 'VERIFIED' ? '已验证' : '已签发' }}
          </div>
          <div class="cert-badge">🏆</div>
          <h3>{{ cert.title }}</h3>
          <div class="cert-hash">
            <span class="hash-label">证书哈希</span>
            <code>{{ cert.certHash?.substring(0, 16) }}...{{ cert.certHash?.substring(-8) }}</code>
          </div>
          <div class="cert-meta">
            <el-tag :type="cert.status === 'ISSUED' ? 'success' : 'info'" size="small" effect="dark">
              {{ cert.status === 'ISSUED' ? '已签发' : cert.status === 'VERIFIED' ? '已验证' : cert.status }}
            </el-tag>
            <span class="cert-date">{{ formatDate(cert.issuedAt) }}</span>
          </div>
          <div v-if="cert.blockchainTxId" class="blockchain-info">
            <el-icon><link /></el-icon>
            <span>区块链交易: {{ cert.blockchainTxId?.substring(0, 16) }}...</span>
          </div>
          <div class="cert-actions">
            <el-button type="primary" size="small" plain @click.stop="verifyCert(cert.certHash)">
              <el-icon><checked /></el-icon> 验证证书
            </el-button>
            <el-button size="small" @click.stop="showCertDetail(cert)">
              <el-icon><view /></el-icon> 查看详情
            </el-button>
          </div>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-if="certifications.length === 0" class="empty-state glass-card">
        <div class="empty-icon">🏆</div>
        <h3>还没有获得证书</h3>
        <p>完成技能测评并通过后，即可获得区块链存证的技能认证证书</p>
        <el-button type="primary" size="large" @click="router.push('/assessments')">
          <el-icon><collection /></el-icon> 去测评
        </el-button>
      </div>

      <!-- 证书详情弹窗 -->
      <el-dialog v-model="detailVisible" :title="detailCert?.title" width="500px">
        <div v-if="detailCert" class="cert-detail">
          <div class="cert-detail-badge">🏆</div>
          <h2>{{ detailCert.title }}</h2>
          <p class="cert-detail-desc">{{ detailCert.description }}</p>
          <div class="cert-detail-info">
            <div class="cdi-row">
              <span class="cdi-label">证书哈希</span>
              <code class="cdi-value">{{ detailCert.certHash }}</code>
            </div>
            <div class="cdi-row">
              <span class="cdi-label">签发时间</span>
              <span class="cdi-value">{{ formatDate(detailCert.issuedAt) }}</span>
            </div>
            <div class="cdi-row" v-if="detailCert.blockchainTxId">
              <span class="cdi-label">区块链交易ID</span>
              <code class="cdi-value">{{ detailCert.blockchainTxId }}</code>
            </div>
            <div class="cdi-row">
              <span class="cdi-label">状态</span>
              <el-tag :type="detailCert.status === 'VERIFIED' ? 'success' : 'info'">
                {{ detailCert.status === 'VERIFIED' ? '已验证' : '已签发' }}
              </el-tag>
            </div>
          </div>
        </div>
        <template #footer>
          <el-button @click="detailVisible = false">关闭</el-button>
          <el-button type="primary" @click="verifyCert(detailCert?.certHash || '')">
            验证证书
          </el-button>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { certificationApi } from '@/api'
import AppHeader from '@/components/AppHeader.vue'
import type { Certification } from '@/types'

const router = useRouter()
const certifications = ref<Certification[]>([])
const detailVisible = ref(false)
const detailCert = ref<Certification | null>(null)

function formatDate(dateStr: string): string {
  if (!dateStr) return ''
  return dateStr.replace('T', ' ').substring(0, 10)
}

function showCertDetail(cert: Certification) {
  detailCert.value = cert
  detailVisible.value = true
}

async function verifyCert(certHash: string) {
  if (!certHash) return
  try {
    const res = await certificationApi.verify(certHash)
    ElMessage.success(`✅ 证书验证成功！状态: ${res.data.status}`)
    // 更新本地状态
    const idx = certifications.value.findIndex(c => c.certHash === certHash)
    if (idx >= 0) certifications.value[idx].status = 'VERIFIED'
  } catch (err: any) {
    ElMessage.error(err.response?.data?.message || '验证失败')
  }
}

onMounted(async () => {
  try {
    const res = await certificationApi.getMy()
    certifications.value = res.data || []
  } catch (err) {
    console.error('加载证书列表失败', err)
  }
})
</script>

<style scoped>
.page-content { padding: 32px 48px; max-width: 1200px; margin: 0 auto; }
.page-header { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 32px; }
.page-header h1 { font-size: 28px; }
.page-desc { color: #a0a0a0; margin-top: 4px; }
.header-stats { font-size: 14px; color: #a0a0a0; }

.cert-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 24px; }
.cert-card { text-align: center; padding: 32px 24px; cursor: pointer; transition: all 0.3s; position: relative; overflow: hidden; }
.cert-card:hover { transform: translateY(-4px); box-shadow: 0 12px 30px rgba(64,158,255,0.15); }

.cert-ribbon { position: absolute; top: 16px; right: -32px; padding: 4px 40px; font-size: 11px; font-weight: 600; transform: rotate(45deg); }
.cert-ribbon.issued { background: rgba(64,158,255,0.15); color: #409eff; }
.cert-ribbon.verified { background: rgba(103,194,58,0.15); color: #67c23a; }

.cert-badge { font-size: 56px; margin-bottom: 16px; }
.cert-card h3 { font-size: 16px; margin-bottom: 16px; }
.cert-hash { margin-bottom: 16px; }
.hash-label { display: block; font-size: 11px; color: #666; margin-bottom: 4px; }
.cert-hash code { font-size: 12px; color: #666; background: rgba(255,255,255,0.04); padding: 4px 8px; border-radius: 4px; font-family: monospace; }
.cert-meta { display: flex; justify-content: center; gap: 12px; align-items: center; margin-bottom: 12px; }
.cert-date { font-size: 12px; color: #666; }
.blockchain-info { display: flex; align-items: center; justify-content: center; gap: 4px; font-size: 12px; color: #67c23a; margin-bottom: 16px; }
.cert-actions { display: flex; gap: 8px; justify-content: center; }

/* 空状态 */
.empty-state { text-align: center; padding: 64px; }
.empty-icon { font-size: 64px; margin-bottom: 16px; }
.empty-state h3 { margin-bottom: 12px; }
.empty-state p { color: #a0a0a0; margin-bottom: 24px; max-width: 400px; margin-left: auto; margin-right: auto; }

/* 详情弹窗 */
.cert-detail { text-align: center; }
.cert-detail-badge { font-size: 72px; margin-bottom: 16px; }
.cert-detail h2 { margin-bottom: 12px; }
.cert-detail-desc { color: #a0a0a0; margin-bottom: 24px; }
.cert-detail-info { text-align: left; display: flex; flex-direction: column; gap: 12px; }
.cdi-row { display: flex; justify-content: space-between; align-items: center; padding: 8px 12px; background: rgba(255,255,255,0.03); border-radius: 8px; }
.cdi-label { font-size: 13px; color: #a0a0a0; }
.cdi-value { font-size: 13px; word-break: break-all; }
.cdi-value code { font-size: 12px; color: #666; background: rgba(255,255,255,0.04); padding: 2px 6px; border-radius: 4px; }

@media (max-width: 900px) {
  .cert-grid { grid-template-columns: 1fr; }
}
</style>