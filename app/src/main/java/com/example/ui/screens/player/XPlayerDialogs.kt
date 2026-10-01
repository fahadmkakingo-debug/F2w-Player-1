package com.example.ui.screens.player

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.screens.video.VideoItem
import com.example.ui.theme.F2WCardBorder
import com.example.ui.theme.F2WCyanPrimary
import com.example.ui.theme.F2WSurfaceElevated
import com.example.ui.theme.F2WTextSecondary
import com.example.ui.theme.F2WVioletAccent

@Composable
fun PlaybackSpeedDialog(
    currentSpeed: Float,
    onSpeedSelected: (Float) -> Unit,
    onDismissRequest: () -> Unit
) {
    val speeds = listOf(0.5f, 0.75f, 1.0f, 1.25f, 1.5f, 1.75f, 2.0f)

    AlertDialog(
        onDismissRequest = onDismissRequest,
        containerColor = Color(0xFF1E1F22),
        title = {
            Text(
                text = "Playback Speed",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                speeds.forEach { speed ->
                    val isSelected = (currentSpeed == speed)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                onSpeedSelected(speed)
                                onDismissRequest()
                            }
                            .padding(vertical = 8.dp, horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (speed == 1.0f) "1.0X (Normal)" else "${speed}X",
                            color = if (isSelected) F2WCyanPrimary else Color.White,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 15.sp
                        )
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                tint = F2WCyanPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Close", color = F2WCyanPrimary)
            }
        }
    )
}

@Composable
fun SubtitleTrackDialog(
    selectedSubtitle: String,
    onSubtitleSelected: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    val subtitles = listOf("None", "English (Embedded)", "Swahili (Auto)", "Select .SRT File...")

    AlertDialog(
        onDismissRequest = onDismissRequest,
        containerColor = Color(0xFF1E1F22),
        title = {
            Text(
                text = "Subtitles (CC)",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                subtitles.forEach { sub ->
                    val isSelected = (selectedSubtitle == sub)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                onSubtitleSelected(sub)
                                onDismissRequest()
                            }
                            .padding(vertical = 8.dp, horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = {
                                onSubtitleSelected(sub)
                                onDismissRequest()
                            },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = F2WCyanPrimary,
                                unselectedColor = Color.White.copy(alpha = 0.5f)
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = sub,
                            color = if (isSelected) F2WCyanPrimary else Color.White,
                            fontSize = 15.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Close", color = F2WCyanPrimary)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistQueueBottomSheet(
    videos: List<VideoItem>,
    currentVideoId: String,
    onVideoSelected: (VideoItem) -> Unit,
    onDismissRequest: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        containerColor = Color(0xFF1E1F22),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Playlist Queue (${videos.size})",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            HorizontalDivider(color = F2WCardBorder.copy(alpha = 0.4f))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(videos) { video ->
                    val isPlaying = video.id == currentVideoId
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isPlaying) F2WCyanPrimary.copy(alpha = 0.15f) else Color.Transparent)
                            .clickable {
                                onVideoSelected(video)
                                onDismissRequest()
                            }
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isPlaying) {
                            Icon(
                                imageVector = Icons.Filled.PlayArrow,
                                contentDescription = null,
                                tint = F2WCyanPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = video.title,
                                color = if (isPlaying) F2WCyanPrimary else Color.White,
                                fontWeight = if (isPlaying) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 14.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${video.resolution} • ${video.durationText}",
                                color = F2WTextSecondary,
                                fontSize = 11.5.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
