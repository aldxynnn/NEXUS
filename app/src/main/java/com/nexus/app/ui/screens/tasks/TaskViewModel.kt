package com.nexus.app.ui.screens.tasks

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nexus.app.data.local.AppDatabase
import com.nexus.app.data.local.TaskEntity
import com.nexus.app.ui.screens.ai.AIPlanTask
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val database =
        AppDatabase.getInstance(application)

    private val taskDao =
        database.taskDao()

    private val prefs =
        application.getSharedPreferences(
            "nexus_auth",
            Context.MODE_PRIVATE
        )

    private val currentUserId =
        prefs.getString("email", "") ?: ""

    val tasks: StateFlow<List<TaskEntity>> =
        taskDao
            .observeTasks(currentUserId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun addTask(
        title: String,
        category: String,
        time: String,
        day: String
    ) {

        if (title.isBlank()) {
            return
        }

        if (currentUserId.isBlank()) {
            return
        }

        viewModelScope.launch {

            taskDao.insert(
                TaskEntity(
                    userId = currentUserId,
                    title = title.trim(),
                    category = category
                        .ifBlank { "General" }
                        .trim(),
                    time = time
                        .ifBlank { "--:--" }
                        .trim(),
                    completed = false,
                    day = day
                )
            )
        }
    }

    /*
     * Menambahkan hasil AI Planner ke Room.
     *
     * Task yang sudah ada tidak akan dibuat ulang.
     */
    fun addPlanFromAI(
        plan: List<AIPlanTask>
    ) {

        if (currentUserId.isBlank()) {
            return
        }

        if (plan.isEmpty()) {
            return
        }

        viewModelScope.launch {

            plan.forEach { aiTask ->

                val title = aiTask.title
                    .trim()

                val time = aiTask.time
                    .ifBlank { "--:--" }
                    .trim()

                val category = aiTask.category
                    .ifBlank { "General" }
                    .trim()

                /*
                 * Cek apakah task yang sama
                 * sudah ada untuk akun ini.
                 */
                val existingCount =
                    taskDao.countExistingTask(
                        userId = currentUserId,
                        title = title,
                        time = time
                    )

                /*
                 * Hanya insert jika belum ada.
                 */
                if (existingCount == 0) {

                    taskDao.insert(
                        TaskEntity(
                            userId = currentUserId,
                            title = title,
                            category = category,
                            time = time,
                            completed = false,
                            day = "Today"
                        )
                    )
                }
            }
        }
    }

    fun toggleTask(
        task: TaskEntity
    ) {

        if (task.userId != currentUserId) {
            return
        }

        viewModelScope.launch {

            taskDao.update(
                task.copy(
                    completed = !task.completed
                )
            )
        }
    }

    fun updateTask(
        task: TaskEntity
    ) {

        if (task.userId != currentUserId) {
            return
        }

        viewModelScope.launch {
            taskDao.update(task)
        }
    }

    fun deleteTask(
        task: TaskEntity
    ) {

        if (task.userId != currentUserId) {
            return
        }

        viewModelScope.launch {
            taskDao.delete(task)
        }
    }

    fun completeTask(
        task: TaskEntity
    ) {

        if (task.userId != currentUserId) {
            return
        }

        viewModelScope.launch {

            taskDao.update(
                task.copy(
                    completed = true
                )
            )
        }
    }

    fun totalTasks(): Int {
        return tasks.value.size
    }

    fun completedTasks(): Int {
        return tasks.value.count {
            it.completed
        }
    }

    fun remainingTasks(): Int {
        return tasks.value.count {
            !it.completed
        }
    }

    fun todayTasks(): List<TaskEntity> {
        return tasks.value.filter {
            it.day == "Today"
        }
    }

    fun upcomingTasks(): List<TaskEntity> {
        return tasks.value.filter {
            it.day == "Upcoming"
        }
    }

    fun completedTaskList(): List<TaskEntity> {
        return tasks.value.filter {
            it.completed
        }
    }
}