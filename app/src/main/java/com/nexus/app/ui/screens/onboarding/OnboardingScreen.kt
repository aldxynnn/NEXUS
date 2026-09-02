package com.nexus.app.ui.screens.onboarding

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexus.app.ui.theme.NexusBackground
import com.nexus.app.ui.theme.NexusPrimary
import com.nexus.app.ui.theme.NexusPrimaryLight
import com.nexus.app.ui.theme.NexusWhite

private data class OnboardingPage(
    val description: String
)

private val pages = listOf(
    OnboardingPage(
        description = "Tasks, schedules, habits,\nand more. All in one place."
    ),
    OnboardingPage(
        description = "Stay focused, remove\ndistractions, get things done."
    ),
    OnboardingPage(
        description = "AI insights that help you\nimprove and grow."
    )
)

@Composable
fun OnboardingScreen(
    onFinished: () -> Unit
) {
    var currentPage by remember {
        mutableIntStateOf(0)
    }

    val page = pages[currentPage]

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF130A2B),
                        Color(0xFF08050F),
                        NexusBackground
                    ),
                    radius = 950f
                )
            )
    ) {

        // =====================================================
        // SKIP
        // =====================================================

        Text(
            text = "Skip",
            color = NexusWhite.copy(alpha = 0.78f),
            fontSize = 14.sp,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(
                    top = 8.dp,
                    end = 24.dp
                )
                .clickable {
                    onFinished()
                }
                .padding(8.dp)
        )

        // =====================================================
        // MAIN CONTENT
        // =====================================================

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    top = 80.dp,
                    bottom = 28.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {

                when (currentPage) {

                    0 -> OrganizeIllustration()

                    1 -> FocusIllustration()

                    2 -> InsightsIllustration()
                }
            }

            // =================================================
            // TITLE
            // =================================================

            when (currentPage) {

                0 -> {
                    RichTitle(
                        first = "Organize",
                        second = "everything"
                    )
                }

                1 -> {
                    RichTitle(
                        first = "Focus on what",
                        second = "matters"
                    )
                }

                2 -> {
                    RichTitle(
                        first = "Get smarter",
                        second = "every day"
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(13.dp)
            )

            Text(
                text = page.description,
                color = NexusWhite.copy(alpha = 0.60f),
                fontSize = 14.sp,
                lineHeight = 21.sp,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            // =================================================
            // BOTTOM NAVIGATION
            // =================================================

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
            ) {

                // =================================================
                // DOTS
                // BENAR-BENAR CENTER TERHADAP LAYAR
                // =================================================

                Row(
                    modifier = Modifier.align(
                        Alignment.Center
                    ),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    pages.indices.forEach { index ->

                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .background(
                                    color =
                                        if (index == currentPage) {
                                            NexusPrimaryLight
                                        } else {
                                            NexusWhite.copy(
                                                alpha = 0.18f
                                            )
                                        },
                                    shape = CircleShape
                                )
                        )
                    }
                }

                // =================================================
                // ARROW
                // =================================================

                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(54.dp)
                        .shadow(
                            elevation = 12.dp,
                            shape = CircleShape,
                            ambientColor = NexusPrimary,
                            spotColor = NexusPrimary
                        )
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    NexusPrimaryLight,
                                    NexusPrimary
                                )
                            ),
                            CircleShape
                        )
                        .clickable {

                            if (currentPage < pages.lastIndex) {
                                currentPage++
                            } else {
                                onFinished()
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {

                    ArrowIcon()
                }
            }
        }
    }
}

// =============================================================
// TITLE
// =============================================================

@Composable
private fun RichTitle(
    first: String,
    second: String
) {

    Text(
        text = AnnotatedString.Builder().apply {

            append(first)
            append("\n")

            pushStyle(
                SpanStyle(
                    color = NexusPrimaryLight
                )
            )

            append(second)

            pop()
        }.toAnnotatedString(),
        color = NexusWhite,
        fontSize = 23.sp,
        lineHeight = 29.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )
}

// =============================================================
// ARROW
// =============================================================

