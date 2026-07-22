package sample.naveedali.newsapp.testutil

import sample.naveedali.newsapp.domain.common.AppResult
import sample.naveedali.newsapp.domain.model.Article
import sample.naveedali.newsapp.domain.repository.NewsRepository

/**
 * Test double standing in for the Retrofit-backed repository. Returns whatever [result] is
 * configured to return and records the last call args so tests can assert on them.
 */
class FakeNewsRepository(
    private var result: AppResult<List<Article>> = AppResult.Success(emptyList()),
) : NewsRepository {

    var lastCountryRequested: String? = null
        private set
    var lastCategoryRequested: String? = null
        private set
    var callCount: Int = 0
        private set

    fun setResult(newResult: AppResult<List<Article>>) {
        result = newResult
    }

    override suspend fun getTopHeadlines(
        country: String,
        category: String?,
    ): AppResult<List<Article>> {
        callCount++
        lastCountryRequested = country
        lastCategoryRequested = category
        return result
    }
}
