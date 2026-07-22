package sample.naveedali.newsapp.data.mapper

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import sample.naveedali.newsapp.data.remote.dto.ArticleDto
import sample.naveedali.newsapp.data.remote.dto.SourceDto

class ArticleMapperTest {

    private fun validDto(
        url: String = "https://example.com/a",
        title: String? = "A real headline",
    ) = ArticleDto(
        source = SourceDto(id = "bbc-news", name = "BBC News"),
        author = "Jane Doe",
        title = title,
        description = "A description",
        url = url,
        urlToImage = "https://example.com/a.jpg",
        publishedAt = "2026-07-22T09:15:00Z",
        content = "Full content",
    )

    @Test
    fun `maps a fully populated dto to the domain model`() {
        val dto = validDto()

        val article = dto.toDomain()

        assertThat(article).isNotNull()
        requireNotNull(article)
        assertThat(article.id).isEqualTo(dto.url)
        assertThat(article.articleUrl).isEqualTo(dto.url)
        assertThat(article.title).isEqualTo(dto.title)
        assertThat(article.sourceName).isEqualTo("BBC News")
        assertThat(article.imageUrl).isEqualTo(dto.urlToImage)
    }

    @Test
    fun `returns null when url is missing`() {
        val dto = validDto().copy(url = null)

        assertThat(dto.toDomain()).isNull()
    }

    @Test
    fun `returns null when url is blank`() {
        val dto = validDto(url = "   ")

        assertThat(dto.toDomain()).isNull()
    }

    @Test
    fun `returns null when title is missing`() {
        val dto = validDto(title = null)

        assertThat(dto.toDomain()).isNull()
    }

    @Test
    fun `returns null for NewsAPI's Removed placeholder title`() {
        val dto = validDto(title = "[Removed]")

        assertThat(dto.toDomain()).isNull()
    }

    @Test
    fun `blank optional fields are normalized to null`() {
        val dto = validDto().copy(description = "   ", author = "")

        val article = dto.toDomain()

        assertThat(article?.description).isNull()
        assertThat(article?.author).isNull()
    }

    @Test
    fun `list mapper drops invalid entries and keeps valid ones`() {
        val dtos = listOf(
            validDto(url = "https://example.com/1"),
            validDto(url = "https://example.com/2", title = "[Removed]"),
            validDto(url = "", title = "No url"),
        )

        val articles = dtos.toDomain()

        assertThat(articles).hasSize(1)
        assertThat(articles.single().id).isEqualTo("https://example.com/1")
    }
}
