package sample.naveedali.newsapp.data.mapper

import sample.naveedali.newsapp.data.remote.dto.ArticleDto
import sample.naveedali.newsapp.domain.model.Article

/**
 * Maps a single DTO to the domain model, or `null` if the DTO is missing fields the domain
 * treats as required (`url` — used as the stable id — and `title`). NewsAPI occasionally
 * returns "[Removed]" placeholder articles with null/garbage fields; those are dropped here
 * rather than leaking into the UI.
 */
fun ArticleDto.toDomain(): Article? {
    val safeUrl = url?.takeIf { it.isNotBlank() } ?: return null
    val safeTitle = title?.takeIf { it.isNotBlank() && it != "[Removed]" } ?: return null

    return Article(
        id = safeUrl,
        title = safeTitle,
        description = description?.takeIf { it.isNotBlank() },
        content = content?.takeIf { it.isNotBlank() },
        author = author?.takeIf { it.isNotBlank() },
        sourceName = source?.name?.takeIf { it.isNotBlank() },
        imageUrl = urlToImage?.takeIf { it.isNotBlank() },
        articleUrl = safeUrl,
        publishedAt = publishedAt.orEmpty(),
    )
}

fun List<ArticleDto>.toDomain(): List<Article> = mapNotNull { it.toDomain() }
