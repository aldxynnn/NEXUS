package com.nexus.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks"
)
data class TaskEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val userId: String,

    val title: String,

    val category: String,

    val time: String,

    val completed: Boolean,

    val day: String,

    val createdAt: Long = System.currentTimeMillis()
)