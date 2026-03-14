package com.example.myapplication.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myapplication.data.local.dao.AttachmentDao
import com.example.myapplication.data.local.dao.CharacterDao
import com.example.myapplication.data.local.dao.MessageDao
import com.example.myapplication.data.local.dao.ModelDao
import com.example.myapplication.data.local.dao.PresetDao
import com.example.myapplication.data.local.dao.ProviderDao
import com.example.myapplication.data.local.dao.SessionDao
import com.example.myapplication.data.local.dao.WorldBookEntryDao
import com.example.myapplication.data.local.entity.AttachmentEntity
import com.example.myapplication.data.local.entity.CharacterEntity
import com.example.myapplication.data.local.entity.MessageEntity
import com.example.myapplication.data.local.entity.ModelEntity
import com.example.myapplication.data.local.entity.PresetEntity
import com.example.myapplication.data.local.entity.ProviderEntity
import com.example.myapplication.data.local.entity.SessionEntity
import com.example.myapplication.data.local.entity.WorldBookEntryEntity

/**
 * Room 数据库定义
 * version = 1：初始版本
 * exportSchema = false：不导出 schema JSON（简化开发）
 */
@Database(
    entities = [
        SessionEntity::class,
        MessageEntity::class,
        ProviderEntity::class,
        PresetEntity::class,
        ModelEntity::class,
        CharacterEntity::class,
        WorldBookEntryEntity::class,
        AttachmentEntity::class
    ],
    version = 8,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun sessionDao(): SessionDao
    abstract fun messageDao(): MessageDao
    abstract fun providerDao(): ProviderDao
    abstract fun presetDao(): PresetDao
    abstract fun modelDao(): ModelDao
    abstract fun characterDao(): CharacterDao
    abstract fun worldBookEntryDao(): WorldBookEntryDao
    abstract fun attachmentDao(): AttachmentDao

    companion object {
        private const val DB_NAME = "chat_app.db"

        /**
         * MIGRATION 3 -> 4：
         * 新增 models 表，用于缓存 Provider 可用模型列表。
         */
        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `models` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `providerId` INTEGER NOT NULL,
                        `modelName` TEXT NOT NULL,
                        `displayName` TEXT NOT NULL,
                        `enabled` INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    "CREATE UNIQUE INDEX IF NOT EXISTS `index_models_providerId_modelName` ON `models` (`providerId`, `modelName`)"
                )
            }
        }

        /**
         * MIGRATION 4 -> 5：
         * sessions 表新增 modelName 字段，用于会话级模型绑定。
         */
        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE `sessions` ADD COLUMN `modelName` TEXT")
            }
        }

        /**
         * MIGRATION 5 -> 6：
         * - 新增 characters 表
         * - sessions 表新增 characterId 字段
         */
         val MIGRATION_5_6 = object : Migration(5, 6) {
             override fun migrate(db: SupportSQLiteDatabase) {
                 db.execSQL(
                     """
                     CREATE TABLE IF NOT EXISTS `characters` (
                         `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                         `name` TEXT NOT NULL,
                         `description` TEXT NOT NULL,
                         `greeting` TEXT NOT NULL
                     )
                     """.trimIndent()
                 )
                 db.execSQL("ALTER TABLE `sessions` ADD COLUMN `characterId` INTEGER")
             }
         }

         /**
          * MIGRATION 6 -> 7：
          * 新增 world_book_entries 表（世界书条目）
          */
          val MIGRATION_6_7 = object : Migration(6, 7) {
              override fun migrate(db: SupportSQLiteDatabase) {
                  db.execSQL(
                      """
                      CREATE TABLE IF NOT EXISTS `world_book_entries` (
                          `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                          `title` TEXT NOT NULL,
                          `keywords` TEXT NOT NULL,
                          `content` TEXT NOT NULL,
                          `enabled` INTEGER NOT NULL,
                          `priority` INTEGER NOT NULL,
                          `createdAt` INTEGER NOT NULL,
                          `updatedAt` INTEGER NOT NULL
                      )
                      """.trimIndent()
                  )
                  db.execSQL(
                      "CREATE INDEX IF NOT EXISTS `index_world_book_entries_enabled` ON `world_book_entries` (`enabled`)"
                  )
              }
          }

         /**
          * MIGRATION 7 -> 8：
          * 新增 attachments 表（文件附件）
          */
          val MIGRATION_7_8 = object : Migration(7, 8) {
              override fun migrate(db: SupportSQLiteDatabase) {
                  db.execSQL(
                      """
                      CREATE TABLE IF NOT EXISTS `attachments` (
                          `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                          `sessionId` INTEGER NOT NULL,
                          `messageId` INTEGER,
                          `fileName` TEXT NOT NULL,
                          `mimeType` TEXT NOT NULL,
                          `localUri` TEXT NOT NULL,
                          `extractedText` TEXT NOT NULL,
                          `createdAt` INTEGER NOT NULL,
                          FOREIGN KEY(`sessionId`) REFERENCES `sessions`(`id`) ON DELETE CASCADE,
                          FOREIGN KEY(`messageId`) REFERENCES `messages`(`id`) ON DELETE CASCADE
                      )
                      """.trimIndent()
                  )
                  db.execSQL(
                      "CREATE INDEX IF NOT EXISTS `index_attachments_sessionId` ON `attachments` (`sessionId`)"
                  )
                  db.execSQL(
                      "CREATE INDEX IF NOT EXISTS `index_attachments_messageId` ON `attachments` (`messageId`)"
                  )
              }
          }

        @Volatile
        private var INSTANCE: AppDatabase? = null

        /** 获取数据库单例 */
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                )
                    .addMigrations(MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6, MIGRATION_6_7, MIGRATION_7_8)
                    // 当前仍使用 fallbackToDestructiveMigration 作为开发阶段兜底：
                    // - 方便快速迭代 schema，不阻断启动
                    // - future 版本会移除 fallback 并严格依赖显式 Migration
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries() // TODO: 仅为临时调试，后续应改为异步初始化
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
