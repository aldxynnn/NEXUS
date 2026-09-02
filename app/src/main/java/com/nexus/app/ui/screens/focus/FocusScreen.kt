package com.nexus.app.ui.screens.focus

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.foundation.Canvas
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nexus.app.ui.theme.NexusBackground
import com.nexus.app.ui.theme.NexusPrimary
import com.nexus.app.ui.theme.NexusPrimaryLight
import com.nexus.app.ui.theme.NexusSurface
import com.nexus.app.ui.theme.NexusTextSecondary
import com.nexus.app.ui.theme.NexusWhite
import kotlinx.coroutines.delay

@Composable
fun FocusScreen(
    focusViewModel: FocusViewModel = viewModel(),
    onNavigate: (String) -> Unit
) {

    val state = focusViewModel.focusState

    LaunchedEffect(state) {
        if (state == FocusState.RUNNING) {
            while (focusViewModel.focusState == FocusState.RUNNING) {
                delay(1000)

                if (focusViewModel.focusState == FocusState.RUNNING) {
                    focusViewModel.tick()
                }
            }
        }
    }

    when (state) {

        FocusState.SETUP -> {
            FocusSetup(
                focusViewModel = focusViewModel
            )
        }

        FocusState.RUNNING,
        FocusState.PAUSED -> {
            FocusTimer(
                focusViewModel = focusViewModel
            )
        }

        FocusState.RESULT -> {
            FocusResult(
                focusViewModel = focusViewModel,
                onDone = {
                    focusViewModel.reset()
                    onNavigate("home")
                }
            )
        }
    }
}

// =============================================================
// SETUP
// =============================================================

