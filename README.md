# ☁️ Cloud-Based Compiler Development Environment

A full-stack, cloud-based Compiler Development Environment & IDE for exploring, visualizing, and analyzing compiler construction stages in real time:

- **Lexical Analysis (Tokens & Patterns)**
- **Syntax Analysis & Abstract Syntax Tree (AST)**
- **Semantic Analysis, Scope Resolution & Type Checking**
- **Symbol Table Generation (Memory Offsets, Scopes, Types)**
- **Intermediate Code Generation (Three-Address Code, Quadruples, Triples & Indirect Triples)**
- **Cloud Project & File Management with Monaco Editor & Docker Support**

---

## 🌟 Key Features

### ✍️ Monaco Code Editor
- Full C-like syntax highlighting with dark theme (`vs-dark`).
- **Real-Time Error Squiggles & Gutter Markers** mapped directly to backend compiler diagnostics.
- Multi-file tab switching with unsaved changes indicator (`*`).
- Keyboard shortcuts: `Ctrl + Enter` to Compile, `Ctrl + S` to Save.
- Autosave toggle for seamless continuous coding.

### 🔤 Lexical Analysis Engine
- Tokenizes keywords (`int`, `float`, `char`, `bool`, `void`, `if`, `else`, `while`, `for`, `return`, `print`, `read`), operators (`+`, `-`, `*`, `/`, `%`, `++`, `--`, `==`, `!=`, `<`, `<=`, `>`, `>=`, `&&`, `||`, `!`, `=`, `+=`, `-=`), literals, delimiters, and comments.
- Searchable & filterable token table with category badges, counts, line, and column coordinates.

### 🌳 Syntax Analysis & AST Visualizer
- Recursive descent parser with Pratt operator precedence parsing.
- Interactive **Abstract Syntax Tree (AST)** visual graph with expand/collapse, line coordinates, and a detailed **Node Inspector**.
- Accurate syntax error diagnostics with expected vs. found token reporting and panic mode synchronization.

### 🧠 Semantic Analysis & Symbol Table
- Multi-level hierarchical scoping (Global, Function, Block, Loop scopes).
- Detects undeclared variables, duplicate declarations in the same scope, type mismatch in assignments and arithmetic/logical operations, and function signature mismatches.
- Searchable and scope-grouped **Symbol Table** displaying Identifier Name, Data Type, Scope Level, Memory Offsets, Byte Sizes, and Reference Counts.

### ⚡ Intermediate Code Generation (IR)
- **Three-Address Code (TAC)**: Instructions with generated temporaries (`t1`, `t2`, ...), labels (`L1`, `L2`, ...), conditional branches (`IF_FALSE x GOTO L1`), and function parameters.
- **Quadruples Table**: Standard `(Operator, Arg1, Arg2, Result)` records.
- **Triples Table**: Position-referenced `(Index, Operator, Arg1, Arg2)` with `(0)`, `(1)` backreferences.
- **Indirect Triples**: Pointer list + statement representation.

### 📊 Dashboard, Compilation History & Report Export
- Real-time compiler execution logger with stage checklist and timing metrics.
- Persistent user compilation history with rerun snapshot capability.
- Analytics dashboard with success rates and compilation statistics.
- Export full analysis reports in **Markdown** and **JSON**.

---

## 🏗️ Technology Stack

| Layer | Technologies |
|-------|--------------|
| **Frontend** | React 18, Vite, Tailwind CSS, Monaco Editor (`@monaco-editor/react`), Lucide React, Axios, React Router 6 |
| **Backend** | Java 21 / 25, Spring Boot 3.3.4, Spring Web, Spring Data JPA, Spring Security, JWT (JJWT), SpringDoc Swagger OpenAPI 3 |
| **Compiler Engine** | Custom Pure Java Lexer, Pratt Parser, Hierarchical Scope Symbol Resolver, TAC / Quadruple / Triple IR Generator |
| **Database** | PostgreSQL (Production/Docker), H2 In-Memory (Zero-config local development) |
| **Deployment** | Docker, Docker Compose, Nginx Reverse Proxy |

---

## 🚀 Getting Started

### Prerequisites
- **Java 21+** (Java 21, 22, 23, 24, or 25)
- **Node.js 18+** & **npm**
- *(Optional for containers)* **Docker & Docker Compose**

---

### Option 1: Running with Docker Compose (Recommended)

Run the entire stack (PostgreSQL + Backend + Frontend) with a single command:

```bash
docker compose up --build
```

- **Frontend IDE**: `http://localhost`
- **Backend API**: `http://localhost:8080`
- **Swagger Documentation**: `http://localhost:8080/swagger-ui.html`

---

### Option 2: Running Locally (Development Mode)

#### 1. Start the Backend:
```bash
cd backend
mvnw.cmd test-compile
mvnw.cmd spring-boot:run
```
*Backend runs on `http://localhost:8080` with embedded H2 database (zero configuration required).*

#### 2. Start the Frontend:
```bash
cd frontend
npm install
npm run dev
```
*Frontend runs on `http://localhost:5173` with automatic API proxying.*

---

## 🧪 Running Tests

Run the automated compiler engine test suite:

```bash
cd backend
mvnw.cmd test
```

Verifies:
1. Valid full-program compilation.
2. Lexical error detection.
3. Syntax error detection and error recovery.
4. Undeclared variable semantic error.
5. Duplicate declaration semantic error.
6. Type mismatch semantic error.
7. TAC, Quadruples, and Triples generation.

---

## 📡 API Endpoints

### Authentication
- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login and retrieve JWT token
- `GET /api/auth/me` - Get active user profile

### Compiler Pipeline
- `POST /api/compiler/compile` - Run full end-to-end compiler pipeline
- `POST /api/compiler/tokenize` - Run Lexical Analysis (Tokens)
- `POST /api/compiler/parse` - Run Syntax Analysis (AST)
- `POST /api/compiler/analyze` - Run Semantic Analysis (Symbol Table)
- `POST /api/compiler/generate-ir` - Generate TAC, Quadruples & Triples

### Project & File Management
- `GET /api/projects` - List user projects
- `POST /api/projects` - Create new project
- `GET /api/projects/{id}/files` - List files in project
- `POST /api/projects/{id}/files` - Create source file
- `PUT /api/files/{id}` - Update file content
- `DELETE /api/files/{id}` - Delete file

### History & Dashboard
- `GET /api/dashboard/stats` - User compilation analytics & recent activity
- `GET /api/history` - User compilation run history
- `DELETE /api/history/{id}` - Delete history record

---

## 📄 Example Program

```c
int factorial(int n) {
    int result = 1;
    int i = 1;
    
    while (i <= n) {
        result = result * i;
        i = i + 1;
    }
    
    return result;
}

int main() {
    int num = 5;
    int fact = factorial(num);
    
    if (fact > 100) {
        print(fact);
    } else {
        print(0);
    }
    
    return 0;
}
```

### Generated Three-Address Code (TAC):
```text
func_factorial:
PARAM_DECL int i
PARAM_DECL int result
result = 1
i = 1
L1:
t1 = i <= n
IF_FALSE t1 GOTO L2
t2 = result * i
result = t2
t3 = i + 1
i = t3
GOTO L1
L2:
RETURN result
FUNC_END factorial
func_main:
PARAM_DECL int num
PARAM_DECL int fact
num = 5
PARAM num
t4 = CALL factorial, 1
fact = t4
t5 = fact > 100
IF_FALSE t5 GOTO L3
PRINT fact
GOTO L4
L3:
PRINT 0
L4:
RETURN 0
FUNC_END main
```
