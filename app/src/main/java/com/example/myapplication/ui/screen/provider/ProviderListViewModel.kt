package com.example.myapplication.ui.screen.provider

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.ChatApp
import com.example.myapplication.data.local.entity.ProviderEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Provider 列表页 ViewModel
 * 负责加载、启用/禁用、设为默认、删除 Provider。
 */
class ProviderListViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as ChatApp
    private val providerRepository = app.providerRepository

    /** Provider 列表 */
    val providers: StateFlow<List<ProviderEntity>> = providerRepository
        .getAllProviders()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    /** 更新启用状态 */
    fun updateEnabled(provider: ProviderEntity, enabled: Boolean) {
        viewModelScope.launch {
            providerRepository.updateProvider(provider.copy(enabled = enabled))
        }
    }

    /** 设置为默认 Provider */
    fun setDefault(id: Long) {
        viewModelScope.launch {
            providerRepository.setDefault(id)
        }
    }

    /** 删除 Provider */
    fun deleteProvider(provider: ProviderEntity) {
        viewModelScope.launch {
            providerRepository.deleteProvider(provider)
        }
    }
}
