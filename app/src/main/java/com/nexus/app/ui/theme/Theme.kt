package com.nexus.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val NexusDarkColors = darkColorScheme(
    primary = NexusPrimary,
    secondary = NexusPrimaryLight,
    tertiary = NexusAccent,

    background = NexusBackground,
    surface = NexusSurface,

    onPrimary = NexusWhite,
    onSecondary = NexusWhite,
    onBackground = NexusWhite,
    onSurface = NexusWhite
)

@Composable
fun NexusTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = NexusDarkColors,
        typography = Typography,
        content = content
    )
}