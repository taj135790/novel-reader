# Android小说阅读器 - 构建指南

## 项目概述
这是一个功能完整的Android小说阅读应用，支持TXT文件导入、智能章节识别、文本朗读、自定义阅读设置等功能。

## 构建APK步骤

### 方法一：使用Android Studio（推荐）

1. **安装Android Studio**
   - 下载并安装最新版Android Studio
   - 确保安装Android SDK（API 21+）

2. **导入项目**
   - 打开Android Studio
   - 选择"Open an existing Android Studio project"
   - 选择本项目根目录（d:\\app\\langdu1）

3. **构建APK**
   - 等待项目同步完成
   - 点击菜单 Build → Build Bundle(s) / APK(s) → Build APK(s)
   - 构建完成后，APK文件将生成在：`app/build/outputs/apk/debug/app-debug.apk`

### 方法二：使用命令行（需要安装Android SDK）

1. **设置环境变量**
   ```bash
   # 设置ANDROID_HOME指向Android SDK路径
   set ANDROID_HOME=C:\Users\YourName\AppData\Local\Android\Sdk
   
   # 将Android SDK工具添加到PATH
   set PATH=%ANDROID_HOME%\tools;%ANDROID_HOME%\platform-tools;%PATH%
   ```

2. **使用gradlew构建**
   ```bash
   # 在项目根目录执行
   .\gradlew.bat assembleDebug
   
   # 或者构建release版本
   .\gradlew.bat assembleRelease
   ```

## 项目特性

### 已实现功能
- ✅ TXT小说导入功能
- ✅ 智能章节识别（支持多种章节格式）
- ✅ 左右翻页动画（平移效果）
- ✅ 文本朗读功能（使用系统TTS引擎）
- ✅ 字体大小、行距、字距调整
- ✅ 章节目录和快速跳转
- ✅ 无欢迎页面，直接使用

### 技术规格
- **最低Android版本**：5.0（API 21）
- **目标Android版本**：13（API 34）
- **开发语言**：Kotlin
- **架构模式**：MVVM
- **依赖库**：Android Jetpack系列

## 安装和使用

1. **安装APK**
   - 将生成的APK文件传输到Android设备
   - 在设备上允许"未知来源"安装
   - 安装APK文件

2. **使用应用**
   - 打开应用后直接进入阅读界面
   - 点击右上角菜单导入TXT文件
   - 左右滑动翻页
   - 点击设置按钮调整阅读参数
   - 点击语音按钮开始/停止朗读

## 注意事项

1. **权限要求**
   - 应用需要存储权限来读取TXT文件
   - 首次导入文件时会请求权限

2. **语音功能**
   - 需要设备支持中文TTS引擎
   - 首次使用可能需要下载语音数据

3. **章节识别**
   - 支持常见章节格式（第X章、Chapter X等）
   - 支持特殊章节（前言、序章、尾声等）

## 项目结构
```
app/
├── src/main/java/com/novel/reader/
│   ├── MainActivity.kt          # 主界面
│   ├── NovelViewModel.kt       # 数据管理
│   ├── NovelParser.kt          # 文本解析
│   ├── ReaderFragment.kt       # 阅读页面
│   ├── ReaderPagerAdapter.kt   # 翻页适配器
│   ├── ChapterListDialog.kt    # 目录对话框
│   └── SettingsDialog.kt       # 设置对话框
├── res/                        # 资源文件
└── AndroidManifest.xml         # 应用配置
```

## 问题排查

如果构建过程中遇到问题：

1. **同步失败**：检查网络连接，确保能访问Google Maven仓库
2. **编译错误**：确保Android Studio和Gradle插件是最新版本
3. **权限问题**：在AndroidManifest.xml中已声明所需权限

## 联系方式
如有问题，请参考Android官方文档或联系开发者。