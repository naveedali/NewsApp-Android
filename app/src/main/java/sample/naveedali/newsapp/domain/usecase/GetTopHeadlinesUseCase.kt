package sample.naveedali.newsapp.domain.usecase

import sample.naveedali.newsapp.domain.common.AppResult
import sample.naveedali.newsapp.domain.error.AppError
import sample.naveedali.newsapp.domain.model.Article
import sample.naveedali.newsapp.domain.repository.NewsRepository
import javax.inject.Inject

/**
 * Fetches top headlines and applies the one piece of business logic that belongs above the
 * repository: an empty-but-successful response is treated as [AppError.EmptyResult] so the
 * UI has a single "no data" signal instead of having to special-case an empty list itself.
 *
 * `invoke` operator lets callers use this as `getTopHeadlines(country = "us")`.
 */
class GetTopHeadlinesUseCase @Inject constructor(
    private val repository: NewsRepository,
) {
    suspend operator fun invoke(
        country: String = "us",
        category: String? = null,
    ): AppResult<List<Article>> {
        val result = repository.getTopHeadlines(country, category)
        return if (result is AppResult.Success && result.data.isEmpty()) {
            AppResult.Error(AppError.EmptyResult)
        } else {
            result
        }
    }
}
