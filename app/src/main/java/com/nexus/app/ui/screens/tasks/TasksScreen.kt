package com.nexus.app.ui.screens.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nexus.app.data.local.TaskEntity
import com.nexus.app.ui.theme.NexusBackground
import com.nexus.app.ui.theme.NexusPrimary
import com.nexus.app.ui.theme.NexusPrimaryLight
import com.nexus.app.ui.theme.NexusSurface
import com.nexus.app.ui.theme.NexusTextSecondary
import com.nexus.app.ui.theme.NexusWhite

enum class TaskFilter {
    ALL,
    TODAY,
    UPCOMING,
    COMPLETED
}

@Composable
fun TasksScreen(
    onBack: () -> Unit,
    taskViewModel: TaskViewModel = viewModel()
) {
    val allTasks by taskViewModel.tasks.collectAsStateWithLifecycle()

    var selectedFilter by remember {
        mutableStateOf(TaskFilter.ALL)
    }

    var showCreateTask by remember {
        mutableStateOf(false)
    }

    var selectedTask by remember {
        mutableStateOf<TaskEntity?>(null)
    }

    if (showCreateTask) {
        CreateTaskScreen(
            onBack = {
                showCreateTask = false
            },
            onCreate = { title, category, time, day ->
                taskViewModel.addTask(
                    title = title,
                    category = category,
                    time = time,
                    day = day
                )

                showCreateTask = false
            }
        )

        return
    }

    val filteredTasks = when (selectedFilter) {
        TaskFilter.ALL -> allTasks

        TaskFilter.TODAY -> {
            allTasks.filter {
                it.day == "Today"
            }
        }

        TaskFilter.UPCOMING -> {
            allTasks.filter {
                it.day == "Upcoming"
            }
        }

        TaskFilter.COMPLETED -> {
            allTasks.filter {
                it.completed
            }
        }
    }

    val remainingTasks = allTasks.count {
        !it.completed
    }

    val completedTasks = allTasks.count {
        it.completed
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NexusBackground)
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {

            item {
                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                // HEADER
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(NexusSurface)
                            .clickable {
                                onBack()
                            },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "‹",
                            color = NexusWhite,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Light
                        )
                    }

                    Spacer(
                        modifier = Modifier.width(14.dp)
                    )

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Tasks",
                            color = NexusWhite,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

                        Text(
                            text = if (remainingTasks == 0) {
                                "You're all caught up"
                            } else {
                                "$remainingTasks tasks remaining"
                            },
                            color = NexusTextSecondary,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(
                        modifier = Modifier.width(12.dp)
                    )

                    // ADD BUTTON
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(15.dp))
                            .background(
                                NexusPrimaryLight.copy(alpha = 0.14f)
                            )
                            .clickable {
                                showCreateTask = true
                            }
                            .padding(
                                horizontal = 15.dp,
                                vertical = 9.dp
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "+",
                            color = NexusPrimaryLight,
                            fontSize = 21.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Add",
                            color = NexusPrimaryLight,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(22.dp)
                )

                // SUMMARY
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TaskSummaryCard(
                        value = allTasks.size.toString(),
                        label = "TOTAL",
                        modifier = Modifier.weight(1f)
                    )

                    TaskSummaryCard(
                        value = remainingTasks.toString(),
                        label = "PENDING",
                        modifier = Modifier.weight(1f)
                    )

                    TaskSummaryCard(
                        value = completedTasks.toString(),
                        label = "DONE",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(
                    modifier = Modifier.height(22.dp)
                )

                // FILTER
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(NexusSurface)
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {

                    FilterChip(
                        text = "All",
                        selected = selectedFilter == TaskFilter.ALL,
                        onClick = {
                            selectedFilter = TaskFilter.ALL
                        },
                        modifier = Modifier.weight(1f)
                    )

                    FilterChip(
                        text = "Today",
                        selected = selectedFilter == TaskFilter.TODAY,
                        onClick = {
                            selectedFilter = TaskFilter.TODAY
                        },
                        modifier = Modifier.weight(1f)
                    )

                    FilterChip(
                        text = "Upcoming",
                        selected = selectedFilter == TaskFilter.UPCOMING,
                        onClick = {
                            selectedFilter = TaskFilter.UPCOMING
                        },
                        modifier = Modifier.weight(1f)
                    )

                    FilterChip(
                        text = "Done",
                        selected = selectedFilter == TaskFilter.COMPLETED,
                        onClick = {
                            selectedFilter = TaskFilter.COMPLETED
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(
                    modifier = Modifier.height(22.dp)
                )

                if (filteredTasks.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = when (selectedFilter) {
                                TaskFilter.ALL -> "ALL TASKS"
                                TaskFilter.TODAY -> "TODAY"
                                TaskFilter.UPCOMING -> "UPCOMING"
                                TaskFilter.COMPLETED -> "COMPLETED"
                            },
                            color = NexusTextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )

                        Spacer(
                            modifier = Modifier.weight(1f)
                        )

                        Text(
                            text = "${filteredTasks.size}",
                            color = NexusPrimaryLight,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )
                }
            }

            if (filteredTasks.isEmpty()) {

                item {
                    EmptyTasks(
                        filter = selectedFilter,
                        onAddTask = {
                            showCreateTask = true
                        }
                    )
                }

            } else {

                items(
                    items = filteredTasks,
                    key = {
                        it.id
                    }
                ) { task ->

                    TaskCard(
                        task = task,
                        onToggle = {
                            taskViewModel.toggleTask(task)
                        },
                        onOpen = {
                            selectedTask = task
                        }
                    )

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )
                }

                item {
                    Spacer(
                        modifier = Modifier.height(24.dp)
                    )
                }
            }
        }
    }

    selectedTask?.let { task ->

        TaskDetailDialog(
            task = task,

            onDismiss = {
                selectedTask = null
            },

            onEdit = { updatedTask ->
                taskViewModel.updateTask(updatedTask)
                selectedTask = null
            },

            onDelete = {
                taskViewModel.deleteTask(task)
                selectedTask = null
            }
        )
    }
}

