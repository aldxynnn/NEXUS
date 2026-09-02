package com.nexus.app.ui.screens.focus

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nexus.app.data.local.AppDatabase
import com.nexus.app.data.local.FocusSessionEntity
import com.nexus.app.data.local.TaskEntity
import com.nexus.app.data.repository.FocusSessionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class FocusState {
    SETUP,
    RUNNING,
    PAUSED,
    RESULT
}

class FocusViewModel(
    application: Application
) : AndroidViewModel(application) {

    // =========================================================
    // DATABASE
    // =========================================================

    private val database =
        AppDatabase.getInstance(application)

    private val repository =
        FocusSessionRepository(
            database.focusSessionDao()
        )

    private val taskDao =
        database.taskDao()

    // =========================================================
    // USER
    // =========================================================

    private val prefs =
        application.getSharedPreferences(
            "nexus_auth",
            Context.MODE_PRIVATE
        )

    /*
     * TaskViewModel menggunakan EMAIL sebagai userId
     * pada TaskEntity.
     *
     * Focus juga menggunakan EMAIL agar task dan
     * focus session sesuai dengan akun yang sedang login.
     */
    private val userId: String
        get() =
            prefs.getString(
                "email",
                ""
            )?.trim() ?: ""

    // =========================================================
    // TASKS
    // =========================================================

    val tasks: StateFlow<List<TaskEntity>> =
        taskDao
            .observeTasks(userId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    // =========================================================
    // FOCUS STATE
    // =========================================================

    var focusState by mutableStateOf(
        FocusState.SETUP
    )
        private set

    var selectedTask by mutableStateOf("")
        private set

    var selectedTaskId by mutableStateOf<Int?>(null)
        private set

    var selectedDuration by mutableStateOf(25)
        private set

    var remainingSeconds by mutableStateOf(
        25 * 60
    )
        private set

    var elapsedSeconds by mutableStateOf(0)
        private set

    var completedSession by mutableStateOf(false)
        private set

    // =========================================================
    // SESSION
    // =========================================================

    private var sessionStartedAt: Long = 0L

    private var sessionSaved = false

    // =========================================================
    // DATABASE OBSERVATION
    // =========================================================

    val sessions: StateFlow<List<FocusSessionEntity>> =
        repository
            .observeSessions(userId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val sessionCount: StateFlow<Int> =
        repository
            .observeSessionCount(userId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = 0
            )

    val totalFocusSeconds: StateFlow<Int> =
        repository
            .observeTotalFocusSeconds(userId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = 0
            )

    // =========================================================
    // SELECT TASK
    // =========================================================

    fun selectTask(
        task: TaskEntity
    ) {

        if (
            focusState == FocusState.RUNNING ||
            focusState == FocusState.PAUSED
        ) {
            return
        }

        selectedTaskId =
            task.id

        selectedTask =
            task.title
    }

    // =========================================================
    // SELECT DURATION
    // =========================================================

    fun selectDuration(
        minutes: Int
    ) {

        if (minutes <= 0) {
            return
        }

        if (
            focusState == FocusState.RUNNING ||
            focusState == FocusState.PAUSED
        ) {
            return
        }

        selectedDuration =
            minutes

        remainingSeconds =
            minutes * 60

        elapsedSeconds =
            0

        completedSession =
            false

        sessionSaved =
            false
    }

    // =========================================================
    // START
    // =========================================================

    fun start() {

        when (focusState) {

            FocusState.SETUP -> {

                if (selectedTask.isBlank()) {
                    return
                }

                if (userId.isBlank()) {
                    return
                }

                sessionStartedAt =
                    System.currentTimeMillis()

                elapsedSeconds =
                    0

                remainingSeconds =
                    selectedDuration * 60

                completedSession =
                    false

                sessionSaved =
                    false

                focusState =
                    FocusState.RUNNING
            }

            FocusState.PAUSED -> {

                focusState =
                    FocusState.RUNNING
            }

            else -> Unit
        }
    }

    // =========================================================
    // PAUSE
    // =========================================================

    fun pause() {

        if (
            focusState == FocusState.RUNNING
        ) {

            focusState =
                FocusState.PAUSED
        }
    }

    // =========================================================
    // TIMER TICK
    // =========================================================

    fun tick() {

        if (
            focusState != FocusState.RUNNING
        ) {
            return
        }

        if (remainingSeconds > 1) {

            remainingSeconds--

            elapsedSeconds++

        } else {

            remainingSeconds =
                0

            elapsedSeconds =
                selectedDuration * 60

            complete()
        }
    }

    // =========================================================
    // STOP
    // =========================================================

    fun stop() {

        if (
            focusState != FocusState.RUNNING &&
            focusState != FocusState.PAUSED
        ) {
            return
        }

        if (elapsedSeconds <= 0) {

            reset()

            return
        }

        completedSession =
            false

        focusState =
            FocusState.RESULT

        saveCurrentSession()
    }

    // =========================================================
    // COMPLETE
    // =========================================================

    fun complete() {

        if (sessionSaved) {
            return
        }

        completedSession =
            true

        focusState =
            FocusState.RESULT

        saveCurrentSession()
    }

    // =========================================================
    // SAVE SESSION
    // =========================================================

    private fun saveCurrentSession() {

        if (sessionSaved) {
            return
        }

        if (selectedTask.isBlank()) {
            return
        }

        if (userId.isBlank()) {
            return
        }

        sessionSaved =
            true

        val finishedAt =
            System.currentTimeMillis()

        val startedAt =
            if (sessionStartedAt > 0L) {
                sessionStartedAt
            } else {
                finishedAt
            }

        val task =
            selectedTask

        val plannedMinutes =
            selectedDuration

        val elapsed =
            elapsedSeconds

        val completed =
            completedSession

        val currentUserId =
            userId

        viewModelScope.launch {

            try {

                repository.saveSession(

                    userId =
                        currentUserId,

                    task =
                        task,

                    plannedMinutes =
                        plannedMinutes,

                    elapsedSeconds =
                        elapsed,

                    completed =
                        completed,

                    startedAt =
                        startedAt,

                    finishedAt =
                        finishedAt
                )

            } catch (
                exception: Exception
            ) {

                exception.printStackTrace()

                sessionSaved =
                    false
            }
        }
    }

    // =========================================================
    // RESET
    // =========================================================

    fun reset() {

        focusState =
            FocusState.SETUP

        remainingSeconds =
            selectedDuration * 60

        elapsedSeconds =
            0

        completedSession =
            false

        sessionStartedAt =
            0L

        sessionSaved =
            false
    }

    // =========================================================
    // PROGRESS
    // =========================================================

    fun progress(): Float {

        val totalSeconds =
            selectedDuration * 60

        if (totalSeconds <= 0) {
            return 0f
        }

        return (
                elapsedSeconds.toFloat() /
                        totalSeconds.toFloat()
                ).coerceIn(
                0f,
                1f
            )
    }

    // =========================================================
    // ELAPSED MINUTES
    // =========================================================

    fun elapsedMinutes(): Int {

        return elapsedSeconds / 60
    }
}