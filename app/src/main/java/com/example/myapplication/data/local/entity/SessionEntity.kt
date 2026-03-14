package com.example.myapplication.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 会话实体 - 对应一个聊天会话
 * @param id 自增主键
 * @param title 会话标题（默认取第一条用户消息摘要）
 * @param providerId 关联的 API 提供者 ID
 * @param presetId 关联的预设 ID（可选，为后续角色/世界书等扩展预留）
 * @param createdAt 创建时间戳
 * @param updatedAt 最后更新时间戳
 * @param isPinned 是否置顶
 * @param modelName 会话绑定的模型名（可选；为空时使用 Provider 默认模型）
 */
@Entity(tableName = "sessions")
data class SessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    // 默认会话标题（会在首轮 AI 回复完成后自动改写）
    val title: String = "新聊天",
    val providerId: Long? = null,
    val presetId: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isPinned: Boolean = false,
    val modelName: String? = null,
    /** 绑定角色 ID（可选） */
    val characterId: Long? = null
)
