package com.portugal1576.marsrover.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteCharacterDao {

    @Query("SELECT * FROM favorite_characters ORDER BY name ASC")
    fun observeAll(): Flow<List<FavoriteCharacterEntity>>

    @Query("SELECT * FROM favorite_characters WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): FavoriteCharacterEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: FavoriteCharacterEntity)

    @Query("DELETE FROM favorite_characters WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT id FROM favorite_characters")
    fun observeIds(): Flow<List<Int>>
}
