package com.example.myapplication.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.ChatApp
import com.example.myapplication.ui.screen.chat.ChatViewModel
import com.example.myapplication.ui.screen.character.CharacterViewModel
import com.example.myapplication.ui.screen.preset.PresetListViewModel
import com.example.myapplication.ui.screen.provider.ProviderListViewModel
import com.example.myapplication.ui.screen.sessionlist.SessionListViewModel
import com.example.myapplication.ui.screen.settings.SettingsViewModel
import com.example.myapplication.ui.screen.worldbook.WorldBookViewModel
import java.lang.IllegalArgumentException

/**
 * ViewModel 工厂类
 * 负责为所有需要依赖注入的 ViewModel 提供实例化服务
 */
class ViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            // ChatViewModel 是一个普通的 ViewModel，需要传入 Repository
            modelClass.isAssignableFrom(ChatViewModel::class.java) -> {
                ChatViewModel(application) as T
            }

            modelClass.isAssignableFrom(PresetListViewModel::class.java) ->
                PresetListViewModel(application) as T

            // ProviderListViewModel 是一个 AndroidViewModel，需要传入 Application
            modelClass.isAssignableFrom(ProviderListViewModel::class.java) ->
                ProviderListViewModel(application) as T

            // SessionListViewModel 是一个 AndroidViewModel，需要传入 Application
            modelClass.isAssignableFrom(SessionListViewModel::class.java) ->
                SessionListViewModel(application) as T

            modelClass.isAssignableFrom(SettingsViewModel::class.java) ->
                SettingsViewModel(application) as T

            modelClass.isAssignableFrom(CharacterViewModel::class.java) ->
                CharacterViewModel((application as ChatApp).characterRepository) as T

            modelClass.isAssignableFrom(WorldBookViewModel::class.java) ->
                WorldBookViewModel((application as ChatApp).worldBookRepository) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
