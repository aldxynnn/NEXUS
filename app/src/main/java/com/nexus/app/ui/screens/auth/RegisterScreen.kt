package com.nexus.app.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexus.app.ui.theme.NexusBackground
import com.nexus.app.ui.theme.NexusPrimary
import com.nexus.app.ui.theme.NexusPrimaryLight
import com.nexus.app.ui.theme.NexusWhite

@Composable
fun RegisterScreen(
    onBack: () -> Unit,
    onLogin: () -> Unit,
    onRegister: (String, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
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
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = 24.dp,
                vertical = 18.dp
            ),
        horizontalAlignment = Alignment.Start
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

        Spacer(modifier = Modifier.height(4.dp))

        // =================================================
        // LOGO
        // =================================================

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            AuthNexusLogo(
                modifier = Modifier.size(62.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Create account",
            color = NexusWhite,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(7.dp))

        Text(
            text = "Start organizing your life with NEXUS",
            color = NexusWhite.copy(alpha = 0.58f),
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(26.dp))

        AuthLabel("Name")

        Spacer(modifier = Modifier.height(7.dp))

        AuthTextField(
            value = name,
            placeholder = "Enter your name",
            onValueChange = {
                name = it
                errorMessage = ""
            }
        )

        Spacer(modifier = Modifier.height(14.dp))

        AuthLabel("Email")

        Spacer(modifier = Modifier.height(7.dp))

        AuthTextField(
            value = email,
            placeholder = "Enter your email",
            onValueChange = {
                email = it
                errorMessage = ""
            }
        )

        Spacer(modifier = Modifier.height(14.dp))

        AuthLabel("Password")

        Spacer(modifier = Modifier.height(7.dp))

        AuthTextField(
            value = password,
            placeholder = "Create a password",
            onValueChange = {
                password = it
                errorMessage = ""
            },
            password = true
        )

        Spacer(modifier = Modifier.height(14.dp))

        AuthLabel("Confirm Password")

        Spacer(modifier = Modifier.height(7.dp))

        AuthTextField(
            value = confirmPassword,
            placeholder = "Confirm your password",
            onValueChange = {
                confirmPassword = it
                errorMessage = ""
            },
            password = true
        )

        if (errorMessage.isNotEmpty()) {

            Spacer(modifier = Modifier.height(9.dp))

            Text(
                text = errorMessage,
                color = Color(0xFFFF6B6B),
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Button(
            onClick = {

                when {
                    name.isBlank() -> {
                        errorMessage = "Name is required."
                    }

                    email.isBlank() -> {
                        errorMessage = "Email is required."
                    }

                    !android.util.Patterns.EMAIL_ADDRESS
                        .matcher(email.trim())
                        .matches() -> {
                        errorMessage = "Enter a valid email."
                    }

                    password.isBlank() -> {
                        errorMessage = "Password is required."
                    }

                    password.length < 6 -> {
                        errorMessage =
                            "Password must be at least 6 characters."
                    }

                    confirmPassword.isBlank() -> {
                        errorMessage =
                            "Please confirm your password."
                    }

                    password != confirmPassword -> {
                        errorMessage =
                            "Passwords do not match."
                    }

                    else -> {

                        errorMessage = ""

                        onRegister(
                            name.trim(),
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
                text = "Sign Up",
                color = NexusWhite,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Already have an account?",
                color = NexusWhite.copy(alpha = 0.48f),
                fontSize = 13.sp
            )

            TextButton(
                onClick = onLogin
            ) {
                Text(
                    text = "Sign in",
                    color = NexusPrimaryLight,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
private fun AuthLabel(
    text: String
) {
    Text(
        text = text,
        color = NexusWhite.copy(alpha = 0.82f),
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium
    )
}

@Composable
private fun AuthTextField(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    password: Boolean = false
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        placeholder = {
            Text(
                text = placeholder,
                color = NexusWhite.copy(alpha = 0.28f)
            )
        },
        singleLine = true,
        visualTransformation =
            if (password)
                PasswordVisualTransformation()
            else
                androidx.compose.ui.text.input.VisualTransformation.None,
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
}