# ============================================================
# generate-secrets.ps1 — 生成强随机密钥与口令 (Windows / PowerShell)
# 用途：为 .env 生成生产级强随机值，避免提交弱口令/固定密钥。
# 用法：powershell -ExecutionPolicy Bypass -File scripts/generate-secrets.ps1
# ============================================================
param(
    [string]$EnvFile = ".env"
)

$ErrorActionPreference = 'Stop'

function New-RandomB64 {
    param([int]$Bytes)
    $buf = New-Object byte[] $Bytes
    [System.Security.Cryptography.RandomNumberGenerator]::Create().GetBytes($buf)
    return [Convert]::ToBase64String($buf)
}

# >= 32 字节（JWT HS256 建议）；口令 >= 24 字节
$jwt    = New-RandomB64 48
$rootPw = New-RandomB64 24
$appPw  = New-RandomB64 24

$block = @"
# ---- 自动生成的强随机密钥 ($(Get-Date -Format u)) ----
# 请勿将真实 .env 提交到仓库
MYSQL_ROOT_PASSWORD=$rootPw
MYSQL_PASSWORD=$appPw
JWT_SECRET=$jwt
"@

if (Test-Path $EnvFile) {
    Write-Warning "$EnvFile 已存在，未覆盖。请将以下值手动合并进 $EnvFile ："
    Write-Output $block
} else {
    if (Test-Path ".env.example") { Copy-Item ".env.example" $EnvFile }
    Add-Content -Path $EnvFile -Value "`n$block"
    Write-Output "已生成 $EnvFile 。请按需补充 DEEPSEEK_API_KEY 与 CORS_ALLOWED_ORIGINS。"
}

Write-Output "`n【安全提示】以上为强随机值，请妥善保管；生产/演示环境切勿将 .env 提交到仓库。"
Write-Output "启动前请执行：docker compose up -d --build"
