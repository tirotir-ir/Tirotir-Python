package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProgressDao {
  @Query("SELECT * FROM user_progress WHERE id = 1")
  fun getUserProgress(): Flow<UserProgressEntity?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun saveUserProgress(progress: UserProgressEntity)

  @Query("SELECT * FROM user_progress WHERE id = 1")
  suspend fun getUserProgressOnce(): UserProgressEntity?
}
