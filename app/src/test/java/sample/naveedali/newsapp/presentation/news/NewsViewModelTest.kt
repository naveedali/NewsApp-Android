package sample.naveedali.newsapp.presentation.news

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import sample.naveedali.newsapp.domain.common.AppResult
import sample.naveedali.newsapp.domain.error.AppError
import sample.naveedali.newsapp.domain.usecase.GetTopHeadlinesUseCase
import sample.naveedali.newsapp.testutil.FakeNewsRepository
import sample.naveedali.newsapp.testutil.MainDispatcherRule
import sample.naveedali.newsapp.testutil.sampleArticle

class NewsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun createViewModel(repository: FakeNewsRepository): NewsViewModel =
        NewsViewModel(GetTopHeadlinesUseCase(repository))

    @Test
    fun `initial load populates success state with articles from the repository`() = runTest {
        val articles = listOf(sampleArticle(id = "1"), sampleArticle(id = "2"))
        val repository = FakeNewsRepository(AppResult.Success(articles))

        val viewModel = createViewModel(repository)

        val state = viewModel.uiState.value
        assertThat(state.content).isEqualTo(NewsContentState.Success(articles))
        assertThat(state.viewMode).isEqualTo(ViewMode.LIST)
    }

    @Test
    fun `initial load populates error state when the repository fails`() = runTest {
        val repository = FakeNewsRepository(AppResult.Error(AppError.Network(cause = null)))

        val viewModel = createViewModel(repository)

        val content = viewModel.uiState.value.content
        assertThat(content).isInstanceOf(NewsContentState.Error::class.java)
        assertThat((content as NewsContentState.Error).message)
            .isEqualTo(AppError.Network(cause = null).message)
    }

    @Test
    fun `onToggleViewMode flips list and grid without touching content`() = runTest {
        val articles = listOf(sampleArticle())
        val repository = FakeNewsRepository(AppResult.Success(articles))
        val viewModel = createViewModel(repository)
        val contentBeforeToggle = viewModel.uiState.value.content

        viewModel.onToggleViewMode()
        assertThat(viewModel.uiState.value.viewMode).isEqualTo(ViewMode.GRID)
        assertThat(viewModel.uiState.value.content).isEqualTo(contentBeforeToggle)

        viewModel.onToggleViewMode()
        assertThat(viewModel.uiState.value.viewMode).isEqualTo(ViewMode.LIST)
    }

    @Test
    fun `loadHeadlines re-fetches from the repository`() = runTest {
        val repository = FakeNewsRepository(AppResult.Success(listOf(sampleArticle(id = "1"))))
        val viewModel = createViewModel(repository)
        assertThat(repository.callCount).isEqualTo(1)

        repository.setResult(AppResult.Success(listOf(sampleArticle(id = "2"))))
        viewModel.loadHeadlines(country = "gb")

        assertThat(repository.callCount).isEqualTo(2)
        assertThat(repository.lastCountryRequested).isEqualTo("gb")
        assertThat((viewModel.uiState.value.content as NewsContentState.Success).articles.first().id)
            .isEqualTo("2")
    }
}
