# Changelog

All notable changes to this project will be documented in this file.

## [1.0.0] - 2026-07-19

### Added
- AI-powered multi-dimensional skill assessment (55 questions, 5 dimensions)
- Metaverse 3D training scenes with 4 immersive environments
- Real-time WebSocket communication (room management, chat, position sync)
- Blockchain certificate issuance and verification (SHA-256 + Hyperledger Fabric)
- Personalized course recommendations based on assessment results
- DeepSeek LLM integration for AI analysis with graceful fallback
- PWA support with offline caching and service worker
- CAPTCHA verification for login/registration
- Notification system (bell icon, 30s polling, unread count)
- Admin dashboard with ECharts data visualization
- Mobile responsive layout with bottom navigation bar
- Bilingual README (English + Chinese)
- 31 automated tests (unit + integration)
- Docker Compose deployment (MySQL + Backend + Nginx)
- GitHub Actions CI/CD pipeline

### Security
- JWT authentication with HMAC-SHA256 signing
- BCrypt password encryption
- IP-based rate limiting (60 req/min sliding window)
- IDOR protection on all user data endpoints
- Global exception handler with sanitized error messages
- CORS configuration with trusted proxy detection