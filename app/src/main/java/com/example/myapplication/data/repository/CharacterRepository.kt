package com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.CharacterDao
import com.example.myapplication.data.local.entity.CharacterEntity
import kotlinx.coroutines.flow.Flow

/**
 * 角色数据仓库
 */
class CharacterRepository(
    private val characterDao: CharacterDao
) {

    fun observeAll(): Flow<List<CharacterEntity>> = characterDao.observeAll()

    suspend fun getById(id: Long): CharacterEntity? = characterDao.getById(id)

    suspend fun upsert(entity: CharacterEntity): Long = characterDao.upsert(entity)

    suspend fun update(entity: CharacterEntity) = characterDao.update(entity)

    suspend fun delete(entity: CharacterEntity) = characterDao.delete(entity)
}
