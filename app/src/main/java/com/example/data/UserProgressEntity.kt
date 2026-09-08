package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_progress")
data class UserProgressEntity(
    @PrimaryKey
    val id: Int = 1,
    val studentName: String = "Python Student",
    val completedWeeksCsv: String = "", // e.g. "1,2,3"
    val completedQuizzesCsv: String = "", // e.g. "1,2"
    val quizScore: Int = 0,
    val labRunsCount: Int = 0,
    val certificateId: String = "TT-PY-2026-84920",
    val certificateIssuedAt: Long = 0L
)
