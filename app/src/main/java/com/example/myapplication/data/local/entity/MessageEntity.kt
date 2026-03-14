package com.example.myapplication.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 消息实体 - 对应聊天中的一条消息
 * @param id 自增主键
 * @param sessionId 所属会话 ID（外键关联 sessions 表）
 * @param role 消息角色：system / user / assistant
 * @param content 消息文本内容
 * @param createdAt 创建时间戳
 * @param tokenCount token 数量（可选，用于统计）
 * @param isError 是否为错误消息
 */
@Entity(
    tableName = "messages",
    foreignKeys = [
        ForeignKey(
            entity = SessionEntity::class,
            parentColumns = ["id"],
            childColumns = ["sessionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("sessionId")]
)
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sessionId: Long,
    val role: String,       // "system" | "user" | "assistant"
    val content: String,
    val createdAt: Long = System.currentTimeMillis(),
    val tokenCount: Int? = null,
    val isError: Boolean = false
)
