package com.nexus.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        TaskEntity::class,
        FocusSessionEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class NexusDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    abstract fun focusSessionDao(): FocusSessionDao

    companion object {

        @Volatile
        private var INSTANCE: NexusDatabase? = null

        fun getInstance(
            context: Context
        ): NexusDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        NexusDatabase::class.java,
                        "nexus_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                INSTANCE = instance

                instance
            }
        }
    }
}