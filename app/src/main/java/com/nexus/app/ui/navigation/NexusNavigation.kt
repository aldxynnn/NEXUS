package com.nexus.app.ui.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nexus.app.data.remote.ApiClient
import com.nexus.app.data.remote.UserLoginRequest
import com.nexus.app.data.remote.UserRegisterRequest
import com.nexus.app.ui.screens.ai.AIScreen
import com.nexus.app.ui.screens.auth.ForgotPasswordScreen
import com.nexus.app.ui.screens.auth.LoginScreen
import com.nexus.app.ui.screens.auth.RegisterScreen
import com.nexus.app.ui.screens.focus.FocusScreen
import com.nexus.app.ui.screens.home.HomeScreen
import com.nexus.app.ui.screens.insights.InsightsScreen
import com.nexus.app.ui.screens.onboarding.OnboardingScreen
import com.nexus.app.ui.screens.profile.ProfileScreen
import com.nexus.app.ui.screens.splash.SplashScreen
import com.nexus.app.ui.screens.tasks.TasksScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object Routes {

    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"

    const val LOGIN = "login"
    const val REGISTER = "register"
    const val FORGOT_PASSWORD = "forgot_password"

    const val HOME = "home"
    const val TASKS = "tasks"
    const val FOCUS = "focus"
    const val INSIGHTS = "insights"
    const val AI = "ai"
    const val PROFILE = "profile"
}

private const val PREFS_NAME = "nexus_auth"

private const val KEY_USER_ID = "user_id"
private const val KEY_NAME = "name"
private const val KEY_EMAIL = "email"
private const val KEY_LOGGED_IN = "logged_in"
private const val KEY_ONBOARDING_DONE = "onboarding_done"

