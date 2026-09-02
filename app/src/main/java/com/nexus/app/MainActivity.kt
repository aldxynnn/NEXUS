package com.nexus.app

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color as ComposeColor
import com.nexus.app.ui.navigation.NexusNavigation
import com.nexus.app.ui.theme.NexusTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = false
        }

        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT

        setContent {
            NexusTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(ComposeColor(0xFF08090D))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .systemBarsPadding()
                    ) {
                        NexusNavigation()
                    }
                }
            }
        }
    }
}