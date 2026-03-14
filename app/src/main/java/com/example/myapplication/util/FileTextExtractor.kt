package com.example.myapplication.util

import android.content.Context
import android.net.Uri
import java.nio.charset.StandardCharsets

/**
 * 文件文本提取工具
 * 支持 txt、md、json、csv 格式
 */
object FileTextExtractor {
    
    private const val MAX_FILE_SIZE = 1024 * 1024 // 1MB 限制
    private const val SUPPORTED_MIME_TYPES = "text/plain,text/markdown,application/json,text/csv"
    
    /**
     * 从 URI 提取文本内容
     * @param context Android Context
     * @param uri 文件 URI
     * @return 提取的文本内容，失败返回 null
     */
    fun extractText(context: Context, uri: Uri): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            
            // 检查文件大小
            val fileSize = inputStream.available()
            if (fileSize > MAX_FILE_SIZE) {
                inputStream.close()
                return null
            }
            
            // 读取文件内容
            val bytes = inputStream.readBytes()
            inputStream.close()
            
            // 尝试用 UTF-8 解码
            String(bytes, StandardCharsets.UTF_8)
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * 检查 MIME 类型是否支持
     */
    fun isSupportedMimeType(mimeType: String): Boolean {
        return SUPPORTED_MIME_TYPES.contains(mimeType)
    }
    
    /**
     * 获取支持的 MIME 类型数组
     */
    fun getSupportedMimeTypes(): Array<String> {
        return arrayOf(
            "text/plain",
            "text/markdown",
            "application/json",
            "text/csv"
        )
    }
}
