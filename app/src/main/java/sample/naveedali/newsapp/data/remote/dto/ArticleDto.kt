package sample.naveedali.newsapp.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * 1:1 mirror of a single element in NewsAPI's `articles` array. Field names match the JSON
 * response exactly, so no `@SerialName` overrides are needed. Everything except `title` is
 * nullable because NewsAPI is inconsistent about which fields are present.
 */
@Serializable
data class ArticleDto(
    val source: SourceDto? = null,
    val author: String? = null,
    val title: String? = null,
    val description: String? = null,
    val url: String? = null,
    val urlToImage: String? = null,
    val publishedAt: String? = null,
    val content: String? = null,
)
