package com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.ModelDao
import com.example.myapplication.data.local.entity.ModelEntity
import com.example.myapplication.data.remote.ModelSyncService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * 模型仓库：负责 models 表的读写与同步逻辑封装
 */
class ModelRepository(
    private val modelDao: ModelDao,
    private val syncService: ModelSyncService = ModelSyncService()
) {

    fun observeModels(providerId: Long): Flow<List<ModelEntity>> = modelDao.observeModels(providerId)

    fun observeModelCount(providerId: Long): Flow<Int> = modelDao.observeModelCount(providerId)

    suspend fun getModelCount(providerId: Long): Int = modelDao.getModelCount(providerId)

    /**
     * 同步模型：请求 /v1/models，解析 data[].id，
     * 然后“清空该 provider 旧 models + 写入新 models”。
     */
    suspend fun syncModels(providerId: Long, baseUrl: String, apiKey: String): Int {
        return withContext(Dispatchers.IO) {
            val ids = syncService.fetchModelIds(baseUrl = baseUrl, apiKey = apiKey)
            val entities = ids.map { id ->
                ModelEntity(
                    providerId = providerId,
                    modelName = id,
                    displayName = id,
                    enabled = true
                )
            }
            modelDao.replaceModels(providerId, entities)
            entities.size
        }
    }

    /** 手动添加模型（作为同步失败或接口不支持时的 fallback） */
    suspend fun addModelManually(providerId: Long, modelName: String, displayName: String = modelName) {
        val normalized = modelName.trim()
        require(normalized.isNotBlank()) { "模型名不能为空" }

        withContext(Dispatchers.IO) {
            modelDao.insert(
                ModelEntity(
                    providerId = providerId,
                    modelName = normalized,
                    displayName = displayName.trim().ifBlank { normalized },
                    enabled = true
                )
            )
        }
    }
}
