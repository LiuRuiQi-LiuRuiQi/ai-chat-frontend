package com.example.myapplication.util

import com.example.myapplication.data.export.ExportSession
import com.google.gson.GsonBuilder
import java.text.SimpleDateFormat
import java.util.*

object ChatExporter {
    
    fun exportToMarkdown(session: ExportSession, includeAttachments: Boolean): String {
        val sb = StringBuilder()
        
        // 标题和导出时间
        sb.appendLine("# ${session.title}")
        sb.appendLine()
        sb.appendLine("导出时间: ${formatDateTime(session.exportedAt)}")
        sb.appendLine()
        sb.appendLine("---")
        sb.appendLine()
        
        // 消息列表
        session.messages.forEach { message ->
            sb.appendLine("## ${message.role}")
            sb.appendLine()
            sb.appendLine("*${formatDateTime(message.createdAt)}*")
            sb.appendLine()
            sb.appendLine(message.content)
            sb.appendLine()
            
            // 附件信息
            if (includeAttachments && !message.attachments.isNullOrEmpty()) {
                message.attachments.forEach { attachment ->
                    sb.appendLine("[Attachment] ${attachment.fileName} (${attachment.mimeType})")
                    if (!attachment.extractedText.isNullOrBlank()) {
                        sb.appendLine("  提取文本: ${attachment.extractedText}")
                    }
                }
                sb.appendLine()
            }
        }
        
        return sb.toString()
    }
    
    fun exportToJson(session: ExportSession, includeAttachments: Boolean): String {
        val gson = GsonBuilder()
            .setPrettyPrinting()
            .create()
        
        val exportData = if (!includeAttachments) {
            session.copy(
                messages = session.messages.map { it.copy(attachments = null) }
            )
        } else {
            session
        }
        
        return gson.toJson(exportData)
    }
    
    fun generateFileName(title: String, sessionId: Long, format: String): String {
        val sanitizedTitle = title
            .replace(Regex("[^a-zA-Z0-9\\u4e00-\\u9fa5_-]"), "_")
            .take(30)
        
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
            .format(Date())
        
        val extension = when (format.lowercase()) {
            "markdown" -> "md"
            "json" -> "json"
            else -> "txt"
        }
        
        return "${sanitizedTitle}_${sessionId}_${timestamp}.${extension}"
    }
    
    private fun formatDateTime(timestamp: Long): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            .format(Date(timestamp))
    }
}
