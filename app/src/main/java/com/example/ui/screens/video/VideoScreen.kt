package com.example.ui.screens.video

import android.Manifest
import android.content.pm.PackageManager
import android.database.ContentObserver
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.VideoLibrary
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.data.media.DemoVideoData
import com.example.data.media.LocalVideoScanner
import com.example.data.media.RecentlyPlayedManager
import com.example.ui.components.F2WEmptyState
import com.example.ui.theme.F2WCardBorder
import com.example.ui.theme.F2WCyanPrimary
import com.example.ui.theme.F2WSurface
import com.example.ui.theme.F2WSurfaceElevated
import com.example.ui.theme.F2WTextPrimary
import com.example.ui.theme.F2WTextSecondary
import com.example.ui.theme.F2WTextTertiary
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoScreen(
    onScanRequest: () -> Unit,
    modifier: Modifier = Modifier,
    videos: List<VideoItem> = emptyList(),
    folders: List<VideoFolder> = emptyList(),
    isListView: Boolean = false,
    onToggleViewMode: () -> Unit = {},
    onVideoClick: (VideoItem) -> Unit = {},
    onFolderClick: (VideoFolder) -> Unit = {}
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val scanner = remember { LocalVideoScanner(context) }
    val recentlyPlayedManager = remember { RecentlyPlayedManager.getInstance(context) }
    val recentlyPlayedIds by recentlyPlayedManager.recentlyPlayedIds.collectAsState()

    var selectedFilter by remember { mutableStateOf(VideoFilterMode.ALL_VIDEO) }
    var scannedVideos by remember {
        mutableStateOf(if (videos.isNotEmpty()) videos else DemoVideoData.sampleVideos)
    }
    var scannedFolders by remember {
        mutableStateOf(if (folders.isNotEmpty()) folders else DemoVideoData.sampleFolders)
    }
    var isScanning by remember { mutableStateOf(false) }
    var activeVideoForMenu by remember { mutableStateOf<VideoItem?>(null) }
    var activeGroupDetail by remember { mutableStateOf<VideoNameGroup?>(null) }
    var activeFolderDetail by remember { mutableStateOf<VideoFolder?>(null) }
    val favoriteVideoIds = remember { mutableStateListOf<String>("demo_avengers_endgame") }

    val vaultManager = remember { com.example.data.security.PrivacyVaultManager.getInstance(context) }
    var movedVaultPaths by remember {
        mutableStateOf(vaultManager.getVaultItems().map { it.originalPath.lowercase() }.toSet())
    }

    LaunchedEffect(Unit) {
        vaultManager.vaultUpdates.collect {
            movedVaultPaths = vaultManager.getVaultItems().map { it.originalPath.lowercase() }.toSet()
        }
    }

    val availableVideos = remember(scannedVideos, movedVaultPaths) {
        if (movedVaultPaths.isEmpty()) scannedVideos
        else scannedVideos.filter { v ->
            !movedVaultPaths.contains(v.uriString.lowercase()) &&
            !movedVaultPaths.any { path -> path.isNotBlank() && path.endsWith(v.title.lowercase()) }
        }
    }

    val nameGroups = remember(availableVideos) {
        VideoNameGrouper.groupVideosByName(availableVideos)
    }

    val computedFolders = remember(availableVideos) {
        scanner.extractFolders(availableVideos)
    }

    if (activeGroupDetail != null) {
        VideoGroupDetailScreen(
            group = activeGroupDetail!!,
            onBack = { activeGroupDetail = null },
            onVideoClick = onVideoClick,
            isListView = isListView
        )
        return
    }

    if (activeFolderDetail != null) {
        VideoFolderDetailScreen(
            folder = activeFolderDetail!!,
            onBack = { activeFolderDetail = null },
            onVideoClick = onVideoClick,
            isListView = isListView
        )
        return
    }

    val permissionToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_VIDEO
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, permissionToRequest) == PackageManager.PERMISSION_GRANTED
        )
    }

    fun triggerScan() {
        coroutineScope.launch {
            isScanning = true
            val detectedVideos = scanner.scanDeviceVideos()
            if (detectedVideos.isNotEmpty()) {
                val detectedFolders = scanner.extractFolders(detectedVideos)
                scannedVideos = detectedVideos
                scannedFolders = detectedFolders
            } else {
                // Keep sample videos for testing if no physical video files exist on device
                scannedVideos = DemoVideoData.sampleVideos
                scannedFolders = DemoVideoData.sampleFolders
            }
            isScanning = false
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
        if (granted) {
            triggerScan()
        }
    }

    // Initial load: If permission is already granted and no videos yet, trigger media scan
    LaunchedEffect(hasPermission) {
        if (hasPermission && scannedVideos.isEmpty()) {
            triggerScan()
        }
    }

    // Automatically update the list when new videos are added to the device storage
    DisposableEffect(hasPermission) {
        if (!hasPermission) return@DisposableEffect onDispose {}

        val contentObserver = object : ContentObserver(Handler(Looper.getMainLooper())) {
            override fun onChange(selfChange: Boolean) {
                super.onChange(selfChange)
                triggerScan()
            }
        }

        try {
            context.contentResolver.registerContentObserver(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                true,
                contentObserver
            )
        } catch (_: Exception) {}

        onDispose {
            try {
                context.contentResolver.unregisterContentObserver(contentObserver)
            } catch (_: Exception) {}
        }
    }

    // Filter videos according to selected filter tab
    val displayedVideos = remember(selectedFilter, availableVideos, recentlyPlayedIds) {
        when (selectedFilter) {
            VideoFilterMode.ALL_VIDEO -> availableVideos
            VideoFilterMode.GROUP_BY_NAME -> availableVideos.sortedBy { it.title.lowercase() }
            VideoFilterMode.RECENTLY_ADDED -> availableVideos.sortedWith(
                compareByDescending<VideoItem> { it.dateAdded }
                    .thenByDescending { it.id.toLongOrNull() ?: 0L }
            )
            VideoFilterMode.RECENTLY_PLAYED -> {
                recentlyPlayedManager.getRecentlyPlayedVideos(availableVideos)
            }
            VideoFilterMode.GROUP_BY_FOLDER -> availableVideos
        }
    }

    val columnsCount = if (isListView) 1 else 2

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(F2WSurface)
            .testTag("video_screen_container")
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(columnsCount),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 14.dp,
                end = 14.dp,
                top = 10.dp,
                bottom = 16.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header: 2 Rows of Filter Pill Buttons matching F2W design
            item(span = { GridItemSpan(columnsCount) }) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Row 1: [ All Video ] [ Group by Name ] [ Group by Folder ]
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilterPillButton(
                            mode = VideoFilterMode.ALL_VIDEO,
                            isSelected = selectedFilter == VideoFilterMode.ALL_VIDEO,
                            onClick = { selectedFilter = VideoFilterMode.ALL_VIDEO }
                        )
                        FilterPillButton(
                            mode = VideoFilterMode.GROUP_BY_NAME,
                            isSelected = selectedFilter == VideoFilterMode.GROUP_BY_NAME,
                            onClick = { selectedFilter = VideoFilterMode.GROUP_BY_NAME }
                        )
                        FilterPillButton(
                            mode = VideoFilterMode.GROUP_BY_FOLDER,
                            isSelected = selectedFilter == VideoFilterMode.GROUP_BY_FOLDER,
                            onClick = { selectedFilter = VideoFilterMode.GROUP_BY_FOLDER }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Row 2: [ Recently Added ] [ Recently Played ]
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilterPillButton(
                            mode = VideoFilterMode.RECENTLY_ADDED,
                            isSelected = selectedFilter == VideoFilterMode.RECENTLY_ADDED,
                            onClick = { selectedFilter = VideoFilterMode.RECENTLY_ADDED }
                        )
                        FilterPillButton(
                            mode = VideoFilterMode.RECENTLY_PLAYED,
                            isSelected = selectedFilter == VideoFilterMode.RECENTLY_PLAYED,
                            onClick = { selectedFilter = VideoFilterMode.RECENTLY_PLAYED }
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Divider Line
                    HorizontalDivider(
                        color = F2WCardBorder,
                        thickness = 1.dp
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                }
            }

            // Status bar showing active mode and video count
            item(span = { GridItemSpan(columnsCount) }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val countText = when (selectedFilter) {
                        VideoFilterMode.GROUP_BY_FOLDER -> "${computedFolders.size} Folders"
                        VideoFilterMode.GROUP_BY_NAME -> "${nameGroups.size} Groups"
                        else -> "${displayedVideos.size} Videos"
                    }

                    Text(
                        text = "${selectedFilter.label} ($countText)",
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = F2WTextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (isScanning) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(12.dp),
                                color = F2WCyanPrimary,
                                strokeWidth = 1.5.dp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Scanning...",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = F2WCyanPrimary,
                                    fontSize = 11.sp
                                )
                            )
                        } else {
                            Text(
                                text = if (isListView) "List View" else "2-Column Grid",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = F2WTextTertiary,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }
            }

            // Video Content Area (List or 2-Column Grid)
            if (selectedFilter == VideoFilterMode.GROUP_BY_NAME) {
                if (nameGroups.isEmpty() && !isScanning) {
                    item(span = { GridItemSpan(columnsCount) }) {
                        F2WEmptyState(
                            icon = Icons.Outlined.VideoLibrary,
                            title = "No Video Groups Found",
                            description = "Videos with matching or similar movie names will be automatically grouped here in 2 columns.",
                            actionLabel = if (hasPermission) "Scan Device Videos" else "Grant Storage Permission",
                            actionIcon = Icons.Filled.Refresh,
                            onActionClick = {
                                if (hasPermission) {
                                    triggerScan()
                                } else {
                                    permissionLauncher.launch(permissionToRequest)
                                }
                                onScanRequest()
                            },
                            tipText = "Franchises like Avatar, Dune, Spider-Man are automatically clustered",
                            testTag = "groups_empty_state"
                        )
                    }
                } else {
                    items(
                        items = nameGroups,
                        key = { it.id }
                    ) { group ->
                        if (isListView) {
                            VideoNameGroupListCard(
                                group = group,
                                onClick = { activeGroupDetail = group }
                            )
                        } else {
                            VideoNameGroupCard(
                                group = group,
                                onClick = { activeGroupDetail = group }
                            )
                        }
                    }
                }
            } else if (selectedFilter == VideoFilterMode.GROUP_BY_FOLDER) {
                if (computedFolders.isEmpty() && !isScanning) {
                    item(span = { GridItemSpan(columnsCount) }) {
                        F2WEmptyState(
                            icon = Icons.Outlined.VideoLibrary,
                            title = "No Video Folders Found",
                            description = "Video folders on your device (e.g. Movies, Downloads, Camera) will be displayed here in 2 columns once storage is scanned.",
                            actionLabel = if (hasPermission) "Scan Device Videos" else "Grant Storage Permission",
                            actionIcon = Icons.Filled.Refresh,
                            onActionClick = {
                                if (hasPermission) {
                                    triggerScan()
                                } else {
                                    permissionLauncher.launch(permissionToRequest)
                                }
                                onScanRequest()
                            },
                            tipText = "Folders are automatically sorted and organized",
                            testTag = "folders_empty_state"
                        )
                    }
                } else {
                    items(
                        items = computedFolders,
                        key = { it.id }
                    ) { folder ->
                        if (isListView) {
                            VideoFolderListCard(
                                folder = folder,
                                onClick = { activeFolderDetail = folder }
                            )
                        } else {
                            VideoFolderCard(
                                folder = folder,
                                onClick = { activeFolderDetail = folder }
                            )
                        }
                    }
                }
            } else {
                if (displayedVideos.isEmpty() && !isScanning) {
                    item(span = { GridItemSpan(columnsCount) }) {
                        if (selectedFilter == VideoFilterMode.RECENTLY_PLAYED) {
                            F2WEmptyState(
                                icon = Icons.Outlined.VideoLibrary,
                                title = "Hakuna Video Zilizotazamwa Karibuni",
                                description = "Video zozote unazotazama zitahifadhiwa hapa kwa mpangilio (hadi video 8) ukiweza kuendeleza pale ulipoishia.",
                                actionLabel = "Tazama Video",
                                actionIcon = Icons.Filled.PlayArrow,
                                onActionClick = { selectedFilter = VideoFilterMode.ALL_VIDEO },
                                tipText = "Hurekodi nafasi ya video uliyotazama mara ya mwisho",
                                testTag = "recently_played_empty_state"
                            )
                        } else {
                            F2WEmptyState(
                                icon = Icons.Outlined.VideoLibrary,
                                title = "No Local Videos Found",
                                description = "Detected videos will be displayed in this clean view with posters, quality (4K/1080P/720P), duration, and playback progress.",
                                actionLabel = if (hasPermission) "Scan Device Videos" else "Grant Storage Permission",
                                actionIcon = Icons.Filled.Refresh,
                                onActionClick = {
                                    if (hasPermission) {
                                        triggerScan()
                                    } else {
                                        permissionLauncher.launch(permissionToRequest)
                                    }
                                    onScanRequest()
                                },
                                tipText = "Supports MP4, MKV, AVI, WebM and all local video formats",
                                testTag = "video_empty_state"
                            )
                        }
                    }
                } else {
                    // Video cards: Horizontal List or 2-Column Grid based on user's toggle
                    items(
                        items = displayedVideos,
                        key = { it.id }
                    ) { video ->
                        if (isListView) {
                            VideoListCard(
                                video = video,
                                isFavorite = favoriteVideoIds.contains(video.id),
                                onToggleFavorite = {
                                    if (favoriteVideoIds.contains(video.id)) {
                                        favoriteVideoIds.remove(video.id)
                                    } else {
                                        favoriteVideoIds.add(video.id)
                                    }
                                },
                                onClick = { onVideoClick(video) },
                                onMoreOptionsClick = { activeVideoForMenu = video }
                            )
                        } else {
                            VideoThumbnailCard(
                                video = video,
                                onClick = { onVideoClick(video) },
                                onMoreOptionsClick = { activeVideoForMenu = video }
                            )
                        }
                    }
                }
            }
        }

        // Smart Pop Menu with Options matching the user's design
        activeVideoForMenu?.let { video ->
            VideoActionSmartMenu(
                video = video,
                onDismissRequest = { activeVideoForMenu = null },
                onDeleteVideo = { deleted ->
                    scannedVideos = scannedVideos.filter { it.id != deleted.id }
                },
                onRenameVideo = { target, newName ->
                    scannedVideos = scannedVideos.map {
                        if (it.id == target.id) it.copy(title = newName) else it
                    }
                },
                onLockInPrivateFolder = { locked ->
                    scannedVideos = scannedVideos.filter { it.id != locked.id }
                }
            )
        }
    }
}

