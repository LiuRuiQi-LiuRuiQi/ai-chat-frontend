package com.example.myapplication.ui.screen.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.widthIn
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.local.entity.CharacterEntity
import com.example.myapplication.data.local.entity.MessageEntity
import com.example.myapplication.ui.component.MarkdownText

/**
 * 聊天屏幕 - 主聊天界面
 * 显示消息列表、输入框、顶部栏（含模型选择、角色选择）
 */
@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
    onNavigateToCharacterList: () -> Unit
) {
    val messages by viewModel.messages.collectAsState()
    val currentSession by viewModel.currentSession.collectAsState()
    val currentCharacter by viewModel.currentCharacter.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val selectedModel by viewModel.selectedModel.collectAsState()

    var inputText by remember { mutableStateOf("") }
    var showModelDialog by remember { mutableStateOf(false) }
    var showCharacterDialog by remember { mutableStateOf(false) }

    val lazyListState = rememberLazyListState()

    // 自动滚动到最新消息
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            lazyListState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // 顶部栏
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 左侧：会话标题
            Text(
                text = currentSession?.title ?: "新聊天",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )

            // 中间：模型名 + 角色名
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clickable { showModelDialog = true }
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedModel?.modelName ?: "选择模型",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    fontSize = 12.sp
                )
                if (currentCharacter != null) {
                    Text(
                        text = " | ${currentCharacter!!.name}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }

            // 右侧：角色选择按钮 + 更多菜单
            IconButton(
                onClick = { showCharacterDialog = true },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "选择角色",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            IconButton(
                onClick = { /* TODO: 更多菜单 */ },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "更多",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // 消息列表
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages) { message ->
                ChatMessageBubble(message = message)
            }
        }

        // 输入框
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                placeholder = { Text("输入消息...") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(24.dp),
                singleLine = true
            )

            IconButton(
                onClick = {
                    if (inputText.isNotBlank()) {
                        viewModel.sendMessage(inputText)
                        inputText = ""
                    }
                },
                enabled = !isLoading && inputText.isNotBlank(),
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "发送",
                    tint = if (isLoading || inputText.isBlank())
                        MaterialTheme.colorScheme.outline
                    else
                        MaterialTheme.colorScheme.primary
                )
            }
        }
    }

    // 模型选择弹窗
    if (showModelDialog) {
        ModelSelectionDialog(
            viewModel = viewModel,
            onDismiss = { showModelDialog = false }
        )
    }

    // 角色选择弹窗
    if (showCharacterDialog) {
        CharacterSelectionDialog(
            currentCharacter = currentCharacter,
            onSelectCharacter = { character ->
                viewModel.selectCharacter(character)
                showCharacterDialog = false
            },
            onDismiss = { showCharacterDialog = false }
        )
    }
}

/**
 * 聊天消息气泡
 */
@Composable
fun ChatMessageBubble(message: MessageEntity) {
    val isUser = message.role == "user"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                 .background(
                     color = if (isUser)
                         MaterialTheme.colorScheme.primary
                     else
                         MaterialTheme.colorScheme.surfaceVariant,
                     shape = RoundedCornerShape(12.dp)
                 )
                 .padding(12.dp)
                 .widthIn(max = 280.dp)
         ) {
              MarkdownText(
                  text = message.content,
                  contentColor = if (isUser) Color.White else MaterialTheme.colorScheme.onSurface
              )
         }
     }
 }

 /**
  * 模型选择弹窗
  */
 @Composable
 fun ModelSelectionDialog(
     viewModel: ChatViewModel,
     onDismiss: () -> Unit
 ) {
     val models by viewModel.availableModels.collectAsState()
     val selectedModel by viewModel.selectedModel.collectAsState()

     AlertDialog(
         onDismissRequest = onDismiss,
         title = { Text("选择模型") },
         text = {
             Column(
                 modifier = Modifier
                     .fillMaxWidth()
                     .height(300.dp)
             ) {
                 LazyColumn(
                     modifier = Modifier.fillMaxWidth(),
                     verticalArrangement = Arrangement.spacedBy(8.dp)
                 ) {
                     items(models) { model ->
                         Row(
                             modifier = Modifier
                                 .fillMaxWidth()
                                 .clickable {
                                     viewModel.selectModel(model)
                                     onDismiss()
                                 }
                                 .padding(12.dp),
                             verticalAlignment = Alignment.CenterVertically
                         ) {
                             Text(
                                 text = model.modelName,
                                 modifier = Modifier.weight(1f),
                                 style = if (selectedModel?.id == model.id)
                                     MaterialTheme.typography.bodyMedium.copy(
                                         color = MaterialTheme.colorScheme.primary
                                     )
                                 else
                                     MaterialTheme.typography.bodyMedium
                             )
                             if (selectedModel?.id == model.id) {
                                 Text("✓", color = MaterialTheme.colorScheme.primary)
                             }
                         }
                     }
                 }
             }
         },
         confirmButton = {
             Button(onClick = onDismiss) {
                 Text("关闭")
             }
         }
     )
 }

 /**
  * 角色选择弹窗
  * 第一阶段：仅显示"无角色"选项和说明文字
  * 后续阶段：接入完整角色列表
  */
 @Composable
 fun CharacterSelectionDialog(
     currentCharacter: CharacterEntity?,
     onSelectCharacter: (CharacterEntity?) -> Unit,
     onDismiss: () -> Unit
 ) {
     AlertDialog(
         onDismissRequest = onDismiss,
         title = { Text("选择角色") },
         text = {
             Column(
                 modifier = Modifier
                     .fillMaxWidth()
                     .padding(8.dp),
                 verticalArrangement = Arrangement.spacedBy(12.dp)
             ) {
                 Text(
                     text = "为当前会话选择一个角色，角色的设定会被注入到 AI 的系统提示中。",
                     style = MaterialTheme.typography.bodySmall,
                     color = MaterialTheme.colorScheme.onSurfaceVariant
                 )

                 // "无角色"选项
                 Row(
                     modifier = Modifier
                         .fillMaxWidth()
                         .clickable {
                             onSelectCharacter(null)
                             onDismiss()
                         }
                         .padding(12.dp),
                     verticalAlignment = Alignment.CenterVertically,
                     horizontalArrangement = Arrangement.SpaceBetween
                 ) {
                     Text(
                         text = "无角色",
                         style = MaterialTheme.typography.bodyMedium
                     )
                     if (currentCharacter == null) {
                         Text("✓", color = MaterialTheme.colorScheme.primary)
                     }
                 }

                 Spacer(modifier = Modifier.height(8.dp))

                 Text(
                     text = "完整角色列表将在后续版本中接入",
                     style = MaterialTheme.typography.labelSmall,
                     color = MaterialTheme.colorScheme.outline
                 )
             }
         },
         confirmButton = {
             Button(onClick = onDismiss) {
                 Text("关闭")
             }
         }
     )
 }
