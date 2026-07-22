package sample.naveedali.newsapp.data.repository

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import sample.naveedali.newsapp.data.mapper.toDomain
import sample.naveedali.newsapp.data.remote.NewsApiService
import sample.naveedali.newsapp.domain.common.AppResult
import sample.naveedali.newsapp.domain.common.DispatcherProvider
import sample.naveedali.newsapp.domain.error.AppError
import sample.naveedali.newsapp.domain.model.Article
import sample.naveedali.newsapp.domain.repository.NewsRepository
import sample.naveedali.newsapp.di.qualifier.NewsApiKey
import java.io.IOException
import javax.inject.Inject

/**
 * Retrofit-backed [NewsRepository]. All exceptions that can come out of the network call are
 * caught here and normalized to [AppError] — nothing above this class ever sees an
 * [IOException], [HttpException] or [SerializationException] directly.
 *
 * NewsAPI can return `status: "error"` with an HTTP 200 in some edge cases, so that field is
 * checked explicitly rather than trusting the HTTP status code alone.
 */
class NewsRepositoryImpl @Inject constructor(
    private val api: NewsApiService,
    @NewsApiKey private val apiKey: String,
    private val dispatcherProvider: DispatcherProvider,
) : NewsRepository {

    override suspend fun getTopHeadlines(
        country: String,
        category: String?,
    ): AppResult<List<Article>> = withContext(dispatcherProvider.io) {
        try {
            val response = api.getTopHeadlines(
                country = country,
                category = category,
                apiKey = apiKey,
            )

            if (response.status == "ok") {
                AppResult.Success(response.articles.orEmpty().toDomain())
            } else {
                AppResult.Error(
                    AppError.Server(
                        code = 0,
                        serverMessage = response.message ?: "NewsAPI returned an error response.",
                    ),
                )
            }
        } catch (e: HttpException) {
            AppResult.Error(AppError.Server(code = e.code(), serverMessage = e.message(), cause = e))
        } catch (e: IOException) {
            // No connectivity, DNS failure, socket timeout, etc.
            AppResult.Error(AppError.Network(e))
        } catch (e: SerializationException) {
            AppResult.Error(AppError.Serialization(e))
        } catch (e: CancellationException) {
            // Never swallow coroutine cancellation — rethrow so structured concurrency works.
            throw e
        } catch (e: Exception) {
            AppResult.Error(AppError.Unknown(e))
        }
    }
}
