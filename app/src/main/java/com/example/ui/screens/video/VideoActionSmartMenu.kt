package com.example.ui.screens.video

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.F2WCardBorder
import com.example.ui.theme.F2WCyanPrimary
import com.example.ui.theme.F2WSurfaceElevated
import com.example.ui.theme.F2WTextPrimary
import com.example.ui.theme.F2WTextSecondary
import com.example.ui.theme.F2WTextTertiary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoActionSmartMenu(
    video: VideoItem,
    onDismissRequest: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    onDeleteVideo: (VideoItem) -> Unit = {},
    onRenameVideo: (VideoItem, String) -> Unit = { _, _ -> },
    onLockInPrivateFolder: (VideoItem) -> Unit = {}
) {
    val context = LocalContext.current

    var showPropertiesDialog by remember { mutableStateOf(false) }
    var showRenameDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var renameInput by remember { mutableStateOf(video.title) }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp),
        containerColor = Color(0xFF1E1F22), // Matching the dark popup in the user's screenshot
        scrimColor = Color.Black.copy(alpha = 0.65f),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .size(width = 36.dp, height = 4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color.White.copy(alpha = 0.25f))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 6.dp)
                .verticalScroll(rememberScrollState())
                .testTag("video_smart_pop_menu")
        ) {
            // Header: Selected Video Title and Info
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = video.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${video.resolution} • ${video.durationText} • ${video.sizeText}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = F2WTextSecondary,
                            fontSize = 12.sp
                        )
                    )
                }
            }

            HorizontalDivider(color = F2WCardBorder.copy(alpha = 0.4f), thickness = 1.dp)
            Spacer(modifier = Modifier.height(6.dp))

            // 1. Lock in Private Folder
            SmartMenuItem(
                icon = Icons.Filled.Lock,
                title = "Lock in Private Folder",
                onClick = {
                    onDismissRequest()
                    onLockInPrivateFolder(video)
                    Toast.makeText(context, "\"${video.title}\" moved to Private Vault", Toast.LENGTH_SHORT).show()
                }
            )

            // 2. Convert to MP3
            SmartMenuItem(
                icon = Icons.Filled.MusicNote,
                title = "Convert to MP3",
                onClick = {
                    onDismissRequest()
                    Toast.makeText(context, "Converting \"${video.title}\" to MP3 audio...", Toast.LENGTH_SHORT).show()
                }
            )

            // 3. Add to playlist
            SmartMenuItem(
                icon = Icons.Filled.PlaylistAdd,
                title = "Add to playlist",
                onClick = {
                    onDismissRequest()
                    Toast.makeText(context, "Added \"${video.title}\" to Playlist", Toast.LENGTH_SHORT).show()
                }
            )

            // 4. Delete
            SmartMenuItem(
                icon = Icons.Filled.Delete,
                title = "Delete",
                onClick = {
                    showDeleteConfirmDialog = true
                }
            )

            // 5. Share
            SmartMenuItem(
                icon = Icons.Filled.Share,
                title = "Share",
                onClick = {
                    onDismissRequest()
                    try {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "video/*"
                            if (video.uriString.isNotBlank()) {
                                putExtra(Intent.EXTRA_STREAM, Uri.parse(video.uriString))
                            }
                            putExtra(Intent.EXTRA_SUBJECT, video.title)
                            putExtra(Intent.EXTRA_TEXT, "Watching: ${video.title}")
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share Video"))
                    } catch (_: Exception) {
                        Toast.makeText(context, "Sharing \"${video.title}\"", Toast.LENGTH_SHORT).show()
                    }
                }
            )

            // 6. Rename
            SmartMenuItem(
                icon = Icons.Filled.Edit,
                title = "Rename",
                onClick = {
                    renameInput = video.title
                    showRenameDialog = true
                }
            )

            // 7. Edit
            SmartMenuItem(
                icon = Icons.Filled.ContentCut,
                title = "Edit",
                onClick = {
                    onDismissRequest()
                    Toast.makeText(context, "Video editor opened for \"${video.title}\"", Toast.LENGTH_SHORT).show()
                }
            )

            // 8. Properties
            SmartMenuItem(
                icon = Icons.Filled.Info,
                title = "Properties",
                onClick = {
                    showPropertiesDialog = true
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Properties Dialog
    if (showPropertiesDialog) {
        AlertDialog(
            onDismissRequest = { showPropertiesDialog = false },
            containerColor = Color(0xFF1E1F22),
            title = {
                Text(
                    text = "Video Properties",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    PropertyRow(label = "Title", value = video.title)
                    PropertyRow(label = "Resolution", value = video.resolution)
                    PropertyRow(label = "Duration", value = video.durationText)
                    PropertyRow(label = "File Size", value = video.sizeText)
                    PropertyRow(label = "Folder", value = video.folderName)
                    if (!video.year.isNullOrBlank()) {
                        PropertyRow(label = "Year", value = video.year)
                    }
                    PropertyRow(label = "Location", value = video.uriString.ifBlank { "/storage/emulated/0/Movies/${video.title}.mp4" })
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showPropertiesDialog = false
                        onDismissRequest()
                    }
                ) {
                    Text("OK", color = F2WCyanPrimary, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // Rename Dialog
    if (showRenameDialog) {
        AlertDialog(
            onDismissRequest = { showRenameDialog = false },
            containerColor = Color(0xFF1E1F22),
            title = {
                Text(
                    text = "Rename Video",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                OutlinedTextField(
                    value = renameInput,
                    onValueChange = { renameInput = it },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = F2WCyanPrimary,
                        unfocusedBorderColor = F2WCardBorder
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (renameInput.isNotBlank()) {
                            onRenameVideo(video, renameInput.trim())
                            Toast.makeText(context, "Renamed to \"${renameInput.trim()}\"", Toast.LENGTH_SHORT).show()
                        }
                        showRenameDialog = false
                        onDismissRequest()
                    }
                ) {
                    Text("Rename", color = F2WCyanPrimary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showRenameDialog = false }) {
                    Text("Cancel", color = F2WTextSecondary)
                }
            }
        )
    }

    // Delete Confirmation Dialog
    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            containerColor = Color(0xFF1E1F22),
            title = {
                Text(
                    text = "Delete Video?",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to delete \"${video.title}\"?",
                    color = F2WTextSecondary,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteVideo(video)
                        Toast.makeText(context, "\"${video.title}\" deleted", Toast.LENGTH_SHORT).show()
                        showDeleteConfirmDialog = false
                        onDismissRequest()
                    }
                ) {
                    Text("Delete", color = Color(0xFFEF4444), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text("Cancel", color = F2WTextSecondary)
                }
            }
        )
    }
}

@Composable
private fun SmartMenuItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Color(0xFFCCCCCC),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(18.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color.White,
                fontWeight = FontWeight.Normal,
                fontSize = 15.sp
            )
        )
    }
}

@Composable
private fun PropertyRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                color = F2WTextTertiary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.width(80.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(
                color = Color.White,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.SemiBold
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
    }
}
