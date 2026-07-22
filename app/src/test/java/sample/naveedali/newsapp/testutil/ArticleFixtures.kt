package sample.naveedali.newsapp.testutil

import sample.naveedali.newsapp.domain.model.Article

/** Shared factory for domain-level test data — keeps individual test bodies focused on intent. */
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
    imageUrl = "https://example.com/image.jpg",
    articleUrl = id,
    publishedAt = "2026-07-22T09:15:00Z",
)
