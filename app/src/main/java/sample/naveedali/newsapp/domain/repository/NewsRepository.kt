package sample.naveedali.newsapp.domain.repository

import sample.naveedali.newsapp.domain.common.AppResult
import sample.naveedali.newsapp.domain.model.Article

/**
 * Boundary between the domain and data layers. The domain layer only depends on this
 * interface; Phase 2 provides the Retrofit-backed implementation, and unit tests provide a
 * fake/in-memory implementation — neither needs to know about the other.
 */
interface NewsRepository {

    /**
     * @param country two-letter ISO 3166-1 country code, e.g. "us".
     * @param category optional NewsAPI category filter (business, technology, sports, ...).
     */
    suspend fun getTopHeadlines(
        country: String = "us",
        category: String? = null,
    ): AppResult<List<Article>>
}
