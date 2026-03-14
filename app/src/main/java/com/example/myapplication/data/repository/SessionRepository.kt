package com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.SessionDao
import com.example.myapplication.data.local.entity.SessionEntity
import kotlinx.coroutines.flow.Flow

/**
 * 会话仓库 - 封装会话相关数据操作
 */
class SessionRepository(private val sessionDao: SessionDao) {

    /** 获取所有会话列表（响应式） */
    fun getAllSessions(): Flow<List<SessionEntity>> = sessionDao.getAllSessions()

    /** 根据 ID 获取会话 */
    suspend fun getSessionById(id: Long): SessionEntity? = sessionDao.getSessionById(id)

    /** 创建新会话，返回会话 ID */
    suspend fun createSession(
        title: String = "新聊天",
        providerId: Long? = null,
        presetId: Long? = null
    ): Long {
        val session = SessionEntity(
            title = title,
            providerId = providerId,
            presetId = presetId
        )
        return sessionDao.insert(session)
    }

    /** 更新会话 */
    suspend fun updateSession(session: SessionEntity) = sessionDao.update(session)

    /** 更新会话标题 */
    suspend fun updateTitle(sessionId: Long, title: String) =
        sessionDao.updateTitle(sessionId, title)

    /** 更新会话时间戳 */
    suspend fun updateTimestamp(sessionId: Long) = sessionDao.updateTimestamp(sessionId)

    /** 删除会话 */
    suspend fun deleteSession(sessionId: Long) = sessionDao.deleteById(sessionId)

    /** 更新会话绑定的模型（为空表示跟随 Provider 默认模型） */
    suspend fun updateModelName(sessionId: Long, modelName: String?) =
        sessionDao.updateModelName(sessionId, modelName)

    /** 更新会话绑定的角色（可为空） */
    suspend fun updateCharacterId(sessionId: Long, characterId: Long?) =
        sessionDao.updateCharacterId(sessionId, characterId)
}
