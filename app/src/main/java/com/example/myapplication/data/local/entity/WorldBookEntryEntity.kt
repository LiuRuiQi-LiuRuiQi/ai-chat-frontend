package com.example.myapplication.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 世界书条目表（第一阶段：全局世界书，支持关键词命中）
 */
@Entity(tableName = "world_book_entries")
data class WorldBookEntryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    /** 条目标题 */
    val title: String,
    /** 关键词（逗号分隔，如 "关键词1,关键词2,关键词3"） */
    val keywords: String,
    /** 条目内容（会在命中时注入到 system prompt） */
    val content: String,
    /** 是否启用 */
    val enabled: Boolean = true,
    /** 优先级（数值越大优先级越高，命中时按优先级从高到低排序） */
    val priority: Int = 0,
    /** 创建时间戳 */
    val createdAt: Long = System.currentTimeMillis(),
    /** 更新时间戳 */
    val updatedAt: Long = System.currentTimeMillis()
)
