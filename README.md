Nexus HR & Payroll SystemAn enterprise-grade, full-stack HR and Payroll management solution built with Java Spring Boot, Spring Data JPA, H2, and a modern Tailwind-powered glassmorphic interface. This system orchestrates and automates employee onboarding, leave management, automated payroll generation (with dynamic deduction calculations), and administrative real-time analytics.🏗 System ArchitectureThe project is designed using the MVC (Model-View-Controller) pattern, strictly separating concerns between the presentation layer, business logic, and database state management.

```mermaid
graph TD
    subgraph Frontend [Presentation Layer - SPA]
        UI[Tailwind UI / Vanilla JS]
        CJ[Chart.js Engine]
    end

subgraph Backend [Application Layer - Spring Boot]
        Ctrl[REST Controllers]
        Serv[Service Layer]
        Repo[Data Repositories]
end

subgraph Database [Storage Layer]
        H2[(H2 In-Memory DB)]
end

UI -->|HTTP Requests / JSON| Ctrl
Ctrl -->|Invokes Engine| Serv
Serv -->|Queries Data| Repo
Repo -->|SQL Dialect| H2
Serv -->|Generates PDF Bytes| UI
```

