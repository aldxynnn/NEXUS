package com.nexus.app.data.repository

import com.nexus.app.data.local.TaskDao
import com.nexus.app.data.local.TaskEntity
import kotlinx.coroutines.flow.Flow

class TaskRepository(
    private val taskDao: TaskDao
) {

    fun observeTasks(
        userId: String
    ): Flow<List<TaskEntity>> {
        return taskDao.observeTasks(userId)
    }

    fun observeRemainingTaskCount(
        userId: String
    ): Flow<Int> {
        return taskDao.observeRemainingCount(userId)
    }

    fun observeCompletedTaskCount(
        userId: String
    ): Flow<Int> {
        return taskDao.observeCompletedCount(userId)
    }

    suspend fun addTask(
        userId: String,
        title: String,
        category: String,
        time: String,
        day: String
    ) {
        taskDao.insert(
            TaskEntity(
                userId = userId,
                title = title.trim(),
                category = category.trim().ifBlank { "General" },
                time = time.trim().ifBlank { "--:--" },
                completed = false,
                day = day
            )
        )
    }

    suspend fun updateTask(
        task: TaskEntity
    ) {
        taskDao.update(task)
    }

    suspend fun deleteTask(
        task: TaskEntity
    ) {
        taskDao.delete(task)
    }

    suspend fun completeTask(
        task: TaskEntity
    ) {
        taskDao.update(
            task.copy(
                completed = true
            )
        )
    }
}