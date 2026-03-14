package com.example.myapplication.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.data.local.entity.WorldBookEntryEntity
import kotlinx.coroutines.flow.Flow

/**
 * 世界书条目 DAO
 */
@Dao
interface WorldBookEntryDao {

    /** 观察所有启用的世界书条目（按优先级从高到低排序） */
    @Query("SELECT * FROM world_book_entries WHERE enabled = 1 ORDER BY priority DESC, id DESC")
    fun observeEnabledEntries(): Flow<List<WorldBookEntryEntity>>

    /** 观察所有世界书条目（包括禁用的，按优先级从高到低排序） */
    @Query("SELECT * FROM world_book_entries ORDER BY priority DESC, id DESC")
    fun observeAll(): Flow<List<WorldBookEntryEntity>>

    /** 获取所有启用的条目（一次性查询） */
    @Query("SELECT * FROM world_book_entries WHERE enabled = 1 ORDER BY priority DESC, id DESC")
    suspend fun getEnabledEntriesOnce(): List<WorldBookEntryEntity>

    /** 根据 ID 获取条目 */
    @Query("SELECT * FROM world_book_entries WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): WorldBookEntryEntity?

    /** 插入或更新条目 */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: WorldBookEntryEntity): Long

    /** 更新条目 */
    @Update
    suspend fun update(entity: WorldBookEntryEntity)

    /** 删除条目 */
    @Delete
    suspend fun delete(entity: WorldBookEntryEntity)

    /** 删除所有条目（仅用于测试） */
    @Query("DELETE FROM world_book_entries")
    suspend fun deleteAll()
}
