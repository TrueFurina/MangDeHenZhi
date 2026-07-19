# Contributing to MangDeHenZhi

Thank you for your interest in contributing! Here's how you can help.

## Development Setup

1. Fork the repository
2. Clone your fork: `git clone https://github.com/your-username/MangDeHenZhi.git`
3. Install dependencies: `cd frontend && npm install`
4. Start backend: `cd backend && mvn spring-boot:run`
5. Start frontend: `cd frontend && npm run dev`

## Code Style

- Java: 4-space indentation, follow Spring Boot conventions
- Vue/TypeScript: 2-space indentation, follow Vue 3 Composition API style
- Run `mvn test` before submitting backend changes
- Run `npm run build` before submitting frontend changes

## Pull Request Process

1. Create a feature branch: `git checkout -b feature/your-feature`
2. Make your changes
3. Run tests: `cd backend && mvn test` and `cd frontend && npm run build`
4. Commit: `git commit -m "feat: add your feature"`
5. Push: `git push origin feature/your-feature`
6. Open a Pull Request

## Reporting Issues

- Use the GitHub issue tracker
- Include steps to reproduce for bugs
- Include screenshots for UI issues
- Tag your issue appropriately (bug, feature, question)