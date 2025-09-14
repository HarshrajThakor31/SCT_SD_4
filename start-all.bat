@echo off
echo Starting E-commerce Scraper...

echo Starting Backend...
start "Backend" cmd /k "cd /d %~dp0 && mvn spring-boot:run"

echo Waiting for backend to start...
timeout /t 15 /nobreak >nul

echo Starting Frontend...
start "Frontend" cmd /k "cd /d %~dp0\frontend && npm start"

echo Both services are starting...
echo Backend: http://localhost:8080
echo Frontend: http://localhost:3000
echo.
echo Default login credentials:
echo Username: admin
echo Password: admin123
pause