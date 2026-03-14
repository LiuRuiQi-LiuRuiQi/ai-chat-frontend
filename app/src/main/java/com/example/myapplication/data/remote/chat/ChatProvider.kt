package com.example.myapplication.data.remote.chat

/**
 * 聊天 Provider 抽象
 * 第一阶段只实现 OpenAI 兼容接口，保留统一入口便于后续扩展。
 */
interface ChatProvider {

    suspend fun streamChat(
        request: UnifiedChatRequest,
        onDelta: suspend (String) -> Unit,
        onComplete: suspend () -> Unit,
        onError: suspend (String) -> Unit
    )
}
