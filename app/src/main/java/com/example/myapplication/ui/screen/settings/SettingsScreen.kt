package com.example.myapplication.ui.screen.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ChatApp
import com.example.myapplication.data.remote.RemoteVersionInfo

/**
 * 设置页 - 四分组结构
 * 分组：聊天体验、默认配置、数据与调试、关于
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = viewModel(
        factory = (LocalContext.current.applicationContext as ChatApp).viewModelFactory
    )
) {
    val isMarkdownEnabled by viewModel.isMarkdownEnabled.collectAsState()
    val activeProviderId by viewModel.activeProviderId.collectAsState()
    val activePresetId by viewModel.activePresetId.collectAsState()
    val isSendHistory by viewModel.isSendHistory.collectAsState()
    val maxHistoryCount by viewModel.maxHistoryCount.collectAsState()
    val updateCheckState by viewModel.updateCheckState.collectAsState()
    val downloadProgress by viewModel.downloadProgress.collectAsState()
    val versionInfo = viewModel.getVersionInfo()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("设置") },
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
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // ===== 分组 1: 聊天体验 =====
            SettingsSectionHeader("聊天体验")

            SettingsToggleItem(
                title = "启用 Markdown 渲染",
                description = "对 AI 回复启用标题、代码块、列表等格式渲染",
                checked = isMarkdownEnabled,
                onCheckedChange = { viewModel.setMarkdownEnabled(it) }
            )

            SettingsToggleItem(
                title = "发送历史消息",
                description = "向 AI 发送请求时包含历史对话上下文",
                checked = isSendHistory,
                onCheckedChange = { viewModel.setSendHistory(it) }
            )

            SettingsTextItem(
                title = "最大历史消息条数",
                value = maxHistoryCount.toString()
            )

            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            // ===== 分组 2: 默认配置 =====
            SettingsSectionHeader("默认配置")

            SettingsTextItem(
                title = "当前默认 Provider",
                value = if (activeProviderId != null) "已设置 (ID: $activeProviderId)" else "未设置"
            )

            SettingsTextItem(
                title = "当前默认 Preset",
                value = if (activePresetId != null) "已设置 (ID: $activePresetId)" else "未设置"
            )

            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            // ===== 分组 3: 数据与调试 =====
            SettingsSectionHeader("数据与调试")

            SettingsButtonItem(
                title = "导出配置",
                description = "导出当前所有设置和配置",
                onClick = {
                    // TODO: 实现导出配置逻辑
                }
            )

            SettingsButtonItem(
                title = "导出日志",
                description = "导出应用运行日志",
                onClick = {
                    // TODO: 实现导出日志逻辑
                }
            )

            SettingsButtonItem(
                title = "检查更新",
                description = "检查是否有新版本可用",
                onClick = {
                    viewModel.checkUpdate()
                }
            )

            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            // ===== 分组 4: 关于 =====
            SettingsSectionHeader("关于")

            SettingsTextItem(
                title = "应用名称",
                value = "AI Chat"
            )

            SettingsTextItem(
                title = "版本号",
                value = versionInfo.versionName
            )

            SettingsTextItem(
                title = "构建号",
                value = versionInfo.versionCode.toString()
            )

            SettingsTextItem(
                title = "数据库版本",
                value = "v${versionInfo.databaseVersion}"
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // 更新检查对话框 - 根据状态显示不同内容
    when (updateCheckState) {
        is UpdateCheckState.Checking -> {
            UpdateCheckingDialog()
        }
        is UpdateCheckState.UpToDate -> {
            UpdateUpToDateDialog(
                onDismiss = { viewModel.resetUpdateState() }
            )
        }
        is UpdateCheckState.UpdateAvailable -> {
            UpdateAvailableDialog(
                remoteVersion = (updateCheckState as UpdateCheckState.UpdateAvailable).remoteVersion,
                onDownload = { viewModel.downloadApk((updateCheckState as UpdateCheckState.UpdateAvailable).remoteVersion.apkUrl) },
                onDismiss = { viewModel.resetUpdateState() }
            )
        }
        is UpdateCheckState.Downloading -> {
            UpdateDownloadingDialog(
                progress = downloadProgress,
                onCancel = { viewModel.resetUpdateState() }
            )
        }
        is UpdateCheckState.DownloadComplete -> {
            UpdateDownloadCompleteDialog(
                onDismiss = { viewModel.resetUpdateState() }
            )
        }
        is UpdateCheckState.Error -> {
            UpdateErrorDialog(
                message = (updateCheckState as UpdateCheckState.Error).message,
                onDismiss = { viewModel.resetUpdateState() }
            )
        }
        else -> {} // Idle 状态不显示对话框
    }
}

/**
 * 分组标题组件
 */
@Composable
private fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
    )
}

/**
 * 开关设置项组件
 */
@Composable
private fun SettingsToggleItem(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

/**
 * 文本设置项组件（只读）
 */
@Composable
private fun SettingsTextItem(
    title: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * 按钮设置项组件
 */
@Composable
private fun SettingsButtonItem(
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Button(onClick = onClick) {
            Text("操作")
        }
    }
}

/**
 * 检查中对话框
 */
@Composable
private fun UpdateCheckingDialog() {
    AlertDialog(
        onDismissRequest = {},
        title = { Text("检查更新") },
        text = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(modifier = Modifier.width(40.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Text("正在检查更新...")
            }
        },
        confirmButton = {}
    )
}

/**
 * 已是最新版本对话框
 */
@Composable
private fun UpdateUpToDateDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("检查更新") },
        text = { Text("当前已是最新版本") },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("确定")
            }
        }
    )
}

/**
 * 有新版本可用对话框
 */
@Composable
private fun UpdateAvailableDialog(
    remoteVersion: RemoteVersionInfo,
    onDownload: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("发现新版本") },
        text = {
            Column {
                Text("版本: ${remoteVersion.versionName} (${remoteVersion.versionCode})")
                Spacer(modifier = Modifier.height(8.dp))
                Text("更新内容:")
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = remoteVersion.changelog,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDownload) {
                Text("下载")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}

/**
 * 下载中对话框
 */
@Composable
private fun UpdateDownloadingDialog(
    progress: Pair<Long, Long>,
    onCancel: () -> Unit
) {
    val (downloaded, total) = progress
    val progressPercent = if (total > 0) (downloaded.toFloat() / total.toFloat()) else 0f

    AlertDialog(
        onDismissRequest = {},
        title = { Text("下载更新") },
        text = {
            Column {
                LinearProgressIndicator(
                    progress = { progressPercent },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${formatBytes(downloaded)} / ${formatBytes(total)}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onCancel) {
                Text("取消")
            }
        }
    )
}

/**
 * 下载完成对话框
 */
@Composable
private fun UpdateDownloadCompleteDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("下载完成") },
        text = { Text("APK 已下载完成，请手动安装") },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("确定")
            }
        }
    )
}

/**
 * 错误对话框
 */
@Composable
private fun UpdateErrorDialog(
    message: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("更新失败") },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("确定")
            }
        }
    )
}

/**
 * 格式化字节大小
 */
private fun formatBytes(bytes: Long): String {
    return when {
        bytes < 1024 -> "$bytes B"
        bytes < 1024 * 1024 -> "${bytes / 1024} KB"
        bytes < 1024 * 1024 * 1024 -> "${bytes / (1024 * 1024)} MB"
        else -> "${bytes / (1024 * 1024 * 1024)} GB"
    }
}
