package com.nexus.app.data.repository

import com.nexus.app.data.local.FocusSessionDao
import com.nexus.app.data.local.FocusSessionEntity
import kotlinx.coroutines.flow.Flow

class FocusSessionRepository(
    private val focusSessionDao: FocusSessionDao
) {

    suspend fun saveSession(
        userId: String,
        task: String,
        plannedMinutes: Int,
        elapsedSeconds: Int,
        completed: Boolean,
        startedAt: Long,
        finishedAt: Long
    ) {

        val session = FocusSessionEntity(
            userId = userId,
            task = task,
            plannedMinutes = plannedMinutes,
            elapsedSeconds = elapsedSeconds,
            completed = completed,
            startedAt = startedAt,
            finishedAt = finishedAt
        )

        focusSessionDao.insert(session)
    }

    fun observeSessions(
        userId: String
    ): Flow<List<FocusSessionEntity>> {
        return focusSessionDao.observeSessions(userId)
    }

    fun observeSessionCount(
        userId: String
    ): Flow<Int> {
        return focusSessionDao.observeSessionCount(userId)
    }

    fun observeTotalFocusSeconds(
        userId: String
    ): Flow<Int> {
        return focusSessionDao.observeTotalFocusSeconds(userId)
    }
}