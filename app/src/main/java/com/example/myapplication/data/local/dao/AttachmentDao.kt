package com.example.myapplication.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.data.local.entity.AttachmentEntity
import kotlinx.coroutines.flow.Flow

/**
 * 附件 DAO - 数据访问对象
 */
@Dao
interface AttachmentDao {
    
    /** 插入附件 */
    @Insert
    suspend fun insert(attachment: AttachmentEntity): Long
    
    /** 更新附件 */
    @Update
    suspend fun update(attachment: AttachmentEntity)
    
    /** 删除附件 */
    @Delete
    suspend fun delete(attachment: AttachmentEntity)
    
    /** 按 ID 查询附件 */
    @Query("SELECT * FROM attachments WHERE id = :id")
    suspend fun getById(id: Long): AttachmentEntity?
    
    /** 查询会话的所有附件（按创建时间升序） */
    @Query("SELECT * FROM attachments WHERE sessionId = :sessionId ORDER BY createdAt ASC")
    suspend fun getBySessionId(sessionId: Long): List<AttachmentEntity>
    
    /** 监听会话的所有附件（Flow） */
    @Query("SELECT * FROM attachments WHERE sessionId = :sessionId ORDER BY createdAt ASC")
    fun observeBySessionId(sessionId: Long): Flow<List<AttachmentEntity>>
    
    /** 查询未绑定消息的附件（待发送） */
    @Query("SELECT * FROM attachments WHERE sessionId = :sessionId AND messageId IS NULL ORDER BY createdAt ASC")
    suspend fun getPendingBySessionId(sessionId: Long): List<AttachmentEntity>
    
    /** 监听未绑定消息的附件（Flow） */
    @Query("SELECT * FROM attachments WHERE sessionId = :sessionId AND messageId IS NULL ORDER BY createdAt ASC")
    fun observePendingBySessionId(sessionId: Long): Flow<List<AttachmentEntity>>
    
    /** 绑定附件到消息 */
    @Query("UPDATE attachments SET messageId = :messageId WHERE id = :attachmentId")
    suspend fun bindToMessage(attachmentId: Long, messageId: Long)
    
    /** 删除会话的所有附件 */
    @Query("DELETE FROM attachments WHERE sessionId = :sessionId")
    suspend fun deleteBySessionId(sessionId: Long)
}
