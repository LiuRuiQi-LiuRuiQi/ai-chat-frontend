package com.example.myapplication.ui.screen.worldbook

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.local.entity.WorldBookEntryEntity

/**
 * 世界书条目编辑页
 * entryId: 条目 ID， 或 -1 表示新建，> 0 表示编辑
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorldBookEditScreen(
    viewModel: WorldBookViewModel,
    entryId: Long? = null,
    onNavigateBack: () -> Unit = {}
) {
    val allEntries by viewModel.allEntries.collectAsState()
    
    // 编辑状态
    var title by remember { mutableStateOf("") }
    var keywords by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var enabled by remember { mutableStateOf(true) }
    var priority by remember { mutableStateOf(0) }
    var isSaving by remember { mutableStateOf(false) }
    
    val isEditMode = entryId != null && entryId > 0
    val pageTitle = if (isEditMode) "编辑世界书" else "新建世界书"
    
    // 加载现有条目
    LaunchedEffect(entryId) {
        if (isEditMode) {
            val entry = allEntries.firstOrNull { it.id == entryId }
            if (entry != null) {
                title = entry.title
                keywords = entry.keywords
                content = entry.content
                enabled = entry.enabled
                priority = entry.priority
            }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(pageTitle) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // 标题
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("标题") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            // 关键词（逗号分隔）
            OutlinedTextField(
                value = keywords,
                onValueChange = { keywords = it },
                label = { Text("关键词（逗号分隔）") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("例：角色名,背景,特征") }
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            // 内容
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("内容") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                maxLines = 10
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            // 优先级
            OutlinedTextField(
                value = priority.toString(),
                onValueChange = { newVal ->
                    priority = newVal.toIntOrNull() ?: 0
                },
                label = { Text("优先级（数字越大越优先）") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            // 启用开关
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("启用此条目", modifier = Modifier.weight(1f))
                Switch(
                    checked = enabled,
                    onCheckedChange = { enabled = it }
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            
            // 保存按钮
            Button(
                onClick = {
                    if (title.isBlank()) {
                        return@Button
                    }
                    isSaving = true
                    val now = System.currentTimeMillis()
                    val entry = WorldBookEntryEntity(
                        id = if (isEditMode) entryId ?: 0L else 0L,
                        title = title,
                        keywords = keywords,
                        content = content,
                        enabled = enabled,
                        priority = priority,
                        createdAt = if (isEditMode) {
                            allEntries.firstOrNull { it.id == entryId }?.createdAt ?: now
                        } else {
                            now
                        },
                        updatedAt = now
                    )
                    viewModel.saveEntry(entry)
                    onNavigateBack()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSaving && title.isNotBlank()
            ) {
                Text(if (isSaving) "保存中..." else "保存")
            }
        }
    }
}
