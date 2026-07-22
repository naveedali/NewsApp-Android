package sample.naveedali.newsapp.testutil

import sample.naveedali.newsapp.domain.model.Article

/**
 * androidTest's own copy of the domain fixture factory — androidTest and test are separate
 * Gradle source sets and don't share code by default, so this small helper is intentionally
 * duplicated rather than wiring up a shared source set for one function.
 */
fun sampleArticle(
    id: String = "https://example.com/article-1",
    title: String = "Sample headline",
): Article = Article(
    id = id,
    title = title,
    description = "Sample description",
    content = "Sample content",
    author = "Jane Doe",
    sourceName = "Example News",
    imageUrl = null,
    articleUrl = id,
    publishedAt = "2026-07-22T09:15:00Z",
)
