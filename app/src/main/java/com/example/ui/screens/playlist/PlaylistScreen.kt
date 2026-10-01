package com.example.ui.screens.playlist

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material.icons.outlined.QueueMusic
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.F2WEmptyState
import com.example.ui.theme.F2WCardBorder
import com.example.ui.theme.F2WCyanPrimary
import com.example.ui.theme.F2WSurface
import com.example.ui.theme.F2WSurfaceElevated
import com.example.ui.theme.F2WTextPrimary
import com.example.ui.theme.F2WTextSecondary
import com.example.ui.theme.F2WTextTertiary
import com.example.ui.theme.F2WVioletAccent

data class PlaylistItem(
    val name: String,
    val icon: ImageVector,
    val itemCount: Int,
    val isSmart: Boolean = false
)

@Composable
fun PlaylistScreen(
    modifier: Modifier = Modifier
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var newPlaylistName by remember { mutableStateOf("") }
    val userPlaylists = remember { mutableStateListOf<PlaylistItem>() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(F2WSurface)
            .testTag("playlist_screen_container")
    ) {
        // Header with "Create Playlist" button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Playlists",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = F2WTextPrimary
                    )
                )
                Text(
                    text = "${userPlaylists.size + 3} Collections Available",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = F2WTextTertiary
                    )
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                F2WCyanPrimary.copy(alpha = 0.2f),
                                F2WVioletAccent.copy(alpha = 0.2f)
                            )
                        )
                    )
                    .border(
                        1.dp,
                        Brush.horizontalGradient(listOf(F2WCyanPrimary, F2WVioletAccent)),
                        RoundedCornerShape(16.dp)
                    )
                    .clickable { showCreateDialog = true }
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .testTag("create_playlist_btn")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "New Playlist",
                        tint = F2WCyanPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "New",
                        color = F2WCyanPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 8.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Built-in Smart Playlists section
            item {
                Text(
                    text = "SMART PLAYLISTS",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = F2WTextTertiary,
                        letterSpacing = 1.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SmartPlaylistCard(
                        title = "Favorites",
                        icon = Icons.Filled.Favorite,
                        iconTint = Color(0xFFFF4D6D),
                        modifier = Modifier.weight(1f)
                    )
                    SmartPlaylistCard(
                        title = "Recents",
                        icon = Icons.Filled.History,
                        iconTint = F2WCyanPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    SmartPlaylistCard(
                        title = "Most Played",
                        icon = Icons.Filled.Whatshot,
                        iconTint = Color(0xFFFF9E00),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "CUSTOM PLAYLISTS",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = F2WTextTertiary,
                        letterSpacing = 1.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            if (userPlaylists.isEmpty()) {
                item {
                    F2WEmptyState(
                        icon = Icons.Outlined.QueueMusic,
                        title = "No Custom Playlists Yet",
                        description = "Organize your favorite local video clips and sound tracks into personalized media queues.",
                        actionLabel = "Create New Playlist",
                        actionIcon = Icons.Filled.Add,
                        onActionClick = { showCreateDialog = true },
                        tipText = "Tap + to create your first custom collection",
                        testTag = "playlist_empty_state"
                    )
                }
            } else {
                items(userPlaylists.size) { index ->
                    val playlist = userPlaylists[index]
                    CustomPlaylistItemRow(playlist = playlist)
                }
            }
        }
    }

    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            containerColor = F2WSurfaceElevated,
            title = {
                Text(
                    text = "Create Playlist",
                    color = F2WTextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "Enter a title for your new media collection:",
                        color = F2WTextSecondary,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = newPlaylistName,
                        onValueChange = { newPlaylistName = it },
                        placeholder = { Text("e.g., Roadtrip Chill, Workout Hits", color = F2WTextTertiary) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = F2WCyanPrimary,
                            unfocusedBorderColor = F2WCardBorder,
                            focusedTextColor = F2WTextPrimary,
                            unfocusedTextColor = F2WTextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("playlist_name_input")
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newPlaylistName.isNotBlank()) {
                            userPlaylists.add(
                                PlaylistItem(
                                    name = newPlaylistName.trim(),
                                    icon = Icons.Outlined.QueueMusic,
                                    itemCount = 0
                                )
                            )
                            newPlaylistName = ""
                            showCreateDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = F2WCyanPrimary),
                    modifier = Modifier.testTag("save_playlist_btn")
                ) {
                    Text("Create", color = Color(0xFF070B12), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateDialog = false }) {
                    Text("Cancel", color = F2WTextSecondary)
                }
            }
        )
    }
}

@Composable
private fun SmartPlaylistCard(
    title: String,
    icon: ImageVector,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(F2WSurfaceElevated)
            .border(1.dp, F2WCardBorder, RoundedCornerShape(16.dp))
            .clickable { /* Future: open smart playlist */ }
            .padding(vertical = 14.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(iconTint.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium.copy(
                    color = F2WTextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )
            )

            Text(
                text = "0 items",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = F2WTextTertiary,
                    fontSize = 11.sp
                )
            )
        }
    }
}

@Composable
private fun CustomPlaylistItemRow(
    playlist: PlaylistItem,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(F2WSurfaceElevated)
            .border(1.dp, F2WCardBorder, RoundedCornerShape(16.dp))
            .clickable { /* Future: open custom playlist */ }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    Brush.linearGradient(
                        listOf(
                            F2WCyanPrimary.copy(alpha = 0.2f),
                            F2WVioletAccent.copy(alpha = 0.2f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = playlist.icon,
                contentDescription = null,
                tint = F2WCyanPrimary,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = playlist.name,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = F2WTextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            )
            Text(
                text = "${playlist.itemCount} files",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = F2WTextTertiary
                )
            )
        }
    }
}
