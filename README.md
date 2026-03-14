# AI Chat Frontend - Android

一个基于 **Jetpack Compose** 的现代化 Android AI 聊天应用前端，支持 OpenAI 兼容接口。

## 🎯 项目特性

✅ **Jetpack Compose** - 现代化声明式 UI 框架  
✅ **MVVM 架构** - 清晰的代码分层  
✅ **Retrofit + OkHttp** - 高效网络请求  
✅ **Room 数据库** - 本地数据持久化  
✅ **DataStore** - 安全的设置存储  
✅ **Coroutines + Flow** - 异步编程  
✅ **Material Design 3** - 现代化 UI 设计  
✅ **100% Kotlin** - 现代化编程语言  

## 📱 功能模块

### 已实现
- ✅ Provider 配置（API Key、Base URL、模型名）
- ✅ 聊天消息发送与接收
- ✅ 聊天历史记录存储
- ✅ 模型选择与切换
- ✅ 会话管理
- ✅ 微信风格 UI

### 规划中
- 🔄 角色系统（Character/Persona）
- 🔄 世界书（World Book）
- 🔄 MCP 集成
- 🔄 Skills 系统

## 🛠️ 技术栈

| 层级 | 技术 | 版本 |
|------|------|------|
| UI | Jetpack Compose | 1.6.x |
| 架构 | MVVM + Repository | - |
| 网络 | Retrofit + OkHttp | 2.11.x / 4.12.x |
| 数据库 | Room | 2.6.x |
| 设置存储 | DataStore | 1.0.x |
| 异步 | Coroutines + Flow | 1.7.x |
| 序列化 | Kotlinx Serialization | 1.6.x |

## 🚀 快速开始

### 环境要求
- **JDK 17+**
- **Android SDK 34+**
- **Gradle 8.6+**（已包含 Wrapper）

### 构建项目

```bash
# 清理构建
./gradlew clean

# 编译 Debug APK
./gradlew assembleDebug

# 编译 Release APK
./gradlew assembleRelease

# 安装到设备
./gradlew installDebug
```

### 生成的 APK 位置
```
app/build/outputs/apk/debug/app-debug.apk
```

## ⚙️ 配置说明

### Provider 配置
应用支持自定义 OpenAI 兼容接口配置：

```kotlin
Provider(
    name = "OpenAI",
    apiKey = "sk-...",
    baseUrl = "https://api.openai.com/v1",
    modelName = "gpt-4o-mini"
)
```

配置通过 DataStore 持久化存储。

## 🔐 安全性

- ✅ API Key 通过 DataStore 加密存储
- ✅ 敏感信息不硬编码
- ✅ 支持 HTTPS 验证

## 📚 相关资源

- [Jetpack Compose 官方文档](https://developer.android.com/jetpack/compose)
- [MVVM 架构指南](https://developer.android.com/jetpack/guide)
- [Retrofit 文档](https://square.github.io/retrofit/)
- [Room 数据库](https://developer.android.com/training/data-storage/room)
- [DataStore](https://developer.android.com/topic/libraries/architecture/datastore)

## 📄 许可证

MIT License

---

**最后更新**: 2026-03-14  
**项目状态**: 🚀 开发中
