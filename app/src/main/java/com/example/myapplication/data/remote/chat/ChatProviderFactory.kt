package com.example.myapplication.data.remote.chat

import com.example.myapplication.data.local.entity.ProviderEntity

/** Provider 工厂：按 apiFormat 创建具体实现 */
object ChatProviderFactory {

    fun create(provider: ProviderEntity): ChatProvider {
        return when (provider.apiFormat) {
            "OpenAICompatible" -> OpenAICompatibleProvider(provider)
            else -> throw IllegalArgumentException("当前暂不支持的接口格式: ${provider.apiFormat}")
        }
    }
}
