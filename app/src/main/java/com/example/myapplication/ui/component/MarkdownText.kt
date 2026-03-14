package com.example.myapplication.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Markdown 渲染组件（纯 Compose 实现，无额外依赖）
 * 支持：标题、粗体、斜体、行内代码、代码块、无序/有序列表、分隔线
 */
@Composable
fun MarkdownText(
    text: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle.Default,
    contentColor: Color = Color.Unspecified
) {
    val blocks = remember(text) { parseMarkdownBlocks(text) }

    Column(modifier = modifier) {
        blocks.forEach { block ->
            when (block) {
                is MarkdownBlock.Heading -> HeadingBlock(block, contentColor)
                is MarkdownBlock.CodeBlock -> CodeBlockView(block)
                is MarkdownBlock.HorizontalRule -> HorizontalRuleView()
                is MarkdownBlock.ListItem -> ListItemBlock(block, textStyle, contentColor)
                is MarkdownBlock.Paragraph -> ParagraphBlock(block, textStyle, contentColor)
            }
        }
    }
}

// ─── 数据模型 ───────────────────────────────────────────────

/** Markdown 块级元素 */
sealed class MarkdownBlock {
    data class Heading(val level: Int, val text: String) : MarkdownBlock()
    data class CodeBlock(val language: String, val code: String) : MarkdownBlock()
    data class ListItem(val ordered: Boolean, val index: Int, val inlineText: String) : MarkdownBlock()
    data class Paragraph(val inlineText: String) : MarkdownBlock()
    object HorizontalRule : MarkdownBlock()
}

// ─── 解析逻辑 ───────────────────────────────────────────────

/** 将原始 Markdown 字符串解析为块级元素列表 */
fun parseMarkdownBlocks(raw: String): List<MarkdownBlock> {
    val result = mutableListOf<MarkdownBlock>()
    val lines = raw.lines()
    var i = 0

    while (i < lines.size) {
        val line = lines[i]

        // 代码块
        if (line.trimStart().startsWith("```")) {
            val lang = line.trimStart().removePrefix("```").trim()
            val codeLines = mutableListOf<String>()
            i++
            while (i < lines.size && !lines[i].trimStart().startsWith("```")) {
                codeLines.add(lines[i])
                i++
            }
            result.add(MarkdownBlock.CodeBlock(lang, codeLines.joinToString("\n")))
            i++ // 跳过结尾 ```
            continue
        }

        // 分隔线
        if (line.trim().matches(Regex("^[-*_]{3,}$"))) {
            result.add(MarkdownBlock.HorizontalRule)
            i++
            continue
        }

        // 标题
        val headingMatch = Regex("^(#{1,6})\\s+(.*)").find(line)
        if (headingMatch != null) {
            val level = headingMatch.groupValues[1].length
            val text = headingMatch.groupValues[2]
            result.add(MarkdownBlock.Heading(level, text))
            i++
            continue
        }

        // 无序列表
        val unorderedMatch = Regex("^([*\\-+])\\s+(.*)").find(line)
        if (unorderedMatch != null) {
            result.add(MarkdownBlock.ListItem(false, 0, unorderedMatch.groupValues[2]))
            i++
            continue
        }

        // 有序列表
        val orderedMatch = Regex("^(\\d+)\\.\\s+(.*)").find(line)
        if (orderedMatch != null) {
            val index = orderedMatch.groupValues[1].toIntOrNull() ?: 1
            result.add(MarkdownBlock.ListItem(true, index, orderedMatch.groupValues[2]))
            i++
            continue
        }

        // 空行 —— 跳过
        if (line.isBlank()) {
            i++
            continue
        }

        // 普通段落（合并连续非空行）
        val paragraphLines = mutableListOf<String>()
        while (i < lines.size && lines[i].isNotBlank()
            && !lines[i].trimStart().startsWith("```")
            && !lines[i].trim().matches(Regex("^[-*_]{3,}$"))
            && !Regex("^#{1,6}\\s+").containsMatchIn(lines[i])
        ) {
            paragraphLines.add(lines[i])
            i++
        }
        if (paragraphLines.isNotEmpty()) {
            result.add(MarkdownBlock.Paragraph(paragraphLines.joinToString(" ")))
        }
    }

    return result
}

// ─── 行内样式解析 ───────────────────────────────────────────

