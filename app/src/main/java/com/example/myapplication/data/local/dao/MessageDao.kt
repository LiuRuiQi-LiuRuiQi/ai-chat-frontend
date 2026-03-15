package com.example.myapplication.data.local.dao

import androidx.room.*
import com.example.myapplication.data.local.entity.MessageEntity
import kotlinx.coroutines.flow.Flow

/** 消息数据访问对象 */
@Dao
interface MessageDao {

    /** 获取指定会话的所有消息，按创建时间正序 */
    @Query("SELECT * FROM messages WHERE sessionId = :sessionId ORDER BY createdAt ASC")
    fun getMessagesBySessionId(sessionId: Long): Flow<List<MessageEntity>>

    /** 一次性获取指定会话的所有消息，按创建时间正序 */
    @Query("SELECT * FROM messages WHERE sessionId = :sessionId ORDER BY createdAt ASC")
    suspend fun getMessagesBySessionIdOnce(sessionId: Long): List<MessageEntity>

    /** 获取指定会话的最后一条消息 */
    @Query("SELECT * FROM messages WHERE sessionId = :sessionId ORDER BY createdAt DESC LIMIT 1")
    suspend fun getLastMessage(sessionId: Long): MessageEntity?

    /** 获取指定会话的消息数量 */
    @Query("SELECT COUNT(*) FROM messages WHERE sessionId = :sessionId")
    suspend fun getMessageCount(sessionId: Long): Int

    /** 插入消息，返回自增 ID */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(message: MessageEntity): Long

    /** 批量插入消息 */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(messages: List<MessageEntity>)

    /** 更新消息 */
    @Update
    suspend fun update(message: MessageEntity)

    /** 删除消息 */
    @Delete
    suspend fun delete(message: MessageEntity)

    /** 删除指定会话的所有消息 */
    @Query("DELETE FROM messages WHERE sessionId = :sessionId")
    suspend fun deleteBySessionId(sessionId: Long)

    /** 按 ID 删除单条消息 */
    @Query("DELETE FROM messages WHERE id = :messageId")
    suspend fun deleteById(messageId: Long)

    /** 删除指定消息之后的所有消息（用于编辑后重新生成） */
    @Query("DELETE FROM messages WHERE sessionId = :sessionId AND id > :messageId")
    suspend fun deleteMessagesAfter(sessionId: Long, messageId: Long)

    /** 按 ID 获取单条消息 */
    @Query("SELECT * FROM messages WHERE sessionId = :sessionId AND id = :messageId")
    suspend fun getMessageById(sessionId: Long, messageId: Long): MessageEntity?

}
