package sample.naveedali.newsapp.ui.news

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

private val displayFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("MMM d, yyyy · h:mm a").withZone(ZoneId.systemDefault())

/**
 * Formats NewsAPI's ISO-8601 `publishedAt` (e.g. "2026-07-22T09:15:00Z") into a readable
 * local string. Falls back to the raw value if parsing fails, or "" if the value is blank,
 * so a malformed date never crashes the UI — it just looks slightly wrong.
 */
fun formatPublishedAt(publishedAt: String): String {
    if (publishedAt.isBlank()) return ""
    return try {
        displayFormatter.format(Instant.parse(publishedAt))
    } catch (e: DateTimeParseException) {
        publishedAt
    }
}
