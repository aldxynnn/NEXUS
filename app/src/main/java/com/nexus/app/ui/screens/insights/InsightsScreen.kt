package com.nexus.app.ui.screens.insights

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nexus.app.ui.theme.NexusBackground
import com.nexus.app.ui.theme.NexusPrimaryLight
import com.nexus.app.ui.theme.NexusSurface
import com.nexus.app.ui.theme.NexusTextSecondary
import com.nexus.app.ui.theme.NexusWhite
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun InsightsScreen(
    insightsViewModel: InsightsViewModel = viewModel()
) {

    var selectedTab by remember {
        mutableStateOf("Overview")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NexusBackground)
            .padding(
                horizontal = 20.dp,
                vertical = 24.dp
            )
    ) {

        Text(
            text = "Insights",
            color = NexusWhite,
            fontSize = 30.sp
        )

        Spacer(
            modifier = Modifier.height(5.dp)
        )

        Text(
            text = "Understand how you work.",
            color = NexusTextSecondary,
            fontSize = 13.sp
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            InsightTab(
                text = "Overview",
                selected = selectedTab == "Overview",
                onClick = {
                    selectedTab = "Overview"
                }
            )

            InsightTab(
                text = "Productivity",
                selected = selectedTab == "Productivity",
                onClick = {
                    selectedTab = "Productivity"
                }
            )

            InsightTab(
                text = "History",
                selected = selectedTab == "History",
                onClick = {
                    selectedTab = "History"
                }
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        when (selectedTab) {

            "Overview" -> {
                OverviewContent(
                    viewModel = insightsViewModel
                )
            }

            "Productivity" -> {
                ProductivityContent(
                    viewModel = insightsViewModel
                )
            }

            "History" -> {
                HistoryContent(
                    viewModel = insightsViewModel
                )
            }
        }
    }
}

@Composable
private fun InsightTab(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    TextButton(
        onClick = onClick
    ) {

        Text(
            text = text,
            color = if (selected) {
                NexusWhite
            } else {
                NexusTextSecondary
            },
            fontSize = 12.sp,
            modifier = Modifier
                .clip(
                    RoundedCornerShape(50.dp)
                )
                .background(
                    if (selected) {
                        NexusPrimaryLight.copy(
                            alpha = 0.18f
                        )
                    } else {
                        NexusSurface
                    }
                )
                .padding(
                    horizontal = 12.dp,
                    vertical = 7.dp
                )
        )
    }
}

@Composable
private fun OverviewContent(
    viewModel: InsightsViewModel
) {

    val sessions by viewModel.sessions.collectAsState()

    /*
     * Semua statistik sekarang mengambil data dari
     * InsightsViewModel agar rumus konsisten.
     */
    val tasks =
        viewModel.completedTasks()

    val focusMinutes =
        viewModel.focusMinutes()

    val completionRate =
        viewModel.completionRate()

    val score =
        viewModel.productivityScore()

    Column {

        ProductivityScoreCard(
            score = score
        )

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            SmallStatCard(
                value = "$tasks",
                label = "Tasks done",
                modifier = Modifier.weight(1f)
            )

            SmallStatCard(
                value = "$focusMinutes",
                label = "Focus min",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            SmallStatCard(
                value = "$completionRate%",
                label = "Completion",
                modifier = Modifier.weight(1f)
            )

            SmallStatCard(
                value = "${sessions.size}",
                label = "Sessions",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(
            modifier = Modifier.height(22.dp)
        )

        InsightMessage(
            title = if (sessions.isEmpty()) {
                "No sessions yet"
            } else {
                "Keep going"
            },
            message = if (sessions.isEmpty()) {
                "Start a focus session to begin building your productivity insights."
            } else {
                "Your focus data is being collected from your completed and ended sessions."
            }
        )
    }
}

@Composable
private fun ProductivityScoreCard(
    score: Int
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(22.dp)
            )
            .background(NexusSurface)
            .padding(22.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(86.dp)
                .clip(CircleShape)
                .background(
                    NexusPrimaryLight.copy(
                        alpha = 0.15f
                    )
                ),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "$score",
                color = NexusWhite,
                fontSize = 28.sp
            )
        }

        Column(
            modifier = Modifier.padding(
                start = 18.dp
            )
        ) {

            Text(
                text = "Productivity Score",
                color = NexusWhite,
                fontSize = 17.sp
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Text(
                text = if (score == 0) {
                    "Start a focus session to generate your score."
                } else {
                    "Based on your focus time and completed sessions."
                },
                color = NexusTextSecondary,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun SmallStatCard(
    value: String,
    label: String,
    modifier: Modifier
) {

    Column(
        modifier = modifier
            .height(100.dp)
            .clip(
                RoundedCornerShape(18.dp)
            )
            .background(NexusSurface)
            .padding(18.dp)
    ) {

        Text(
            text = value,
            color = NexusWhite,
            fontSize = 24.sp
        )

        Spacer(
            modifier = Modifier.height(5.dp)
        )

        Text(
            text = label,
            color = NexusTextSecondary,
            fontSize = 11.sp
        )
    }
}

@Composable
private fun InsightMessage(
    title: String,
    message: String
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(18.dp)
            )
            .background(
                NexusPrimaryLight.copy(
                    alpha = 0.08f
                )
            )
            .padding(18.dp)
    ) {

        Text(
            text = title,
            color = NexusPrimaryLight,
            fontSize = 15.sp
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = message,
            color = NexusTextSecondary,
            fontSize = 12.sp
        )
    }
}
@Composable
private fun ProductivityContent(
    viewModel: InsightsViewModel
) {

    val data =
        viewModel.productivityChartData()

    val bestDay =
        viewModel.bestDay()

    val totalSessions =
        viewModel.filteredSessionCount()

    Column {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            PeriodButton(
                text = "Today",
                selected = viewModel.selectedPeriod == "Today",
                onClick = {
                    viewModel.selectPeriod("Today")
                }
            )

            PeriodButton(
                text = "Week",
                selected = viewModel.selectedPeriod == "Week",
                onClick = {
                    viewModel.selectPeriod("Week")
                }
            )

            PeriodButton(
                text = "Month",
                selected = viewModel.selectedPeriod == "Month",
                onClick = {
                    viewModel.selectPeriod("Month")
                }
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = "Productivity",
            color = NexusWhite,
            fontSize = 17.sp
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        WeeklyChart(
            data = data
        )

        Spacer(
            modifier = Modifier.height(22.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(18.dp)
                )
                .background(NexusSurface)
                .padding(18.dp)
        ) {

            Text(
                text = "Best productivity day",
                color = NexusTextSecondary,
                fontSize = 11.sp
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = bestDay.day,
                color = NexusWhite,
                fontSize = 22.sp
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Text(
                text = "${bestDay.score}% productivity • ${bestDay.focusMinutes} min focus",
                color = NexusTextSecondary,
                fontSize = 12.sp
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "$totalSessions total session(s)",
                color = NexusTextSecondary,
                fontSize = 11.sp
            )
        }
    }
}


@Composable
private fun PeriodButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    TextButton(
        onClick = onClick
    ) {

        Text(
            text = text,
            color = if (selected) {
                NexusWhite
            } else {
                NexusTextSecondary
            },
            modifier = Modifier
                .clip(
                    RoundedCornerShape(50.dp)
                )
                .background(
                    if (selected) {
                        NexusPrimaryLight.copy(
                            alpha = 0.18f
                        )
                    } else {
                        NexusSurface
                    }
                )
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
        )
    }
}

@Composable
private fun WeeklyChart(
    data: List<DailyProductivity>
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(20.dp)
            )
            .background(NexusSurface)
            .padding(18.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {

            data.forEach { item ->

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {

                    val barHeight =
                        if (item.score <= 0) {
                            4f
                        } else {
                            (item.score * 1.35f)
                                .coerceAtMost(140f)
                        }

                    Box(
                        modifier = Modifier
                            .size(
                                width = 22.dp,
                                height = barHeight.dp
                            )
                            .clip(
                                RoundedCornerShape(
                                    topStart = 8.dp,
                                    topEnd = 8.dp
                                )
                            )
                            .background(
                                NexusPrimaryLight
                            )
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = item.day,
                        color = NexusTextSecondary,
                        fontSize = 9.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun HistoryContent(
    viewModel: InsightsViewModel
) {

    val sessions by viewModel.sessions.collectAsState()

    Column {

        Text(
            text = "Focus History",
            color = NexusWhite,
            fontSize = 18.sp
        )

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        if (sessions.isEmpty()) {

            Text(
                text = "Belum ada focus session.",
                color = NexusTextSecondary,
                fontSize = 13.sp
            )

        } else {

            sessions.forEach { session ->

                HistoryCard(
                    session = FocusHistory(
                        task = session.task,
                        duration = session.elapsedSeconds / 60,
                        date = formatHistoryDate(
                            session.finishedAt
                        ),
                        completed = session.completed
                    )
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )
            }
        }
    }
}

@Composable
private fun HistoryCard(
    session: FocusHistory
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(18.dp)
            )
            .background(NexusSurface)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(
                    NexusPrimaryLight.copy(
                        alpha = 0.12f
                    )
                ),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = if (session.completed) {
                    "✓"
                } else {
                    "–"
                },
                color = NexusPrimaryLight,
                fontSize = 18.sp
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(
                    start = 14.dp
                )
        ) {

            Text(
                text = session.task,
                color = NexusWhite,
                fontSize = 14.sp
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "${session.duration} min • ${session.date}",
                color = NexusTextSecondary,
                fontSize = 11.sp
            )
        }

        Text(
            text = if (session.completed) {
                "Done"
            } else {
                "Ended"
            },
            color = if (session.completed) {
                NexusPrimaryLight
            } else {
                NexusTextSecondary
            },
            fontSize = 11.sp
        )
    }
}

private fun formatHistoryDate(
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