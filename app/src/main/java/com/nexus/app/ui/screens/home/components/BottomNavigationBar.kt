package com.nexus.app.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexus.app.ui.theme.NexusPrimaryLight
import com.nexus.app.ui.theme.NexusSurface
import com.nexus.app.ui.theme.NexusTextSecondary

@Composable
fun BottomNavigationBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .background(NexusSurface)
            .padding(
                horizontal = 4.dp,
                vertical = 10.dp
            ),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {

        NavigationItem(
            icon = "⌂",
            label = "Home",
            selected = currentRoute == "home",
            onClick = {
                onNavigate("home")
            }
        )

        NavigationItem(
            icon = "✓",
            label = "Tasks",
            selected = currentRoute == "tasks",
            onClick = {
                onNavigate("tasks")
            }
        )

        NavigationItem(
            icon = "◉",
            label = "Focus",
            selected = currentRoute == "focus",
            onClick = {
                onNavigate("focus")
            }
        )

        NavigationItem(
            icon = "▥",
            label = "Insights",
            selected = currentRoute == "insights",
            onClick = {
                onNavigate("insights")
            }
        )

        NavigationItem(
            icon = "✦",
            label = "AI",
            selected = currentRoute == "ai",
            onClick = {
                onNavigate("ai")
            }
        )

        NavigationItem(
            icon = "●",
            label = "Profile",
            selected = currentRoute == "profile",
            onClick = {
                onNavigate("profile")
            }
        )
    }
}

@Composable
private fun NavigationItem(
    icon: String,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(
                color = if (selected) {
                    NexusPrimaryLight.copy(alpha = 0.12f)
                } else {
                    NexusSurface
                },
                shape = RoundedCornerShape(14.dp)
            )
            .clickable {
                onClick()
            }
            .padding(
                horizontal = 7.dp,
                vertical = 7.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = icon,
            color = if (selected) {
                NexusPrimaryLight
            } else {
                NexusTextSecondary
            },
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            fontWeight = if (selected) {
                FontWeight.Bold
            } else {
                FontWeight.Normal
            }
        )

        Text(
            text = label,
            color = if (selected) {
                NexusPrimaryLight
            } else {
                NexusTextSecondary
            },
            fontSize = 9.sp,
            textAlign = TextAlign.Center,
            fontWeight = if (selected) {
                FontWeight.SemiBold
            } else {
                FontWeight.Normal
            }
        )
    }
}