package com.nexus.app.ui.screens.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexus.app.ui.theme.NexusBackground
import com.nexus.app.ui.theme.NexusPrimary
import com.nexus.app.ui.theme.NexusPrimaryLight
import com.nexus.app.ui.theme.NexusWhite
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onFinished: () -> Unit
) {
    var visible by remember {
        mutableStateOf(false)
    }

    val logoScale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.78f,
        animationSpec = tween(
            durationMillis = 900,
            easing = FastOutSlowInEasing
        ),
        label = "logoScale"
    )

    val logoAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(800),
        label = "logoAlpha"
    )

    LaunchedEffect(Unit) {
        visible = true

        delay(1900)

        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF190B35),
                        Color(0xFF090611),
                        NexusBackground
                    ),
                    radius = 900f
                )
            ),
        contentAlignment = Alignment.Center
    ) {

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(
                animationSpec = tween(700)
            ) + scaleIn(
                initialScale = 0.85f,
                animationSpec = tween(900)
            )
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Box(
                    modifier = Modifier
                        .size(190.dp)
                        .scale(logoScale)
                        .alpha(logoAlpha),
                    contentAlignment = Alignment.Center
                ) {

                    // =================================================
                    // OUTER GLOW
                    // =================================================

                    Box(
                        modifier = Modifier
                            .size(170.dp)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        NexusPrimaryLight.copy(alpha = 0.30f),
                                        NexusPrimary.copy(alpha = 0.16f),
                                        Color.Transparent
                                    )
                                ),
                                CircleShape
                            )
                    )

                    // =================================================
                    // OUTER RING
                    // =================================================

                    Box(
                        modifier = Modifier
                            .size(148.dp)
                            .border(
                                width = 2.dp,
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        NexusPrimaryLight,
                                        NexusPrimary.copy(alpha = 0.65f),
                                        Color.Transparent
                                    )
                                ),
                                shape = CircleShape
                            )
                    )

                    // =================================================
                    // INNER RING
                    // =================================================

                    Box(
                        modifier = Modifier
                            .size(128.dp)
                            .border(
                                width = 1.dp,
                                color = NexusPrimary.copy(alpha = 0.35f),
                                shape = CircleShape
                            )
                    )

                    NexusLogo(
                        modifier = Modifier.size(78.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.height(18.dp)
                )

                Text(
                    text = "N E X U S",
                    color = NexusWhite,
                    fontSize = 21.sp,
                    letterSpacing = 7.sp
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text = "Your life, intelligently",
                    color = NexusWhite.copy(alpha = 0.82f),
                    fontSize = 15.sp
                )

                Text(
                    text = "connected.",
                    color = NexusWhite.copy(alpha = 0.82f),
                    fontSize = 15.sp
                )
            }
        }
    }
}

// =============================================================
// NEXUS LOGO
// =============================================================

@Composable
private fun NexusLogo(
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
    ) {

        val purple = NexusPrimaryLight
        val bright = Color(0xFF9B5CFF)

        val left = size.width * 0.28f
        val right = size.width * 0.72f
        val top = size.height * 0.20f
        val bottom = size.height * 0.80f

        // Soft glow
        drawLine(
            color = purple.copy(alpha = 0.12f),
            start = Offset(left, bottom),
            end = Offset(left, top),
            strokeWidth = 30f,
            cap = StrokeCap.Round
        )

        drawLine(
            color = purple.copy(alpha = 0.12f),
            start = Offset(right, top),
            end = Offset(right, bottom),
            strokeWidth = 30f,
            cap = StrokeCap.Round
        )

        drawLine(
            color = purple.copy(alpha = 0.10f),
            start = Offset(left, top),
            end = Offset(right, bottom),
            strokeWidth = 30f,
            cap = StrokeCap.Round
        )

        // Main N
        drawLine(
            color = purple,
            start = Offset(left, bottom),
            end = Offset(left, top),
            strokeWidth = 15f,
            cap = StrokeCap.Round
        )

        drawLine(
            color = bright,
            start = Offset(left, top),
            end = Offset(right, bottom),
            strokeWidth = 15f,
            cap = StrokeCap.Round
        )

        drawLine(
            color = purple,
            start = Offset(right, bottom),
            end = Offset(right, top),
            strokeWidth = 15f,
            cap = StrokeCap.Round
        )

        // Highlight
        drawLine(
            color = Color.White.copy(alpha = 0.16f),
            start = Offset(left + 2f, top + 3f),
            end = Offset(left + 2f, bottom - 3f),
            strokeWidth = 3f,
            cap = StrokeCap.Round
        )
    }
}