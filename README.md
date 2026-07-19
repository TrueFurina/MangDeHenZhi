<!-- README-I18N:START -->
**English** | [**汉语**](./README.zh.md)
<!-- README-I18N:END -->

# MangDeHenZhi (芒德矩阵)

> **AI + Metaverse + Blockchain** — Vocational Skills Training & Certification Platform

[![CI](https://github.com/TrueFurina/MangDeHenZhi/actions/workflows/ci.yml/badge.svg)](https://github.com/TrueFurina/MangDeHenZhi/actions/workflows/ci.yml)
[![Java 17](https://img.shields.io/badge/Java-17-blue)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-brightgreen)](https://spring.io/projects/spring-boot)
[![Vue 3](https://img.shields.io/badge/Vue-3.5-4FC08D)](https://vuejs.org/)
[![Vite](https://img.shields.io/badge/Vite-6-646CFF)](https://vitejs.dev/)
[![Three.js](https://img.shields.io/badge/Three.js-r170-000000)](https://threejs.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow)](LICENSE)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen)](https://github.com/TrueFurina/MangDeHenZhi/pulls)

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Tech Stack](#-tech-stack)
- [Quick Start](#-quick-start)
- [Docker Deployment](#-docker-deployment)
- [DeepSeek AI Integration](#-deepseek-ai-integration)
- [3D Metaverse Scenes](#-3d-metaverse-scenes)
- [WebSocket Real-time Communication](#-websocket-real-time-communication)
- [API Documentation](#-api-documentation)
- [Project Structure](#-project-structure)
- [Testing](#-testing)
- [Development Commands](#-development-commands)
- [Environment Variables](#-environment-variables)
- [Contributing](#-contributing)

---

## 🎯 Overview

MangDeHenZhi is an integrated vocational skills training & certification platform combining **AI-powered assessments**, **metaverse 3D training environments**, and **blockchain credential verification**.

### Core Features

| Module | Stack | Description |
|--------|-------|-------------|
| 🧠 **AI Assessment** | Spring Boot + LLM API | Multi-dimensional skill evaluation with AI-generated analysis reports and personalized learning recommendations |
| 🌐 **Metaverse Training** | Three.js + WebSocket | Immersive 3D virtual interview/scenario training with interactive AI characters |
| 🔗 **Blockchain Certification** | Hyperledger Fabric (reserved) | Skill certificates stored on-chain, tamper-proof, globally verifiable |
| 📚 **Course Management** | Vue 3 + Element Plus | Course browsing, enrollment, and personalized learning path recommendations |
| 💬 **Real-time Chat** | WebSocket | In-room text chat, position sync, and WebRTC signaling for metaverse sessions |

---

## 🛠 Tech Stack

### Backend (backend/)

| Component | Version | Purpose |
|-----------|---------|---------|
| Java | 17 | Runtime |
| Spring Boot | 3.3.5 | Framework |
| Spring Security + JWT | — | Authentication & Authorization |
| Spring Data JPA | — | Data persistence |
| H2 / MySQL | — | Database (H2 for dev, MySQL for production) |
| Maven | 3.9+ | Build tool |
| WebSocket | — | Real-time communication |

### Frontend (frontend/)

| Component | Version | Purpose |
|-----------|---------|---------|
| Vue | 3.5+ | UI framework |
| Vite | 6+ | Build tool |
| TypeScript | 5+ | Type safety |
| Element Plus | 2.9+ | UI component library |
| Three.js | 0.170+ | 3D rendering |
| Pinia | 2.3+ | State management |
| ECharts | 5+ | Data visualization |
| PWA | — | Offline support |

---

## 🚀 Quick Start

### Prerequisites

- JDK 17+
- Node.js 18+
- Maven 3.9+ (or use `mvnw`)

### Start Backend

```bash
cd backend
# Dev mode (H2 in-memory DB, auto-seeded test data)
mvn spring-boot:run

# Production mode (MySQL)
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

### Start Frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend runs at `http://localhost:5173` with API proxy to backend on port `8080`.

### Test Accounts

| Role | Username | Password |
|------|----------|----------|
| Admin | admin | admin123 |
| Teacher | teacher | teacher123 |
| Student | student | student123 |

---

## 🐳 Docker Deployment

### Production

```bash
docker compose up -d
```

Starts MySQL 8.0 + Spring Boot backend + Nginx frontend.

### Development

```bash
docker compose -f docker-compose.dev.yml up -d
```

Starts MySQL + Maven dev server (with hot reload) + Vite dev server (with HMR).

---

## 🤖 DeepSeek AI Integration

The platform supports AI-powered assessment analysis via the DeepSeek API. Set your API key as an environment variable:

```bash
# Windows (PowerShell)
[System.Environment]::SetEnvironmentVariable('DEEPSEEK_API_KEY', 'sk-your-key', 'User')

# Linux/macOS
export DEEPSEEK_API_KEY=sk-your-key
```

When the API key is configured, the platform uses DeepSeek for:
- **Assessment Analysis**: Generates detailed evaluation reports for each assessment dimension
- **Learning Recommendations**: Suggests personalized courses and learning paths based on scores

If the API key is not set, the platform gracefully falls back to built-in mock analysis.

---

## 🌐 3D Metaverse Scenes

Built with **Three.js**, the metaverse module provides 4 immersive training environments:

| Scene | Description | AI Characters |
|-------|-------------|---------------|
| 🎯 **Interview Room** | Simulate real job interviews with AI interviewers | Interviewer, HR, Tech Lead |
| 📚 **Classroom** | Immersive online learning with AI instructor | Instructor |
| 🤝 **Meeting Room** | Team collaboration simulation | Up to 6 participants |
| ⚡ **Training Room** | Skill-specific training with real-time feedback | Trainer, Assistant |

Each scene features:
- Realistic 3D rendering with shadows, fog, and lighting
- Animated AI characters with floating idle animations
- Ambient particle effects
- Interactive OrbitControls for camera manipulation
- CSS2D labels for character identification

---

## 💬 WebSocket Real-time Communication

The metaverse scenes support real-time multi-user interaction via WebSocket:

| Feature | Description |
|---------|-------------|
| Room Management | Join/leave rooms with user tracking |
| Position Sync | Broadcast user movement and rotation |
| Text Chat | Real-time messaging within rooms |
| WebRTC Signaling | Voice/video call signaling relay |
| Auto-reconnect | Client-side auto-reconnect with 3s interval |

---

## 📖 API Documentation

When running in dev mode, Swagger UI is available at:

```
http://localhost:8080/swagger-ui.html
```

### Key Endpoints

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/auth/register` | User registration |
| POST | `/api/auth/login` | User login |
| GET | `/api/users/me` | Get current user |
| GET | `/api/assessments` | List all assessments |
| POST | `/api/assessments/submit` | Submit assessment |
| GET | `/api/assessments/results` | Get assessment history (paginated) |
| GET | `/api/courses` | List courses (paginated, sortable) |
| GET | `/api/certifications/my` | Get my certificates |
| GET | `/api/certifications/verify/{hash}` | Verify certificate |
| POST | `/api/metaverse/sessions` | Create metaverse session |
| WS | `/ws/metaverse` | WebSocket for real-time interaction |
| GET | `/api/notifications` | Get user notifications |

---

## 📁 Project Structure

```
MangDeHenZhi/
├── backend/                    # Spring Boot backend
│   ├── src/main/java/com/mangdehenzhi/
│   │   ├── ai/                 # AI analysis models
│   │   ├── blockchain/         # Blockchain service
│   │   ├── config/             # Security, JWT, CORS, RateLimit
│   │   ├── controller/         # REST controllers (10)
│   │   ├── dto/                # Data transfer objects
│   │   ├── entity/             # JPA entities (7)
│   │   ├── enums/              # Enum types
│   │   ├── exception/          # Global exception handler
│   │   ├── metaverse/          # Metaverse data models
│   │   ├── repository/         # Data repositories (7)
│   │   ├── service/            # Business logic (9)
│   │   └── websocket/          # WebSocket handler
│   └── src/main/resources/
│       └── application.yml     # App configuration
├── frontend/                   # Vue 3 + Vite frontend
│   └── src/
│       ├── api/                # API client (Axios)
│       ├── components/         # Shared components
│       ├── composables/        # Vue composables
│       ├── router/             # Route config
│       ├── stores/             # Pinia state management
│       ├── styles/             # Global CSS
│       ├── types/              # TypeScript types
│       └── views/              # Page components (18)
├── docs/                       # Documentation
└── JSMO-PAGE/                  # Original landing page (preserved)
```

---

## 🧪 Testing

### Backend Tests (31 tests, all passing)

```bash
cd backend && mvn test
```

| Test Class | Tests | Coverage |
|-----------|-------|----------|
| `AuthControllerTest` | 4 | Registration & login |
| `CourseControllerTest` | 2 | Course listing |
| `HealthControllerTest` | 1 | Health check |
| `SearchControllerTest` | 2 | Course search |
| `FullFlowIntegrationTest` | 4 | Full user journey |
| `AssessmentServiceTest` | 7 | Assessment submission & results |
| `CertificationServiceTest` | 4 | Certificate issuance & verification |
| `UserServiceTest` | 7 | User registration & login |

### Frontend Build

```bash
cd frontend && npm run build
```

PWA-enabled build with automatic service worker generation.

---

## 💻 Development Commands

```bash
make help          # Show all commands
make backend-dev   # Start backend dev server
make backend-test  # Run backend tests
make frontend-dev  # Start frontend dev server
make frontend-build # Build frontend
make docker-up     # Start production Docker stack
make docker-dev    # Start development Docker stack
```

---

## 🔐 Environment Variables

Copy `.env.example` to `.env` and fill in values:

| Variable | Required | Description |
|----------|----------|-------------|
| `MYSQL_PASSWORD` | Production | MySQL user password |
| `MYSQL_ROOT_PASSWORD` | Production | MySQL root password |
| `JWT_SECRET` | Production | JWT signing key (≥32 chars) |
| `DEEPSEEK_API_KEY` | Optional | DeepSeek LLM API key |

---

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'feat: add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.

---

<p align="center">Made with ❤️ by the MangDeHenZhi Team</p>