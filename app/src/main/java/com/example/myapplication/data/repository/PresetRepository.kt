package com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.PresetDao
import com.example.myapplication.data.local.entity.PresetEntity
import kotlinx.coroutines.flow.Flow

/**
 * 预设仓库 - 封装预设相关数据操作
 */
class PresetRepository(private val presetDao: PresetDao) {

    /** 获取所有预设（响应式） */
    fun getAllPresets(): Flow<List<PresetEntity>> = presetDao.getAllPresets()

    /** 根据 ID 获取预设 */
    suspend fun getPresetById(id: Long): PresetEntity? = presetDao.getPresetById(id)

    /** 获取默认预设 */
    suspend fun getDefaultPreset(): PresetEntity? = presetDao.getDefaultPreset()

    /** 添加预设，返回 ID */
    suspend fun addPreset(
        name: String,
        systemPrompt: String,
        description: String? = null,
        isDefault: Boolean = false,
        temperature: Float? = null,
        topP: Float? = null,
        maxTokens: Int? = null
    ): Long {
        if (isDefault) {
            presetDao.clearDefault()
        }
        val preset = PresetEntity(
            name = name,
            systemPrompt = systemPrompt,
            description = description,
            isDefault = isDefault,
            temperature = temperature,
            topP = topP,
            maxTokens = maxTokens,
            updatedAt = System.currentTimeMillis()
        )
        return presetDao.insert(preset)
    }

    /** 更新预设 */
    suspend fun updatePreset(preset: PresetEntity) = presetDao.update(preset)

    /** 删除预设 */
    suspend fun deletePreset(preset: PresetEntity) = presetDao.delete(preset)

    /** 设置默认预设 */
    suspend fun setDefault(id: Long) {
        presetDao.clearDefault()
        presetDao.setDefault(id)
    }
}
