@echo off
setlocal

set ANDROID_SDK=D:\android-sdk
set BUILD_TOOLS_VERSION=36.0.0
set PLATFORM_VERSION=android-36
set JAVA_HOME=C:\Users\sasha\.jdks\temurin-24.0.1

set PROJECT_DIR=%~dp0
set SRC_FILE=%PROJECT_DIR%JavaToast_plugin.java
set OUTPUT_DIR=%PROJECT_DIR%build
set FINAL_DEX=%PROJECT_DIR%plugin.dex

echo Очистка предыдущих сборок...
if exist "%OUTPUT_DIR%" (
    del /q "%OUTPUT_DIR%\*.*" >nul 2>&1
) else (
    mkdir "%OUTPUT_DIR%"
)

echo.
echo [1/3] Компиляция Java в .class файлы...
"%JAVA_HOME%\bin\javac" --release 8 -classpath "%ANDROID_SDK%\platforms\%PLATFORM_VERSION%\android.jar" -d "%OUTPUT_DIR%" "%SRC_FILE%"
if errorlevel 1 (
    echo [ОШИБКА] Компиляция Java не удалась!
    pause
    exit /b 1
)

echo.
echo [2/3] Создание чистого JAR архива...
cd "%OUTPUT_DIR%"
"%JAVA_HOME%\bin\jar" cvf "classes.jar" *
if errorlevel 1 (
    echo [ОШИБКА] Создание JAR архива не удалось!
    pause
    exit /b 1
)

echo.
echo [3/3] Конвертация JAR в DEX формат...
"%ANDROID_SDK%\build-tools\%BUILD_TOOLS_VERSION%\d8" --release --output "%OUTPUT_DIR%" "classes.jar"
if errorlevel 1 (
    echo [ОШИБКА] Конвертация в DEX не удалась!
    pause
    exit /b 1
)

if exist "%OUTPUT_DIR%\classes.dex" (
    rename "%OUTPUT_DIR%\classes.dex" "plugin.dex"
    move "%OUTPUT_DIR%\plugin.dex" "%PROJECT_DIR%"
    del classes.jar
    echo.
    echo [УСПЕХ] Плагин успешно собран!
    echo Результат: %FINAL_DEX%
    for %%F in ("%FINAL_DEX%") do echo Размер файла: %%~zF байт
) else (
    echo [ОШИБКА] Конечный файл classes.dex не найден!
)

pause
