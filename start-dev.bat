@echo off
echo ===================================================
echo   Cloud-Based Compiler Development Environment
echo ===================================================
echo Starting Spring Boot Backend and React Frontend...
echo.

start "CloudCompiler Backend (Port 8080)" cmd /k "cd backend && mvnw.cmd spring-boot:run"
start "CloudCompiler Frontend (Port 5173)" cmd /k "cd frontend && npm run dev"

echo.
echo ===================================================
echo Backend starting at: http://localhost:8080
echo Swagger UI at:       http://localhost:8080/swagger-ui.html
echo Frontend starting at: http://localhost:5173
echo ===================================================
echo.
pause
