package com.example.myapplication.data.remote

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

/**
 * 模型同步服务
 *
 * 使用 OkHttp 请求 OpenAI 兼容接口：
 * GET {baseUrl}/v1/models
 * Header: Authorization: Bearer {apiKey}
 */
class ModelSyncService(
    private val okHttpClient: OkHttpClient = OkHttpClient(),
    private val gson: Gson = Gson()
) {

    /**
     * 拉取模型列表（返回模型 id 列表）
     */
    @Throws(IOException::class)
    fun fetchModelIds(baseUrl: String, apiKey: String): List<String> {
        val url = buildModelsUrl(baseUrl)

        val request = Request.Builder()
            .url(url)
            .get()
            .header("Authorization", "Bearer $apiKey")
            .build()

        okHttpClient.newCall(request).execute().use { response ->
            val bodyStr = response.body?.string().orEmpty()
            if (!response.isSuccessful) {
                throw IOException("获取模型失败：HTTP ${response.code} ${response.message} ${bodyStr.take(200)}")
            }
            val parsed = gson.fromJson(bodyStr, ModelsResponse::class.java)
            return parsed?.data?.mapNotNull { it.id }?.distinct().orEmpty()
        }
    }

    private fun buildModelsUrl(baseUrl: String): String {
        val trimmed = baseUrl.trim().removeSuffix("/")
        return "$trimmed/v1/models"
    }

    private data class ModelsResponse(
        val data: List<ModelItem>? = null
    )

    private data class ModelItem(
        val id: String? = null
    )
}
