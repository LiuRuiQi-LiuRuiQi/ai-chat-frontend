package com.example.myapplication.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.data.local.entity.CharacterEntity
import kotlinx.coroutines.flow.Flow

/**
 * 角色表 DAO
 */
@Dao
interface CharacterDao {

    @Query("SELECT * FROM characters ORDER BY id DESC")
    fun observeAll(): Flow<List<CharacterEntity>>

    @Query("SELECT * FROM characters WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): CharacterEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: CharacterEntity): Long

    @Update
    suspend fun update(entity: CharacterEntity)

    @Delete
    suspend fun delete(entity: CharacterEntity)
}
