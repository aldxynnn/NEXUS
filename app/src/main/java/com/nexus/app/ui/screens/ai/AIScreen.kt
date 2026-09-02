package com.nexus.app.ui.screens.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nexus.app.data.local.TaskEntity
import com.nexus.app.ui.screens.tasks.TaskViewModel
import com.nexus.app.ui.theme.NexusBackground
import com.nexus.app.ui.theme.NexusPrimary
import com.nexus.app.ui.theme.NexusPrimaryLight
import com.nexus.app.ui.theme.NexusSurface
import com.nexus.app.ui.theme.NexusTextSecondary
import com.nexus.app.ui.theme.NexusWhite

@Composable
fun AIScreen(
    aiViewModel: AIViewModel = viewModel(),
    taskViewModel: TaskViewModel = viewModel()
) {
    val selectedTab = aiViewModel.selectedTab
    val tasks by taskViewModel.tasks.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NexusBackground)
            .navigationBarsPadding()
    ) {
        AIHeader()

        AITabRow(
            selectedTab = selectedTab,
            onTabSelected = {
                aiViewModel.selectTab(it)
            }
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        when (selectedTab) {
            AITab.CHAT -> {
                ChatContent(
                    viewModel = aiViewModel
                )
            }

            AITab.PLANNER -> {
                PlannerContent(
                    viewModel = aiViewModel,
                    taskViewModel = taskViewModel
                )
            }

            AITab.INSIGHTS -> {
                AIInsightsContent(
                    viewModel = aiViewModel,
                    tasks = tasks
                )
            }
        }
    }
}

@Composable
private fun AIHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 20.dp,
                end = 20.dp,
                top = 22.dp,
                bottom = 12.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.linearGradient(
                        listOf(
                            NexusPrimary,
                            NexusPrimaryLight
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "✦",
                color = NexusWhite,
                fontSize = 24.sp
            )
        }

        Spacer(
            modifier = Modifier.width(13.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Nexus AI",
                color = NexusWhite,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(2.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(NexusPrimaryLight)
                )

                Spacer(
                    modifier = Modifier.width(6.dp)
                )

                Text(
                    text = "Your productivity copilot",
                    color = NexusTextSecondary,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
private fun AITabRow(
    selectedTab: AITab,
    onTabSelected: (AITab) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(NexusSurface)
            .padding(5.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        AITabItem(
            icon = "AI",
            text = "Chat",
            selected = selectedTab == AITab.CHAT,
            modifier = Modifier.weight(1f),
            onClick = {
                onTabSelected(AITab.CHAT)
            }
        )

        AITabItem(
            icon = "☷",
            text = "Planner",
            selected = selectedTab == AITab.PLANNER,
            modifier = Modifier.weight(1f),
            onClick = {
                onTabSelected(AITab.PLANNER)
            }
        )

        AITabItem(
            icon = "✦",
            text = "Insights",
            selected = selectedTab == AITab.INSIGHTS,
            modifier = Modifier.weight(1f),
            onClick = {
                onTabSelected(AITab.INSIGHTS)
            }
        )
    }
}

@Composable
private fun AITabItem(
    icon: String,
    text: String,
    selected: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (selected) {
                    NexusPrimary.copy(alpha = 0.18f)
                } else {
                    Color.Transparent
                }
            )
            .clickable {
                onClick()
            }
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = icon,
            color = if (selected) {
                NexusPrimaryLight
            } else {
                NexusTextSecondary
            },
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.width(6.dp)
        )

        Text(
            text = text,
            color = if (selected) {
                NexusWhite
            } else {
                NexusTextSecondary
            },
            fontSize = 12.sp,
            fontWeight = if (selected) {
                FontWeight.SemiBold
            } else {
                FontWeight.Normal
            }
        )
    }
}

@Composable
private fun ChatContent(
    viewModel: AIViewModel
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(
                top = 4.dp,
                bottom = 12.dp
            )
        ) {
            if (viewModel.messages.size == 1) {
                item {
                    ChatWelcome(
                        onPromptClick = {
                            viewModel.updateInput(it)
                        }
                    )
                }
            }

            itemsIndexed(
                items = viewModel.messages
            ) { _, message ->
                MessageBubble(
                    message = message
                )
            }

            if (viewModel.isLoading) {
                item {
                    AIThinkingBubble()
                }
            }
        }

        ChatInput(
            value = viewModel.inputText,
            onValueChange = {
                viewModel.updateInput(it)
            },
            onSend = {
                viewModel.sendMessage()
            },
            enabled = !viewModel.isLoading
        )
    }
}

