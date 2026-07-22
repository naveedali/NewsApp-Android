package sample.naveedali.newsapp.domain.model

/**
 * Domain representation of a news article — deliberately decoupled from the NewsAPI response
 * shape (see data/remote/dto). `id` is derived from `url` since NewsAPI does not provide a
 * stable identifier, which keeps LazyColumn/LazyVerticalGrid keys stable across recompositions.
 */
data class Article(
    val id: String,
    val title: String,
    val description: String?,
    val content: String?,
    val author: String?,
    val sourceName: String?,
    val imageUrl: String?,
    val articleUrl: String,
    val publishedAt: String,
)
