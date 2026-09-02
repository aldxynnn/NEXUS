package com.nexus.app.ui.screens.profile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexus.app.ui.theme.NexusBackground
import com.nexus.app.ui.theme.NexusPrimaryLight
import com.nexus.app.ui.theme.NexusSurface
import com.nexus.app.ui.theme.NexusTextSecondary
import com.nexus.app.ui.theme.NexusWhite

private const val PREFS_NAME = "nexus_auth"
private const val KEY_PROFILE_IMAGE = "profile_image"

@Composable
fun ProfileScreen(
    onLogout: () -> Unit
) {
    val context = LocalContext.current

    val prefs = remember {
        context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
    }

    val name = prefs.getString("name", "") ?: ""
    val email = prefs.getString("email", "") ?: ""

    val displayName = name.ifBlank {
        "User"
    }

    val initial = displayName
        .trim()
        .firstOrNull()
        ?.uppercase()
        ?: "U"

    var profileImageUri by remember {
        mutableStateOf(
            prefs.getString(KEY_PROFILE_IMAGE, null)
        )
    }

    val imageBitmap = remember(profileImageUri) {
        profileImageUri?.let { uriString ->
            loadBitmapFromUri(
                context = context,
                uri = Uri.parse(uriString)
            )
        }
    }

    val imagePickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->

            if (uri != null) {

                profileImageUri = uri.toString()

                prefs.edit()
                    .putString(
                        KEY_PROFILE_IMAGE,
                        uri.toString()
                    )
                    .apply()
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NexusBackground)
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = 20.dp,
                vertical = 22.dp
            )
    ) {

        // =====================================================
        // HEADER
        // =====================================================

        Text(
            text = "Profile",
            color = NexusWhite,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(5.dp)
        )

        Text(
            text = "Manage your personal account",
            color = NexusTextSecondary,
            fontSize = 12.sp
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        // =====================================================
        // PROFILE CARD
        // =====================================================

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(26.dp)
                )
                .background(NexusSurface)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // =================================================
            // PROFILE PHOTO
            // =================================================

            Box(
                modifier = Modifier
                    .size(104.dp)
                    .clip(CircleShape)
                    .background(
                        NexusPrimaryLight.copy(
                            alpha = 0.10f
                        )
                    )
                    .clickable {
                        imagePickerLauncher.launch(
                            "image/*"
                        )
                    },
                contentAlignment = Alignment.Center
            ) {

                if (imageBitmap != null) {

                    Image(
                        bitmap = imageBitmap.asImageBitmap(),
                        contentDescription = "Profile photo",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )

                } else {

                    Box(
                        modifier = Modifier
                            .size(84.dp)
                            .clip(CircleShape)
                            .background(
                                NexusPrimaryLight.copy(
                                    alpha = 0.16f
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = initial,
                            color = NexusPrimaryLight,
                            fontSize = 34.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // CAMERA / EDIT INDICATOR

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(NexusPrimaryLight),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "✎",
                        color = NexusBackground,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "Tap to change photo",
                color = NexusTextSecondary,
                fontSize = 10.sp
            )

            Spacer(
                modifier = Modifier.height(17.dp)
            )

            Text(
                text = displayName,
                color = NexusWhite,
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Text(
                text = email.ifBlank {
                    "No email available"
                },
                color = NexusTextSecondary,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(
            modifier = Modifier.height(25.dp)
        )

        // =====================================================
        // ACCOUNT
        // =====================================================

        SectionTitle(
            title = "ACCOUNT"
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(21.dp)
                )
                .background(NexusSurface)
                .padding(
                    horizontal = 18.dp,
                    vertical = 5.dp
                )
        ) {

            ProfileInfoRow(
                icon = "◎",
                label = "Name",
                value = displayName
            )

            HorizontalDivider(
                color = NexusTextSecondary.copy(
                    alpha = 0.10f
                )
            )

            ProfileInfoRow(
                icon = "@",
                label = "Email",
                value = email.ifBlank {
                    "-"
                }
            )
        }

        Spacer(
            modifier = Modifier.height(25.dp)
        )

        // =====================================================
        // WORKSPACE
        // =====================================================

        SectionTitle(
            title = "WORKSPACE"
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(21.dp)
                )
                .background(NexusSurface)
        ) {

            WorkspaceRow(
                icon = "◐",
                title = "Focus & Productivity",
                subtitle = "Build deeper work sessions",
                showDivider = true
            )

            WorkspaceRow(
                icon = "✓",
                title = "Personal Workspace",
                subtitle = "Manage your tasks and routines",
                showDivider = false
            )
        }

        Spacer(
            modifier = Modifier.height(25.dp)
        )

        // =====================================================
        // APP
        // =====================================================

        SectionTitle(
            title = "APP"
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(21.dp)
                )
                .background(NexusSurface)
                .padding(
                    horizontal = 18.dp,
                    vertical = 5.dp
                )
        ) {

            ProfileInfoRow(
                icon = "N",
                label = "Application",
                value = "NEXUS"
            )

            HorizontalDivider(
                color = NexusTextSecondary.copy(
                    alpha = 0.10f
                )
            )

            ProfileInfoRow(
                icon = "•",
                label = "Version",
                value = "1.0"
            )
        }

        Spacer(
            modifier = Modifier.height(30.dp)
        )

        // =====================================================
        // LOGOUT
        // =====================================================

        Button(
            onClick = {

                prefs.edit()
                    .clear()
                    .apply()

                onLogout()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(17.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF3A2024),
                contentColor = Color(0xFFFF7B86)
            )
        ) {

            Text(
                text = "Log out",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text = "NEXUS",
            modifier = Modifier.fillMaxWidth(),
            color = NexusTextSecondary.copy(
                alpha = 0.40f
            ),
            fontSize = 9.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 1.2.sp,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )
    }
}

// =============================================================
// SECTION TITLE
// =============================================================

@Composable
private fun SectionTitle(
    title: String
) {

    Text(
        text = title,
        color = NexusTextSecondary,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.2.sp
    )
}

// =============================================================
// PROFILE INFO ROW
// =============================================================

@Composable
private fun ProfileInfoRow(
    icon: String,
    label: String,
    value: String
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 15.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(
                    RoundedCornerShape(11.dp)
                )
                .background(
                    NexusPrimaryLight.copy(
                        alpha = 0.10f
                    )
                ),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = icon,
                color = NexusPrimaryLight,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.width(12.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = label,
                color = NexusTextSecondary,
                fontSize = 10.sp
            )

            Spacer(
                modifier = Modifier.height(3.dp)
            )

            Text(
                text = value,
                color = NexusWhite,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// =============================================================
// WORKSPACE ROW
// =============================================================

@Composable
private fun WorkspaceRow(
    icon: String,
    title: String,
    subtitle: String,
    showDivider: Boolean
) {

    Column {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 18.dp,
                    vertical = 16.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(
                        RoundedCornerShape(12.dp)
                    )
                    .background(
                        NexusPrimaryLight.copy(
                            alpha = 0.11f
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = icon,
                    color = NexusPrimaryLight,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.width(13.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = title,
                    color = NexusWhite,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = subtitle,
                    color = NexusTextSecondary,
                    fontSize = 10.sp
                )
            }

            Text(
                text = "›",
                color = NexusTextSecondary,
                fontSize = 22.sp
            )
        }

        if (showDivider) {

            HorizontalDivider(
                modifier = Modifier.padding(
                    horizontal = 18.dp
                ),
                color = NexusTextSecondary.copy(
                    alpha = 0.10f
                )
            )
        }
    }
}

// =============================================================
// LOAD PROFILE IMAGE
// =============================================================

private fun loadBitmapFromUri(
    context: Context,
    uri: Uri
): Bitmap? {

    return try {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

            val source =
                android.graphics.ImageDecoder.createSource(
                    context.contentResolver,
                    uri
                )

            android.graphics.ImageDecoder.decodeBitmap(
                source
            )

        } else {

            MediaStore.Images.Media.getBitmap(
                context.contentResolver,
                uri
            )
        }

    } catch (
        e: Exception
    ) {

        try {

            context.contentResolver
                .openInputStream(uri)
                ?.use { inputStream ->
                    BitmapFactory.decodeStream(
                        inputStream
                    )
                }

        } catch (
            _: Exception
        ) {

            null
        }
    }
}