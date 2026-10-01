package com.example.ui.screens.audio

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.outlined.Audiotrack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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

@Composable
fun AudioScreen(
    onScanRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("Tracks") }
    val categories = listOf("Tracks", "Albums", "Artists", "Folders", "Genres")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(F2WSurface)
            .testTag("audio_screen_container")
    ) {
        // Audio Category Tabs
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { category ->
                val isSelected = category == selectedCategory
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .then(
                            if (isSelected) {
                                Modifier
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(
                                                F2WCyanPrimary.copy(alpha = 0.2f),
                                                F2WVioletAccent.copy(alpha = 0.2f)
                                            )
                                        )
                                    )
                                    .border(
                                        width = 1.dp,
                                        brush = Brush.horizontalGradient(
                                            listOf(F2WCyanPrimary, F2WVioletAccent)
                                        ),
                                        shape = RoundedCornerShape(20.dp)
                                    )
                            } else {
                                Modifier
                                    .background(F2WSurfaceElevated)
                                    .border(
                                        width = 1.dp,
                                        color = F2WCardBorder,
                                        shape = RoundedCornerShape(20.dp)
                                    )
                            }
                        )
                        .clickable { selectedCategory = category }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .testTag("audio_chip_$category")
                ) {
                    Text(
                        text = category,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) F2WCyanPrimary else F2WTextSecondary
                    )
                }
            }
        }

        // Sub-header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "0 Tracks",
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = F2WTextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = " • Hi-Res Audio",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = F2WTextTertiary
                    )
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { /* Sort */ },
                    modifier = Modifier.size(36.dp).testTag("audio_sort_btn")
                ) {
                    Icon(
                        imageVector = Icons.Filled.Sort,
                        contentDescription = "Sort Music",
                        tint = F2WTextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(
                    onClick = { /* Shuffle */ },
                    modifier = Modifier.size(36.dp).testTag("audio_shuffle_btn")
                ) {
                    Icon(
                        imageVector = Icons.Filled.Shuffle,
                        contentDescription = "Shuffle Music",
                        tint = F2WTextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Content Area
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
        ) {
            item {
                F2WEmptyState(
                    icon = Icons.Outlined.Audiotrack,
                    title = "No Local Audio Tracks Found",
                    description = "Music, albums, audiobooks, and ringtones saved on your phone will appear here organized by artist and album.",
                    actionLabel = "Scan Audio Files",
                    actionIcon = Icons.Filled.Refresh,
                    onActionClick = onScanRequest,
                    tipText = "Lossless FLAC, ALAC, WAV, and MP3 ready",
                    testTag = "audio_empty_state"
                )
            }
        }
    }
}
