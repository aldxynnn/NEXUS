package com.nexus.app.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query(
        """
        SELECT *
        FROM tasks
        WHERE userId = :userId
        ORDER BY createdAt DESC
        """
    )
    fun observeTasks(
        userId: String
    ): Flow<List<TaskEntity>>

    @Insert
    suspend fun insert(
        task: TaskEntity
    )

    @Update
    suspend fun update(
        task: TaskEntity
    )

    @Delete
    suspend fun delete(
        task: TaskEntity
    )

    @Query(
        """
        DELETE FROM tasks
        WHERE id = :id
        """
    )
    suspend fun deleteById(
        id: Int
    )

    @Query(
        """
        SELECT COUNT(*)
        FROM tasks
        WHERE userId = :userId
        AND title = :title
        AND time = :time
        """
    )
    suspend fun countExistingTask(
        userId: String,
        title: String,
        time: String
    ): Int

    @Query(
        """
        SELECT COUNT(*)
        FROM tasks
        WHERE userId = :userId
        AND completed = 0
        """
    )
    fun observeRemainingCount(
        userId: String
    ): Flow<Int>

    @Query(
        """
        SELECT COUNT(*)
        FROM tasks
        WHERE userId = :userId
        AND completed = 1
        """
    )
    fun observeCompletedCount(
        userId: String
    ): Flow<Int>
}