/** 将行内 Markdown（粗体、斜体、行内代码）转为 AnnotatedString */
fun parseInlineMarkdown(
    text: String,
    contentColor: Color = Color.Unspecified
): AnnotatedString = buildAnnotatedString {
    var i = 0
    while (i < text.length) {
        // 行内代码 `code`
        if (text[i] == '`') {
            val end = text.indexOf('`', i + 1)
            if (end != -1) {
                withStyle(
                    SpanStyle(
                        fontFamily = FontFamily.Monospace,
                        background = Color(0x1A000000),
                        fontSize = 13.sp
                    )
                ) {
                    append(text.substring(i + 1, end))
                }
                i = end + 1
                continue
            }
        }

        // 粗体 **text**
        if (i + 1 < text.length && text[i] == '*' && text[i + 1] == '*') {
            val end = text.indexOf("**", i + 2)
            if (end != -1) {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(text.substring(i + 2, end))
                }
                i = end + 2
                continue
            }
        }

        // 斜体 *text*
        if (text[i] == '*') {
            val end = text.indexOf('*', i + 1)
            if (end != -1) {
                withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                    append(text.substring(i + 1, end))
                }
                i = end + 1
                continue
            }
        }

        // 普通字符
        append(text[i])
        i++
    }
}

// ─── UI 子组件 ──────────────────────────────────────────────

/** 标题块 */
@Composable
private fun HeadingBlock(block: MarkdownBlock.Heading, contentColor: Color) {
    val (fontSize, fontWeight, topPad, botPad) = when (block.level) {
        1 -> listOf(22.sp, FontWeight.Bold, 8.dp, 4.dp)
        2 -> listOf(19.sp, FontWeight.Bold, 6.dp, 3.dp)
        3 -> listOf(16.sp, FontWeight.SemiBold, 4.dp, 2.dp)
        else -> listOf(14.sp, FontWeight.SemiBold, 2.dp, 1.dp)
    }
    @Suppress("UNCHECKED_CAST")
    Text(
        text = block.text,
        style = TextStyle(
            fontSize = fontSize as androidx.compose.ui.unit.TextUnit,
            fontWeight = fontWeight as FontWeight,
            color = contentColor
        ),
        modifier = Modifier.padding(top = topPad as androidx.compose.ui.unit.Dp, bottom = botPad as androidx.compose.ui.unit.Dp)
    )
}

/** 代码块，含语言标签 + 一键复制 + 横向滚动 */
@Composable
private fun CodeBlockView(block: MarkdownBlock.CodeBlock) {
    val clipboard = LocalClipboardManager.current
    var copied by remember { mutableStateOf(false) }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFF1E1E1E),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column {
            // 顶栏：语言 + 复制按钮
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2D2D2D))
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = block.language.ifBlank { "code" },
                    style = TextStyle(
                        fontSize = 11.sp,
                        color = Color(0xFFAAAAAA),
                        fontFamily = FontFamily.Monospace
                    )
                )
                IconButton(
                    onClick = {
                        clipboard.setText(AnnotatedString(block.code))
                        copied = true
                    },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ContentCopy,
                        contentDescription = if (copied) "已复制" else "复制",
                        tint = if (copied) Color(0xFF4CAF50) else Color(0xFFAAAAAA),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // 代码内容，横向可滚动
            SelectionContainer {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(12.dp)
                ) {
                    Text(
                        text = block.code,
                        style = TextStyle(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 13.sp,
                            color = Color(0xFFD4D4D4),
                            lineHeight = 20.sp
                        )
                    )
                }
            }
        }
    }
}

/** 分隔线 */
@Composable
private fun HorizontalRuleView() {
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 8.dp),
        color = Color(0x33000000),
        thickness = 1.dp
    )
}

/** 列表项 */
@Composable
private fun ListItemBlock(
    block: MarkdownBlock.ListItem,
    textStyle: TextStyle,
    contentColor: Color
) {
    Row(
        modifier = Modifier.padding(vertical = 1.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = if (block.ordered) "${block.index}. " else "• ",
            style = textStyle.copy(color = contentColor),
            modifier = Modifier.width(20.dp)
        )
        Text(
            text = parseInlineMarkdown(block.inlineText, contentColor),
            style = textStyle.copy(color = contentColor),
            modifier = Modifier.weight(1f)
        )
    }
}

/** 普通段落 */
@Composable
private fun ParagraphBlock(
    block: MarkdownBlock.Paragraph,
    textStyle: TextStyle,
    contentColor: Color
) {
    Text(
        text = parseInlineMarkdown(block.inlineText, contentColor),
        style = textStyle.copy(color = contentColor),
        modifier = Modifier.padding(vertical = 1.dp)
    )
}