@Composable
private fun ArrowIcon() {

    Canvas(
        modifier = Modifier.size(25.dp)
    ) {

        val stroke = 2.2f

        drawLine(
            color = Color.White,
            start = Offset(
                size.width * 0.20f,
                size.height * 0.50f
            ),
            end = Offset(
                size.width * 0.78f,
                size.height * 0.50f
            ),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )

        drawLine(
            color = Color.White,
            start = Offset(
                size.width * 0.55f,
                size.height * 0.27f
            ),
            end = Offset(
                size.width * 0.80f,
                size.height * 0.50f
            ),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )

        drawLine(
            color = Color.White,
            start = Offset(
                size.width * 0.55f,
                size.height * 0.73f
            ),
            end = Offset(
                size.width * 0.80f,
                size.height * 0.50f
            ),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
    }
}

// =============================================================
// PAGE 1
// =============================================================

@Composable
private fun OrganizeIllustration() {

    Box(
        modifier = Modifier.size(280.dp),
        contentAlignment = Alignment.Center
    ) {

        Box(
            modifier = Modifier
                .size(230.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            NexusPrimary.copy(alpha = 0.18f),
                            Color.Transparent
                        )
                    ),
                    CircleShape
                )
        )

        Canvas(
            modifier = Modifier.size(270.dp)
        ) {

            drawOval(
                brush = Brush.linearGradient(
                    colors = listOf(
                        NexusPrimaryLight.copy(alpha = 0.75f),
                        Color.Transparent,
                        NexusPrimary.copy(alpha = 0.65f)
                    )
                ),
                topLeft = Offset(15f, 85f),
                size = androidx.compose.ui.geometry.Size(
                    width = size.width - 30f,
                    height = 95f
                ),
                style = Stroke(width = 2f)
            )
        }

        Box(
            modifier = Modifier
                .size(
                    width = 145.dp,
                    height = 175.dp
                )
                .offset(y = (-8).dp)
                .shadow(elevation = 20.dp)
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF29204A),
                            Color(0xFF11101D)
                        )
                    ),
                    RoundedCornerShape(16.dp)
                )
                .border(
                    width = 1.dp,
                    color = NexusPrimaryLight.copy(alpha = 0.35f),
                    shape = RoundedCornerShape(16.dp)
                )
        ) {

            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(13.dp)
            ) {

                repeat(4) { index ->

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(17.dp)
                                .background(
                                    NexusPrimary.copy(alpha = 0.9f),
                                    RoundedCornerShape(4.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {

                            Text(
                                text = "✓",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(
                            modifier = Modifier.size(8.dp)
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(7.dp)
                                .background(
                                    NexusWhite.copy(
                                        alpha =
                                            if (index == 0) {
                                                0.65f
                                            } else {
                                                0.25f
                                            }
                                    ),
                                    CircleShape
                                )
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .size(58.dp)
                .offset(
                    x = (-70).dp,
                    y = 78.dp
                )
                .background(
                    Color(0xFF211A39),
                    RoundedCornerShape(10.dp)
                )
                .border(
                    1.dp,
                    NexusPrimaryLight.copy(alpha = 0.35f),
                    RoundedCornerShape(10.dp)
                ),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "31",
                color = NexusWhite,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Box(
            modifier = Modifier
                .size(54.dp)
                .offset(
                    x = 70.dp,
                    y = 45.dp
                )
                .background(
                    NexusPrimary,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "✓",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// =============================================================
// PAGE 2
// =============================================================

@Composable
private fun FocusIllustration() {

    Box(
        modifier = Modifier.size(280.dp),
        contentAlignment = Alignment.Center
    ) {

        Box(
            modifier = Modifier
                .size(240.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            NexusPrimary.copy(alpha = 0.20f),
                            Color.Transparent
                        )
                    ),
                    CircleShape
                )
        )

        Canvas(
            modifier = Modifier.size(240.dp)
        ) {

            drawOval(
                color = NexusPrimary.copy(alpha = 0.45f),
                topLeft = Offset(10f, 70f),
                size = androidx.compose.ui.geometry.Size(
                    width = size.width - 20f,
                    height = 100f
                ),
                style = Stroke(width = 2f)
            )

            val center = Offset(
                size.width / 2f,
                size.height / 2f
            )

            drawCircle(
                color = Color(0xFF24194A),
                radius = 64f
            )

            drawCircle(
                color = NexusPrimary.copy(alpha = 0.80f),
                radius = 49f
            )

            drawCircle(
                color = Color(0xFF30205E),
                radius = 34f
            )

            drawCircle(
                color = NexusPrimaryLight,
                radius = 17f
            )

            drawCircle(
                color = Color.White.copy(alpha = 0.75f),
                radius = 5f
            )

            drawLine(
                color = NexusPrimaryLight,
                start = Offset(
                    center.x + 58f,
                    center.y - 65f
                ),
                end = Offset(
                    center.x + 10f,
                    center.y - 10f
                ),
                strokeWidth = 9f,
                cap = StrokeCap.Round
            )

            drawLine(
                color = NexusPrimaryLight,
                start = Offset(
                    center.x + 58f,
                    center.y - 65f
                ),
                end = Offset(
                    center.x + 32f,
                    center.y - 60f
                ),
                strokeWidth = 7f,
                cap = StrokeCap.Round
            )

            drawLine(
                color = NexusPrimaryLight,
                start = Offset(
                    center.x + 58f,
                    center.y - 65f
                ),
                end = Offset(
                    center.x + 52f,
                    center.y - 40f
                ),
                strokeWidth = 7f,
                cap = StrokeCap.Round
            )
        }

        Box(
            modifier = Modifier
                .offset(
                    x = (-67).dp,
                    y = (-78).dp
                )
                .background(
                    Color(0xFF181522),
                    RoundedCornerShape(8.dp)
                )
                .border(
                    1.dp,
                    NexusWhite.copy(alpha = 0.08f),
                    RoundedCornerShape(8.dp)
                )
                .padding(
                    horizontal = 12.dp,
                    vertical = 8.dp
                )
        ) {

            Column {

                Text(
                    text = "Focus Time",
                    color = NexusWhite.copy(alpha = 0.55f),
                    fontSize = 9.sp
                )

                Text(
                    text = "12h 45m",
                    color = NexusWhite,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Box(
            modifier = Modifier
                .offset(
                    x = (-72).dp,
                    y = 87.dp
                )
                .size(62.dp)
                .background(
                    Color(0xFF171321),
                    RoundedCornerShape(8.dp)
                )
                .border(
                    1.dp,
                    NexusPrimary.copy(alpha = 0.30f),
                    RoundedCornerShape(8.dp)
                )
        ) {

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {

                val path = Path().apply {

                    moveTo(
                        0f,
                        size.height * 0.72f
                    )

                    cubicTo(
                        size.width * 0.20f,
                        size.height * 0.30f,
                        size.width * 0.35f,
                        size.height * 0.80f,
                        size.width * 0.55f,
                        size.height * 0.40f
                    )

                    cubicTo(
                        size.width * 0.70f,
                        size.height * 0.15f,
                        size.width * 0.82f,
                        size.height * 0.45f,
                        size.width,
                        size.height * 0.10f
                    )
                }

                drawPath(
                    path = path,
                    color = NexusPrimaryLight,
                    style = Stroke(
                        width = 3f,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }
        }
    }
}

// =============================================================
// PAGE 3
// =============================================================

@Composable
private fun InsightsIllustration() {

    Box(
        modifier = Modifier.size(280.dp),
        contentAlignment = Alignment.Center
    ) {

        Box(
            modifier = Modifier
                .size(245.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            NexusPrimary.copy(alpha = 0.22f),
                            Color.Transparent
                        )
                    ),
                    CircleShape
                )
        )

        Canvas(
            modifier = Modifier.size(180.dp)
        ) {

            val brainColor = NexusPrimaryLight

            drawCircle(
                color = brainColor.copy(alpha = 0.75f),
                radius = 34f,
                center = Offset(
                    size.width * 0.38f,
                    size.height * 0.45f
                )
            )

            drawCircle(
                color = brainColor.copy(alpha = 0.68f),
                radius = 30f,
                center = Offset(
                    size.width * 0.29f,
                    size.height * 0.58f
                )
            )

            drawCircle(
                color = brainColor.copy(alpha = 0.78f),
                radius = 27f,
                center = Offset(
                    size.width * 0.45f,
                    size.height * 0.65f
                )
            )

            drawCircle(
                color = brainColor.copy(alpha = 0.78f),
                radius = 34f,
                center = Offset(
                    size.width * 0.62f,
                    size.height * 0.45f
                )
            )

            drawCircle(
                color = brainColor.copy(alpha = 0.68f),
                radius = 30f,
                center = Offset(
                    size.width * 0.71f,
                    size.height * 0.58f
                )
            )

            drawCircle(
                color = brainColor.copy(alpha = 0.78f),
                radius = 27f,
                center = Offset(
                    size.width * 0.55f,
                    size.height * 0.65f
                )
            )

            drawLine(
                color = Color(0xFF241440),
                start = Offset(
                    size.width * 0.50f,
                    size.height * 0.25f
                ),
                end = Offset(
                    size.width * 0.50f,
                    size.height * 0.76f
                ),
                strokeWidth = 4f
            )

            drawLine(
                color = Color(0xFFB78AFF).copy(alpha = 0.55f),
                start = Offset(
                    size.width * 0.24f,
                    size.height * 0.48f
                ),
                end = Offset(
                    size.width * 0.43f,
                    size.height * 0.50f
                ),
                strokeWidth = 3f,
                cap = StrokeCap.Round
            )

            drawLine(
                color = Color(0xFFB78AFF).copy(alpha = 0.55f),
                start = Offset(
                    size.width * 0.57f,
                    size.height * 0.50f
                ),
                end = Offset(
                    size.width * 0.76f,
                    size.height * 0.48f
                ),
                strokeWidth = 3f,
                cap = StrokeCap.Round
            )
        }

        Box(
            modifier = Modifier
                .offset(
                    x = 8.dp,
                    y = 91.dp
                )
                .background(
                    Color(0xFF181521),
                    RoundedCornerShape(10.dp)
                )
                .border(
                    1.dp,
                    NexusWhite.copy(alpha = 0.08f),
                    RoundedCornerShape(10.dp)
                )
                .padding(
                    horizontal = 14.dp,
                    vertical = 10.dp
                )
        ) {

            Column {

                Text(
                    text = "AI Insight",
                    color = NexusWhite,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = "You're most productive",
                    color = NexusWhite.copy(alpha = 0.60f),
                    fontSize = 9.sp
                )

                Text(
                    text = "at 7PM - 10PM",
                    color = NexusPrimaryLight,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Sparkle(
            modifier = Modifier.offset(
                x = (-88).dp,
                y = (-62).dp
            )
        )

        Sparkle(
            modifier = Modifier.offset(
                x = 92.dp,
                y = (-74).dp
            )
        )

        Sparkle(
            modifier = Modifier.offset(
                x = 93.dp,
                y = 36.dp
            )
        )
    }
}

// =============================================================
// SPARKLE
// =============================================================

@Composable
private fun Sparkle(
    modifier: Modifier = Modifier
) {

    Canvas(
        modifier = modifier.size(18.dp)
    ) {

        val center = Offset(
            size.width / 2f,
            size.height / 2f
        )

        drawLine(
            color = NexusPrimaryLight,
            start = Offset(
                center.x,
                1f
            ),
            end = Offset(
                center.x,
                size.height - 1f
            ),
            strokeWidth = 2f,
            cap = StrokeCap.Round
        )

        drawLine(
            color = NexusPrimaryLight,
            start = Offset(
                1f,
                center.y
            ),
            end = Offset(
                size.width - 1f,
                center.y
            ),
            strokeWidth = 2f,
            cap = StrokeCap.Round
        )
    }
}