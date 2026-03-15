package com.example.myapplication.util

import android.util.Log
import com.example.myapplication.data.local.entity.AttachmentEntity
import com.example.myapplication.data.local.entity.CharacterEntity
import com.example.myapplication.data.local.entity.MessageEntity
import com.example.myapplication.data.local.entity.PresetEntity
import com.example.myapplication.data.local.entity.WorldBookEntryEntity
import com.example.myapplication.data.remote.chat.UnifiedMessage

/**
 * 上下文长度管理器
 * 
 * 职责：
 * 1. 统一构建发送前的上下文（system prompt + character + worldbook + attachments + history + user input）
 * 2. 根据模型上下文限制进行智能裁剪
 * 3. 预留输出空间
 * 4. 提供调试日志
 */
object ContextManager {
    
    private const val TAG = "ContextManager"
    
    // 默认上下文限制（字符数）
    private const val DEFAULT_CONTEXT_LIMIT = 16000
    
    // 输出预留比例（20%）
    private const val OUTPUT_RESERVE_RATIO = 0.2f
    
    // 附件单个最大长度（避免单个附件过长）
    private const val MAX_ATTACHMENT_LENGTH = 8000
    
    /**
     * 上下文构建结果
     */
    data class ContextBuildResult(
        val messages: List<UnifiedMessage>,
        val stats: ContextStats
    )
    
    /**
     * 上下文统计信息
     */
    data class ContextStats(
        val totalLength: Int,
        val systemPromptLength: Int,
        val characterDescLength: Int,
        val worldBookLength: Int,
        val attachmentsLength: Int,
        val historyLength: Int,
        val userInputLength: Int,
        val historyMessageCount: Int,
        val truncatedHistoryCount: Int,
        val contextLimit: Int,
        val outputReserve: Int
    )
    
    /**
     * 构建并裁剪上下文
     * 
     * @param preset 预设（可选）
     * @param character 角色（可选）
     * @param worldBookEntries 命中的世界书条目
     * @param attachments 附件列表
     * @param historyMessages 历史消息
     * @param userInput 当前用户输入
     * @param contextLimit 上下文限制（字符数， 使用默认值）
     * @return 构建结果
     */
    fun buildContext(
        preset: PresetEntity?,
        character: CharacterEntity?,
        worldBookEntries: List<WorldBookEntryEntity>,
        attachments: List<AttachmentEntity>,
        historyMessages: List<MessageEntity>,
        userInput: String,
        contextLimit: Int? = null
    ): ContextBuildResult {
        val limit = contextLimit ?: DEFAULT_CONTEXT_LIMIT
        val outputReserve = (limit * OUTPUT_RESERVE_RATIO).toInt()
        val availableForInput = limit - outputReserve
        
        // 第一步：构建必须保留的部分（优先级最高）
        val systemPrompt = preset?.systemPrompt?.takeIf { it.isNotBlank() }
        val characterDesc = character?.description?.takeIf { it.isNotBlank() }
        
        val systemPromptMsg = systemPrompt?.let { UnifiedMessage(role = "system", content = it) }
        val characterDescMsg = characterDesc?.let { UnifiedMessage(role = "system", content = it) }
        
        // 世界书内容（已按优先级排序）
        val worldBookMsgs = worldBookEntries.map { entry ->
            UnifiedMessage(role = "system", content = entry.content)
        }
        
        // 附件内容（裁剪过长附件）
        val attachmentMsgs = attachments.map { attachment ->
            val truncatedText = if (attachment.extractedText.length > MAX_ATTACHMENT_LENGTH) {
                attachment.extractedText.take(MAX_ATTACHMENT_LENGTH) + "\n...[文件内容过长，已截断]"
            } else {
                attachment.extractedText
            }
            UnifiedMessage(
                role = "system",
                content = "[Attached File: ${attachment.fileName}]\n$truncatedText"
            )
        }
        
        // 当前用户输入（必须保留）
        val userInputMsg = UnifiedMessage(role = "user", content = userInput)
        
        // 计算必须保留部分的长度
        val systemPromptLength = systemPrompt?.length ?: 0
        val characterDescLength = characterDesc?.length ?: 0
        val worldBookLength = worldBookMsgs.sumOf { it.content.length }
        val attachmentsLength = attachmentMsgs.sumOf { it.content.length }
        val userInputLength = userInput.length
        
        val requiredLength = systemPromptLength + characterDescLength + 
            worldBookLength + attachmentsLength + userInputLength
        
        // 第二步：计算历史消息可用空间
        val availableForHistory = availableForInput - requiredLength
        
        // 第三步：裁剪历史消息（从旧到新保留，直到超出限制）
        val (historyMsgs, truncatedCount) = truncateHistory(
            historyMessages,
            availableForHistory
        )
        
        val historyLength = historyMsgs.sumOf { it.content.length }
        
        // 第四步：组装最终消息列表
        val finalMessages = buildList {
            systemPromptMsg?.let { add(it) }
            characterDescMsg?.let { add(it) }
            addAll(worldBookMsgs)
            addAll(attachmentMsgs)
            addAll(historyMsgs)
            add(userInputMsg)
        }
        
        val totalLength = finalMessages.sumOf { it.content.length }
        
        // 统计信息
        val stats = ContextStats(
            totalLength = totalLength,
            systemPromptLength = systemPromptLength,
            characterDescLength = characterDescLength,
            worldBookLength = worldBookLength,
            attachmentsLength = attachmentsLength,
            historyLength = historyLength,
            userInputLength = userInputLength,
            historyMessageCount = historyMsgs.size,
            truncatedHistoryCount = truncatedCount,
            contextLimit = limit,
            outputReserve = outputReserve
        )
        
        // 调试日志
        logContextStats(stats)
        
        return ContextBuildResult(
            messages = finalMessages,
            stats = stats
        )
    }
    
