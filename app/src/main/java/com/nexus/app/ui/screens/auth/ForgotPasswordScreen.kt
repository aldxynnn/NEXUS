package com.nexus.app.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexus.app.ui.theme.NexusBackground
import com.nexus.app.ui.theme.NexusPrimary
import com.nexus.app.ui.theme.NexusPrimaryLight
import com.nexus.app.ui.theme.NexusWhite

@Composable
fun ForgotPasswordScreen(
    onBack: () -> Unit,
    onResetPassword: (String, String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF180B35),
                        Color(0xFF0B0715),
                        NexusBackground
                    ),
                    radius = 850f
                )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 24.dp,
                    vertical = 18.dp
                )
        ) {

            // =================================================
            // BACK
            // =================================================

            IconButton(
                onClick = onBack
            ) {
                Text(
                    text = "‹",
                    color = NexusWhite,
                    fontSize = 34.sp
                )
            }

            Spacer(modifier = Modifier.height(34.dp))

            // =================================================
            // LOCK ICON
            // =================================================

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {

                Box(
                    modifier = Modifier
                        .size(108.dp)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    NexusPrimaryLight.copy(alpha = 0.25f),
                                    NexusPrimary.copy(alpha = 0.12f),
                                    Color.Transparent
                                )
                            ),
                            CircleShape
                        )
                        .border(
                            width = 1.dp,
                            color = NexusPrimary.copy(alpha = 0.35f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "🔒",
                        fontSize = 42.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // =================================================
            // TITLE
            // =================================================

            Text(
                text = "Forgot password?",
                color = NexusWhite,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(7.dp))

            Text(
                text = "No worries! Enter your email and we'll send you a reset link.",
                color = NexusWhite.copy(alpha = 0.58f),
                fontSize = 14.sp,
                lineHeight = 21.sp
            )

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Email",
                color = NexusWhite.copy(alpha = 0.82f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(7.dp))

            TextField(
                value = email,
                onValueChange = {
                    email = it
                    message = ""
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                placeholder = {
                    Text(
                        text = "Enter your email",
                        color = NexusWhite.copy(alpha = 0.28f)
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF14121C),
                    unfocusedContainerColor = Color(0xFF14121C),
                    disabledContainerColor = Color(0xFF14121C),
                    focusedTextColor = NexusWhite,
                    unfocusedTextColor = NexusWhite,
                    cursorColor = NexusPrimaryLight,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            if (message.isNotEmpty()) {

                Spacer(modifier = Modifier.height(9.dp))

                Text(
                    text = message,
                    color = Color(0xFFFF6B6B),
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {

                    when {

                        email.isBlank() -> {
                            message = "Email is required."
                        }

                        !android.util.Patterns.EMAIL_ADDRESS
                            .matcher(email.trim())
                            .matches() -> {
                            message = "Enter a valid email."
                        }

                        else -> {

                            message = ""

                            // Callback lama dipertahankan.
                            // Password kosong karena halaman ini
                            // sekarang hanya meminta reset link.
                            onResetPassword(
                                email.trim(),
                                ""
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NexusPrimary
                )
            ) {

                Text(
                    text = "Send Reset Link",
                    color = NexusWhite,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "Remember your password?",
                    color = NexusWhite.copy(alpha = 0.45f),
                    fontSize = 13.sp
                )

                Text(
                    text = "  Sign in",
                    color = NexusPrimaryLight,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}