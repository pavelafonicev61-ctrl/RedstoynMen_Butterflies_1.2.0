@echo off
setlocal
where gradle >nul 2>nul
if errorlevel 1 (
  echo Gradle is not installed or is not on PATH.
  echo Use the GitHub Actions workflow in .github\workflows\build.yml to build the JAR online.
  exit /b 1
)
call gradle build --no-daemon
if errorlevel 1 exit /b %errorlevel%
echo.
echo BUILD OK. JAR files are in build\libs
endlocal
