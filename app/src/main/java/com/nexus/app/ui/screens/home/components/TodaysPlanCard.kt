package com.nexus.app.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexus.app.ui.theme.NexusPrimaryLight
import com.nexus.app.ui.theme.NexusSurface
import com.nexus.app.ui.theme.NexusTextSecondary
import com.nexus.app.ui.theme.NexusWhite

data class TodayTask(
    val time: String,
    val title: String,
    val category: String,
    val completed: Boolean = false
)

@Composable
fun TodaysPlanCard(
    tasks: List<TodayTask>,
    onSeeAll: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(20.dp)
            )
            .background(NexusSurface)
            .padding(20.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceBetween,
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Text(
                text = "Today's Plan",
                color = NexusWhite,
                fontSize = 18.sp
            )

            androidx.compose.material3.TextButton(
                onClick = onSeeAll
            ) {

                Text(
                    text = "See all",
                    color = NexusPrimaryLight,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        if (tasks.isEmpty()) {

            Text(
                text = "No tasks planned for today.",
                color = NexusTextSecondary,
                fontSize = 13.sp
            )

        } else {

            tasks.take(5).forEachIndexed { index, task ->

                TaskRow(
                    task = task
                )

                if (index < tasks.take(5).lastIndex) {

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun TaskRow(
    task: TodayTask
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Text(
            text = task.time,
            color = NexusTextSecondary,
            fontSize = 12.sp,
            modifier = Modifier.weight(0.25f)
        )

        Column(
            modifier = Modifier.weight(0.75f)
        ) {

            Text(
                text = task.title,
                color = if (task.completed) {
                    NexusTextSecondary
                } else {
                    NexusWhite
                },
                fontSize = 14.sp
            )

            Spacer(
                modifier = Modifier.height(3.dp)
            )

            Text(
                text = if (task.completed) {
                    "${task.category} • Completed"
                } else {
                    task.category
                },
                color = if (task.completed) {
                    NexusPrimaryLight
                } else {
                    NexusTextSecondary
                },
                fontSize = 11.sp
            )
        }
    }
}