package com.nexus.app.ui.screens.auth

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexus.app.ui.theme.NexusBackground
import com.nexus.app.ui.theme.NexusPrimary
import com.nexus.app.ui.theme.NexusPrimaryLight
import com.nexus.app.ui.theme.NexusWhite

@Composable
fun LoginScreen(
    onRegister: () -> Unit,
    onForgotPassword: () -> Unit,
    onLogin: (String, String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

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
                    vertical = 22.dp
                ),
            horizontalAlignment = Alignment.Start
        ) {

            // =================================================
            // LOGO
            // =================================================

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                AuthNexusLogo(
                    modifier = Modifier.size(76.dp)
                )
            }

            Spacer(
                modifier = Modifier.height(26.dp)
            )

            // =================================================
            // TITLE
            // =================================================

            Text(
                text = "Welcome back",
                color = NexusWhite,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(7.dp)
            )

            Text(
                text = "Sign in to continue your journey",
                color = NexusWhite.copy(alpha = 0.58f),
                fontSize = 14.sp
            )

            Spacer(
                modifier = Modifier.height(30.dp)
            )

            // =================================================
            // EMAIL
            // =================================================

            Text(
                text = "Email",
                color = NexusWhite.copy(alpha = 0.82f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(
                modifier = Modifier.height(7.dp)
            )

            TextField(
                value = email,
                onValueChange = {
                    email = it
                    errorMessage = ""
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
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            // =================================================
            // PASSWORD
            // =================================================

            Text(
                text = "Password",
                color = NexusWhite.copy(alpha = 0.82f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(
                modifier = Modifier.height(7.dp)
            )

            TextField(
                value = password,
                onValueChange = {
                    password = it
                    errorMessage = ""
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                placeholder = {
                    Text(
                        text = "Enter your password",
                        color = NexusWhite.copy(alpha = 0.28f)
                    )
                },
                singleLine = true,
                visualTransformation =
                    if (passwordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                trailingIcon = {
                    TextButton(
                        onClick = {
                            passwordVisible = !passwordVisible
                        }
                    ) {
                        Text(
                            text = if (passwordVisible) "○" else "◉",
                            color = NexusWhite.copy(alpha = 0.45f),
                            fontSize = 15.sp
                        )
                    }
                },
                shape = RoundedCornerShape(10.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF14121C),
                    unfocusedContainerColor = Color(0xFF14121C),
                    disabledContainerColor = Color(0xFF14121C),
                    focusedTextColor = NexusWhite,
                    unfocusedTextColor = NexusWhite,
                    cursorColor = NexusPrimaryLight,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )

            // =================================================
            // ERROR
            // =================================================

            if (errorMessage.isNotEmpty()) {

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = errorMessage,
                    color = Color(0xFFFF6B6B),
                    fontSize = 12.sp
                )
            }

            // =================================================
            // FORGOT PASSWORD
            // =================================================

            TextButton(
                onClick = onForgotPassword,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 2.dp)
            ) {
                Text(
                    text = "Forgot password?",
                    color = NexusPrimaryLight,
                    fontSize = 12.sp
                )
            }

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            // =================================================
            // SIGN IN
            // =================================================

            Button(
                onClick = {

                    when {

                        email.isBlank() -> {
                            errorMessage =
                                "Email is required."
                        }

                        !android.util.Patterns.EMAIL_ADDRESS
                            .matcher(email.trim())
                            .matches() -> {
                            errorMessage =
                                "Enter a valid email."
                        }

                        password.isBlank() -> {
                            errorMessage =
                                "Password is required."
                        }

                        password.length < 6 -> {
                            errorMessage =
                                "Password must be at least 6 characters."
                        }

                        else -> {

                            errorMessage = ""

                            onLogin(
                                email.trim(),
                                password
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
                    text = "Sign In",
                    color = NexusWhite,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            // =================================================
            // REGISTER
            // =================================================

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Don't have an account?",
                    color = NexusWhite.copy(alpha = 0.48f),
                    fontSize = 13.sp
                )

                TextButton(
                    onClick = onRegister
                ) {
                    Text(
                        text = "Sign up",
                        color = NexusPrimaryLight,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}