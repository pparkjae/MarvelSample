package com.marvel.data.model

import androidx.room.*

@Dao
interface CharacterDao {
    @Query("SELECT * FROM character_table ORDER BY id ASC")
    fun getCharacter(): List<CharacterTable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(character: CharacterTable): Long

    @Query("DELETE FROM character_table WHERE id=:id")
    suspend fun delete(id: Int): Int
}