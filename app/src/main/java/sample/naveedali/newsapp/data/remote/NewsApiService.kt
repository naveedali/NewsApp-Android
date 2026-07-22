package sample.naveedali.newsapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import sample.naveedali.newsapp.data.remote.dto.TopHeadlinesResponseDto

/**
 * Thin Retrofit interface — no logic here. Base URL ("https://newsapi.org/v2/") is configured
 * where the Retrofit instance is built (Phase 4 DI module).
 */
interface NewsApiService {

    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String,
        @Query("category") category: String?,
        @Query("apiKey") apiKey: String,
    ): TopHeadlinesResponseDto
}
