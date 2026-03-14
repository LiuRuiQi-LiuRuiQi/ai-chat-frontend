package com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.MessageDao
import com.example.myapplication.data.local.entity.MessageEntity
import kotlinx.coroutines.flow.Flow

/**
 * 消息仓库 - 封装消息相关数据操作
 */
class MessageRepository(private val messageDao: MessageDao) {

    /** 获取指定会话的所有消息（响应式） */
    fun getMessagesBySessionId(sessionId: Long): Flow<List<MessageEntity>> =
        messageDao.getMessagesBySessionId(sessionId)

    /** 一次性获取指定会话的所有消息 */
    suspend fun getMessagesOnce(sessionId: Long): List<MessageEntity> =
        messageDao.getMessagesBySessionIdOnce(sessionId)

    /** 获取指定会话的最后一条消息 */
    suspend fun getLastMessage(sessionId: Long): MessageEntity? =
        messageDao.getLastMessage(sessionId)

    /** 发送用户消息 */
    suspend fun addUserMessage(sessionId: Long, content: String): Long {
        val message = MessageEntity(
            sessionId = sessionId,
            role = "user",
            content = content
        )
        return messageDao.insert(message)
    }

    /** 添加 AI 回复消息 */
    suspend fun addAssistantMessage(sessionId: Long, content: String): Long {
        val message = MessageEntity(
            sessionId = sessionId,
            role = "assistant",
            content = content
        )
        return messageDao.insert(message)
    }

    /** 添加系统消息 */
    suspend fun addSystemMessage(sessionId: Long, content: String): Long {
        val message = MessageEntity(
            sessionId = sessionId,
            role = "system",
            content = content
        )
        return messageDao.insert(message)
    }

    /** 添加错误消息 */
    suspend fun addErrorMessage(sessionId: Long, content: String): Long {
        val message = MessageEntity(
            sessionId = sessionId,
            role = "assistant",
            content = content,
            isError = true
        )
        return messageDao.insert(message)
    }

    /** 更新消息内容（用于流式更新） */
    suspend fun updateMessage(message: MessageEntity) = messageDao.update(message)

    /** 删除指定会话的所有消息 */
    suspend fun deleteBySessionId(sessionId: Long) = messageDao.deleteBySessionId(sessionId)
}