@Composable
private fun FocusSetup(
    focusViewModel: FocusViewModel
) {

    val tasks by focusViewModel.tasks.collectAsStateWithLifecycle()

    val selectedDuration = focusViewModel.selectedDuration
    val selectedTask = focusViewModel.selectedTask

    var showTaskList by remember {
        mutableStateOf(false)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(NexusBackground)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {

        item {

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            // HEADER
            Text(
                text = "Focus",
                color = NexusWhite,
                fontSize = 31.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Text(
                text = "Deep work starts with one clear intention.",
                color = NexusTextSecondary,
                fontSize = 13.sp
            )

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            // SESSION PREVIEW
            FocusPreviewCard(
                task = selectedTask,
                duration = selectedDuration
            )

            Spacer(
                modifier = Modifier.height(26.dp)
            )

            Text(
                text = "FOCUS TASK",
                color = NexusTextSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            FocusOptionCard(
                title = selectedTask.ifBlank {
                    "Select a task"
                },
                subtitle = if (selectedTask.isBlank()) {
                    "Choose what you want to focus on"
                } else {
                    "Tap to change task"
                },
                selected = selectedTask.isNotBlank(),
                onClick = {
                    showTaskList = !showTaskList
                }
            )

            if (showTaskList) {

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                if (tasks.isEmpty()) {

                    EmptyFocusTasks()

                } else {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(NexusSurface)
                            .padding(vertical = 6.dp)
                    ) {

                        tasks.forEach { task ->

                            TextButton(
                                onClick = {
                                    focusViewModel.selectTask(task)
                                    showTaskList = false
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = 8.dp
                                    )
                            ) {

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Box(
                                        modifier = Modifier
                                            .size(34.dp)
                                            .clip(CircleShape)
                                            .background(
                                                NexusPrimaryLight.copy(
                                                    alpha = 0.10f
                                                )
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "○",
                                            color = NexusPrimaryLight,
                                            fontSize = 17.sp
                                        )
                                    }

                                    Spacer(
                                        modifier = Modifier.width(12.dp)
                                    )

                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {

                                        Text(
                                            text = task.title,
                                            color = NexusWhite,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Medium,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )

                                        Spacer(
                                            modifier = Modifier.height(3.dp)
                                        )

                                        Text(
                                            text = "${task.category.ifBlank { "General" }} • ${task.time.ifBlank { "--:--" }}",
                                            color = NexusTextSecondary,
                                            fontSize = 10.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(26.dp)
            )

            Text(
                text = "SESSION LENGTH",
                color = NexusTextSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                DurationOption(
                    minutes = 15,
                    selected = selectedDuration == 15,
                    onClick = {
                        focusViewModel.selectDuration(15)
                    },
                    modifier = Modifier.weight(1f)
                )

                DurationOption(
                    minutes = 25,
                    selected = selectedDuration == 25,
                    onClick = {
                        focusViewModel.selectDuration(25)
                    },
                    modifier = Modifier.weight(1f)
                )

                DurationOption(
                    minutes = 45,
                    selected = selectedDuration == 45,
                    onClick = {
                        focusViewModel.selectDuration(45)
                    },
                    modifier = Modifier.weight(1f)
                )

                DurationOption(
                    minutes = 60,
                    selected = selectedDuration == 60,
                    onClick = {
                        focusViewModel.selectDuration(60)
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            Button(
                onClick = {
                    focusViewModel.start()
                },
                enabled = selectedTask.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(17.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NexusPrimary,
                    disabledContainerColor = NexusSurface
                )
            ) {
                Text(
                    text = "Start Focus",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "Minimize distractions and give this session your full attention.",
                color = NexusTextSecondary,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(
                modifier = Modifier.height(28.dp)
            )
        }
    }
}

// =============================================================
// PREVIEW CARD
// =============================================================

@Composable
private fun FocusPreviewCard(
    task: String,
    duration: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(NexusSurface)
            .padding(18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(
                    NexusPrimaryLight.copy(alpha = 0.12f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "◉",
                color = NexusPrimaryLight,
                fontSize = 22.sp
            )
        }

        Spacer(
            modifier = Modifier.width(14.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = if (task.isBlank()) {
                    "Ready to focus"
                } else {
                    task
                },
                color = NexusWhite,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "$duration minute session",
                color = NexusTextSecondary,
                fontSize = 11.sp
            )
        }
    }
}

// =============================================================
// OPTION CARD
// =============================================================

@Composable
private fun FocusOptionCard(
    title: String,
    subtitle: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(
                if (selected) {
                    NexusPrimaryLight.copy(alpha = 0.11f)
                } else {
                    NexusSurface
                }
            )
            .clickable(
                onClick = onClick
            )
            .padding(17.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(
                    NexusPrimaryLight.copy(alpha = 0.13f)
                ),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = if (selected) "✓" else "+",
                color = NexusPrimaryLight,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.width(13.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = title,
                color = NexusWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = subtitle,
                color = NexusTextSecondary,
                fontSize = 11.sp
            )
        }

        Text(
            text = "›",
            color = NexusTextSecondary,
            fontSize = 22.sp
        )
    }
}

// =============================================================
// EMPTY TASKS
// =============================================================

@Composable
private fun EmptyFocusTasks() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(NexusSurface)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "No tasks available",
            color = NexusWhite,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(
            modifier = Modifier.height(5.dp)
        )

        Text(
            text = "Create a task first from the Tasks page.",
            color = NexusTextSecondary,
            fontSize = 11.sp,
            textAlign = TextAlign.Center
        )
    }
}

// =============================================================
// DURATION
// =============================================================

@Composable
private fun DurationOption(
    minutes: Int,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(15.dp))
            .background(
                if (selected) {
                    NexusPrimaryLight.copy(alpha = 0.18f)
                } else {
                    NexusSurface
                }
            )
            .clickable(
                onClick = onClick
            )
            .padding(
                vertical = 13.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "$minutes",
            color = if (selected) {
                NexusPrimaryLight
            } else {
                NexusWhite
            },
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(2.dp)
        )

        Text(
            text = "min",
            color = NexusTextSecondary,
            fontSize = 9.sp
        )
    }
}

// =============================================================
// TIMER
// =============================================================

@Composable
private fun FocusTimer(
    focusViewModel: FocusViewModel
) {

    val remaining = focusViewModel.remainingSeconds
    val progress = focusViewModel.progress()
    val running = focusViewModel.focusState == FocusState.RUNNING

    val minutes = remaining / 60
    val seconds = remaining % 60

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NexusBackground)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        Text(
            text = if (running) {
                "FOCUSING"
            } else {
                "PAUSED"
            },
            color = if (running) {
                NexusPrimaryLight
            } else {
                NexusTextSecondary
            },
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.4.sp
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Text(
            text = focusViewModel.selectedTask,
            color = NexusWhite,
            fontSize = 19.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(40.dp)
        )

        // TIMER RING
        Box(
            modifier = Modifier.size(285.dp),
            contentAlignment = Alignment.Center
        ) {

            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {

                drawArc(
                    color = NexusSurface,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(
                        width = 13.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                )

                drawArc(
                    color = NexusPrimaryLight,
                    startAngle = -90f,
                    sweepAngle = 360f * progress.coerceIn(0f, 1f),
                    useCenter = false,
                    style = Stroke(
                        width = 13.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = String.format(
                        "%02d:%02d",
                        minutes,
                        seconds
                    ),
                    color = NexusWhite,
                    fontSize = 47.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(7.dp)
                )

                Text(
                    text = if (running) {
                        "Stay focused"
                    } else {
                        "Take a breath"
                    },
                    color = NexusTextSecondary,
                    fontSize = 11.sp
                )
            }
        }

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(NexusSurface)
                .padding(
                    horizontal = 18.dp,
                    vertical = 14.dp
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column {
                Text(
                    text = "SESSION PROGRESS",
                    color = NexusTextSecondary,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = "${(progress * 100).toInt()}% completed",
                    color = NexusWhite,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Text(
                text = if (running) {
                    "● LIVE"
                } else {
                    "Ⅱ PAUSED"
                },
                color = if (running) {
                    NexusPrimaryLight
                } else {
                    NexusTextSecondary
                },
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.height(25.dp)
        )

        Button(
            onClick = {
                if (running) {
                    focusViewModel.pause()
                } else {
                    focusViewModel.start()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = NexusPrimary
            )
        ) {

            Text(
                text = if (running) {
                    "Pause Session"
                } else {
                    "Resume Session"
                },
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(
            modifier = Modifier.height(5.dp)
        )

        TextButton(
            onClick = {
                focusViewModel.stop()
            }
        ) {

            Text(
                text = "End Session",
                color = NexusTextSecondary,
                fontSize = 12.sp
            )
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Text(
            text = "Stay with one task until the session ends.",
            color = NexusTextSecondary,
            fontSize = 10.sp,
            textAlign = TextAlign.Center
        )
    }
}

// =============================================================
// RESULT
// =============================================================

@Composable
private fun FocusResult(
    focusViewModel: FocusViewModel,
    onDone: () -> Unit
) {

    val completed = focusViewModel.completedSession
    val minutes = focusViewModel.elapsedMinutes()
    val progress = focusViewModel.progress()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NexusBackground)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(
                    NexusPrimaryLight.copy(alpha = 0.12f)
                ),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = if (completed) "✓" else "—",
                color = NexusPrimaryLight,
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.height(25.dp)
        )

        Text(
            text = if (completed) {
                "Focus Complete"
            } else {
                "Session Ended"
            },
            color = NexusWhite,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = focusViewModel.selectedTask,
            color = NexusTextSecondary,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(
            modifier = Modifier.height(30.dp)
        )

        // STATS CARD
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(NexusSurface)
                .padding(vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            ResultStat(
                value = "$minutes",
                label = "MINUTES"
            )

            ResultStat(
                value = "${(progress * 100).toInt()}%",
                label = "PROGRESS"
            )

            ResultStat(
                value = if (completed) {
                    "DONE"
                } else {
                    "ENDED"
                },
                label = "STATUS"
            )
        }

        Spacer(
            modifier = Modifier.height(30.dp)
        )

        Text(
            text = if (completed) {
                "Great work. You completed your focus session."
            } else {
                "Every focused minute still counts."
            },
            color = NexusTextSecondary,
            fontSize = 11.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Button(
            onClick = onDone,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = NexusPrimary
            )
        ) {

            Text(
                text = "Back to Home",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// =============================================================
// RESULT STAT
// =============================================================

@Composable
private fun ResultStat(
    value: String,
    label: String
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = value,
            color = NexusWhite,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(5.dp)
        )

        Text(
            text = label,
            color = NexusTextSecondary,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.6.sp
        )
    }
}