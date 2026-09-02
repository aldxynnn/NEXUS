package com.nexus.app.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexus.app.ui.theme.NexusPrimaryLight
import com.nexus.app.ui.theme.NexusSurface
import com.nexus.app.ui.theme.NexusTextSecondary
import com.nexus.app.ui.theme.NexusWhite

@Composable
fun InsightCard(
    remainingTasks: Int,
    completedTasks: Int,
    sessionCount: Int,
    onClick: () -> Unit
) {

    val title =
        when {
            remainingTasks == 0 &&
                    completedTasks > 0 ->
                "All tasks completed"

            remainingTasks > 0 ->
                "$remainingTasks task(s) remaining"

            else ->
                "Start planning your day"
        }

    val message =
        when {
            remainingTasks > 0 &&
                    sessionCount > 0 ->
                "You have focus activity recorded. Continue with your remaining tasks."

            remainingTasks > 0 ->
                "Choose a task and start a focus session when you're ready."

            completedTasks > 0 ->
                "Great work. Your completed tasks are being tracked."

            else ->
                "Add a task from the Tasks menu to start building your productivity data."
        }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(20.dp)
            )
            .background(NexusSurface)
            .clickable(
                onClick = onClick
            )
            .padding(20.dp)
    ) {

        Text(
            text = "✦  NEXUS INSIGHT",
            color = NexusPrimaryLight,
            fontSize = 12.sp
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = title,
            color = NexusWhite,
            fontSize = 16.sp
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