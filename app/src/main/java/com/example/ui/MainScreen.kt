package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.media.DemoVideoData
import com.example.ui.components.F2WTopBar
import com.example.ui.navigation.FloatingNavBar
import com.example.ui.navigation.NavTab
import com.example.ui.screens.audio.AudioScreen
import com.example.ui.screens.player.XVideoPlayerScreen
import com.example.ui.screens.playlist.PlaylistScreen
import com.example.ui.screens.privacy.PrivacyScreen
import com.example.ui.screens.settings.SettingsScreen
import com.example.ui.screens.theme.ThemePickerScreen
import com.example.ui.screens.video.VideoItem
import com.example.ui.screens.video.VideoScreen
import com.example.ui.theme.F2WBackground
import com.example.ui.theme.F2WCardBorder
import com.example.ui.theme.F2WCyanPrimary
import com.example.ui.theme.F2WSurface
import com.example.ui.theme.F2WSurfaceElevated
import com.example.ui.theme.F2WTextPrimary
import com.example.ui.theme.F2WTextSecondary
import com.example.ui.theme.F2WTextTertiary
import com.example.ui.theme.F2WVioletAccent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier
) {
    // Video tab is selected by default as required
    var selectedTab by remember { mutableStateOf(NavTab.VIDEO) }
    var showSearchDialog by remember { mutableStateOf(false) }
    var showSettingsPage by remember { mutableStateOf(false) }
    var showThemePicker by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var activePlayingVideo by remember { mutableStateOf<VideoItem?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Fullscreen XPlayer when a video is clicked
    if (activePlayingVideo != null) {
        XVideoPlayerScreen(
            video = activePlayingVideo!!,
            allVideos = DemoVideoData.sampleVideos,
            onBack = { activePlayingVideo = null },
            onVideoChange = { activePlayingVideo = it }
        )
        return
    }

    if (showThemePicker) {
        ThemePickerScreen(
            onBack = { showThemePicker = false }
        )
        return
    }

    if (showSettingsPage) {
        SettingsScreen(
            onBack = { showSettingsPage = false }
        )
        return
    }

    // Back handling: If on secondary tab, return to Video tab
    BackHandler(enabled = selectedTab != NavTab.VIDEO) {
        selectedTab = NavTab.VIDEO
    }

    val subtitle = when (selectedTab) {
        NavTab.VIDEO -> "Local Media Player"
        NavTab.AUDIO -> "Hi-Res Audio Player"
        NavTab.PLAYLIST -> "Media Playlists"
        NavTab.PRIVACY -> "Private Media Vault"
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = F2WBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            F2WTopBar(
                subtitle = subtitle,
                onSearchClick = { showSearchDialog = true },
                onThemeClick = { showThemePicker = true },
                onRefreshClick = {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Refreshing media storage...")
                    }
                },
                onEqualiserClick = {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Equaliser: Audio enhancements ready")
                    }
                },
                onSettingsClick = { showSettingsPage = true }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Main Content Area with Smooth Animation
            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                },
                label = "screen_tab_transition",
                modifier = Modifier.fillMaxSize()
            ) { targetTab ->
                when (targetTab) {
                    NavTab.VIDEO -> VideoScreen(
                        onScanRequest = {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Local storage scanner initialized for video discovery.")
                            }
                        },
                        onVideoClick = { clickedVideo ->
                            activePlayingVideo = clickedVideo
                        }
                    )
                    NavTab.AUDIO -> AudioScreen(
                        onScanRequest = {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Local storage scanner initialized for audio discovery.")
                            }
                        }
                    )
                    NavTab.PLAYLIST -> PlaylistScreen()
                    NavTab.PRIVACY -> PrivacyScreen()
                }
            }

            // Premium 3D Floating Bottom Navigation Card
            FloatingNavBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }

    // Search Dialog
    if (showSearchDialog) {
        AlertDialog(
            onDismissRequest = { showSearchDialog = false },
            containerColor = F2WSurfaceElevated,
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Search Media",
                        color = F2WTextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(
                        onClick = { showSearchDialog = false },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close",
                            tint = F2WTextSecondary
                        )
                    }
                }
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search videos, audio, artists...", color = F2WTextTertiary) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Search,
                                contentDescription = null,
                                tint = F2WCyanPrimary
                            )
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = F2WCyanPrimary,
                            unfocusedBorderColor = F2WCardBorder,
                            focusedTextColor = F2WTextPrimary,
                            unfocusedTextColor = F2WTextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("media_search_input")
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Instant search will index local files as soon as storage is scanned.",
                        color = F2WTextTertiary,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showSearchDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = F2WCyanPrimary),
                    modifier = Modifier.testTag("submit_search_btn")
                ) {
                    Text("Search", color = Color(0xFF070B12), fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}
