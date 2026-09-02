package com.nexus.app.ui.screens.auth

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import com.nexus.app.ui.theme.NexusPrimary
import com.nexus.app.ui.theme.NexusPrimaryLight

@Composable
fun AuthNexusLogo(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {

        // Glow luar
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            NexusPrimaryLight.copy(alpha = 0.30f),
                            NexusPrimary.copy(alpha = 0.14f),
                            Color.Transparent
                        )
                    ),
                    CircleShape
                )
        )

        // Ring utama
        Box(
            modifier = Modifier
                .fillMaxSize(0.84f)
                .border(
                    width = 1.5.dp,
                    color = NexusPrimary.copy(alpha = 0.75f),
                    shape = CircleShape
                )
        )

        // Ring dalam
        Box(
            modifier = Modifier
                .fillMaxSize(0.70f)
                .border(
                    width = 1.dp,
                    color = NexusPrimary.copy(alpha = 0.28f),
                    shape = CircleShape
                )
        )

        Canvas(
            modifier = Modifier.size(38.dp)
        ) {
            drawNexusN()
        }
    }
}

private fun DrawScope.drawNexusN() {

    val purple = NexusPrimaryLight
    val bright = Color(0xFF9B5CFF)

    val left = size.width * 0.25f
    val right = size.width * 0.75f
    val top = size.height * 0.15f
    val bottom = size.height * 0.85f

    // =========================================================
    // GLOW
    // =========================================================

    drawLine(
        color = purple.copy(alpha = 0.20f),
        start = Offset(left, bottom),
        end = Offset(left, top),
        strokeWidth = 13f,
        cap = StrokeCap.Round
    )

    drawLine(
        color = purple.copy(alpha = 0.20f),
        start = Offset(right, top),
        end = Offset(right, bottom),
        strokeWidth = 13f,
        cap = StrokeCap.Round
    )

    drawLine(
        color = bright.copy(alpha = 0.18f),
        start = Offset(left, top),
        end = Offset(right, bottom),
        strokeWidth = 13f,
        cap = StrokeCap.Round
    )

    // =========================================================
    // MAIN N
    // =========================================================

    drawLine(
        color = purple,
        start = Offset(left, bottom),
        end = Offset(left, top),
        strokeWidth = 6f,
        cap = StrokeCap.Round
    )

    drawLine(
        color = bright,
        start = Offset(left, top),
        end = Offset(right, bottom),
        strokeWidth = 6f,
        cap = StrokeCap.Round
    )

    drawLine(
        color = purple,
        start = Offset(right, bottom),
        end = Offset(right, top),
        strokeWidth = 6f,
        cap = StrokeCap.Round
    )

    // =========================================================
    // HIGHLIGHT
    // =========================================================

    drawLine(
        color = Color.White.copy(alpha = 0.20f),
        start = Offset(
            left + 1f,
            top + 2f
        ),
        end = Offset(
            left + 1f,
            bottom - 2f
        ),
        strokeWidth = 1.5f,
        cap = StrokeCap.Round
    )
}