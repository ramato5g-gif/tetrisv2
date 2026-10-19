@echo off
cd /d "%~dp0"
where gradle >nul 2>&1 || (echo Gradle non trovato. Installa Gradle oppure apri in Android Studio. & pause & exit /b 1)
gradle wrapper --gradle-version 8.11.1
pause