@Composable
private fun FilterPillButton(
    mode: VideoFilterMode,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val primaryColor = F2WCyanPrimary
    val surfaceElevated = F2WSurfaceElevated
    val cardBorder = F2WCardBorder
    val textSecondary = F2WTextSecondary

    val backgroundColor = if (isSelected) primaryColor else surfaceElevated
    val contentColor = if (isSelected) Color.White else textSecondary
    val borderColor = if (isSelected) primaryColor else cardBorder

    Box(
        modifier = modifier
            .height(34.dp)
            .clip(RoundedCornerShape(17.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(17.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = Color.White),
                onClick = onClick
            )
            .testTag(mode.testTag)
            .padding(horizontal = 9.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Icon
            if (mode == VideoFilterMode.RECENTLY_ADDED) {
                // "NEW" badge box matching the user's mockup
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(3.dp))
                        .background(if (isSelected) Color.White.copy(alpha = 0.28f) else Color(0xFF133B47))
                        .padding(horizontal = 3.dp, vertical = 1.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "NEW",
                        color = contentColor,
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.3.sp
                    )
                }
            } else {
                Icon(
                    imageVector = mode.icon,
                    contentDescription = mode.label,
                    tint = contentColor,
                    modifier = Modifier.size(14.dp)
                )
            }

            Spacer(modifier = Modifier.width(5.dp))

            Text(
                text = mode.label,
                color = contentColor,
                fontSize = 11.5.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                maxLines = 1
            )
        }
    }
}
