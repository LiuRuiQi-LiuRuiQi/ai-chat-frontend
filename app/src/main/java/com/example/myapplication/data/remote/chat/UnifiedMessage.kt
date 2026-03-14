package com.example.myapplication.data.remote.chat

import com.google.gson.annotations.SerializedName

/** 统一聊天消息结构，便于后续扩展到不同 Provider */
data class UnifiedMessage(
    val role: String,
    val content: String
)

/**
 * OpenAI 兼容聊天请求体（非 stream 字段也保持兼容，便于后续扩展）
 *
 * 说明：
 * - 项目序列化为 Gson，因此使用 @SerializedName 对齐 OpenAI 字段名
 */
data class UnifiedChatRequest(
    val model: String,
    val messages: List<UnifiedMessage>,
    val stream: Boolean = true,
    @SerializedName("temperature")
    val temperature: Float? = null,
    @SerializedName("top_p")
    val topP: Float? = null,
    @SerializedName("max_tokens")
    val maxTokens: Int? = null
)
