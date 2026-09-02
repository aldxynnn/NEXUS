package com.nexus.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "focus_sessions"
)
data class FocusSessionEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val userId: String,

    val task: String,

    val plannedMinutes: Int,

    val elapsedSeconds: Int,

    val completed: Boolean,

    val startedAt: Long,

    val finishedAt: Long
)