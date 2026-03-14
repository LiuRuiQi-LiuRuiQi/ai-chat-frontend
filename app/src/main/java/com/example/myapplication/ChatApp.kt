package com.example.myapplication

import android.app.Application
import com.example.myapplication.data.datastore.SettingsDataStore
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.repository.AttachmentRepository
import com.example.myapplication.data.repository.CharacterRepository
import com.example.myapplication.data.repository.MessageRepository
import com.example.myapplication.data.repository.ModelRepository
import com.example.myapplication.data.repository.PresetRepository
import com.example.myapplication.data.repository.ProviderRepository
import com.example.myapplication.data.repository.SessionRepository
import com.example.myapplication.data.repository.WorldBookRepository
import com.example.myapplication.ui.ViewModelFactory
import okhttp3.OkHttpClient

/**
 * 全局 Application 类
 * 负责初始化数据库、Repository、DataStore 等全局单例
 * 采用手动依赖注入（后续可迁移到 Hilt/Koin）
 */
class ChatApp : Application() {

    /** 数据库实例 */
    val database: AppDatabase by lazy { AppDatabase.getInstance(this) }

    /** 各 Repository 实例 */
    val sessionRepository: SessionRepository by lazy {
        SessionRepository(database.sessionDao())
    }
    val messageRepository: MessageRepository by lazy {
        MessageRepository(database.messageDao())
    }
    val providerRepository: ProviderRepository by lazy {
        ProviderRepository(database.providerDao())
    }
    val presetRepository: PresetRepository by lazy {
        PresetRepository(database.presetDao())
    }

    /** 模型仓库 */
    val modelRepository: ModelRepository by lazy {
        ModelRepository(database.modelDao())
    }

    /** 角色仓库 */
    val characterRepository: CharacterRepository by lazy {
        CharacterRepository(database.characterDao())
    }

    /** 世界书仓库 */
    val worldBookRepository: WorldBookRepository by lazy {
        WorldBookRepository(database.worldBookEntryDao())
    }

    /** 附件仓库 */
    val attachmentRepository: AttachmentRepository by lazy {
        AttachmentRepository(database.attachmentDao())
    }

    /** ViewModel 工厂 */
    val viewModelFactory: ViewModelFactory by lazy {
        ViewModelFactory(this)
    }

    /** 设置存储 */
    val settingsDataStore: SettingsDataStore by lazy {
        SettingsDataStore(this)
    }

    /** OkHttpClient 单例 */
    val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder().build()
    }
}
