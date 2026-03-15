package com.example.myapplication.ui.screen.chat

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.ChatApp
import com.example.myapplication.data.local.entity.AttachmentEntity
import com.example.myapplication.data.local.entity.CharacterEntity
import com.example.myapplication.data.local.entity.MessageEntity
import com.example.myapplication.data.local.entity.ModelEntity
import com.example.myapplication.data.local.entity.ProviderEntity
import com.example.myapplication.data.local.entity.PresetEntity
import com.example.myapplication.data.local.entity.SessionEntity
import com.example.myapplication.data.local.entity.WorldBookEntryEntity
import com.example.myapplication.data.remote.chat.ChatProviderFactory
import com.example.myapplication.data.remote.chat.UnifiedChatRequest
import com.example.myapplication.data.remote.chat.UnifiedMessage
import com.example.myapplication.util.FileTextExtractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * 聊天页 ViewModel
 * 通过 loadSession(sessionId) 延迟绑定会话。
 */
class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as ChatApp
    private val sessionRepo = app.sessionRepository
    private val messageRepo = app.messageRepository
    private val providerRepo = app.providerRepository
    private val presetRepo = app.presetRepository
    private val modelRepo = app.modelRepository
    private val characterRepo = app.characterRepository
    private val worldBookRepo = app.worldBookRepository
    private val attachmentRepo = app.attachmentRepository
    private val settings = app.settingsDataStore

    companion object {
        private const val DEFAULT_SESSION_TITLE = "新聊天"
        // 兼容旧版本默认标题，避免升级后自动标题逻辑失效
        private const val LEGACY_DEFAULT_SESSION_TITLE = "新对话"
        private const val AUTO_TITLE_MAX_LENGTH = 20
    }

    private var currentSessionId: Long = -1L
    private var messagesJob: Job? = null
    private var modelsJob: Job? = null

     /** 当前会话信息 */
     private val _session = MutableStateFlow<SessionEntity?>(null)
     val session: StateFlow<SessionEntity?> = _session.asStateFlow()
     val currentSession: StateFlow<SessionEntity?> = _session.asStateFlow()

     /** 消息列表 */
     private val _messages = MutableStateFlow<List<MessageEntity>>(emptyList())
     val messages: StateFlow<List<MessageEntity>> = _messages.asStateFlow()

     /** 当前 Provider 的可用模型列表（来自 models 缓存表） */
     private val _availableModels = MutableStateFlow<List<ModelEntity>>(emptyList())
     val availableModels: StateFlow<List<ModelEntity>> = _availableModels.asStateFlow()

     /** 当前选中的模型 */
     private val _selectedModel = MutableStateFlow<ModelEntity?>(null)
     val selectedModel: StateFlow<ModelEntity?> = _selectedModel.asStateFlow()

     /** 当前选中的 Provider */
     private val _selectedProvider = MutableStateFlow<ProviderEntity?>(null)
     val selectedProvider: StateFlow<ProviderEntity?> = _selectedProvider.asStateFlow()

     /** 当前会话"实际使用的模型名"（会话绑定优先，其次 Provider 默认模型） */
     private val _effectiveModel = MutableStateFlow("")
     val effectiveModel: StateFlow<String> = _effectiveModel.asStateFlow()

     /** 当前会话绑定的角色 */
     private val _currentCharacter = MutableStateFlow<CharacterEntity?>(null)
     val currentCharacter: StateFlow<CharacterEntity?> = _currentCharacter.asStateFlow()

     /** 是否正在等待回复 */
     private val _isLoading = MutableStateFlow(false)
     val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

     /** 供界面弹出提示的错误消息流 */
     private val _errorMessages = MutableSharedFlow<String>()
     val errorMessages = _errorMessages.asSharedFlow()

     /** 最后一次命中的世界书条目列表 */
     private val _lastMatchedWorldBookEntries = MutableStateFlow<List<WorldBookEntryEntity>>(emptyList())
     val lastMatchedWorldBookEntries: StateFlow<List<WorldBookEntryEntity>> = _lastMatchedWorldBookEntries.asStateFlow()

     /** 待发送的附件列表（未绑定到消息） */
     private val _pendingAttachments = MutableStateFlow<List<AttachmentEntity>>(emptyList())
     val pendingAttachments: StateFlow<List<AttachmentEntity>> = _pendingAttachments.asStateFlow()
     /** 正在编辑的消息 ID */
     private val _editingMessageId = MutableStateFlow<Long?>(null)
     val editingMessageId: StateFlow<Long?> = _editingMessageId.asStateFlow()

     /** 编辑中的消息内容 */
     private val _editingMessageContent = MutableStateFlow("")
     val editingMessageContent: StateFlow<String> = _editingMessageContent.asStateFlow()


     /** 加载指定会话数据 */
     fun loadSession(sessionId: Long) {
         if (sessionId == currentSessionId) return
         currentSessionId = sessionId
         messagesJob?.cancel()
         modelsJob?.cancel()

         viewModelScope.launch {
             _session.value = sessionRepo.getSessionById(sessionId)

             // 加载会话绑定的角色
             val session = _session.value
             if (session?.characterId != null && session.characterId > 0) {
                 _currentCharacter.value = characterRepo.getById(session.characterId)
                 
                 // 处理开场白逻辑：首次绑定角色且会话无消息时
                 val messages = messageRepo.getMessagesOnce(sessionId)
                 if (messages.isEmpty() && _currentCharacter.value?.greeting?.isNotBlank() == true) {
                     messageRepo.addAssistantMessage(sessionId, _currentCharacter.value!!.greeting)
                 }
             } else {
                 _currentCharacter.value = null
             }

             // 刷新顶部栏展示的"当前实际模型名"
             val provider = resolveProvider()
             _effectiveModel.value = if (provider != null) resolveModel(provider) else ""
             
             // 启动模型列表监听（在 suspend 上下文中调用 resolveProvider）
             startModelsObservation()
         }

         messagesJob = viewModelScope.launch {
             messageRepo.getMessagesBySessionId(sessionId).collect { msgList ->
                 _messages.value = msgList
             }
         }
     }
     
     /** 启动模型列表监听 */
     private fun startModelsObservation() {
         modelsJob = viewModelScope.launch {
             val provider = resolveProvider()
             if (provider == null) {
                 _availableModels.value = emptyList()
                 return@launch
             }
             modelRepo.observeModels(provider.id).collect { list ->
                 _availableModels.value = list
             }
         }
     }

    /** 发送消息并接入真实聊天接口 */
    fun sendMessage(content: String) {
        val text = content.trim()
        if (text.isBlank() || currentSessionId == -1L || _isLoading.value) return

        viewModelScope.launch {
            _isLoading.value = true

            try {
                val userMessageId = messageRepo.addUserMessage(currentSessionId, text)
                sessionRepo.updateTimestamp(currentSessionId)

                // 自动标题触发条件：仍为默认标题 + 当前是首条用户消息
                val currentSession = sessionRepo.getSessionById(currentSessionId)
                val isDefaultTitle = currentSession?.title == DEFAULT_SESSION_TITLE ||
                    currentSession?.title == LEGACY_DEFAULT_SESSION_TITLE
                val userMessageCountAfterInsert = messageRepo.getMessagesOnce(currentSessionId)
                    .count { it.role == "user" }
                val shouldAutoTitleOnFirstAssistantReply = isDefaultTitle && userMessageCountAfterInsert == 1
                _session.value = currentSession

                // 解析当前实际使用的 Preset（会话优先，其次全局 active）
                val preset = resolvePreset()

                val provider = resolveProvider()
                if (provider == null) {
                    handleSendError("未找到可用的 Provider，请先在设置中配置并启用一个默认 Provider")
                    return@launch
                }

                if (!provider.enabled) {
                    handleSendError("当前 Provider 已被禁用")
                    return@launch
                }

                val sendHistory = settings.isSendHistory.first()
                val maxHistoryCount = settings.maxHistoryCount.first().coerceAtLeast(1)
                val localMessages = messageRepo.getMessagesOnce(currentSessionId)
                val pending = attachmentRepo.getPendingBySessionId(currentSessionId)

                // 注意：system prompt 必须注入在最前面（覆盖 sendHistory 与不发送历史两种分支）
                // 消息顺序：① preset.systemPrompt → ② character.description → ③ worldbook 命中内容 → ④ 历史消息 → ⑤ 当前用户输入
                val historyMessages = buildList {
                    val sp = preset?.systemPrompt?.takeIf { it.isNotBlank() }
                    if (sp != null) add(UnifiedMessage(role = "system", content = sp))

                    // 注入角色描述
                    val characterDesc = _currentCharacter.value?.description?.takeIf { it.isNotBlank() }
                    if (characterDesc != null) add(UnifiedMessage(role = "system", content = characterDesc))

                    // 注入世界书命中内容
                    val enabledWorldBookEntries = worldBookRepo.getEnabledEntriesOnce()
                    val matchedEntries = mutableListOf<WorldBookEntryEntity>()
                    if (enabledWorldBookEntries.isNotEmpty()) {
                        val userTextLower = text.lowercase()
                        enabledWorldBookEntries.forEach { entry ->
                            val keywords = entry.keywords.split(",").map { it.trim().lowercase() }.filter { it.isNotBlank() }
                            if (keywords.any { userTextLower.contains(it) }) matchedEntries.add(entry)
                        }
                        matchedEntries.sortByDescending { it.priority }
                        matchedEntries.forEach { entry ->
                            add(UnifiedMessage(role = "system", content = entry.content))
                            Log.d("ChatViewModel", "WorldBook matched: ${entry.title} (priority=${entry.priority})")
                        }
                        _lastMatchedWorldBookEntries.value = matchedEntries
                    }

                    // 注入文件附件内容
                    pending.forEach { attachment ->
                        add(UnifiedMessage(role = "system", content = "[Attached File: ${attachment.fileName}]\n${attachment.extractedText}"))
                    }

                    if (sendHistory) {
                        localMessages.takeLast(maxHistoryCount).forEach { entity ->
                            add(UnifiedMessage(role = entity.role, content = entity.content))
                        }
                    } else {
                        val currentUser = localMessages.firstOrNull { it.id == userMessageId }
                        if (currentUser != null) {
                            add(UnifiedMessage(role = currentUser.role, content = currentUser.content))
                        } else {
                            add(UnifiedMessage(role = "user", content = text))
                        }
                    }
                }

                val assistantMessageId = messageRepo.addAssistantMessage(currentSessionId, "")
                var accumulatedText = ""

                val chatProvider = ChatProviderFactory.create(provider)
                chatProvider.streamChat(
                    request = UnifiedChatRequest(
                        model = resolveModel(provider),
                        messages = historyMessages,
                        stream = true,
                        temperature = preset?.temperature,
                        topP = preset?.topP,
                        maxTokens = preset?.maxTokens
                    ),
                    onDelta = { delta ->
                        accumulatedText += delta
                        messageRepo.updateMessage(
                            MessageEntity(
                                id = assistantMessageId,
                                sessionId = currentSessionId,
                                role = "assistant",
                                content = accumulatedText
                            )
                        )
                    },
                    onComplete = {
                        if (accumulatedText.isBlank()) {
                            messageRepo.updateMessage(
                                MessageEntity(
                                    id = assistantMessageId,
                                    sessionId = currentSessionId,
                                    role = "assistant",
                                    content = "（空回复）"
                                )
                            )
                        }

                        // 绑定待发送附件到本次用户消息
                        pending.forEach { attachment ->
                            viewModelScope.launch {
                                attachmentRepo.bindToMessage(attachment.id, userMessageId)
                            }
                        }
                        _pendingAttachments.value = emptyList()

                        // 仅在"首轮 AI 回复完成后"自动生成标题
                        if (shouldAutoTitleOnFirstAssistantReply) {
                            val firstUserMessage = messageRepo.getMessagesOnce(currentSessionId)
                                .firstOrNull { it.role == "user" }
                                ?.content
                                .orEmpty()
                            val autoTitle = buildAutoTitleFromFirstUserMessage(firstUserMessage)
                            if (autoTitle.isNotBlank()) {
                                sessionRepo.updateTitle(currentSessionId, autoTitle)
                            }
                        }

                        sessionRepo.updateTimestamp(currentSessionId)
                        _session.value = sessionRepo.getSessionById(currentSessionId)
                        _isLoading.value = false
                    },
                    onError = { error ->
                        val finalContent = if (accumulatedText.isNotBlank()) {
                            accumulatedText + "\n\n[请求中断] $error"
                        } else {
                            "[请求失败] $error"
                        }
                        messageRepo.updateMessage(
                            MessageEntity(
                                id = assistantMessageId,
                                sessionId = currentSessionId,
                                role = "assistant",
                                content = finalContent,
                                isError = true
                            )
                        )
                        _errorMessages.emit(error)
                        _isLoading.value = false
                    }
                )
            } catch (e: Exception) {
                handleSendError(e.message ?: "发送失败")
            }
        }
    }

    /**
     * 用户在聊天页选择角色
     */
    fun selectCharacter(character: CharacterEntity?) {
        if (currentSessionId == -1L) return
        viewModelScope.launch {
            val characterId = character?.id
            sessionRepo.updateCharacterId(currentSessionId, characterId)
            sessionRepo.updateTimestamp(currentSessionId)
            _currentCharacter.value = character
        }
    }

    /**
     * 用户选择文件后添加附件
     */
    fun addAttachment(uri: Uri) {
        if (currentSessionId == -1L) return
        viewModelScope.launch {
            try {
                val fileName = getFileNameFromUri(uri) ?: "未知文件"
                val mimeType = getMimeTypeFromUri(uri) ?: "text/plain"
                val extractedText = FileTextExtractor.extractText(getApplication(), uri)
                
                if (extractedText == null) {
                    _errorMessages.emit("文件读取失败：$fileName")
                    return@launch
                }
                
                val attachment = AttachmentEntity(
                    sessionId = currentSessionId,
                    fileName = fileName,
                    mimeType = mimeType,
                    localUri = uri.toString(),
                    extractedText = extractedText
                )
                attachmentRepo.insert(attachment)
                _pendingAttachments.value = attachmentRepo.getPendingBySessionId(currentSessionId)
            } catch (e: Exception) {
                _errorMessages.emit("添加附件失败：${e.message}")
            }
        }
    }

    /**
     * 用户移除待发送附件
     */
    fun removeAttachment(attachmentId: Long) {
        if (currentSessionId == -1L) return
        viewModelScope.launch {
            try {
                val attachment = attachmentRepo.getById(attachmentId)
                if (attachment != null) {
                    attachmentRepo.delete(attachment)
                    _pendingAttachments.value = attachmentRepo.getPendingBySessionId(currentSessionId)
                }
            } catch (e: Exception) {
                _errorMessages.emit("删除附件失败：${e.message}")
            }
        }
    }

    /**
     * 从 URI 获取文件名
     */
    private fun getFileNameFromUri(uri: Uri): String? {
        return try {
            val cursor = getApplication<ChatApp>().contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                val nameIndex = it.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                if (it.moveToFirst() && nameIndex >= 0) {
                    it.getString(nameIndex)
                } else null
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 从 URI 获取 MIME 类型
     */
    private fun getMimeTypeFromUri(uri: Uri): String? {
        return try {
            getApplication<ChatApp>().contentResolver.getType(uri)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 解析当前实际使用的 Preset
     * 优先级：会话绑定 presetId > 全局 activePresetId > null（不使用 preset）
     */
    private suspend fun resolvePreset(): PresetEntity? {
        val sessionPresetId = _session.value?.presetId
        if (sessionPresetId != null) {
            presetRepo.getPresetById(sessionPresetId)?.let { return it }
        }

        val activePresetId = settings.activePresetId.first()
        if (activePresetId != null) {
            presetRepo.getPresetById(activePresetId)?.let { return it }
        }

        return null
    }

    /** 解析当前实际使用的 Provider */
    private suspend fun resolveProvider(): ProviderEntity? {
        val sessionProviderId = _session.value?.providerId
        if (sessionProviderId != null) {
            providerRepo.getProviderById(sessionProviderId)?.let { return it }
        }

        val activeProviderId = settings.activeProviderId.first()
        if (activeProviderId != null) {
            providerRepo.getProviderById(activeProviderId)?.let { return it }
        }

        return providerRepo.getDefaultProvider()
    }

     /**
      * 用户在聊天页选择模型：
      * - 写入 sessions.modelName（会话级绑定）
      * - 刷新顶部栏显示
      */
     fun selectModel(model: ModelEntity?) {
         if (currentSessionId == -1L) return
         viewModelScope.launch {
             val modelName = model?.modelName?.trim()?.takeIf { it.isNotBlank() }
             sessionRepo.updateModelName(currentSessionId, modelName)
             sessionRepo.updateTimestamp(currentSessionId)

             _session.value = sessionRepo.getSessionById(currentSessionId)
             _selectedModel.value = model

             val provider = resolveProvider()
             _effectiveModel.value = if (provider != null) resolveModel(provider) else modelName.orEmpty()
         }
     }

    /**
     * 解析当前"实际使用的模型名"
     * 优先级：会话绑定 modelName > Provider 默认模型
     */
    private fun resolveModel(provider: ProviderEntity): String {
        val bound = _session.value?.modelName?.trim()
        if (!bound.isNullOrBlank()) return bound
        return provider.defaultModel.trim()
    }

    /** 基于首条用户消息生成自动标题（最长 20 字） */
    private fun buildAutoTitleFromFirstUserMessage(content: String): String {
        val normalized = content
            .trim()
            .replace(Regex("\\s+"), " ")
        if (normalized.isBlank()) return ""
        return normalized.take(AUTO_TITLE_MAX_LENGTH)
    }

    /** 统一处理发送错误 */
    private suspend fun handleSendError(message: String) {
        messageRepo.addErrorMessage(currentSessionId, "[发送失败] $message")
        _errorMessages.emit(message)
        _isLoading.value = false
    }

    /** 开始编辑消息 */
    fun startEditMessage(messageId: Long, content: String) {
        _editingMessageId.value = messageId
        _editingMessageContent.value = content
    }

    /** 取消编辑 */
    fun cancelEditMessage() {
        _editingMessageId.value = null
        _editingMessageContent.value = ""
    }

    /** 删除消息 */
    fun deleteMessage(messageId: Long) {
        if (currentSessionId == -1L) return
        viewModelScope.launch {
            messageRepo.deleteMessage(messageId)
        }
    }

    /** 重新生成 Assistant 回复 */
    fun regenerateMessage(messageId: Long) {
        if (currentSessionId == -1L) return
        viewModelScope.launch {
            // 获取要删除的消息
            val message = messageRepo.getMessageById(currentSessionId, messageId) ?: return@launch
            
            // 删除该消息及其后续所有消息
            messageRepo.deleteMessagesAfter(currentSessionId, messageId - 1)
            messageRepo.deleteMessage(messageId)
            
            // 获取前一条消息（应该是 user 消息）
            val messages = messageRepo.getMessagesOnce(currentSessionId)
            val prevMessage = messages.lastOrNull()
            
            if (prevMessage != null && prevMessage.role == "user") {
                // 重新发送该用户消息
                sendMessage(prevMessage.content)
            }
        }
    }

    /** 复制消息到剪贴板 */
    fun copyMessageToClipboard(context: android.content.Context, content: String) {
        val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = android.content.ClipData.newPlainText("message", content)
        clipboard.setPrimaryClip(clip)
    }

}
