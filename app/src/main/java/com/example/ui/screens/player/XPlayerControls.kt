package com.example.ui.screens.player

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ClosedCaption
import androidx.compose.material.icons.filled.FitScreen
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.QueueMusic
import androidx.compose.material.icons.filled.ScreenRotation
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.F2WCyanPrimary
import com.example.ui.theme.F2WVioletAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun XPlayerTopBar(
    title: String,
    decoderMode: String,
    onBackClick: () -> Unit,
    onDecoderClick: () -> Unit,
    onSubtitlesClick: () -> Unit,
    onPlaylistClick: () -> Unit,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = 0.85f),
                        Color.Transparent
                    )
                )
            )
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back Button
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.testTag("player_back_btn")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            // Video Title
            Text(
                text = title,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
            )

            // Right Action Icons
            // 1. Decoder (HW / SW)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .clickable(onClick = onDecoderClick)
                    .padding(horizontal = 6.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.HighQuality,
                        contentDescription = "Decoder",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = decoderMode,
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // 2. Subtitles [CC]
            IconButton(onClick = onSubtitlesClick) {
                Icon(
                    imageVector = Icons.Filled.ClosedCaption,
                    contentDescription = "Subtitles",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }

            // 3. Playlist Queue
            IconButton(onClick = onPlaylistClick) {
                Icon(
                    imageVector = Icons.Filled.QueueMusic,
                    contentDescription = "Playlist Queue",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }

            // 4. Overflow Menu (Three Dots)
            IconButton(onClick = onMoreClick) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "More",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
fun XPlayerQuickControlsRow(
    isOrientationLocked: Boolean,
    isMuted: Boolean,
    isBackgroundAudio: Boolean,
    speedText: String,
    onOrientationToggle: () -> Unit,
    onMuteToggle: () -> Unit,
    onBackgroundAudioToggle: () -> Unit,
    onSpeedClick: () -> Unit,
    onExpandClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(horizontal = 14.dp, vertical = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 1. Orientation Lock
        QuickCircleButton(
            icon = Icons.Filled.ScreenRotation,
            isActive = isOrientationLocked,
            onClick = onOrientationToggle,
            contentDescription = "Rotation Lock"
        )

        // 2. Quick Mute
        QuickCircleButton(
            icon = if (isMuted) Icons.Filled.VolumeMute else Icons.Filled.VolumeUp,
            isActive = isMuted,
            onClick = onMuteToggle,
            contentDescription = "Mute"
        )

        // 3. Background Audio Play
        QuickCircleButton(
            icon = Icons.Filled.Headphones,
            isActive = isBackgroundAudio,
            onClick = onBackgroundAudioToggle,
            contentDescription = "Background Audio"
        )

        // 4. Playback Speed Button (e.g. "1X")
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(Color(0x661E1E1E))
                .border(1.dp, Color.White.copy(alpha = 0.25f), CircleShape)
                .clickable(onClick = onSpeedClick),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = speedText,
                color = if (speedText != "1X") F2WCyanPrimary else Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // 5. Expand Quick Options
        QuickCircleButton(
            icon = Icons.Filled.KeyboardArrowRight,
            isActive = false,
            onClick = onExpandClick,
            contentDescription = "More Quick Options"
        )
    }
}

@Composable
private fun QuickCircleButton(
    icon: ImageVector,
    isActive: Boolean,
    onClick: () -> Unit,
    contentDescription: String
) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(if (isActive) F2WCyanPrimary.copy(alpha = 0.35f) else Color(0x661E1E1E))
            .border(
                1.dp,
                if (isActive) F2WCyanPrimary else Color.White.copy(alpha = 0.25f),
                CircleShape
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = if (isActive) F2WCyanPrimary else Color.White,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun XPlayerScreenshotButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(Color(0x661E1E1E))
            .border(1.dp, Color.White.copy(alpha = 0.25f), CircleShape)
            .clickable(onClick = onClick)
            .testTag("player_screenshot_btn"),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.CameraAlt,
            contentDescription = "Take Screenshot",
            tint = Color.White,
            modifier = Modifier.size(22.dp)
        )
    }
}

@Composable
fun XPlayerBottomBar(
    currentTimeText: String,
    totalDurationText: String,
    progress: Float,
    isPlaying: Boolean,
    isLocked: Boolean,
    onSeek: (Float) -> Unit,
    onLockToggle: () -> Unit,
    onPrevious: () -> Unit,
    onPlayPause: () -> Unit,
    onNext: () -> Unit,
    onFullscreenToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Black.copy(alpha = 0.9f)
                    )
                )
            )
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Row 1: Time & Seekbar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Current Position Text
                Text(
                    text = currentTimeText,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.width(46.dp)
                )

                // Seekbar Slider with vibrant gradient/color
                Slider(
                    value = progress.coerceIn(0f, 1f),
                    onValueChange = onSeek,
                    colors = SliderDefaults.colors(
                        thumbColor = Color.White,
                        activeTrackColor = F2WVioletAccent,
                        inactiveTrackColor = Color.White.copy(alpha = 0.25f)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp)
                        .testTag("player_seekbar")
                )

                // Total Duration Text
                Text(
                    text = totalDurationText,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.width(46.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Row 2: Action Controls (Lock, Prev, Play/Pause, Next, Fullscreen)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1. Screen Lock Button
                IconButton(onClick = onLockToggle) {
                    Icon(
                        imageVector = if (isLocked) Icons.Filled.Lock else Icons.Filled.LockOpen,
                        contentDescription = "Screen Lock",
                        tint = if (isLocked) F2WCyanPrimary else Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // 2. Previous Video Button
                IconButton(onClick = onPrevious) {
                    Icon(
                        imageVector = Icons.Filled.SkipPrevious,
                        contentDescription = "Previous",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }

                // 3. Center Big Play/Pause Button
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.15f))
                        .border(1.5.dp, Color.White, CircleShape)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(bounded = true, color = Color.White),
                            onClick = onPlayPause
                        )
                        .testTag("player_play_pause_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                // 4. Next Video Button
                IconButton(onClick = onNext) {
                    Icon(
                        imageVector = Icons.Filled.SkipNext,
                        contentDescription = "Next",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }

                // 5. Fullscreen / Aspect Ratio Button
                IconButton(onClick = onFullscreenToggle) {
                    Icon(
                        imageVector = Icons.Filled.Fullscreen,
                        contentDescription = "Aspect Ratio",
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun XPlayerLockedFloatingButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(50.dp)
            .clip(CircleShape)
            .background(Color.Black.copy(alpha = 0.65f))
            .border(1.5.dp, F2WCyanPrimary, CircleShape)
            .clickable(onClick = onClick)
            .testTag("player_unlock_floating_btn"),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Lock,
            contentDescription = "Unlock Controls",
            tint = F2WCyanPrimary,
            modifier = Modifier.size(24.dp)
        )
    }
}
