package com.example.myapplication.ui.screen.preset

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.ChatApp
import com.example.myapplication.data.local.entity.PresetEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Preset 列表页 ViewModel
 * - 负责展示列表、删除、设为 active preset
 */
class PresetListViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as ChatApp
    private val presetRepo = app.presetRepository
    private val settings = app.settingsDataStore

    /** 所有预设（响应式） */
    val presets: StateFlow<List<PresetEntity>> = presetRepo.getAllPresets()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun deletePreset(preset: PresetEntity) {
        viewModelScope.launch {
            presetRepo.deletePreset(preset)
        }
    }

    fun setActivePreset(presetId: Long) {
        viewModelScope.launch {
            settings.setActivePresetId(presetId)
        }
    }
}
