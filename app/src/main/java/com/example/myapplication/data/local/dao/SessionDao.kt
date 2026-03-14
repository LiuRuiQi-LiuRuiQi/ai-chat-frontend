package com.example.myapplication.data.local.dao

import androidx.room.*
import com.example.myapplication.data.local.entity.SessionEntity
import kotlinx.coroutines.flow.Flow

/** 会话数据访问对象 */
@Dao
interface SessionDao {

    /** 获取所有会话，按置顶优先、更新时间倒序 */
    @Query("SELECT * FROM sessions ORDER BY isPinned DESC, updatedAt DESC")
    fun getAllSessions(): Flow<List<SessionEntity>>

    /** 根据 ID 获取单个会话 */
    @Query("SELECT * FROM sessions WHERE id = :id")
    suspend fun getSessionById(id: Long): SessionEntity?

    /** 插入会话，返回自增 ID */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: SessionEntity): Long

    /** 更新会话 */
    @Update
    suspend fun update(session: SessionEntity)

    /** 删除会话 */
    @Delete
    suspend fun delete(session: SessionEntity)

    /** 根据 ID 删除会话 */
    @Query("DELETE FROM sessions WHERE id = :id")
    suspend fun deleteById(id: Long)

    /** 更新会话的最后修改时间 */
    @Query("UPDATE sessions SET updatedAt = :timestamp WHERE id = :id")
    suspend fun updateTimestamp(id: Long, timestamp: Long = System.currentTimeMillis())

    /** 更新会话标题 */
    @Query("UPDATE sessions SET title = :title, updatedAt = :timestamp WHERE id = :id")
    suspend fun updateTitle(id: Long, title: String, timestamp: Long = System.currentTimeMillis())

    /** 更新会话绑定的模型（为空表示跟随 Provider 默认模型） */
    @Query("UPDATE sessions SET modelName = :modelName WHERE id = :sessionId")
    suspend fun updateModelName(sessionId: Long, modelName: String?)

    /** 更新会话绑定的角色（可为空） */
    @Query("UPDATE sessions SET characterId = :characterId WHERE id = :sessionId")
    suspend fun updateCharacterId(sessionId: Long, characterId: Long?)
}
