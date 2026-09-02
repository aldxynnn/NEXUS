package com.nexus.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    @Insert
    suspend fun insert(
        user: UserEntity
    )

    @Query(
        """
        SELECT *
        FROM users
        WHERE email = :email
        LIMIT 1
        """
    )
    suspend fun getUserByEmail(
        email: String
    ): UserEntity?

    @Query(
        """
        SELECT *
        FROM users
        WHERE email = :email
        AND password = :password
        LIMIT 1
        """
    )
    suspend fun login(
        email: String,
        password: String
    ): UserEntity?

    @Query(
        """
        SELECT COUNT(*)
        FROM users
        WHERE email = :email
        """
    )
    suspend fun emailExists(
        email: String
    ): Int

    @Query(
        """
        UPDATE users
        SET password = :newPassword
        WHERE email = :email
        """
    )
    suspend fun updatePassword(
        email: String,
        newPassword: String
    )
}