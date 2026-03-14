package com.example.myapplication.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 角色表（第一阶段：用于注入 system prompt + 可选开场白）。
 */
@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    /** 角色名称（列表展示用） */
    val name: String,
    /** 角色描述（会注入到对话 system 上下文） */
    val description: String,
    /** 开场白（当会话没有任何消息时，可自动插入一条 assistant 消息） */
    val greeting: String = ""
)
