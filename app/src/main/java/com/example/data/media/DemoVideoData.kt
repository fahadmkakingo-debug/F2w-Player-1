package com.example.data.media

import com.example.ui.screens.video.VideoFolder
import com.example.ui.screens.video.VideoItem

object DemoVideoData {
    val sampleVideos: List<VideoItem> = listOf(
        // Featured Videos matching user's design reference
        VideoItem(
            id = "demo_avengers_endgame",
            title = "Avengers.Endgame.2019.mkv",
            durationText = "3:01:02",
            durationMs = 10862000L,
            playbackProgressMs = 5400000L,
            sizeText = "2.8 GB",
            resolution = "4K",
            year = "2019",
            folderName = "Movies",
            uriString = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=800&auto=format&fit=crop&q=80",
            dateAdded = 1711935000L
        ),
        VideoItem(
            id = "demo_tutorial_flutter",
            title = "Tutorial_Flutter_2024.mp4",
            durationText = "1:23:45",
            durationMs = 5025000L,
            playbackProgressMs = 0L,
            sizeText = "450 MB",
            resolution = "1080P",
            year = "2024",
            folderName = "Downloads",
            uriString = "https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5?w=800&auto=format&fit=crop&q=80",
            dateAdded = 1711934000L
        ),
        VideoItem(
            id = "demo_wildlife_africa",
            title = "Wildlife_Documentary_Africa.avi",
            durationText = "52:10",
            durationMs = 3130000L,
            playbackProgressMs = 0L,
            sizeText = "1.2 GB",
            resolution = "1080P",
            year = "2024",
            folderName = "Movies",
            uriString = "https://images.unsplash.com/photo-1516426122078-c23e76319801?w=800&auto=format&fit=crop&q=80",
            dateAdded = 1711933000L
        ),

        // Avatar Franchise (Demonstrating Avatar 1, Avatar 2, Avatar 3)
        VideoItem(
            id = "demo_avatar_1",
            title = "Avatar 1",
            durationText = "02:42:00",
            durationMs = 9720000L,
            playbackProgressMs = 5400000L,
            sizeText = "5.1 GB",
            resolution = "4K",
            year = "2009",
            folderName = "Movies",
            uriString = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=800&auto=format&fit=crop&q=80",
            dateAdded = 1711929700L
        ),
        VideoItem(
            id = "demo_avatar_2",
            title = "Avatar 2: The Way of Water",
            durationText = "03:12:40",
            durationMs = 11560000L,
            playbackProgressMs = 7200000L,
            sizeText = "6.8 GB",
            resolution = "4K",
            year = "2022",
            folderName = "Movies",
            uriString = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=800&auto=format&fit=crop&q=80",
            dateAdded = 1711584000L
        ),
        VideoItem(
            id = "demo_avatar_3",
            title = "Avatar 3: Fire and Ash",
            durationText = "03:05:00",
            durationMs = 11100000L,
            playbackProgressMs = 0L,
            sizeText = "7.2 GB",
            resolution = "4K",
            year = "2025",
            folderName = "Movies",
            uriString = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=800&auto=format&fit=crop&q=80",
            dateAdded = 1711929800L
        ),

        // Dune Franchise
        VideoItem(
            id = "demo_dune_1",
            title = "Dune: Part One",
            durationText = "02:35:00",
            durationMs = 9300000L,
            playbackProgressMs = 3100000L,
            sizeText = "3.8 GB",
            resolution = "4K",
            year = "2021",
            folderName = "Movies",
            uriString = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=800&auto=format&fit=crop&q=80",
            dateAdded = 1711929500L
        ),
        VideoItem(
            id = "demo_dune_2",
            title = "Dune: Part Two",
            durationText = "02:46:15",
            durationMs = 9975000L,
            playbackProgressMs = 4500000L,
            sizeText = "4.2 GB",
            resolution = "4K",
            year = "2024",
            folderName = "Movies",
            uriString = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=800&auto=format&fit=crop&q=80",
            dateAdded = 1711929600L
        ),

        // Spider-Man Franchise
        VideoItem(
            id = "demo_spiderman_1",
            title = "Spider-Man: Into the Spider-Verse",
            durationText = "01:57:00",
            durationMs = 7020000L,
            playbackProgressMs = 2000000L,
            sizeText = "2.6 GB",
            resolution = "1080P",
            year = "2018",
            folderName = "Movies",
            uriString = "https://images.unsplash.com/photo-1579783902614-a3fb3927b675?w=800&auto=format&fit=crop&q=80",
            dateAdded = 1711843100L
        ),
        VideoItem(
            id = "demo_spiderman_2",
            title = "Spider-Man: Across the Spider-Verse",
            durationText = "02:20:00",
            durationMs = 8400000L,
            playbackProgressMs = 5800000L,
            sizeText = "3.1 GB",
            resolution = "1080P",
            year = "2023",
            folderName = "Movies",
            uriString = "https://images.unsplash.com/photo-1579783902614-a3fb3927b675?w=800&auto=format&fit=crop&q=80",
            dateAdded = 1711843200L
        ),

        // Individual Movies (remain as individual groups)
        VideoItem(
            id = "demo_3",
            title = "Oppenheimer Special Edition",
            durationText = "03:00:22",
            durationMs = 10822000L,
            playbackProgressMs = 1200000L,
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
            playbackProgressMs = 900000L,
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

    val sampleFolders: List<VideoFolder>
        get() = sampleVideos
            .groupBy { it.folderName }
            .map { (folderName, items) ->
                VideoFolder(
                    id = folderName,
                    name = folderName,
                    videoCount = items.size,
                    path = "/storage/emulated/0/$folderName",
                    videos = items
                )
            }
            .sortedByDescending { it.videoCount }
}
