package com.nexus.app.data.repository

import com.nexus.app.data.local.FocusSessionDao
import com.nexus.app.data.local.FocusSessionEntity
import com.nexus.app.data.local.TaskDao
import com.nexus.app.data.local.TaskEntity
import kotlinx.coroutines.flow.Flow

class NexusRepository(
    private val taskDao: TaskDao,
    private val focusSessionDao: FocusSessionDao
) {

    // =====================================================
    // TASKS
    // =====================================================

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
                title = title,
                category = category,
                time = time,
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

    // =====================================================
    // FOCUS
    // =====================================================

    fun observeFocusSessions(
        userId: String
    ): Flow<List<FocusSessionEntity>> {
        return focusSessionDao.observeSessions(userId)
    }

    fun observeFocusSessionCount(
        userId: String
    ): Flow<Int> {
        return focusSessionDao.observeSessionCount(userId)
    }

    fun observeTotalFocusSeconds(
        userId: String
    ): Flow<Int> {
        return focusSessionDao.observeTotalFocusSeconds(userId)
    }

    suspend fun saveFocusSession(
        userId: String,
        task: String,
        plannedMinutes: Int,
        elapsedSeconds: Int,
        completed: Boolean,
        startedAt: Long,
        finishedAt: Long
    ) {
        focusSessionDao.insert(
            FocusSessionEntity(
                userId = userId,
                task = task,
                plannedMinutes = plannedMinutes,
                elapsedSeconds = elapsedSeconds,
                completed = completed,
                startedAt = startedAt,
                finishedAt = finishedAt
            )
        )
    }
}