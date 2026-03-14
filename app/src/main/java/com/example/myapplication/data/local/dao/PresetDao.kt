package com.example.myapplication.data.local.dao

import androidx.room.*
import com.example.myapplication.data.local.entity.PresetEntity
import kotlinx.coroutines.flow.Flow

/** 预设数据访问对象 */
@Dao
interface PresetDao {

    /** 获取所有预设 */
    @Query("SELECT * FROM presets ORDER BY createdAt DESC")
    fun getAllPresets(): Flow<List<PresetEntity>>

    /** 根据 ID 获取预设 */
    @Query("SELECT * FROM presets WHERE id = :id")
    suspend fun getPresetById(id: Long): PresetEntity?

    /** 获取默认预设 */
    @Query("SELECT * FROM presets WHERE isDefault = 1 LIMIT 1")
    suspend fun getDefaultPreset(): PresetEntity?

    /** 插入预设，返回自增 ID */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(preset: PresetEntity): Long

    /** 更新预设 */
    @Update
    suspend fun update(preset: PresetEntity)

    /** 删除预设 */
    @Delete
    suspend fun delete(preset: PresetEntity)

    /** 清除所有默认标记 */
    @Query("UPDATE presets SET isDefault = 0")
    suspend fun clearDefault()

    /** 设置指定预设为默认 */
    @Query("UPDATE presets SET isDefault = 1 WHERE id = :id")
    suspend fun setDefault(id: Long)
}
