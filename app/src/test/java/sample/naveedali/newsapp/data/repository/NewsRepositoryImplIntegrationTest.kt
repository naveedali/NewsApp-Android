package sample.naveedali.newsapp.data.repository

import com.google.common.truth.Truth.assertThat
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import sample.naveedali.newsapp.data.remote.NewsApiService
import sample.naveedali.newsapp.domain.common.AppResult
import sample.naveedali.newsapp.domain.error.AppError
import sample.naveedali.newsapp.domain.repository.NewsRepository
import sample.naveedali.newsapp.testutil.FakeDispatcherProvider

/**
 * Integration test: runs [NewsRepositoryImpl] against a real Retrofit + OkHttp + kotlinx
 * serialization stack, talking to [MockWebServer] instead of the live NewsAPI. This is the
 * layer that catches wiring mistakes unit tests with fakes can't (wrong converter, wrong
 * base URL, response shapes that don't actually deserialize, etc.) — it runs on the plain
 * JVM, no emulator/device required.
 */
class NewsRepositoryImplIntegrationTest {

    private lateinit var server: MockWebServer

    // Typed as the interface so Kotlin resolves getTopHeadlines' default parameter values —
    // overriding functions can't redeclare defaults, so calling through the impl type directly
    // would require every argument to be passed explicitly.
    private lateinit var repository: NewsRepository

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()

        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
        val api = retrofit.create(NewsApiService::class.java)

        repository = NewsRepositoryImpl(
            api = api,
            apiKey = "test-api-key",
            dispatcherProvider = FakeDispatcherProvider(UnconfinedTestDispatcher()),
        )
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `parses a successful response into domain articles`() = runTest {
        server.enqueue(MockResponse().setBody(SUCCESS_BODY).setResponseCode(200))

        val result = repository.getTopHeadlines(country = "us")

        assertThat(result).isInstanceOf(AppResult.Success::class.java)
        val articles = (result as AppResult.Success).data
        assertThat(articles).hasSize(1)
        assertThat(articles.first().title).isEqualTo("Real headline")
        assertThat(articles.first().sourceName).isEqualTo("BBC News")

        val recordedRequest = server.takeRequest()
        assertThat(recordedRequest.path).contains("apiKey=test-api-key")
        assertThat(recordedRequest.path).contains("country=us")
    }

    @Test
    fun `maps NewsAPI's logical error envelope even when HTTP status is 200`() = runTest {
        server.enqueue(MockResponse().setBody(ERROR_ENVELOPE_BODY).setResponseCode(200))

        val result = repository.getTopHeadlines(country = "us")

        assertThat(result).isInstanceOf(AppResult.Error::class.java)
        val error = (result as AppResult.Error).error
        assertThat(error).isInstanceOf(AppError.Server::class.java)
        assertThat(error.message).isEqualTo("Your API key is invalid.")
    }

    @Test
    fun `maps non-2xx HTTP responses to a Server error carrying the status code`() = runTest {
        server.enqueue(MockResponse().setBody(ERROR_ENVELOPE_BODY).setResponseCode(401))

        val result = repository.getTopHeadlines(country = "us")

        assertThat(result).isInstanceOf(AppResult.Error::class.java)
        val error = (result as AppResult.Error).error
        assertThat(error).isInstanceOf(AppError.Server::class.java)
        assertThat((error as AppError.Server).code).isEqualTo(401)
    }

    @Test
    fun `maps malformed json to a Serialization error`() = runTest {
        server.enqueue(MockResponse().setBody("{ this is not valid json").setResponseCode(200))

        val result = repository.getTopHeadlines(country = "us")

        assertThat(result).isInstanceOf(AppResult.Error::class.java)
        assertThat((result as AppResult.Error).error).isInstanceOf(AppError.Serialization::class.java)
    }

    @Test
    fun `maps a dead connection to a Network error`() = runTest {
        server.shutdown()

        val result = repository.getTopHeadlines(country = "us")

        assertThat(result).isInstanceOf(AppResult.Error::class.java)
        assertThat((result as AppResult.Error).error).isInstanceOf(AppError.Network::class.java)
    }

    private companion object {
        const val SUCCESS_BODY = """
            {
              "status": "ok",
              "totalResults": 1,
              "articles": [
                {
                  "source": {"id": "bbc-news", "name": "BBC News"},
                  "author": "Jane Doe",
                  "title": "Real headline",
                  "description": "A description",
                  "url": "https://example.com/a",
                  "urlToImage": "https://example.com/a.jpg",
                  "publishedAt": "2026-07-22T09:15:00Z",
                  "content": "Full content"
                }
              ]
            }
        """

        const val ERROR_ENVELOPE_BODY = """
            {
              "status": "error",
              "code": "apiKeyInvalid",
              "message": "Your API key is invalid."
            }
        """
    }
}
