package com.example.myapplication.ui.screen.sessionlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.ChatApp
import com.example.myapplication.data.local.entity.SessionEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * 会话列表 ViewModel
 */
class SessionListViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as ChatApp
    private val sessionRepo = app.sessionRepository
    private val messageRepo = app.messageRepository
    private val settings = app.settingsDataStore

    /** 所有会话列表 */
    val sessions: StateFlow<List<SessionEntity>> = sessionRepo.getAllSessions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** 会话最后一条消息摘要（key=sessionId, value=summary） */
    private val _lastMessagePreviewMap = MutableStateFlow<Map<Long, String>>(emptyMap())
    val lastMessagePreviewMap: StateFlow<Map<Long, String>> = _lastMessagePreviewMap

    init {
        // 当会话列表变化时，刷新摘要（最小改动方案：一次性查询每个会话最后一条消息）
        viewModelScope.launch {
            sessions.collect { sessionList ->
                val previewMap = buildMap {
                    sessionList.forEach { session ->
                        val preview = messageRepo.getLastMessage(session.id)?.content
                            ?.trim()
                            ?.replace("\n", " ")
                            ?.take(60)
                            .orEmpty()
                        put(session.id, preview)
                    }
                }
                _lastMessagePreviewMap.value = previewMap
            }
        }
    }

    /** 创建新会话，返回会话 ID */
    fun createSession(onCreated: (Long) -> Unit) {
        viewModelScope.launch {
            val presetId = settings.activePresetId.firstOrNull()
            val id = sessionRepo.createSession(presetId = presetId)
            onCreated(id)
        }
    }

    /** 重命名会话 */
    fun renameSession(sessionId: Long, newTitle: String) {
        val title = newTitle.trim()
        if (title.isBlank()) return
        viewModelScope.launch {
            sessionRepo.updateTitle(sessionId, title.take(50))
        }
    }

    /** 删除会话 */
    fun deleteSession(sessionId: Long) {
        viewModelScope.launch {
            sessionRepo.deleteSession(sessionId)
        }
    }
}
