package com.example.myapplication.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 模型实体
 *
 * 用于缓存某个 Provider 可用的模型列表。
 * - providerId: 关联 providers.id
 * - modelName: OpenAI 兼容接口 /v1/models 返回的 id
 */
@Entity(
    tableName = "models",
    indices = [
        Index(value = ["providerId", "modelName"], unique = true)
    ]
)
data class ModelEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val providerId: Long,
    val modelName: String,
    val displayName: String,
    val enabled: Boolean = true
)
