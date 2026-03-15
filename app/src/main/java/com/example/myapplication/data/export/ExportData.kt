package com.example.myapplication.data.export

import com.example.myapplication.data.local.entity.AttachmentEntity
import com.example.myapplication.data.local.entity.MessageEntity

/**
 * 导出消息数据类
 */
data class ExportMessage(
    val id: Long,
    val role: String,
    val content: String,
    val createdAt: Long,
    val isError: Boolean,
    val attachments: List<ExportAttachment>?
)

/**
 * 导出附件数据类
 */
data class ExportAttachment(
    val fileName: String,
    val mimeType: String,
    val localUri: String,
    val extractedText: String?
)

/**
 * 导出会话数据类
 */
data class ExportSession(
    val sessionId: Long,
    val title: String,
    val exportedAt: Long,
    val messages: List<ExportMessage>
)

/**
 * 扩展函数：将 MessageEntity 转换为 ExportMessage
 */
fun MessageEntity.toExportMessage(attachments: List<AttachmentEntity>): ExportMessage {
    return ExportMessage(
        id = this.id,
        role = this.role,
        content = this.content,
        createdAt = this.createdAt,
        isError = this.isError,
        attachments = if (attachments.isEmpty()) null else attachments.map { it.toExportAttachment() }
    )
}

/**
 * 扩展函数：将 AttachmentEntity 转换为 ExportAttachment
 */
fun AttachmentEntity.toExportAttachment(): ExportAttachment {
    return ExportAttachment(
        fileName = this.fileName,
        mimeType = this.mimeType,
        localUri = this.localUri,
        extractedText = this.extractedText
    )
}
