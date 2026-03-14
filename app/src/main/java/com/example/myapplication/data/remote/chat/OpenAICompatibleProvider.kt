package com.example.myapplication.data.remote.chat

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.example.myapplication.data.local.entity.ProviderEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * OpenAI 兼容接口实现
 * 使用 SSE 流式读取 /v1/chat/completions 返回内容。
 */
class OpenAICompatibleProvider(
    private val provider: ProviderEntity,
    private val client: OkHttpClient = OkHttpClient()
) : ChatProvider {

    private val gson = Gson()
    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    override suspend fun streamChat(
        request: UnifiedChatRequest,
        onDelta: suspend (String) -> Unit,
        onComplete: suspend () -> Unit,
        onError: suspend (String) -> Unit
    ) = withContext(Dispatchers.IO) {
        val baseUrl = provider.baseUrl.trim().trimEnd('/')
        if (baseUrl.isBlank()) {
            onError("Base URL 不能为空")
            return@withContext
        }
        if (provider.apiKey.isBlank()) {
            onError("API Key 不能为空")
            return@withContext
        }
        if (request.model.isBlank()) {
            onError("模型名不能为空")
            return@withContext
        }

        val body = gson.toJson(request).toRequestBody(jsonMediaType)
        val httpRequest = Request.Builder()
            .url("$baseUrl/v1/chat/completions")
            .addHeader("Authorization", "Bearer ${provider.apiKey}")
            .addHeader("Content-Type", "application/json")
            .post(body)
            .build()

        runCatching {
            client.newCall(httpRequest).execute()
        }.onFailure {
            onError(it.message ?: "网络请求失败")
        }.onSuccess { response ->
            response.use { resp ->
                if (!resp.isSuccessful) {
                    val errorBody = resp.body?.string().orEmpty()
                    val msg = if (errorBody.isNotBlank()) {
                        "请求失败(${resp.code}): $errorBody"
                    } else {
                        "请求失败，HTTP ${resp.code}"
                    }
                    onError(msg)
                    return@withContext
                }

                val bodySource = resp.body ?: run {
                    onError("响应体为空")
                    return@withContext
                }

                val reader = bodySource.charStream().buffered()
                var completed = false

                reader.useLines { lines ->
                    lines.forEach { rawLine ->
                        val line = rawLine.trim()
                        if (!line.startsWith("data:")) return@forEach

                        val data = line.removePrefix("data:").trim()
                        if (data.isBlank()) return@forEach

                        if (data == "[DONE]") {
                            completed = true
                            return@forEach
                        }

                        runCatching {
                            val json = JsonParser.parseString(data).asJsonObject
                            val choices = json.getAsJsonArray("choices")
                            if (choices != null && choices.size() > 0) {
                                val first = choices[0].asJsonObject
                                val delta = first.getAsJsonObject("delta")
                                val content = delta?.get("content")?.asString
                                if (!content.isNullOrEmpty()) {
                                    kotlinx.coroutines.runBlocking {
                                        onDelta(content)
                                    }
                                }
                            }
                        }.onFailure {
                            // 忽略单行解析失败，避免中断整条流
                        }

                        if (completed) return@useLines
                    }
                }

                onComplete()
            }
        }
    }
}
