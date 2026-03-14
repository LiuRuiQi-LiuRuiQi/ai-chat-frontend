package com.example.myapplication.ui.screen.worldbook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.entity.WorldBookEntryEntity
import com.example.myapplication.data.repository.WorldBookRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * 世界书 ViewModel
 * 管理世界书条目的增删改查和启用/禁用状态
 */
class WorldBookViewModel(
    private val worldBookRepository: WorldBookRepository
) : ViewModel() {

    /** 所有世界书条目（实时更新） */
    val allEntries: StateFlow<List<WorldBookEntryEntity>> = worldBookRepository.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** 仅启用的条目（实时更新） */
    val enabledEntries: StateFlow<List<WorldBookEntryEntity>> = worldBookRepository.observeEnabledEntries()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /**
     * 保存条目（新建或更新）
     */
    fun saveEntry(entry: WorldBookEntryEntity) {
        viewModelScope.launch {
            worldBookRepository.upsert(entry)
        }
    }

    /**
     * 删除条目
     */
    fun deleteEntry(entryId: Long) {
        viewModelScope.launch {
            val entry = worldBookRepository.getById(entryId)
            if (entry != null) {
                worldBookRepository.delete(entry)
            }
        }
    }

     /**
      * 切换条目启用状态
      */
     fun toggleEnabled(entryId: Long, enabled: Boolean) {
         viewModelScope.launch {
             val entry = worldBookRepository.getById(entryId)
             if (entry != null) {
                 worldBookRepository.update(entry.copy(enabled = enabled))
             }
         }
     }

     /**
      * 更新条目优先级
      */
     fun updatePriority(entryId: Long, priority: Int) {
         viewModelScope.launch {
             val entry = worldBookRepository.getById(entryId)
             if (entry != null) {
                 worldBookRepository.update(entry.copy(priority = priority))
             }
         }
     }
}