@Composable
private fun ChatWelcome(
    onPromptClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.linearGradient(
                        listOf(
                            NexusPrimary.copy(alpha = 0.30f),
                            NexusSurface
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(
                            NexusPrimaryLight.copy(alpha = 0.15f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✦",
                        color = NexusPrimaryLight,
                        fontSize = 21.sp
                    )
                }

                Spacer(
                    modifier = Modifier.height(15.dp)
                )

                Text(
                    text = "What can I help you\nwith today?",
                    color = NexusWhite,
                    fontSize = 22.sp,
                    lineHeight = 27.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "Ask me to organize your tasks, build a plan, or improve your productivity.",
                    color = NexusTextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )
            }
        }

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        Text(
            text = "TRY ASKING",
            color = NexusTextSecondary,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.sp
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        QuickPrompt(
            text = "Help me plan my day",
            onClick = {
                onPromptClick("Help me plan my day")
            }
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        QuickPrompt(
            text = "How can I stay focused?",
            onClick = {
                onPromptClick("How can I stay focused?")
            }
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        QuickPrompt(
            text = "Break my project into tasks",
            onClick = {
                onPromptClick("Break my project into tasks")
            }
        )
    }
}

@Composable
private fun QuickPrompt(
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(NexusSurface)
            .clickable {
                onClick()
            }
            .padding(
                horizontal = 15.dp,
                vertical = 13.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "✦",
            color = NexusPrimaryLight,
            fontSize = 14.sp
        )

        Spacer(
            modifier = Modifier.width(10.dp)
        )

        Text(
            text = text,
            color = NexusWhite,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun MessageBubble(
    message: AIMessage
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isUser) {
            Arrangement.End
        } else {
            Arrangement.Start
        }
    ) {
        if (!message.isUser) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(
                        NexusPrimary.copy(alpha = 0.15f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "✦",
                    color = NexusPrimaryLight,
                    fontSize = 14.sp
                )
            }

            Spacer(
                modifier = Modifier.width(8.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(
                    if (message.isUser) {
                        0.82f
                    } else {
                        0.86f
                    }
                )
                .clip(
                    RoundedCornerShape(
                        topStart = 18.dp,
                        topEnd = 18.dp,
                        bottomStart = if (message.isUser) {
                            18.dp
                        } else {
                            5.dp
                        },
                        bottomEnd = if (message.isUser) {
                            5.dp
                        } else {
                            18.dp
                        }
                    )
                )
                .background(
                    if (message.isUser) {
                        NexusPrimary
                    } else {
                        NexusSurface
                    }
                )
                .padding(15.dp)
        ) {
            Text(
                text = if (message.isUser) {
                    "You"
                } else {
                    "Nexus AI"
                },
                color = if (message.isUser) {
                    NexusWhite.copy(alpha = 0.70f)
                } else {
                    NexusPrimaryLight
                },
                fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Text(
                text = message.text,
                color = NexusWhite,
                fontSize = 13.sp,
                lineHeight = 19.sp
            )
        }
    }
}

@Composable
private fun AIThinkingBubble() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(
                    NexusPrimary.copy(alpha = 0.15f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "✦",
                color = NexusPrimaryLight,
                fontSize = 14.sp
            )
        }

        Spacer(
            modifier = Modifier.width(9.dp)
        )

        Text(
            text = "Nexus AI is thinking...",
            color = NexusTextSecondary,
            fontSize = 11.sp
        )
    }
}

@Composable
private fun ChatInput(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    enabled: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 10.dp
            ),
        verticalAlignment = Alignment.Bottom
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            placeholder = {
                Text(
                    text = "Ask Nexus AI...",
                    color = NexusTextSecondary,
                    fontSize = 12.sp
                )
            },
            modifier = Modifier.weight(1f),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = NexusSurface,
                unfocusedContainerColor = NexusSurface,
                disabledContainerColor = NexusSurface,
                focusedBorderColor = NexusPrimaryLight,
                unfocusedBorderColor = NexusSurface,
                focusedTextColor = NexusWhite,
                unfocusedTextColor = NexusWhite,
                cursorColor = NexusPrimaryLight
            ),
            shape = RoundedCornerShape(20.dp),
            maxLines = 3
        )

        Spacer(
            modifier = Modifier.width(8.dp)
        )

        Button(
            onClick = onSend,
            enabled = enabled && value.isNotBlank(),
            modifier = Modifier.size(52.dp),
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = NexusPrimary,
                disabledContainerColor = NexusSurface
            )
        ) {
            Text(
                text = "↑",
                color = if (enabled && value.isNotBlank()) {
                    NexusWhite
                } else {
                    NexusTextSecondary
                },
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun PlannerContent(
    viewModel: AIViewModel,
    taskViewModel: TaskViewModel
) {
    when {
        viewModel.plannerLoading -> {
            PlannerLoading()
        }

        !viewModel.plannerGenerated -> {
            PlannerInput(
                value = viewModel.plannerInput,
                onValueChange = {
                    viewModel.updatePlannerInput(it)
                },
                onGenerate = {
                    viewModel.generatePlan()
                }
            )

            viewModel.plannerError?.let { error ->
                Text(
                    text = error,
                    color = NexusTextSecondary,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(
                        horizontal = 20.dp,
                        vertical = 8.dp
                    )
                )
            }
        }

        else -> {
            PlannerResult(
                viewModel = viewModel,
                onAddPlan = {
                    taskViewModel.addPlanFromAI(
                        viewModel.generatedPlan
                    )

                    viewModel.clearPlan()
                }
            )
        }
    }
}

@Composable
private fun PlannerLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "✦",
                color = NexusPrimaryLight,
                fontSize = 32.sp
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "Nexus AI is building your plan...",
                color = NexusWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "Please wait a moment.",
                color = NexusTextSecondary,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun PlannerInput(
    value: String,
    onValueChange: (String) -> Unit,
    onGenerate: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                NexusPrimary.copy(alpha = 0.28f),
                                NexusSurface
                            )
                        )
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Text(
                        text = "☷",
                        color = NexusPrimaryLight,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(14.dp)
                    )

                    Text(
                        text = "Build your perfect plan.",
                        color = NexusWhite,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(7.dp)
                    )

                    Text(
                        text = "Tell Nexus what you want to accomplish and AI will organize it into practical steps.",
                        color = NexusTextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        item {
            Text(
                text = "YOUR GOAL",
                color = NexusTextSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp
            )
        }

        item {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(145.dp),
                placeholder = {
                    Text(
                        text = "Example: Finish my Android project this week",
                        color = NexusTextSecondary,
                        fontSize = 12.sp
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = NexusSurface,
                    unfocusedContainerColor = NexusSurface,
                    focusedBorderColor = NexusPrimaryLight,
                    unfocusedBorderColor = NexusSurface,
                    focusedTextColor = NexusWhite,
                    unfocusedTextColor = NexusWhite,
                    cursorColor = NexusPrimaryLight
                ),
                shape = RoundedCornerShape(18.dp)
            )
        }

        item {
            Text(
                text = "QUICK GOALS",
                color = NexusTextSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PlannerSuggestion(
                    text = "Study",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onValueChange(
                            "Create a focused study plan for today"
                        )
                    }
                )

                PlannerSuggestion(
                    text = "Project",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onValueChange(
                            "Break my project into manageable tasks"
                        )
                    }
                )

                PlannerSuggestion(
                    text = "Work",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onValueChange(
                            "Organize my workday efficiently"
                        )
                    }
                )
            }
        }

        item {
            Button(
                onClick = onGenerate,
                enabled = value.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NexusPrimary
                ),
                shape = RoundedCornerShape(17.dp)
            ) {
                Text(
                    text = "✦",
                    fontSize = 16.sp
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Text(
                    text = "Generate AI Plan",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun PlannerSuggestion(
    text: String,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(NexusSurface)
            .clickable {
                onClick()
            }
            .padding(vertical = 11.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = NexusWhite,
            fontSize = 11.sp
        )
    }
}

@Composable
private fun PlannerResult(
    viewModel: AIViewModel,
    onAddPlan: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Your AI Plan",
                        color = NexusWhite,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = "A practical structure generated for you.",
                        color = NexusTextSecondary,
                        fontSize = 11.sp
                    )
                }

                TextButton(
                    onClick = {
                        viewModel.clearPlan()
                    }
                ) {
                    Text(
                        text = "Reset",
                        color = NexusPrimaryLight
                    )
                }
            }
        }

        itemsIndexed(
            items = viewModel.generatedPlan
        ) { index, task ->
            PlannerTaskCard(
                number = index + 1,
                task = task
            )
        }

        item {
            Button(
                onClick = onAddPlan,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NexusPrimary
                ),
                shape = RoundedCornerShape(17.dp)
            ) {
                Text(
                    text = "✓",
                    fontSize = 16.sp
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Text(
                    text = "Add Plan to Tasks",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun PlannerTaskCard(
    number: Int,
    task: AIPlanTask
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(NexusSurface)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(
                    NexusPrimary.copy(alpha = 0.16f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$number",
                color = NexusPrimaryLight,
                fontSize = 13.sp,
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
                text = task.title,
                color = NexusWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Text(
                text = "${task.time} • ${task.duration}",
                color = NexusTextSecondary,
                fontSize = 10.sp
            )

            Spacer(
                modifier = Modifier.height(3.dp)
            )

            Text(
                text = task.category,
                color = NexusPrimaryLight,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
private fun AIInsightsContent(
    viewModel: AIViewModel,
    tasks: List<TaskEntity>
) {
    LaunchedEffect(tasks) {
        viewModel.generateInsights(tasks)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                NexusPrimary.copy(alpha = 0.30f),
                                NexusSurface
                            )
                        )
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Text(
                        text = "✦",
                        color = NexusPrimaryLight,
                        fontSize = 27.sp
                    )

                    Spacer(
                        modifier = Modifier.height(13.dp)
                    )

                    Text(
                        text = "Understand how\nyou work best.",
                        color = NexusWhite,
                        fontSize = 22.sp,
                        lineHeight = 27.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(7.dp)
                    )

                    Text(
                        text = "Nexus analyzes your actual tasks and turns them into useful recommendations.",
                        color = NexusTextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        when {
            viewModel.insightsLoading -> {
                item {
                    InsightsLoading()
                }
            }

            viewModel.insightsError != null -> {
                item {
                    InsightsError(
                        message = viewModel.insightsError
                            ?: "Unknown error"
                    )
                }
            }

            viewModel.insights.isEmpty() -> {
                item {
                    InsightsEmpty(
                        taskCount = tasks.size
                    )
                }
            }

            else -> {
                itemsIndexed(
                    items = viewModel.insights
                ) { _, insight ->
                    AIInsightCardItem(
                        title = insight.title,
                        value = insight.value,
                        description = insight.description
                    )
                }
            }
        }
    }
}

@Composable
private fun InsightsLoading() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 30.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "✦",
                color = NexusPrimaryLight,
                fontSize = 30.sp
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = "Nexus AI is analyzing your tasks...",
                color = NexusWhite,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun InsightsError(
    message: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(NexusSurface)
            .padding(18.dp)
    ) {
        Column {
            Text(
                text = "AI Insights unavailable",
                color = NexusWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = message,
                color = NexusTextSecondary,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun InsightsEmpty(
    taskCount: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(NexusSurface)
            .padding(18.dp)
    ) {
        Column {
            Text(
                text = "Not enough data yet",
                color = NexusWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = if (taskCount == 0) {
                    "Add some tasks first so Nexus AI can analyze your productivity."
                } else {
                    "Nexus AI has not generated insights from your current tasks yet."
                },
                color = NexusTextSecondary,
                fontSize = 11.sp,
                lineHeight = 17.sp
            )
        }
    }
}

@Composable
private fun AIInsightCardItem(
    title: String,
    value: String,
    description: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(NexusSurface)
            .padding(18.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(NexusPrimaryLight)
            )

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Text(
                text = title,
                color = NexusTextSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.8.sp
            )
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Text(
            text = value,
            color = NexusWhite,
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(7.dp)
        )

        Text(
            text = description,
            color = NexusTextSecondary,
            fontSize = 12.sp,
            lineHeight = 18.sp
        )
    }
}