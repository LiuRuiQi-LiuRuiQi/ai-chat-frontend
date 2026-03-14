package com.example.myapplication.ui.screen.provider

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ChatApp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.heightIn
import com.example.myapplication.data.local.entity.ModelEntity
import com.example.myapplication.data.local.entity.ProviderEntity
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Provider 编辑页
 * 支持新增与编辑 OpenAI 兼容 Provider。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProviderEditScreen(
    providerId: Long?,
    onNavigateBack: () -> Unit,
    viewModel: ProviderEditViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(providerId) {
        viewModel.loadProvider(providerId)
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is ProviderEditEvent.ShowMessage -> snackbarHostState.showSnackbar(event.message)
                ProviderEditEvent.Saved -> onNavigateBack()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(if (providerId == null) "新增 Provider" else "编辑 Provider") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            Surface(tonalElevation = 3.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = { viewModel.saveProvider() },
                        enabled = !uiState.isSaving
                    ) {
                        if (uiState.isSaving) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .height(16.dp),
                                strokeWidth = 2.dp
                            )
                        }
                        Text("保存")
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            OutlinedTextField(
                value = uiState.name,
                onValueChange = viewModel::updateName,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("名称") },
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = uiState.baseUrl,
                onValueChange = viewModel::updateBaseUrl,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Base URL") },
                placeholder = { Text("例如：https://api.openai.com/") },
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = uiState.apiKey,
                onValueChange = viewModel::updateApiKey,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("API Key") },
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = uiState.defaultModel,
                onValueChange = viewModel::updateDefaultModel,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("默认模型") },
                placeholder = { Text("例如：gpt-4o-mini") },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // --- 模型同步区 ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "已同步模型：${uiState.modelCount}",
                    style = MaterialTheme.typography.bodyLarge
                )

                TextButton(
                    onClick = { viewModel.syncModels() },
                    enabled = !uiState.isSyncingModels && !uiState.isSaving
                ) {
                    if (uiState.isSyncingModels) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .height(16.dp),
                            strokeWidth = 2.dp
                        )
                    }
                    Text("同步模型")
                }
            }

            // 模型列表（点击选为默认模型）
            if (uiState.models.isNotEmpty()) {
                Text(
                    text = "点击选择默认模型",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 240.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    uiState.models.forEach { model ->
                        val isSelected = uiState.defaultModel == model.modelName
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.updateDefaultModel(model.modelName) }
                                .padding(vertical = 6.dp, horizontal = 4.dp),
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                            androidx.compose.material3.RadioButton(
                                selected = isSelected,
                                onClick = { viewModel.updateDefaultModel(model.modelName) }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = model.displayName.ifBlank { model.modelName },
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (isSelected)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.manualModelName,
                onValueChange = viewModel::updateManualModelName,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("手动添加模型") },
                placeholder = { Text("例如：gpt-4o-mini") },
                singleLine = true
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = { viewModel.addModelManually() },
                    enabled = uiState.manualModelName.isNotBlank() && !uiState.isSaving
                ) {
                    Text("添加")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- Provider 基础设置 ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("启用", style = MaterialTheme.typography.bodyLarge)
                Switch(
                    checked = uiState.enabled,
                    onCheckedChange = viewModel::updateEnabled
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("设为默认", style = MaterialTheme.typography.bodyLarge)
                Switch(
                    checked = uiState.isDefault,
                    onCheckedChange = viewModel::updateIsDefault
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "接口格式：OpenAICompatible",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
data class ProviderEditUiState(
    val providerId: Long? = null,
    val name: String = "",
    val baseUrl: String = "",
    val apiKey: String = "",
    val defaultModel: String = "",
    val enabled: Boolean = true,
    val isDefault: Boolean = false,
    val isSaving: Boolean = false,

    /** 已同步模型列表（来自本地 models 表） */
    val models: List<ModelEntity> = emptyList(),
    val modelCount: Int = 0,
    val isSyncingModels: Boolean = false,

    /** 手动添加模型输入（fallback 入口） */
    val manualModelName: String = ""
)

sealed interface ProviderEditEvent {
    data class ShowMessage(val message: String) : ProviderEditEvent
    data object Saved : ProviderEditEvent
}

/**
 * Provider 编辑页 ViewModel
 */
class ProviderEditViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as ChatApp
    private val providerRepository = app.providerRepository
    private val modelRepository = app.modelRepository

    private val _uiState = MutableStateFlow(ProviderEditUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ProviderEditEvent>()
    val events = _events.asSharedFlow()

    private var modelCountJob: Job? = null

    fun loadProvider(providerId: Long?) {
        if (_uiState.value.providerId == providerId) return

        modelCountJob?.cancel()
        modelCountJob = null

        if (providerId == null) {
            _uiState.value = ProviderEditUiState()
            return
        }

        viewModelScope.launch {
            val provider = providerRepository.getProviderById(providerId)
            if (provider == null) {
                _events.emit(ProviderEditEvent.ShowMessage("Provider 不存在"))
                return@launch
            }

            _uiState.value = ProviderEditUiState(
                providerId = provider.id,
                name = provider.name,
                baseUrl = provider.baseUrl,
                apiKey = provider.apiKey,
                defaultModel = provider.defaultModel,
                enabled = provider.enabled,
                isDefault = provider.isDefault,
                manualModelName = ""
            )

            modelCountJob = launch {
                modelRepository.observeModels(provider.id).collect { list ->
                    _uiState.value = _uiState.value.copy(models = list, modelCount = list.size)
                }
            }
        }
    }

    fun updateName(value: String) {
        _uiState.value = _uiState.value.copy(name = value)
    }

    fun updateBaseUrl(value: String) {
        _uiState.value = _uiState.value.copy(baseUrl = value)
    }

    fun updateApiKey(value: String) {
        _uiState.value = _uiState.value.copy(apiKey = value)
    }

    fun updateDefaultModel(value: String) {
        _uiState.value = _uiState.value.copy(defaultModel = value)
    }

    fun updateManualModelName(value: String) {
        _uiState.value = _uiState.value.copy(manualModelName = value)
    }

    fun updateEnabled(value: Boolean) {
        _uiState.value = _uiState.value.copy(enabled = value)
    }

    fun updateIsDefault(value: Boolean) {
        _uiState.value = _uiState.value.copy(isDefault = value)
    }

    fun syncModels() {
        val state = _uiState.value
        val providerId = state.providerId
        if (providerId == null) {
            viewModelScope.launch {
                _events.emit(ProviderEditEvent.ShowMessage("请先保存 Provider 后再同步模型"))
            }
            return
        }

        val baseUrl = state.baseUrl.trim()
        if (baseUrl.isBlank()) {
            viewModelScope.launch {
                _events.emit(ProviderEditEvent.ShowMessage("Base URL 不能为空"))
            }
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSyncingModels = true)
            try {
                val count = modelRepository.syncModels(
                    providerId = providerId,
                    baseUrl = baseUrl,
                    apiKey = state.apiKey.trim()
                )
                _events.emit(ProviderEditEvent.ShowMessage("同步完成：$count 个模型"))
            } catch (e: Exception) {
                _events.emit(ProviderEditEvent.ShowMessage(e.message ?: "同步失败"))
            } finally {
                _uiState.value = _uiState.value.copy(isSyncingModels = false)
            }
        }
    }

    fun addModelManually() {
        val state = _uiState.value
        val providerId = state.providerId
        if (providerId == null) {
            viewModelScope.launch {
                _events.emit(ProviderEditEvent.ShowMessage("请先保存 Provider 后再添加模型"))
            }
            return
        }

        val modelName = state.manualModelName.trim()
        if (modelName.isBlank()) {
            viewModelScope.launch {
                _events.emit(ProviderEditEvent.ShowMessage("模型名不能为空"))
            }
            return
        }

        viewModelScope.launch {
            try {
                modelRepository.addModelManually(providerId = providerId, modelName = modelName)
                _uiState.value = _uiState.value.copy(manualModelName = "")
                _events.emit(ProviderEditEvent.ShowMessage("已添加模型：$modelName"))
            } catch (e: Exception) {
                _events.emit(ProviderEditEvent.ShowMessage(e.message ?: "添加失败"))
            }
        }
    }

    fun saveProvider() {
        val state = _uiState.value
        val normalizedBaseUrl = state.baseUrl.trim()

        if (state.name.isBlank()) {
            viewModelScope.launch { _events.emit(ProviderEditEvent.ShowMessage("名称不能为空")) }
            return
        }
        if (normalizedBaseUrl.isBlank()) {
            viewModelScope.launch { _events.emit(ProviderEditEvent.ShowMessage("Base URL 不能为空")) }
            return
        }
        if (state.defaultModel.isBlank()) {
            viewModelScope.launch { _events.emit(ProviderEditEvent.ShowMessage("默认模型不能为空")) }
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true)
            try {
                val provider = ProviderEntity(
                    id = state.providerId ?: 0L,
                    name = state.name.trim(),
                    baseUrl = normalizedBaseUrl,
                    apiKey = state.apiKey.trim(),
                    defaultModel = state.defaultModel.trim(),
                    enabled = state.enabled,
                    isDefault = state.isDefault
                )
                if (state.providerId == null) {
                    providerRepository.saveProvider(provider)
                } else {
                    providerRepository.updateProvider(provider)
                }
                _events.emit(ProviderEditEvent.Saved)
            } catch (e: Exception) {
                _events.emit(ProviderEditEvent.ShowMessage(e.message ?: "保存失败"))
            } finally {
                _uiState.value = _uiState.value.copy(isSaving = false)
            }
        }
    }
}