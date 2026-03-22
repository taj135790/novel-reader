# 📖 小说阅读器 - Novel Reader

一个功能完整的Android小说阅读应用，支持TXT文件导入、智能章节识别、文本朗读等功能。

## ✨ 功能特性

- 📚 **TXT小说导入** - 支持从文件系统导入TXT格式小说
- 🔍 **智能章节识别** - 自动识别章节标题和内容分割
- 📖 **左右翻页动画** - 平滑的平移翻页效果
- 🔊 **文本朗读功能** - 使用Android系统TTS引擎
- ⚙️ **阅读设置调整** - 字体大小、行距、字距自定义
- 📑 **章节目录** - 章节列表和快速跳转
- 🚀 **直接使用** - 无欢迎页面，打开即用

## 📱 下载APK

[![GitHub Actions](https://github.com/您的用户名/novel-reader/actions/workflows/build.yml/badge.svg)](https://github.com/您的用户名/novel-reader/actions)

点击上方徽章或在Actions页面下载最新APK文件。

## 🛠️ 技术架构

- **语言**: Kotlin
- **架构**: MVVM (Model-View-ViewModel)
- **最低Android版本**: 5.0 (API 21)
- **目标Android版本**: 13 (API 34)
- **依赖库**: Android Jetpack系列

## 🚀 快速开始

### 方法一：GitHub自动构建（推荐）

1. **上传到GitHub**
   - 将本项目上传到您的GitHub仓库
   - GitHub会自动开始构建APK

2. **下载APK**
   - 在仓库的"Actions"标签页下载生成的APK
   - 安装到Android设备即可使用

### 方法二：本地构建

```bash
# 使用Gradle构建
./gradlew assembleDebug

# APK文件位置
app/build/outputs/apk/debug/app-debug.apk
```

## 📖 使用说明

### 导入小说
1. 点击右上角菜单 → "导入小说"
2. 选择TXT文件
3. 应用会自动识别章节并开始阅读

### 阅读控制
- **左右滑动** - 翻页
- **设置按钮** - 调整字体、间距等
- **目录按钮** - 查看章节列表
- **语音按钮** - 开始/停止朗读

### 章节识别规则
应用支持以下章节格式：
- `第X章 标题`
- `Chapter X Title`
- `X. 标题`
- 特殊章节：前言、序章、尾声、后记、番外

## 🏗️ 项目结构

```
novel-reader/
├── app/
│   ├── src/main/java/com/novel/reader/
│   │   ├── MainActivity.kt          # 主界面
│   │   ├── NovelViewModel.kt       # 数据管理
│   │   ├── NovelParser.kt          # 文本解析
│   │   ├── ReaderFragment.kt       # 阅读页面
│   │   ├── ReaderPagerAdapter.kt   # 翻页适配器
│   │   ├── ChapterListDialog.kt    # 目录对话框
│   │   └── SettingsDialog.kt       # 设置对话框
│   ├── res/                        # 资源文件
│   └── AndroidManifest.xml         # 应用配置
├── .github/workflows/
│   └── build.yml                   # 自动构建配置
└── README.md                       # 项目说明
```

## 🔧 开发环境

- Android Studio Arctic Fox+
- Java 8+
- Android SDK 21+

## 📄 许可证

本项目采用 MIT 许可证。

## 🤝 贡献

欢迎提交Issue和Pull Request！

## 📞 联系方式

如有问题，请通过GitHub Issues联系我们。

---

**享受阅读的乐趣！** 📚