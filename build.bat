@echo off
setlocal EnableExtensions
cd /d "%~dp0"
title Important Mod - Fabric 1.21.11 Build

where java >nul 2>nul
if errorlevel 1 (
  echo [ERROR] Java not found. Install JDK 21 first: https://adoptium.net/temurin/releases/?version=21
  pause
  exit /b 1
)
java -version 2>&1 | findstr /C:"21." >nul
if errorlevel 1 (
  echo [WARNING] Java 21 is recommended for Minecraft 1.21.11.
)

where gradle >nul 2>nul
if not errorlevel 1 (
  echo [INFO] Using installed Gradle...
  gradle build
  goto result
)

set "GRADLE_HOME_LOCAL=%LOCALAPPDATA%\ImportantMod\gradle-8.14.3"
set "GRADLE_ZIP=%TEMP%\important-gradle-8.14.3-bin.zip"
if exist "%GRADLE_HOME_LOCAL%\bin\gradle.bat" goto runbuild

echo [INFO] Gradle not found. Downloading Gradle 8.14.3 automatically...
where powershell >nul 2>nul
if errorlevel 1 (
  echo [ERROR] PowerShell is required to download Gradle.
  pause
  exit /b 1
)
if not exist "%LOCALAPPDATA%\ImportantMod" mkdir "%LOCALAPPDATA%\ImportantMod"
powershell -NoProfile -ExecutionPolicy Bypass -Command "$ProgressPreference='SilentlyContinue'; Invoke-WebRequest 'https://services.gradle.org/distributions/gradle-8.14.3-bin.zip' -OutFile '%GRADLE_ZIP%'"
if errorlevel 1 (
  echo [ERROR] Could not download Gradle. Check your internet connection and try again.
  pause
  exit /b 1
)
powershell -NoProfile -ExecutionPolicy Bypass -Command "Expand-Archive -LiteralPath '%GRADLE_ZIP%' -DestinationPath '%LOCALAPPDATA%\ImportantMod' -Force"
if errorlevel 1 (
  echo [ERROR] Could not extract Gradle.
  pause
  exit /b 1
)

goto runbuild

:runbuild
call "%GRADLE_HOME_LOCAL%\bin\gradle.bat" build

:result
if errorlevel 1 (
  echo.
  echo [FAILED] Build failed. Copy the error text and send it for troubleshooting.
) else (
  echo.
  echo [SUCCESS] Build finished. Look for the mod JAR in:
  echo %CD%\build\libs\important-1.0.0+1.21.11.jar
  if exist "build\libs" explorer "%CD%\build\libs"
)
pause
