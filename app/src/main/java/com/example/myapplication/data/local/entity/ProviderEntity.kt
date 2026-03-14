package com.example.myapplication.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * API 提供者实体
 *
 * 第一版仅支持 OpenAICompatible 格式。
 * 保留 isDefault 字段，便于后续和聊天发送逻辑联动。
 */
@Entity(tableName = "providers")
data class ProviderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val baseUrl: String,
    val apiKey: String,
    val apiFormat: String = "OpenAICompatible",
    val defaultModel: String,
    val enabled: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val isDefault: Boolean = false
)
