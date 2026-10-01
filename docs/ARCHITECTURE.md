# Cloud Compiler Architecture

## System Overview

```text
┌─────────────────────────────────────────────────────────────┐
│                 REACT 18 MONACO IDE FRONTEND                │
│  - Monaco Code Editor (Diagnostics Markers & Shortcuts)     │
│  - Interactive AST Visualizer & Node Inspector              │
│  - Filterable Tokens & Symbol Table                         │
│  - Three-Address Code, Quadruples & Triples Viewers         │
│  - Real-Time Compilation Stage Logger                       │
│  - Workspace Project & Source File Tree                     │
└──────────────────────────────┬──────────────────────────────┘
                               │ REST / JSON (JWT Protected)
┌──────────────────────────────▼──────────────────────────────┐
│             SPRING BOOT 3 REST BACKEND (JAVA 21)            │
│  - AuthController: JWT Authentication & User Session        │
│  - ProjectController & SourceFileController: Project CRUD   │
│  - CompilerController: Multi-Stage Compilation API          │
│  - HistoryController & DashboardController: Analytics       │
└──────────────────────────────┬──────────────────────────────┘
                               │
            ┌──────────────────┴──────────────────┐
            ▼                                     ▼
┌───────────────────────┐             ┌───────────────────────┐
│    COMPILER ENGINE    │             │   DATABASE STORAGE    │
│  1. Lexer (Tokens)    │             │  PostgreSQL / H2      │
│  2. Parser (AST)      │             │  Users, Projects,     │
│  3. Semantic (Scope)  │             │  Files, History       │
│  4. IR (TAC / Quads)  │             │                       │
└───────────────────────┘             └───────────────────────┘
```

## Security & Isolation
1. **No Untrusted Execution on Host**: Source code undergoes pure abstract lexical, syntax, semantic, and intermediate code analysis and code translation without executing arbitrary host binaries.
2. **Authentication**: BCrypt hashed passwords and JWT token validation with granular user-scoped access control.
3. **Data Integrity**: Foreign key constraints and soft cascade handling.
