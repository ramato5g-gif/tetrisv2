@echo off
cd /d "%~dp0"
if not exist gradlew.bat (echo Esegui prima PREPARA_GRADLE.bat & pause & exit /b 1)
call gradlew.bat assembleDebug --no-daemon
pause
