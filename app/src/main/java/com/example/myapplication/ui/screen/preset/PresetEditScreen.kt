package com.example.myapplication.ui.screen.preset

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.myapplication.ChatApp
import com.example.myapplication.ui.navigation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Preset 编辑页
 *
 * 为了最小改动：这里不单独创建 ViewModel，而是直接通过 Repository + LaunchedEffect 加载/保存。
 * 后续若要扩展复杂状态（校验、预览、参数更多），再引入专用 VM。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PresetEditScreen(
    presetId: Long,
    onNavigateBack: () -> Unit
) {
    val app = LocalContext.current.applicationContext as ChatApp
    val presetRepo = app.presetRepository
    val coroutineScope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var systemPrompt by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var temperatureText by remember { mutableStateOf("") }
    var topPText by remember { mutableStateOf("") }
    var maxTokensText by remember { mutableStateOf("") }

    LaunchedEffect(presetId) {
        if (presetId != Screen.PresetEdit.NEW_PRESET_ID) {
            val preset = withContext(Dispatchers.IO) { presetRepo.getPresetById(presetId) }
            if (preset != null) {
                name = preset.name
                systemPrompt = preset.systemPrompt
                description = preset.description.orEmpty()
                temperatureText = preset.temperature?.toString().orEmpty()
                topPText = preset.topP?.toString().orEmpty()
                maxTokensText = preset.maxTokens?.toString().orEmpty()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (presetId == Screen.PresetEdit.NEW_PRESET_ID) "New Preset" else "Edit Preset") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description (optional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = systemPrompt,
                onValueChange = { systemPrompt = it },
                label = { Text("System Prompt") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = temperatureText,
                onValueChange = { temperatureText = it },
                label = { Text("Temperature (optional)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = topPText,
                onValueChange = { topPText = it },
                label = { Text("Top P (optional)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = maxTokensText,
                onValueChange = { maxTokensText = it },
                label = { Text("Max Tokens (optional)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    val t = temperatureText.toFloatOrNull()
                    val p = topPText.toFloatOrNull()
                    val m = maxTokensText.toIntOrNull()

                    coroutineScope.launch(Dispatchers.IO) {
                        if (presetId == Screen.PresetEdit.NEW_PRESET_ID) {
                            presetRepo.addPreset(
                                name = name.ifBlank { "Untitled" },
                                systemPrompt = systemPrompt,
                                description = description.ifBlank { null },
                                isDefault = false,
                                temperature = t,
                                topP = p,
                                maxTokens = m
                            )
                        } else {
                            val old = presetRepo.getPresetById(presetId) ?: return@launch
                            val updated = old.copy(
                                name = name.ifBlank { old.name },
                                systemPrompt = systemPrompt,
                                description = description.ifBlank { null },
                                temperature = t,
                                topP = p,
                                maxTokens = m,
                                updatedAt = System.currentTimeMillis()
                            )
                            presetRepo.updatePreset(updated)
                        }
                        withContext(Dispatchers.Main) { onNavigateBack() }
                    }
                }
            ) {
                Text("Save")
            }
        }
    }
}
