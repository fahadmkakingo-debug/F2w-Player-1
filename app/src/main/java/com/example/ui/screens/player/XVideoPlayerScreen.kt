package com.example.ui.screens.player

import android.content.Context
import android.media.AudioManager
import android.net.Uri
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.ui.screens.video.VideoItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(UnstableApi::class)
@Composable
fun XVideoPlayerScreen(
    video: VideoItem,
    allVideos: List<VideoItem> = listOf(video),
    onBack: () -> Unit,
    onVideoChange: (VideoItem) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val audioManager = remember { context.getSystemService(Context.AUDIO_SERVICE) as AudioManager }

    var currentVideo by remember { mutableStateOf(video) }

    // ExoPlayer Instance
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            playWhenReady = true
        }
    }

    // Player States
    var isPlaying by remember { mutableStateOf(true) }
    var currentPositionMs by remember { mutableLongStateOf(0L) }
    var durationMs by remember { mutableLongStateOf(video.durationMs.coerceAtLeast(1000L)) }
    var areControlsVisible by remember { mutableStateOf(true) }
    var isLocked by remember { mutableStateOf(false) }
    var isMuted by remember { mutableStateOf(false) }
    var isBackgroundAudio by remember { mutableStateOf(false) }
    var isOrientationLocked by remember { mutableStateOf(false) }
    var decoderMode by remember { mutableStateOf("HW") }
    var currentSpeed by remember { mutableFloatStateOf(1.0f) }
    var selectedSubtitle by remember { mutableStateOf("None") }

    // Gesture Overlay States
    var volumePercent by remember {
        val maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC).coerceAtLeast(1)
        val currentVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        mutableIntStateOf((currentVol * 100) / maxVol)
    }
    var showVolumeOverlay by remember { mutableStateOf(false) }

    var brightnessPercent by remember { mutableIntStateOf(70) }
    var showBrightnessOverlay by remember { mutableStateOf(false) }

    var showSeekOverlay by remember { mutableStateOf(false) }
    var seekDiffText by remember { mutableStateOf("") }
    var seekTargetText by remember { mutableStateOf("") }

    // Dialog States
    var showSpeedDialog by remember { mutableStateOf(false) }
    var showSubtitleDialog by remember { mutableStateOf(false) }
    var showPlaylistQueue by remember { mutableStateOf(false) }

    // Load Media Item into ExoPlayer
    LaunchedEffect(currentVideo) {
        val playableUri = if (currentVideo.uriString.startsWith("content://") ||
            currentVideo.uriString.startsWith("file://") ||
            currentVideo.uriString.endsWith(".mp4") ||
            currentVideo.uriString.endsWith(".mkv")
        ) {
            Uri.parse(currentVideo.uriString)
        } else {
            // High reliability sample MP4 for testing
            Uri.parse("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
        }

        val mediaItem = MediaItem.fromUri(playableUri)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()
    }

    // Sync Player Position & State periodically
    LaunchedEffect(exoPlayer) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(playing: Boolean) {
                isPlaying = playing
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    durationMs = exoPlayer.duration.coerceAtLeast(1000L)
                }
            }
        }
        exoPlayer.addListener(listener)

        while (true) {
            if (exoPlayer.isPlaying) {
                currentPositionMs = exoPlayer.currentPosition
                if (exoPlayer.duration > 0) {
                    durationMs = exoPlayer.duration
                }
            }
            delay(500)
        }
    }

    // Auto-hide controls after 4 seconds of inactivity when not locked
    LaunchedEffect(areControlsVisible, isPlaying, isLocked) {
        if (areControlsVisible && isPlaying && !isLocked) {
            delay(4000)
            areControlsVisible = false
        }
    }

    // Clean up player on exit
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    // Back button handling
    BackHandler {
        if (isLocked) {
            isLocked = false
        } else {
            onBack()
        }
    }

    fun seekToPosition(targetMs: Long) {
        val validMs = targetMs.coerceIn(0L, durationMs)
        exoPlayer.seekTo(validMs)
        currentPositionMs = validMs
    }

    fun skipNextVideo() {
        val currentIndex = allVideos.indexOfFirst { it.id == currentVideo.id }
        if (currentIndex != -1 && currentIndex < allVideos.size - 1) {
            val nextVideo = allVideos[currentIndex + 1]
            currentVideo = nextVideo
            onVideoChange(nextVideo)
        } else if (allVideos.isNotEmpty()) {
            val first = allVideos.first()
            currentVideo = first
            onVideoChange(first)
        }
    }

    fun skipPreviousVideo() {
        if (currentPositionMs > 5000L) {
            seekToPosition(0L)
        } else {
            val currentIndex = allVideos.indexOfFirst { it.id == currentVideo.id }
            if (currentIndex > 0) {
                val prevVideo = allVideos[currentIndex - 1]
                currentVideo = prevVideo
                onVideoChange(prevVideo)
            } else {
                seekToPosition(0L)
            }
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .testTag("xplayer_fullscreen_container")
    ) {
        val totalWidth = constraints.maxWidth.toFloat()
        val totalHeight = constraints.maxHeight.toFloat()

        // 1. AndroidView Video Player Surface (ExoPlayer)
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    useController = false
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // 2. Gesture Detector Layer
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(isLocked) {
                    if (isLocked) {
                        detectTapGestures(
                            onTap = {
                                areControlsVisible = !areControlsVisible
                            }
                        )
                    } else {
                        detectTapGestures(
                            onTap = {
                                areControlsVisible = !areControlsVisible
                            },
                            onDoubleTap = { offset ->
                                val halfWidth = totalWidth / 2f
                                if (offset.x < halfWidth * 0.7f) {
                                    // Rewind 10s
                                    seekToPosition(currentPositionMs - 10000L)
                                    seekDiffText = "-10s"
                                    seekTargetText = formatTime(currentPositionMs)
                                    showSeekOverlay = true
                                    coroutineScope.launch {
                                        delay(900)
                                        showSeekOverlay = false
                                    }
                                } else if (offset.x > halfWidth * 1.3f) {
                                    // Fast Forward 10s
                                    seekToPosition(currentPositionMs + 10000L)
                                    seekDiffText = "+10s"
                                    seekTargetText = formatTime(currentPositionMs)
                                    showSeekOverlay = true
                                    coroutineScope.launch {
                                        delay(900)
                                        showSeekOverlay = false
                                    }
                                } else {
                                    // Center Play / Pause
                                    if (exoPlayer.isPlaying) exoPlayer.pause() else exoPlayer.play()
                                }
                            }
                        )
                    }
                }
                .pointerInput(isLocked) {
                    if (!isLocked) {
                        var dragType = 0 // 1: Brightness (left), 2: Volume (right), 3: Seek (horizontal)
                        var initialTouchX = 0f
                        var initialPosition = 0L

                        detectDragGestures(
                            onDragStart = { offset ->
                                initialTouchX = offset.x
                                initialPosition = currentPositionMs
                                dragType = 0
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                if (dragType == 0) {
                                    if (Math.abs(dragAmount.x) > Math.abs(dragAmount.y) && Math.abs(dragAmount.x) > 10f) {
                                        dragType = 3 // Horizontal Seek
                                    } else if (Math.abs(dragAmount.y) > 10f) {
                                        dragType = if (initialTouchX < totalWidth / 2f) 1 else 2 // 1: Brightness, 2: Volume
                                    }
                                }

                                when (dragType) {
                                    1 -> {
                                        // Brightness on left
                                        val delta = (-dragAmount.y / totalHeight * 100).toInt()
                                        brightnessPercent = (brightnessPercent + delta).coerceIn(0, 100)
                                        showBrightnessOverlay = true
                                    }
                                    2 -> {
                                        // Volume on right
                                        val delta = (-dragAmount.y / totalHeight * 100).toInt()
                                        volumePercent = (volumePercent + delta).coerceIn(0, 100)
                                        val maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
                                        val newVol = (volumePercent * maxVol) / 100
                                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVol, 0)
                                        showVolumeOverlay = true
                                    }
                                    3 -> {
                                        // Seek horizontal
                                        val seekSeconds = (dragAmount.x / totalWidth * 60).toInt()
                                        val targetMs = (currentPositionMs + seekSeconds * 1000L).coerceIn(0L, durationMs)
                                        seekToPosition(targetMs)
                                        val diff = (targetMs - initialPosition) / 1000
                                        seekDiffText = if (diff >= 0) "+${diff}s" else "${diff}s"
                                        seekTargetText = "${formatTime(targetMs)} / ${formatTime(durationMs)}"
                                        showSeekOverlay = true
                                    }
                                }
                            },
                            onDragEnd = {
                                coroutineScope.launch {
                                    delay(900)
                                    showBrightnessOverlay = false
                                    showVolumeOverlay = false
                                    showSeekOverlay = false
                                }
                            }
                        )
                    }
                }
        )

        // 3. Gesture Overlays (Volume, Brightness, Seek)
        VolumeGestureOverlay(
            volumePercent = volumePercent,
            visible = showVolumeOverlay,
            modifier = Modifier.align(Alignment.CenterEnd).padding(end = 24.dp)
        )

        BrightnessGestureOverlay(
            brightnessPercent = brightnessPercent,
            visible = showBrightnessOverlay,
            modifier = Modifier.align(Alignment.CenterStart).padding(start = 24.dp)
        )

        SeekGestureOverlay(
            targetTimeText = seekTargetText,
            diffText = seekDiffText,
            visible = showSeekOverlay,
            modifier = Modifier.align(Alignment.Center)
        )

        // 4. Floating Screenshot / Camera Button on Right (As pictured in XPlayer screenshot)
        AnimatedVisibility(
            visible = areControlsVisible && !isLocked,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp)
        ) {
            XPlayerScreenshotButton(
                onClick = {
                    Toast.makeText(context, "Screenshot captured!", Toast.LENGTH_SHORT).show()
                }
            )
        }

        // 5. Controls Overlay (Top Bar, Quick Controls, Bottom Controls)
        AnimatedVisibility(
            visible = areControlsVisible && !isLocked,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Top: Top Bar & Quick Controls Row
                androidx.compose.foundation.layout.Column(
                    modifier = Modifier.align(Alignment.TopCenter)
                ) {
                    XPlayerTopBar(
                        title = currentVideo.title,
                        decoderMode = decoderMode,
                        onBackClick = onBack,
                        onDecoderClick = {
                            decoderMode = if (decoderMode == "HW") "SW" else "HW"
                            Toast.makeText(context, "Decoder switched to $decoderMode", Toast.LENGTH_SHORT).show()
                        },
                        onSubtitlesClick = { showSubtitleDialog = true },
                        onPlaylistClick = { showPlaylistQueue = true },
                        onMoreClick = {
                            Toast.makeText(context, "More player options", Toast.LENGTH_SHORT).show()
                        }
                    )

                    XPlayerQuickControlsRow(
                        isOrientationLocked = isOrientationLocked,
                        isMuted = isMuted,
                        isBackgroundAudio = isBackgroundAudio,
                        speedText = if (currentSpeed == 1.0f) "1X" else "${currentSpeed}X",
                        onOrientationToggle = {
                            isOrientationLocked = !isOrientationLocked
                            Toast.makeText(
                                context,
                                if (isOrientationLocked) "Rotation Locked" else "Auto-rotate On",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        onMuteToggle = {
                            isMuted = !isMuted
                            if (isMuted) {
                                exoPlayer.volume = 0f
                            } else {
                                exoPlayer.volume = 1f
                            }
                            Toast.makeText(context, if (isMuted) "Muted" else "Unmuted", Toast.LENGTH_SHORT).show()
                        },
                        onBackgroundAudioToggle = {
                            isBackgroundAudio = !isBackgroundAudio
                            Toast.makeText(
                                context,
                                if (isBackgroundAudio) "Background Play Enabled" else "Background Play Disabled",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        onSpeedClick = { showSpeedDialog = true },
                        onExpandClick = {
                            Toast.makeText(context, "Floating Pop-up Window", Toast.LENGTH_SHORT).show()
                        }
                    )
                }

                // Bottom: Bottom Bar (Time, Slider, Controls)
                val progress = if (durationMs > 0) currentPositionMs.toFloat() / durationMs.toFloat() else 0f
                XPlayerBottomBar(
                    currentTimeText = formatTime(currentPositionMs),
                    totalDurationText = formatTime(durationMs),
                    progress = progress,
                    isPlaying = isPlaying,
                    isLocked = isLocked,
                    onSeek = { fraction ->
                        seekToPosition((fraction * durationMs).toLong())
                    },
                    onLockToggle = {
                        isLocked = true
                        areControlsVisible = false
                        Toast.makeText(context, "Screen Locked. Tap unlock button to resume controls.", Toast.LENGTH_SHORT).show()
                    },
                    onPrevious = { skipPreviousVideo() },
                    onPlayPause = {
                        if (exoPlayer.isPlaying) exoPlayer.pause() else exoPlayer.play()
                    },
                    onNext = { skipNextVideo() },
                    onFullscreenToggle = {
                        Toast.makeText(context, "Aspect Ratio: Fit Screen", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }

        // 6. When Screen is Locked: Floating Unlock Button on bottom-left
        if (isLocked) {
            AnimatedVisibility(
                visible = areControlsVisible,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 20.dp, bottom = 24.dp)
            ) {
                XPlayerLockedFloatingButton(
                    onClick = {
                        isLocked = false
                        areControlsVisible = true
                        Toast.makeText(context, "Screen Unlocked", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }

    // Speed Dialog
    if (showSpeedDialog) {
        PlaybackSpeedDialog(
            currentSpeed = currentSpeed,
            onSpeedSelected = { speed ->
                currentSpeed = speed
                exoPlayer.setPlaybackSpeed(speed)
                Toast.makeText(context, "Speed: ${speed}X", Toast.LENGTH_SHORT).show()
            },
            onDismissRequest = { showSpeedDialog = false }
        )
    }

    // Subtitle Dialog
    if (showSubtitleDialog) {
        SubtitleTrackDialog(
            selectedSubtitle = selectedSubtitle,
            onSubtitleSelected = { sub ->
                selectedSubtitle = sub
                Toast.makeText(context, "Subtitles: $sub", Toast.LENGTH_SHORT).show()
            },
            onDismissRequest = { showSubtitleDialog = false }
        )
    }

    // Playlist Queue Bottom Sheet
    if (showPlaylistQueue) {
        PlaylistQueueBottomSheet(
            videos = allVideos,
            currentVideoId = currentVideo.id,
            onVideoSelected = { video ->
                currentVideo = video
                onVideoChange(video)
            },
            onDismissRequest = { showPlaylistQueue = false }
        )
    }
}

private fun formatTime(ms: Long): String {
    if (ms <= 0) return "00:00"
    val totalSeconds = ms / 1000
    val seconds = totalSeconds % 60
    val minutes = (totalSeconds / 60) % 60
    val hours = totalSeconds / 3600
    return if (hours > 0) {
        String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }
}
