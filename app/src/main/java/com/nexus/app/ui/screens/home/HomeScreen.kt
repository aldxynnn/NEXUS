package com.nexus.app.ui.screens.home

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nexus.app.ui.screens.home.components.BottomNavigationBar
import com.nexus.app.ui.screens.home.components.InsightCard
import com.nexus.app.ui.screens.home.components.ProductivityCard
import com.nexus.app.ui.screens.home.components.TodayTask
import com.nexus.app.ui.screens.home.components.TodaysPlanCard
import com.nexus.app.ui.screens.insights.InsightsViewModel
import com.nexus.app.ui.screens.tasks.TaskViewModel
import com.nexus.app.ui.theme.NexusBackground
import com.nexus.app.ui.theme.NexusPrimaryLight
import com.nexus.app.ui.theme.NexusSurface
import com.nexus.app.ui.theme.NexusTextSecondary
import com.nexus.app.ui.theme.NexusWhite

@Composable
fun HomeScreen(
    onNavigate: (String) -> Unit,
    userName: String,
    taskViewModel: TaskViewModel = viewModel(),
    insightsViewModel: InsightsViewModel = viewModel()
) {
    val tasks by taskViewModel.tasks.collectAsState()
    val sessions by insightsViewModel.sessions.collectAsState()

    val todayTasks = tasks
        .filter {
            it.day == "Today"
        }
        .sortedBy {
            it.time
        }
        .map {
            TodayTask(
                time = it.time,
                title = it.title,
                category = it.category,
                completed = it.completed
            )
        }

    val productivityScore =
        insightsViewModel.productivityScore()

    val remainingTasks =
        tasks.count {
            !it.completed
        }

    val completedTasks =
        tasks.count {
            it.completed
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NexusBackground)
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    horizontal = 20.dp,
                    vertical = 22.dp
                )
        ) {

            // =================================================
            // HEADER
            // =================================================

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "Good morning 👋",
                        color = NexusTextSecondary,
                        fontSize = 13.sp
                    )

                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )

                    Text(
                        text = userName.ifBlank {
                            "User"
                        },
                        color = NexusWhite,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = "Let's make today productive.",
                        color = NexusTextSecondary,
                        fontSize = 13.sp
                    )
                }

                Spacer(
                    modifier = Modifier.height(1.dp)
                )

                // PROFILE BUTTON

                Column(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(
                            NexusSurface
                        )
                        .clickable {
                            onNavigate("profile")
                        }
                        .padding(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Column(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(
                                NexusPrimaryLight.copy(
                                    alpha = 0.15f
                                )
                            )
                            .padding(11.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Text(
                            text = "●",
                            color = NexusPrimaryLight,
                            fontSize = 19.sp
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            // =================================================
            // PRODUCTIVITY SCORE
            // =================================================

            ProductivityCard(
                score = productivityScore
            )

            Spacer(
                modifier = Modifier.height(22.dp)
            )

            // =================================================
            // TODAY'S PLAN
            // =================================================

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {

                    Text(
                        text = "Today's Plan",
                        color = NexusWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(3.dp)
                    )

                    Text(
                        text = if (todayTasks.isEmpty()) {
                            "No tasks planned for today"
                        } else {
                            "${todayTasks.size} task(s) planned"
                        },
                        color = NexusTextSecondary,
                        fontSize = 12.sp
                    )
                }

                if (todayTasks.isNotEmpty()) {
                    Text(
                        text = "View all",
                        color = NexusPrimaryLight,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable {
                            onNavigate("tasks")
                        }
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            TodaysPlanCard(
                tasks = todayTasks,
                onSeeAll = {
                    onNavigate("tasks")
                }
            )

            Spacer(
                modifier = Modifier.height(22.dp)
            )

            // =================================================
            // INSIGHTS
            // =================================================

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {

                    Text(
                        text = "Your Progress",
                        color = NexusWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(3.dp)
                    )

                    Text(
                        text = "A quick look at your activity",
                        color = NexusTextSecondary,
                        fontSize = 12.sp
                    )
                }

                Text(
                    text = "Insights",
                    color = NexusPrimaryLight,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable {
                        onNavigate("insights")
                    }
                )
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            InsightCard(
                remainingTasks = remainingTasks,
                completedTasks = completedTasks,
                sessionCount = sessions.size,
                onClick = {
                    onNavigate("insights")
                }
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )
        }

        // =====================================================
        // BOTTOM NAVIGATION
        // =====================================================

        BottomNavigationBar(
            currentRoute = "home",
            onNavigate = onNavigate
        )
    }
}