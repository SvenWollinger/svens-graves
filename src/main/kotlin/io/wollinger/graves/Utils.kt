package io.wollinger.graves

object Utils {
    fun formatTime(seconds: Int): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60

        val parts = mutableListOf<String>()
        if (hours > 0) parts.add("${hours}h")
        if (minutes > 0) parts.add("${minutes}m")
        if (secs > 0 || parts.isEmpty()) parts.add("${secs}s")

        return parts.joinToString(" ")
    }
}