package com.example.myapplication.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.myapplication.data.local.entity.ModelEntity
import kotlinx.coroutines.flow.Flow

/**
 * 模型表 DAO
 */
@Dao
interface ModelDao {

    /** 观察某个 Provider 的模型列表 */
    @Query("SELECT * FROM models WHERE providerId = :providerId ORDER BY modelName ASC")
    fun observeModels(providerId: Long): Flow<List<ModelEntity>>

    /** 观察某个 Provider 的模型数量 */
    @Query("SELECT COUNT(*) FROM models WHERE providerId = :providerId")
    fun observeModelCount(providerId: Long): Flow<Int>

    /** 获取某个 Provider 的模型数量（一次性） */
    @Query("SELECT COUNT(*) FROM models WHERE providerId = :providerId")
    suspend fun getModelCount(providerId: Long): Int

    /** 删除某个 Provider 的全部模型 */
    @Query("DELETE FROM models WHERE providerId = :providerId")
    suspend fun deleteByProviderId(providerId: Long)

    /** 批量插入模型（Replace 用于覆盖手动改名等情况） */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(models: List<ModelEntity>)

    /** 插入单个模型（用于手动添加） */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(model: ModelEntity): Long

    /**
     * 替换某个 Provider 的全部模型：
     * 先清空旧数据，再插入新数据（用于“同步模型”）。
     */
    @Transaction
    suspend fun replaceModels(providerId: Long, models: List<ModelEntity>) {
        deleteByProviderId(providerId)
        if (models.isNotEmpty()) insertAll(models)
    }
}
