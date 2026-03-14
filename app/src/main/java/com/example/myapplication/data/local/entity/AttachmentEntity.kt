package com.example.myapplication.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 附件实体 - 存储文件上传记录与提取的文本内容
 * 支持 txt、md、json、csv 格式
 */
@Entity(
    tableName = "attachments",
    foreignKeys = [
        ForeignKey(
            entity = SessionEntity::class,
            parentColumns = ["id"],
            childColumns = ["sessionId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MessageEntity::class,
            parentColumns = ["id"],
            childColumns = ["messageId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("sessionId"),
        Index("messageId")
    ]
)
data class AttachmentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    /** 关联的会话 ID */
    val sessionId: Long,
    
    /** 关联的消息 ID（发送前为 null，发送后绑定） */
    val messageId: Long? = null,
    
    /** 文件名 */
    val fileName: String,
    
    /** MIME 类型（text/plain, text/markdown, application/json, text/csv） */
    val mimeType: String,
    
    /** 文件 URI 字符串 */
    val localUri: String,
    
    /** 提取的纯文本内容 */
    val extractedText: String,
    
    /** 创建时间戳（毫秒） */
    val createdAt: Long = System.currentTimeMillis()
)
