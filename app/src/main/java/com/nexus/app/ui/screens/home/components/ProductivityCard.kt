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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexus.app.ui.theme.NexusPrimary
import com.nexus.app.ui.theme.NexusPrimaryLight
import com.nexus.app.ui.theme.NexusWhite

@Composable
fun ProductivityCard(
    score: Int
) {

    val description =
        when {
            score >= 80 -> "Excellent"
            score >= 60 -> "Good progress"
            score >= 40 -> "Keep going"
            score > 0 -> "Getting started"
            else -> "No data yet"
        }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(20.dp)
            )
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        NexusPrimary.copy(alpha = 0.9f),
                        NexusPrimaryLight.copy(alpha = 0.65f)
                    )
                )
            )
            .padding(20.dp),
        verticalAlignment =
            Alignment.CenterVertically,
        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {

        Column {

            Text(
                text = "PRODUCTIVITY SCORE",
                color = NexusWhite.copy(alpha = 0.7f),
                fontSize = 11.sp
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "$score",
                color = NexusWhite,
                fontSize = 42.sp
            )

            Text(
                text = description,
                color = NexusWhite.copy(alpha = 0.8f),
                fontSize = 13.sp
            )
        }

        Text(
            text = "◯",
            color = NexusWhite.copy(alpha = 0.8f),
            fontSize = 74.sp
        )
    }
}