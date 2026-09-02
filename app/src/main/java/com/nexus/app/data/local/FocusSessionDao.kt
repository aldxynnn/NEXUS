package com.nexus.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nexus.app.data.local.FocusSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FocusSessionDao {

    @Insert
    suspend fun insert(
        session: FocusSessionEntity
    )

    @Query(
        """
        SELECT *
        FROM focus_sessions
        WHERE userId = :userId
        ORDER BY startedAt DESC
        """
    )
    fun observeSessions(userId: String): Flow<List<FocusSessionEntity>>

    @Query(
        """
        SELECT COUNT(*)
        FROM focus_sessions
        WHERE userId = :userId
        """
    )
    fun observeSessionCount(userId: String): Flow<Int>

    @Query(
        """
        SELECT COALESCE(SUM(elapsedSeconds), 0)
        FROM focus_sessions
        WHERE userId = :userId
        """
    )
    fun observeTotalFocusSeconds(userId: String): Flow<Int>
}