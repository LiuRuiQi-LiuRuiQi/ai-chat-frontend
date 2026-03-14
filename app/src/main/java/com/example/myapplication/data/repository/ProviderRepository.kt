package com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.ProviderDao
import com.example.myapplication.data.local.entity.ProviderEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

/**
 * API 提供者仓库 - 封装提供者相关数据操作
 */
class ProviderRepository(private val providerDao: ProviderDao) {

    /** 获取所有提供者（响应式） */
    fun getAllProviders(): Flow<List<ProviderEntity>> = providerDao.getAllProviders()

    /** 根据 ID 获取提供者 */
    suspend fun getProviderById(id: Long): ProviderEntity? = providerDao.getProviderById(id)

    /** 获取默认提供者 */
    suspend fun getDefaultProvider(): ProviderEntity? = providerDao.getDefaultProvider()

    /** 添加提供者，返回 ID */
    suspend fun addProvider(
        name: String,
        baseUrl: String,
        apiKey: String,
        apiFormat: String,
        defaultModel: String,
        enabled: Boolean,
        isDefault: Boolean = false
    ): Long {
        if (isDefault) {
            providerDao.clearDefault()
        }
        val provider = ProviderEntity(
            name = name,
            baseUrl = baseUrl,
            apiKey = apiKey,
            apiFormat = apiFormat,
            defaultModel = defaultModel,
            enabled = enabled,
            isDefault = isDefault
        )
        return providerDao.insert(provider)
    }

    /** 保存提供者；id=0 时新增，否则更新 */
    suspend fun saveProvider(provider: ProviderEntity): Long {
        if (provider.isDefault) {
            providerDao.clearDefault()
        }
        return providerDao.insert(provider)
    }

    /** 更新提供者 */
    suspend fun updateProvider(provider: ProviderEntity) {
        if (provider.isDefault) {
            providerDao.clearDefault()
        }
        providerDao.update(provider)
    }

    /** 删除提供者 */
    suspend fun deleteProvider(provider: ProviderEntity) = providerDao.delete(provider)

    /** 设置默认提供者 */
    suspend fun setDefault(id: Long) {
        providerDao.clearDefault()
        providerDao.setDefault(id)
    }

    /**
     * 测试连接（占位实现）
     * 当前不发真实网络请求，避免因网络/鉴权失败阻断正常保存与编辑流程。
     */
    suspend fun testConnection(
        baseUrl: String,
        apiKey: String,
        apiFormat: String,
        model: String
    ): Result<String> {
        delay(800)
        if (baseUrl.isBlank()) return Result.failure(IllegalArgumentException("API 地址不能为空"))
        if (apiFormat != "OpenAICompatible") {
            return Result.failure(IllegalArgumentException("当前仅支持 OpenAICompatible"))
        }
        val maskedKey = if (apiKey.length >= 6) {
            "${apiKey.take(3)}***${apiKey.takeLast(2)}"
        } else {
            "未填写或过短"
        }
        val modelText = model.ifBlank { "未指定默认模型" }
        return Result.success("连接测试通过（模拟）。格式: $apiFormat，模型: $modelText，Key: $maskedKey")
    }
}
