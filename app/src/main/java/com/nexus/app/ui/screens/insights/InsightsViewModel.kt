package com.nexus.app.ui.screens.insights

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nexus.app.data.local.AppDatabase
import com.nexus.app.data.local.FocusSessionEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class DailyProductivity(
    val day: String,
    val score: Int,
    val focusMinutes: Int,
    val completedTasks: Int
)

data class FocusHistory(
    val task: String,
    val duration: Int,
    val date: String,
    val completed: Boolean
)

class InsightsViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val database =
        AppDatabase.getInstance(application)

    private val dao =
        database.focusSessionDao()

    private val prefs =
        application.getSharedPreferences(
            "nexus_auth",
            Context.MODE_PRIVATE
        )

    private val userId: String
        get() = prefs.getString(
            "email",
            ""
        )?.trim() ?: ""

    val sessions: StateFlow<List<FocusSessionEntity>> =
        dao.observeSessions(userId).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    var selectedPeriod by mutableStateOf("Week")
        private set

    fun selectPeriod(period: String) {
        if (
            period != "Today" &&
            period != "Week" &&
            period != "Month"
        ) {
            return
        }

        selectedPeriod = period
    }

    // =========================================================
    // FILTER
    // =========================================================

    private fun filteredSessions(): List<FocusSessionEntity> {

        val allSessions = sessions.value

        if (allSessions.isEmpty()) {
            return emptyList()
        }

        val calendar = Calendar.getInstance()

        return when (selectedPeriod) {

            "Today" -> {

                calendar.set(
                    Calendar.HOUR_OF_DAY,
                    0
                )
                calendar.set(
                    Calendar.MINUTE,
                    0
                )
                calendar.set(
                    Calendar.SECOND,
                    0
                )
                calendar.set(
                    Calendar.MILLISECOND,
                    0
                )

                val start = calendar.timeInMillis

                allSessions.filter {
                    it.startedAt >= start
                }
            }

            "Week" -> {

                calendar.set(
                    Calendar.DAY_OF_WEEK,
                    calendar.firstDayOfWeek
                )
                calendar.set(
                    Calendar.HOUR_OF_DAY,
                    0
                )
                calendar.set(
                    Calendar.MINUTE,
                    0
                )
                calendar.set(
                    Calendar.SECOND,
                    0
                )
                calendar.set(
                    Calendar.MILLISECOND,
                    0
                )

                val start = calendar.timeInMillis

                allSessions.filter {
                    it.startedAt >= start
                }
            }

            "Month" -> {

                calendar.set(
                    Calendar.DAY_OF_MONTH,
                    1
                )
                calendar.set(
                    Calendar.HOUR_OF_DAY,
                    0
                )
                calendar.set(
                    Calendar.MINUTE,
                    0
                )
                calendar.set(
                    Calendar.SECOND,
                    0
                )
                calendar.set(
                    Calendar.MILLISECOND,
                    0
                )

                val start = calendar.timeInMillis

                allSessions.filter {
                    it.startedAt >= start
                }
            }

            else -> allSessions
        }
    }

    // =========================================================
    // OVERVIEW
    // =========================================================

    fun completedTasks(): Int {
        return filteredSessions().count {
            it.completed
        }
    }

    fun focusMinutes(): Int {
        return filteredSessions().sumOf {
            it.elapsedSeconds
        } / 60
    }

    fun completionRate(): Int {

        val periodSessions =
            filteredSessions()

        if (periodSessions.isEmpty()) {
            return 0
        }

        val completed =
            periodSessions.count {
                it.completed
            }

        return (
                completed.toFloat() /
                        periodSessions.size.toFloat() *
                        100f
                )
            .toInt()
            .coerceIn(0, 100)
    }

    fun productivityScore(): Int {

        val periodSessions =
            filteredSessions()

        if (periodSessions.isEmpty()) {
            return 0
        }

        val plannedSeconds =
            periodSessions.sumOf {
                it.plannedMinutes * 60
            }

        if (plannedSeconds <= 0) {
            return 0
        }

        val elapsedSeconds =
            periodSessions.sumOf {
                it.elapsedSeconds
            }

        val focusScore =
            (
                    elapsedSeconds.toFloat() /
                            plannedSeconds.toFloat() *
                            100f
                    )
                .toInt()
                .coerceIn(0, 100)

        val completionScore =
            completionRate()

        return (
                focusScore * 0.7f +
                        completionScore * 0.3f
                )
            .toInt()
            .coerceIn(0, 100)
    }

    // =========================================================
    // PRODUCTIVITY CHART
    // =========================================================

    fun productivityChartData(): List<DailyProductivity> {

        return when (selectedPeriod) {

            "Today" -> {

                val todaySessions =
                    filteredSessions()

                val focusMinutes =
                    todaySessions.sumOf {
                        it.elapsedSeconds
                    } / 60

                val completedTasks =
                    todaySessions.count {
                        it.completed
                    }

                val plannedMinutes =
                    todaySessions.sumOf {
                        it.plannedMinutes
                    }

                val score =
                    if (plannedMinutes <= 0) {
                        0
                    } else {
                        (
                                focusMinutes.toFloat() /
                                        plannedMinutes.toFloat() *
                                        100f
                                )
                            .toInt()
                            .coerceIn(0, 100)
                    }

                listOf(
                    DailyProductivity(
                        day = "Today",
                        score = score,
                        focusMinutes = focusMinutes,
                        completedTasks = completedTasks
                    )
                )
            }

            "Week" -> {

                val filtered =
                    filteredSessions()

                val calendar =
                    Calendar.getInstance()

                calendar.set(
                    Calendar.DAY_OF_WEEK,
                    calendar.firstDayOfWeek
                )
                calendar.set(
                    Calendar.HOUR_OF_DAY,
                    0
                )
                calendar.set(
                    Calendar.MINUTE,
                    0
                )
                calendar.set(
                    Calendar.SECOND,
                    0
                )
                calendar.set(
                    Calendar.MILLISECOND,
                    0
                )

                val startOfWeek =
                    calendar.timeInMillis

                val dayNames =
                    listOf(
                        "Mon",
                        "Tue",
                        "Wed",
                        "Thu",
                        "Fri",
                        "Sat",
                        "Sun"
                    )

                (0..6).map { index ->

                    val dayStart =
                        startOfWeek +
                                index * 24L * 60L * 60L * 1000L

                    val dayEnd =
                        dayStart +
                                24L * 60L * 60L * 1000L

                    val daySessions =
                        filtered.filter {
                            it.startedAt >= dayStart &&
                                    it.startedAt < dayEnd
                        }

                    val focusMinutes =
                        daySessions.sumOf {
                            it.elapsedSeconds
                        } / 60

                    val completedTasks =
                        daySessions.count {
                            it.completed
                        }

                    val plannedMinutes =
                        daySessions.sumOf {
                            it.plannedMinutes
                        }

                    val score =
                        if (plannedMinutes <= 0) {
                            0
                        } else {
                            (
                                    focusMinutes.toFloat() /
                                            plannedMinutes.toFloat() *
                                            100f
                                    )
                                .toInt()
                                .coerceIn(0, 100)
                        }

                    DailyProductivity(
                        day = dayNames[index],
                        score = score,
                        focusMinutes = focusMinutes,
                        completedTasks = completedTasks
                    )
                }
            }

            "Month" -> {

                val filtered =
                    filteredSessions()

                val calendar =
                    Calendar.getInstance()

                val year =
                    calendar.get(Calendar.YEAR)

                val month =
                    calendar.get(Calendar.MONTH)

                val daysInMonth =
                    calendar.getActualMaximum(
                        Calendar.DAY_OF_MONTH
                    )

                (1..daysInMonth).map { day ->

                    val daySessions =
                        filtered.filter {

                            val sessionCalendar =
                                Calendar.getInstance()

                            sessionCalendar.timeInMillis =
                                it.startedAt

                            sessionCalendar.get(
                                Calendar.YEAR
                            ) == year &&
                                    sessionCalendar.get(
                                        Calendar.MONTH
                                    ) == month &&
                                    sessionCalendar.get(
                                        Calendar.DAY_OF_MONTH
                                    ) == day
                        }

                    val focusMinutes =
                        daySessions.sumOf {
                            it.elapsedSeconds
                        } / 60

                    val completedTasks =
                        daySessions.count {
                            it.completed
                        }

                    val plannedMinutes =
                        daySessions.sumOf {
                            it.plannedMinutes
                        }

                    val score =
                        if (plannedMinutes <= 0) {
                            0
                        } else {
                            (
                                    focusMinutes.toFloat() /
                                            plannedMinutes.toFloat() *
                                            100f
                                    )
                                .toInt()
                                .coerceIn(0, 100)
                        }

                    DailyProductivity(
                        day = day.toString(),
                        score = score,
                        focusMinutes = focusMinutes,
                        completedTasks = completedTasks
                    )
                }
            }

            else -> emptyList()
        }
    }

    // =========================================================
    // BEST DAY
    // =========================================================

    fun bestDay(): DailyProductivity {

        val data =
            productivityChartData()

        return data.maxByOrNull {
            it.score
        } ?: DailyProductivity(
            day = "-",
            score = 0,
            focusMinutes = 0,
            completedTasks = 0
        )
    }

    // =========================================================
    // SESSION COUNT
    // =========================================================

    fun filteredSessionCount(): Int {
        return filteredSessions().size
    }

    // =========================================================
    // HISTORY
    // =========================================================

    fun focusHistory(): List<FocusHistory> {

        return filteredSessions().map { session ->

            FocusHistory(
                task = session.task,
                duration = session.elapsedSeconds / 60,
                date = formatDate(
                    session.finishedAt
                ),
                completed = session.completed
            )
        }
    }

    private fun formatDate(
        timestamp: Long
    ): String {

        if (timestamp <= 0L) {
            return "-"
        }

        return SimpleDateFormat(
            "dd MMM yyyy, HH:mm",
            Locale.getDefault()
        ).format(
            Date(timestamp)
        )
    }
}
