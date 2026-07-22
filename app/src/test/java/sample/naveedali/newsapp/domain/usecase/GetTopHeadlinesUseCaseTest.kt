package sample.naveedali.newsapp.domain.usecase

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Test
import sample.naveedali.newsapp.domain.common.AppResult
import sample.naveedali.newsapp.domain.error.AppError
import sample.naveedali.newsapp.testutil.FakeNewsRepository
import sample.naveedali.newsapp.testutil.sampleArticle

class GetTopHeadlinesUseCaseTest {

    @Test
    fun `returns success with articles when repository has results`() = runTest {
        val articles = listOf(sampleArticle(id = "1"), sampleArticle(id = "2"))
        val repository = FakeNewsRepository(AppResult.Success(articles))
        val useCase = GetTopHeadlinesUseCase(repository)

        val result = useCase(country = "us")

        assertThat(result).isEqualTo(AppResult.Success(articles))
    }

    @Test
    fun `converts an empty but successful result into EmptyResult error`() = runTest {
        val repository = FakeNewsRepository(AppResult.Success(emptyList()))
        val useCase = GetTopHeadlinesUseCase(repository)

        val result = useCase(country = "us")

        assertThat(result).isInstanceOf(AppResult.Error::class.java)
        assertThat((result as AppResult.Error).error).isInstanceOf(AppError.EmptyResult::class.java)
    }

    @Test
    fun `propagates repository errors unchanged`() = runTest {
        val networkError = AppError.Network(cause = null)
        val repository = FakeNewsRepository(AppResult.Error(networkError))
        val useCase = GetTopHeadlinesUseCase(repository)

        val result = useCase(country = "us")

        assertThat(result).isEqualTo(AppResult.Error(networkError))
    }

    @Test
    fun `forwards country and category to the repository`() = runTest {
        val repository = FakeNewsRepository(AppResult.Success(listOf(sampleArticle())))
        val useCase = GetTopHeadlinesUseCase(repository)

        useCase(country = "gb", category = "technology")

        assertThat(repository.lastCountryRequested).isEqualTo("gb")
        assertThat(repository.lastCategoryRequested).isEqualTo("technology")
    }
}