@Composable
fun NexusNavigation() {

    val navController = rememberNavController()
    val scope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {

        // =====================================================
        // SPLASH
        // =====================================================

        composable(Routes.SPLASH) {

            SplashScreen(
                onFinished = {

                    val context = navController.context

                    val prefs = context.getSharedPreferences(
                        PREFS_NAME,
                        Context.MODE_PRIVATE
                    )

                    val isLoggedIn = prefs.getBoolean(
                        KEY_LOGGED_IN,
                        false
                    )

                    val onboardingDone = prefs.getBoolean(
                        KEY_ONBOARDING_DONE,
                        false
                    )

                    when {
                        isLoggedIn -> {

                            navController.navigate(
                                Routes.HOME
                            ) {
                                popUpTo(Routes.SPLASH) {
                                    inclusive = true
                                }
                            }
                        }

                        !onboardingDone -> {

                            navController.navigate(
                                Routes.ONBOARDING
                            ) {
                                popUpTo(Routes.SPLASH) {
                                    inclusive = true
                                }
                            }
                        }

                        else -> {

                            navController.navigate(
                                Routes.LOGIN
                            ) {
                                popUpTo(Routes.SPLASH) {
                                    inclusive = true
                                }
                            }
                        }
                    }
                }
            )
        }

        // =====================================================
        // ONBOARDING
        // =====================================================

        composable(Routes.ONBOARDING) {

            OnboardingScreen(
                onFinished = {

                    val context = navController.context

                    context
                        .getSharedPreferences(
                            PREFS_NAME,
                            Context.MODE_PRIVATE
                        )
                        .edit()
                        .putBoolean(
                            KEY_ONBOARDING_DONE,
                            true
                        )
                        .apply()

                    navController.navigate(
                        Routes.LOGIN
                    ) {
                        popUpTo(Routes.ONBOARDING) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // =====================================================
        // LOGIN
        // =====================================================

        composable(Routes.LOGIN) {

            LoginScreen(

                onRegister = {
                    navController.navigate(
                        Routes.REGISTER
                    )
                },

                onForgotPassword = {
                    navController.navigate(
                        Routes.FORGOT_PASSWORD
                    )
                },

                onLogin = { email, password ->

                    scope.launch(Dispatchers.IO) {

                        try {

                            val response =
                                ApiClient.api.login(
                                    UserLoginRequest(
                                        email = email.trim(),
                                        password = password
                                    )
                                )

                            if (response.isSuccessful) {

                                val user = response.body()

                                if (user != null) {

                                    withContext(
                                        Dispatchers.Main
                                    ) {

                                        val context =
                                            navController.context

                                        saveUser(
                                            context = context,
                                            userId = user.id,
                                            name = user.name,
                                            email = user.email
                                        )

                                        navController.navigate(
                                            Routes.HOME
                                        ) {
                                            popUpTo(
                                                Routes.LOGIN
                                            ) {
                                                inclusive = true
                                            }
                                        }
                                    }

                                } else {

                                    withContext(
                                        Dispatchers.Main
                                    ) {
                                        println(
                                            "LOGIN ERROR: Empty response"
                                        )
                                    }
                                }

                            } else {

                                val error =
                                    response
                                        .errorBody()
                                        ?.string()

                                println(
                                    "LOGIN FAILED: ${response.code()} $error"
                                )
                            }

                        } catch (e: Exception) {

                            println(
                                "LOGIN EXCEPTION: ${e.message}"
                            )

                            e.printStackTrace()
                        }
                    }
                }
            )
        }

        // =====================================================
        // REGISTER
        // =====================================================

        composable(Routes.REGISTER) {

            RegisterScreen(

                onBack = {
                    navController.popBackStack()
                },

                onLogin = {
                    navController.navigate(
                        Routes.LOGIN
                    ) {
                        popUpTo(Routes.REGISTER) {
                            inclusive = true
                        }
                    }
                },

                onRegister = { name, email, password ->

                    scope.launch(Dispatchers.IO) {

                        try {

                            val response =
                                ApiClient.api.register(
                                    UserRegisterRequest(
                                        name = name.trim(),
                                        email = email.trim(),
                                        password = password
                                    )
                                )

                            if (response.isSuccessful) {

                                val user = response.body()

                                if (user != null) {

                                    withContext(
                                        Dispatchers.Main
                                    ) {

                                        val context =
                                            navController.context

                                        saveUser(
                                            context = context,
                                            userId = user.id,
                                            name = user.name,
                                            email = user.email
                                        )

                                        navController.navigate(
                                            Routes.HOME
                                        ) {
                                            popUpTo(
                                                Routes.REGISTER
                                            ) {
                                                inclusive = true
                                            }
                                        }
                                    }

                                } else {

                                    withContext(
                                        Dispatchers.Main
                                    ) {
                                        println(
                                            "REGISTER ERROR: Empty response"
                                        )
                                    }
                                }

                            } else {

                                val error =
                                    response
                                        .errorBody()
                                        ?.string()

                                println(
                                    "REGISTER FAILED: ${response.code()} $error"
                                )
                            }

                        } catch (e: Exception) {

                            println(
                                "REGISTER EXCEPTION: ${e.message}"
                            )

                            e.printStackTrace()
                        }
                    }
                }
            )
        }

        // =====================================================
        // FORGOT PASSWORD
        // =====================================================

        composable(Routes.FORGOT_PASSWORD) {

            ForgotPasswordScreen(

                onBack = {
                    navController.popBackStack()
                },

                onResetPassword = { _, _ ->
                    navController.popBackStack()
                }
            )
        }

        // =====================================================
        // HOME
        // =====================================================

        composable(Routes.HOME) {

            val context = navController.context

            val prefs =
                context.getSharedPreferences(
                    PREFS_NAME,
                    Context.MODE_PRIVATE
                )

            val userName =
                prefs.getString(
                    KEY_NAME,
                    "User"
                ) ?: "User"

            HomeScreen(
                userName = userName,

                onNavigate = { route ->

                    navController.navigate(route)
                }
            )
        }

        // =====================================================
        // TASKS
        // =====================================================

        composable(Routes.TASKS) {

            TasksScreen(
                onBack = {

                    navController.navigate(
                        Routes.HOME
                    ) {
                        popUpTo(
                            Routes.HOME
                        ) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // =====================================================
        // FOCUS
        // =====================================================

        composable(Routes.FOCUS) {

            FocusScreen(

                onNavigate = { route ->

                    navController.navigate(route) {

                        popUpTo(
                            Routes.FOCUS
                        ) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // =====================================================
        // INSIGHTS
        // =====================================================

        composable(Routes.INSIGHTS) {

            InsightsScreen()
        }

        // =====================================================
        // AI
        // =====================================================

        composable(Routes.AI) {

            AIScreen()
        }

        // =====================================================
        // PROFILE
        // =====================================================

        composable(Routes.PROFILE) {

            ProfileScreen(

                onLogout = {

                    val context =
                        navController.context

                    clearUserSession(context)

                    navController.navigate(
                        Routes.LOGIN
                    ) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}

// =============================================================
// SAVE USER
// =============================================================

private fun saveUser(
    context: Context,
    userId: Int,
    name: String,
    email: String
) {

    context
        .getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        .edit()
        .putInt(
            KEY_USER_ID,
            userId
        )
        .putString(
            KEY_NAME,
            name
        )
        .putString(
            KEY_EMAIL,
            email
        )
        .putBoolean(
            KEY_LOGGED_IN,
            true
        )
        .apply()
}

// =============================================================
// CLEAR USER SESSION
// =============================================================

private fun clearUserSession(
    context: Context
) {

    context
        .getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        .edit()
        .remove(KEY_USER_ID)
        .remove(KEY_NAME)
        .remove(KEY_EMAIL)
        .putBoolean(
            KEY_LOGGED_IN,
            false
        )
        .apply()
}