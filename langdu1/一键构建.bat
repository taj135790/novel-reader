@echo off
echo 正在准备Android APK构建环境...
echo.

REM 检查是否安装了Java
echo 检查Java环境...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误：未检测到Java环境
    echo 请先安装Java 8或更高版本
    echo 下载地址：https://adoptium.net/
    pause
    exit /b 1
)

echo Java环境检测通过！

REM 检查是否安装了Android SDK
echo 检查Android SDK...
if not exist "%ANDROID_HOME%" (
    echo 警告：未设置ANDROID_HOME环境变量
    echo 尝试在常见位置查找Android SDK...
    
    if exist "C:\Users\%USERNAME%\AppData\Local\Android\Sdk" (
        set ANDROID_HOME=C:\Users\%USERNAME%\AppData\Local\Android\Sdk
        echo 找到Android SDK：%ANDROID_HOME%
    ) else if exist "C:\Android\Sdk" (
        set ANDROID_HOME=C:\Android\Sdk
        echo 找到Android SDK：%ANDROID_HOME%
    ) else (
        echo 错误：未找到Android SDK
        echo 请先安装Android Studio或Android SDK
        echo 下载地址：https://developer.android.com/studio
        pause
        exit /b 1
    )
)

echo Android SDK检测通过！

REM 设置环境变量
set PATH=%ANDROID_HOME%\tools;%ANDROID_HOME%\platform-tools;%PATH%

REM 检查gradle
echo 检查Gradle...
if exist "gradlew.bat" (
    echo 使用项目自带的Gradle Wrapper
    call gradlew.bat assembleDebug
) else (
    gradle -v >nul 2>&1
    if %errorlevel% equ 0 (
        echo 使用系统Gradle
        gradle assembleDebug
    ) else (
        echo 错误：未找到Gradle，正在下载Gradle Wrapper...
        REM 这里可以添加自动下载gradle wrapper的逻辑
        echo 请手动安装Gradle或使用Android Studio构建
        pause
        exit /b 1
    )
)

if %errorlevel% equ 0 (
    echo.
    echo ✅ APK构建成功！
    echo 📱 APK文件位置：app\build\outputs\apk\debug\app-debug.apk
    echo.
    echo 将APK文件传输到Android设备安装即可使用
) else (
    echo.
    echo ❌ APK构建失败
    echo 请检查错误信息并重试
)

pause