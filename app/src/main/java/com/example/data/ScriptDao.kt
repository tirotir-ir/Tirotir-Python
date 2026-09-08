package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ScriptDao {
  @Query("SELECT * FROM saved_scripts ORDER BY updatedAt DESC")
  fun getAllScripts(): Flow<List<ScriptEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertScript(script: ScriptEntity): Long

  @Update
  suspend fun updateScript(script: ScriptEntity)

  @Delete
  suspend fun deleteScript(script: ScriptEntity)

  @Query("DELETE FROM saved_scripts WHERE id = :id")
  suspend fun deleteById(id: Long)
}
