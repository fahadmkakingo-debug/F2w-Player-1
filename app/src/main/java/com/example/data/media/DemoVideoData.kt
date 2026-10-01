package com.example.data.media

import com.example.ui.screens.video.VideoFolder
import com.example.ui.screens.video.VideoItem

object DemoVideoData {
    val sampleVideos: List<VideoItem> = listOf(
        VideoItem(
            id = "demo_1",
            title = "Dune: Part Two",
            durationText = "02:46:15",
            durationMs = 9975000L,
            playbackProgressMs = 4500000L, // Partially watched (~45%)
            sizeText = "4.2 GB",
            resolution = "4K",
            year = "2024",
            folderName = "Movies",
            uriString = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=800&auto=format&fit=crop&q=80",
            dateAdded = 1711929600L
        ),
        VideoItem(
            id = "demo_2",
            title = "Spider-Man: Across the Spider-Verse",
            durationText = "02:20:00",
            durationMs = 8400000L,
            playbackProgressMs = 5800000L, // Partially watched (~69%)
            sizeText = "3.1 GB",
            resolution = "1080P",
            year = "2023",
            folderName = "Movies",
            uriString = "https://images.unsplash.com/photo-1579783902614-a3fb3927b675?w=800&auto=format&fit=crop&q=80",
            dateAdded = 1711843200L
        ),
        VideoItem(
            id = "demo_3",
            title = "Oppenheimer Special Edition",
            durationText = "03:00:22",
            durationMs = 10822000L,
            playbackProgressMs = 1200000L, // Partially watched (~11%)
            sizeText = "5.6 GB",
            resolution = "4K",
            year = "2023",
            folderName = "Movies",
            uriString = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=800&auto=format&fit=crop&q=80",
            dateAdded = 1711756800L
        ),
        VideoItem(
            id = "demo_4",
            title = "Cyberpunk 2077 Night City Walkthrough",
            durationText = "00:48:30",
            durationMs = 2910000L,
            playbackProgressMs = 0L,
            sizeText = "1.4 GB",
            resolution = "1080P",
            year = "2024",
            folderName = "Downloads",
            uriString = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=800&auto=format&fit=crop&q=80",
            dateAdded = 1711670400L
        ),
        VideoItem(
            id = "demo_5",
            title = "Avatar: The Way of Water",
            durationText = "03:12:40",
            durationMs = 11560000L,
            playbackProgressMs = 7200000L, // Partially watched (~62%)
            sizeText = "6.8 GB",
            resolution = "4K",
            year = "2022",
            folderName = "Movies",
            uriString = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=800&auto=format&fit=crop&q=80",
            dateAdded = 1711584000L
        ),
        VideoItem(
            id = "demo_6",
            title = "Interstellar IMAX Remastered",
            durationText = "02:49:00",
            durationMs = 10140000L,
            playbackProgressMs = 0L,
            sizeText = "4.8 GB",
            resolution = "4K",
            year = "2014",
            folderName = "Sci-Fi",
            uriString = "https://images.unsplash.com/photo-1446776811953-b23d57bd21aa?w=800&auto=format&fit=crop&q=80",
            dateAdded = 1711497600L
        ),
        VideoItem(
            id = "demo_7",
            title = "4K Nature Wildlife Safari Kenya",
            durationText = "00:24:18",
            durationMs = 1458000L,
            playbackProgressMs = 900000L, // Partially watched (~61%)
            sizeText = "850 MB",
            resolution = "4K",
            year = "2024",
            folderName = "Camera",
            uriString = "https://images.unsplash.com/photo-1534177616072-ef7dc120449d?w=800&auto=format&fit=crop&q=80",
            dateAdded = 1711411200L
        ),
        VideoItem(
            id = "demo_8",
            title = "The Batman Dark Knight Edition",
            durationText = "02:56:00",
            durationMs = 10560000L,
            playbackProgressMs = 0L,
            sizeText = "3.9 GB",
            resolution = "1080P",
            year = "2022",
            folderName = "Movies",
            uriString = "https://images.unsplash.com/photo-1509281373149-e957c6296406?w=800&auto=format&fit=crop&q=80",
            dateAdded = 1711324800L
        )
    )

    val sampleFolders: List<VideoFolder> = listOf(
        VideoFolder("Movies", "Movies", videoCount = 5, path = "/storage/emulated/0/Movies"),
        VideoFolder("Downloads", "Downloads", videoCount = 1, path = "/storage/emulated/0/Download"),
        VideoFolder("Camera", "Camera", videoCount = 1, path = "/storage/emulated/0/DCIM/Camera"),
        VideoFolder("Sci-Fi", "Sci-Fi", videoCount = 1, path = "/storage/emulated/0/Movies/Sci-Fi")
    )
}
