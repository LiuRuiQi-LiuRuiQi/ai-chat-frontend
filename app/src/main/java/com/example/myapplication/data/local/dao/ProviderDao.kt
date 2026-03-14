package com.example.myapplication.data.local.dao

import androidx.room.*
import com.example.myapplication.data.local.entity.ProviderEntity
import kotlinx.coroutines.flow.Flow

/** API 提供者数据访问对象 */
@Dao
interface ProviderDao {

    /** 获取所有提供者 */
    @Query("SELECT * FROM providers ORDER BY createdAt DESC")
    fun getAllProviders(): Flow<List<ProviderEntity>>

    /** 根据 ID 获取提供者 */
    @Query("SELECT * FROM providers WHERE id = :id")
    suspend fun getProviderById(id: Long): ProviderEntity?

    /** 获取默认提供者 */
    @Query("SELECT * FROM providers WHERE isDefault = 1 LIMIT 1")
    suspend fun getDefaultProvider(): ProviderEntity?

    /** 插入提供者，返回自增 ID */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(provider: ProviderEntity): Long

    /** 更新提供者 */
    @Update
    suspend fun update(provider: ProviderEntity)

    /** 删除提供者 */
    @Delete
    suspend fun delete(provider: ProviderEntity)

    /** 清除所有默认标记 */
    @Query("UPDATE providers SET isDefault = 0")
    suspend fun clearDefault()

    /** 设置指定提供者为默认 */
    @Query("UPDATE providers SET isDefault = 1 WHERE id = :id")
    suspend fun setDefault(id: Long)
}
