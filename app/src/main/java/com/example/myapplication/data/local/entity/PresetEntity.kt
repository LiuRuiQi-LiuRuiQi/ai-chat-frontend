package com.example.myapplication.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 预设实体 - 存储 system prompt 预设配置
 * 后续可扩展为角色卡、世界书入口
 * @param id 自增主键
 * @param name 预设名称
 * @param systemPrompt 系统提示词
 * @param description 预设描述
 * @param createdAt 创建时间戳
 * @param isDefault 是否为默认预设
 */
@Entity(tableName = "presets")
data class PresetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    /** 预设名称（展示用） */
    val name: String,
    /** system prompt（会插入到对话历史最前） */
    val systemPrompt: String,
    /** 备注说明（可选） */
    val description: String? = null,
    /** 创建时间 */
    val createdAt: Long = System.currentTimeMillis(),
    /** 最近更新时间（用于排序/展示，可选功能预留） */
    val updatedAt: Long = System.currentTimeMillis(),
    /** 是否默认预设（最多 1 个） */
    val isDefault: Boolean = false,
    /** 采样温度（可选， 表示不传给后端，使用模型默认） */
    val temperature: Float? = null,
    /** top_p（可选， 表示不传给后端，使用模型默认） */
    val topP: Float? = null,
    /** max_tokens（可选， 表示不传给后端，使用模型默认） */
    val maxTokens: Int? = null
)
