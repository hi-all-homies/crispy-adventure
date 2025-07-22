package ru.stanise.animebrowsing.config

import androidx.compose.ui.graphics.Color

object Config {
    const val BASE_URL = "https://api.jikan.moe/v4/"

    const val LIMIT = 25

    const val TIMEOUT_MILLIS = 5_000L

    val CHART_COLORS = listOf(
        Color(0xFFEF5350), // Red
        Color(0xFFAB47BC), // Purple
        Color(0xFF5C6BC0), // Indigo
        Color(0xFF29B6F6), // Light Blue
        Color(0xFF26A69A), // Teal
        Color(0xFF66BB6A), // Green
        Color(0xFF9CCC65), // Light Green
        Color(0xFFD4E157), // Lime
        Color(0xFFFFCA28), // Amber
        Color(0xFFFFA726), // Orange
        Color(0xFFFF7043), // Deep Orange
        Color(0xFFA1887F), // Brown
        Color(0xFF90A4AE), // Blue Grey
        Color(0xFFBA68C8), // Light Purple
        Color(0xFF4DB6AC), // Aqua
        Color(0xFF81C784), // Soft Green
        Color(0xFFFF8A65), // Coral
        Color(0xFFF06292), // Pink
        Color(0xFF7986CB), // Dusty Indigo
        Color(0xFF2C4A60)  // Mint Green
    )

}