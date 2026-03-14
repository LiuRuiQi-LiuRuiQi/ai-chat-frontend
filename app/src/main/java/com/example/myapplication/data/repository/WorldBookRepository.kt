package com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.WorldBookEntryDao
import com.example.myapplication.data.local.entity.WorldBookEntryEntity
import kotlinx.coroutines.flow.Flow

/**
 * 世界书数据仓库
 */
class WorldBookRepository(
    private val worldBookEntryDao: WorldBookEntryDao
) {

    fun observeEnabledEntries(): Flow<List<WorldBookEntryEntity>> =
        worldBookEntryDao.observeEnabledEntries()

    fun observeAll(): Flow<List<WorldBookEntryEntity>> =
        worldBookEntryDao.observeAll()

    suspend fun getEnabledEntriesOnce(): List<WorldBookEntryEntity> =
        worldBookEntryDao.getEnabledEntriesOnce()

    suspend fun getById(id: Long): WorldBookEntryEntity? =
        worldBookEntryDao.getById(id)

    suspend fun upsert(entity: WorldBookEntryEntity): Long =
        worldBookEntryDao.upsert(entity)

    suspend fun update(entity: WorldBookEntryEntity) =
        worldBookEntryDao.update(entity)

    suspend fun delete(entity: WorldBookEntryEntity) =
        worldBookEntryDao.delete(entity)

    /**
     * 根据文本内容命中世界书条目
     * 返回命中的条目列表（已按优先级排序）
     */
    suspend fun findMatchingEntries(vararg texts: String): List<WorldBookEntryEntity> {
        val enabledEntries = getEnabledEntriesOnce()
        val combinedText = texts.joinToString(" ").lowercase()

        return enabledEntries.filter { entry ->
            entry.keywords
                .split(",")
                .map { it.trim().lowercase() }
                .filter { it.isNotBlank() }
                .any { keyword -> combinedText.contains(keyword) }
        }
    }
}