@Composable
private fun TaskSummaryCard(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(NexusSurface)
            .padding(
                vertical = 15.dp,
                horizontal = 10.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = value,
            color = NexusWhite,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = label,
            color = NexusTextSecondary,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp
        )
    }
}

@Composable
private fun FilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (selected) {
                    NexusPrimaryLight.copy(alpha = 0.20f)
                } else {
                    NexusSurface
                }
            )
            .clickable {
                onClick()
            }
            .padding(
                vertical = 10.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            color = if (selected) {
                NexusWhite
            } else {
                NexusTextSecondary
            },
            fontSize = 11.sp,
            fontWeight = if (selected) {
                FontWeight.SemiBold
            } else {
                FontWeight.Normal
            }
        )
    }
}

@Composable
private fun TaskCard(
    task: TaskEntity,
    onToggle: () -> Unit,
    onOpen: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(NexusSurface)
            .clickable {
                onOpen()
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // CHECK BUTTON
        Column(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(
                    if (task.completed) {
                        NexusPrimaryLight.copy(alpha = 0.18f)
                    } else {
                        NexusBackground
                    }
                )
                .clickable {
                    onToggle()
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (task.completed) "✓" else "",
                color = NexusPrimaryLight,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.width(14.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = task.title,
                color = if (task.completed) {
                    NexusTextSecondary
                } else {
                    NexusWhite
                },
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(
                modifier = Modifier.height(7.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = task.category.ifBlank {
                        "General"
                    },
                    color = NexusPrimaryLight,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "  •  ",
                    color = NexusTextSecondary,
                    fontSize = 9.sp
                )

                Text(
                    text = task.time.ifBlank {
                        "--:--"
                    },
                    color = NexusTextSecondary,
                    fontSize = 10.sp
                )
            }
        }

        Spacer(
            modifier = Modifier.width(10.dp)
        )

        Column(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(NexusBackground),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "›",
                color = NexusTextSecondary,
                fontSize = 21.sp
            )
        }
    }
}

@Composable
private fun EmptyTasks(
    filter: TaskFilter,
    onAddTask: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 65.dp,
                bottom = 50.dp,
                start = 20.dp,
                end = 20.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(
                    NexusPrimaryLight.copy(alpha = 0.10f)
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (filter == TaskFilter.COMPLETED) {
                    "✓"
                } else {
                    "+"
                },
                color = NexusPrimaryLight,
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        Text(
            text = when (filter) {
                TaskFilter.ALL -> "No tasks yet"
                TaskFilter.TODAY -> "No tasks for today"
                TaskFilter.UPCOMING -> "No upcoming tasks"
                TaskFilter.COMPLETED -> "No completed tasks"
            },
            color = NexusWhite,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = when (filter) {
                TaskFilter.ALL -> "Create your first task to get started."
                TaskFilter.TODAY -> "Your schedule is clear for today."
                TaskFilter.UPCOMING -> "You don't have any upcoming tasks."
                TaskFilter.COMPLETED -> "Completed tasks will appear here."
            },
            color = NexusTextSecondary,
            fontSize = 13.sp
        )

        if (filter == TaskFilter.ALL) {

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            TextButton(
                onClick = onAddTask
            ) {
                Text(
                    text = "+ Create your first task",
                    color = NexusPrimaryLight,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun CreateTaskScreen(
    onBack: () -> Unit,
    onCreate: (
        String,
        String,
        String,
        String
    ) -> Unit
) {
    var title by remember {
        mutableStateOf("")
    }

    var category by remember {
        mutableStateOf("")
    }

    var time by remember {
        mutableStateOf("")
    }

    var day by remember {
        mutableStateOf("Today")
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(NexusSurface)
                    .clickable {
                        onBack()
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "‹",
                    color = NexusWhite,
                    fontSize = 28.sp
                )
            }

            Spacer(
                modifier = Modifier.width(14.dp)
            )

            Column {
                Text(
                    text = "Create Task",
                    color = NexusWhite,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = "Add something you want to accomplish.",
                    color = NexusTextSecondary,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(
            modifier = Modifier.height(30.dp)
        )

        Text(
            text = "TASK DETAILS",
            color = NexusTextSecondary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        OutlinedTextField(
            value = title,
            onValueChange = {
                title = it
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Task title")
            },
            placeholder = {
                Text("What needs to be done?")
            },
            singleLine = true,
            shape = RoundedCornerShape(15.dp)
        )

        Spacer(
            modifier = Modifier.height(13.dp)
        )

        OutlinedTextField(
            value = category,
            onValueChange = {
                category = it
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Category")
            },
            placeholder = {
                Text("Work, Study, Personal...")
            },
            singleLine = true,
            shape = RoundedCornerShape(15.dp)
        )

        Spacer(
            modifier = Modifier.height(13.dp)
        )

        OutlinedTextField(
            value = time,
            onValueChange = {
                time = it
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Time")
            },
            placeholder = {
                Text("Example: 20:00")
            },
            singleLine = true,
            shape = RoundedCornerShape(15.dp)
        )

        Spacer(
            modifier = Modifier.height(25.dp)
        )

        Text(
            text = "DAY",
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
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            DayButton(
                text = "Today",
                selected = day == "Today",
                onClick = {
                    day = "Today"
                },
                modifier = Modifier.weight(1f)
            )

            DayButton(
                text = "Upcoming",
                selected = day == "Upcoming",
                onClick = {
                    day = "Upcoming"
                },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        Button(
            onClick = {
                onCreate(
                    title,
                    category,
                    time,
                    day
                )
            },
            enabled = title.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(15.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = NexusPrimary,
                disabledContainerColor = NexusSurface
            )
        ) {
            Text(
                text = "Create Task",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun DayButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(
                if (selected) {
                    NexusPrimaryLight.copy(alpha = 0.18f)
                } else {
                    NexusSurface
                }
            )
            .clickable {
                onClick()
            }
            .padding(
                vertical = 13.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            color = if (selected) {
                NexusWhite
            } else {
                NexusTextSecondary
            },
            fontSize = 13.sp,
            fontWeight = if (selected) {
                FontWeight.SemiBold
            } else {
                FontWeight.Normal
            }
        )
    }
}

@Composable
private fun TaskDetailDialog(
    task: TaskEntity,
    onDismiss: () -> Unit,
    onEdit: (TaskEntity) -> Unit,
    onDelete: () -> Unit
) {
    var editing by remember {
        mutableStateOf(false)
    }

    var title by remember {
        mutableStateOf(task.title)
    }

    var category by remember {
        mutableStateOf(task.category)
    }

    var time by remember {
        mutableStateOf(task.time)
    }

    if (editing) {

        AlertDialog(
            onDismissRequest = onDismiss,

            title = {
                Text(
                    text = "Edit Task",
                    fontWeight = FontWeight.Bold
                )
            },

            text = {
                Column {

                    OutlinedTextField(
                        value = title,
                        onValueChange = {
                            title = it
                        },
                        label = {
                            Text("Title")
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    OutlinedTextField(
                        value = category,
                        onValueChange = {
                            category = it
                        },
                        label = {
                            Text("Category")
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    OutlinedTextField(
                        value = time,
                        onValueChange = {
                            time = it
                        },
                        label = {
                            Text("Time")
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },

            confirmButton = {
                TextButton(
                    onClick = {
                        onEdit(
                            task.copy(
                                title = title.trim(),
                                category = category.trim(),
                                time = time.trim()
                            )
                        )
                    },
                    enabled = title.isNotBlank()
                ) {
                    Text(
                        text = "Save",
                        color = NexusPrimaryLight,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },

            dismissButton = {
                TextButton(
                    onClick = onDismiss
                ) {
                    Text("Cancel")
                }
            }
        )

    } else {

        AlertDialog(
            onDismissRequest = onDismiss,

            title = {
                Text(
                    text = task.title,
                    fontWeight = FontWeight.Bold
                )
            },

            text = {
                Column {

                    DetailRow(
                        label = "Category",
                        value = task.category.ifBlank {
                            "General"
                        }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(
                            vertical = 10.dp
                        )
                    )

                    DetailRow(
                        label = "Time",
                        value = task.time.ifBlank {
                            "--:--"
                        }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(
                            vertical = 10.dp
                        )
                    )

                    DetailRow(
                        label = "Day",
                        value = task.day
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(
                            vertical = 10.dp
                        )
                    )

                    DetailRow(
                        label = "Status",
                        value = if (task.completed) {
                            "Completed"
                        } else {
                            "Pending"
                        }
                    )
                }
            },

            confirmButton = {
                TextButton(
                    onClick = {
                        editing = true
                    }
                ) {
                    Text(
                        text = "Edit",
                        color = NexusPrimaryLight,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },

            dismissButton = {
                Row {

                    TextButton(
                        onClick = onDelete
                    ) {
                        Text(
                            text = "Delete"
                        )
                    }

                    TextButton(
                        onClick = onDismiss
                    ) {
                        Text("Close")
                    }
                }
            }
        )
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = label,
            color = NexusTextSecondary,
            fontSize = 13.sp
        )

        Spacer(
            modifier = Modifier.width(16.dp)
        )

        Text(
            text = value,
            color = NexusWhite,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}