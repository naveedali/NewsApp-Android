package sample.naveedali.newsapp.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * Envelope NewsAPI wraps every top-headlines response in. `status` is "ok" or "error"; on
 * error NewsAPI still returns HTTP 200 in some cases and puts the real signal in this field
 * (`code` / `message`), which is why the repository checks `status` explicitly rather than
 * trusting the HTTP status code alone.
 */
@Serializable
data class TopHeadlinesResponseDto(
    val status: String,
    val totalResults: Int? = null,
    val articles: List<ArticleDto>? = null,
    val code: String? = null,
    val message: String? = null,
)
