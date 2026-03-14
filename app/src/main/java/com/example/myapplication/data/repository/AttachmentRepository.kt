package com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.AttachmentDao
import com.example.myapplication.data.local.entity.AttachmentEntity
import kotlinx.coroutines.flow.Flow

/**
 * 附件仓库 - 业务逻辑层
 */
class AttachmentRepository(private val dao: AttachmentDao) {
    
    /** 插入附件 */
    suspend fun insert(attachment: AttachmentEntity): Long {
        return dao.insert(attachment)
    }
    
    /** 更新附件 */
    suspend fun update(attachment: AttachmentEntity) {
        dao.update(attachment)
    }
    
    /** 删除附件 */
    suspend fun delete(attachment: AttachmentEntity) {
        dao.delete(attachment)
    }
    
    /** 按 ID 查询附件 */
    suspend fun getById(id: Long): AttachmentEntity? {
        return dao.getById(id)
    }
    
    /** 查询会话的所有附件 */
    suspend fun getBySessionId(sessionId: Long): List<AttachmentEntity> {
        return dao.getBySessionId(sessionId)
    }
    
    /** 监听会话的所有附件 */
    fun observeBySessionId(sessionId: Long): Flow<List<AttachmentEntity>> {
        return dao.observeBySessionId(sessionId)
    }
    
    /** 查询未绑定消息的附件（待发送） */
    suspend fun getPendingBySessionId(sessionId: Long): List<AttachmentEntity> {
        return dao.getPendingBySessionId(sessionId)
    }
    
    /** 监听未绑定消息的附件（Flow） */
    fun observePendingBySessionId(sessionId: Long): Flow<List<AttachmentEntity>> {
        return dao.observePendingBySessionId(sessionId)
    }
    
    /** 绑定附件到消息 */
    suspend fun bindToMessage(attachmentId: Long, messageId: Long) {
        dao.bindToMessage(attachmentId, messageId)
    }
    
    /** 删除会话的所有附件 */
    suspend fun deleteBySessionId(sessionId: Long) {
        dao.deleteBySessionId(sessionId)
    }
}