    /**
     * 裁剪历史消息
     * 策略：从最新消息开始向前保留，直到超出可用空间
     * 
     * @param messages 历史消息列表
     * @param availableLength 可用长度
     * @return Pair(保留的消息列表, 被裁剪的消息数量)
     */
    private fun truncateHistory(
        messages: List<MessageEntity>,
        availableLength: Int
    ): Pair<List<UnifiedMessage>, Int> {
        if (availableLength <= 0) {
            return Pair(emptyList(), messages.size)
        }
        
        val result = mutableListOf<UnifiedMessage>()
        var accumulatedLength = 0
        var truncatedCount = 0
        
        // 从最新消息开始向前遍历
        for (i in messages.indices.reversed()) {
            val msg = messages[i]
            val msgLength = msg.content.length
            
            if (accumulatedLength + msgLength <= availableLength) {
                // 还有空间，保留这条消息
                result.add(0, UnifiedMessage(role = msg.role, content = msg.content))
                accumulatedLength += msgLength
            } else {
                // 空间不足，裁剪剩余所有旧消息
                truncatedCount = i + 1
                break
            }
        }
        
        return Pair(result, truncatedCount)
    }
    
    /**
     * 输出上下文统计日志
     */
    private fun logContextStats(stats: ContextStats) {
        Log.d(TAG, "=== Context Build Stats ===")
        Log.d(TAG, "Total Length: ${stats.totalLength} / ${stats.contextLimit - stats.outputReserve} (available)")
        Log.d(TAG, "Output Reserve: ${stats.outputReserve}")
        Log.d(TAG, "System Prompt: ${stats.systemPromptLength}")
        Log.d(TAG, "Character Desc: ${stats.characterDescLength}")
        Log.d(TAG, "WorldBook: ${stats.worldBookLength}")
        Log.d(TAG, "Attachments: ${stats.attachmentsLength}")
        Log.d(TAG, "History: ${stats.historyLength} (${stats.historyMessageCount} messages)")
        Log.d(TAG, "User Input: ${stats.userInputLength}")
        
        if (stats.truncatedHistoryCount > 0) {
            Log.d(TAG, "⚠️ Truncated ${stats.truncatedHistoryCount} old messages")
        }
        
        Log.d(TAG, "===========================")
    }
}
