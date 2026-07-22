package sample.naveedali.newsapp.ui.news

import androidx.compose.ui.test.assertDoesNotExist
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import sample.naveedali.newsapp.domain.model.Article
import sample.naveedali.newsapp.presentation.news.NewsContentState
import sample.naveedali.newsapp.presentation.news.NewsUiState
import sample.naveedali.newsapp.presentation.news.ViewMode
import sample.naveedali.newsapp.testutil.sampleArticle
import sample.naveedali.newsapp.ui.theme.NewsAppTheme

/**
 * Exercises the *stateless* [NewsScreen] directly (no ViewModel, no Hilt) — it takes
 * [NewsUiState] and plain callbacks, so these tests only need the Compose test rule and run
 * fast without an DI graph or network stack in the loop.
 */
@RunWith(AndroidJUnit4::class)
class NewsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun setScreen(
        uiState: NewsUiState,
        onToggleViewMode: () -> Unit = {},
        onRetry: () -> Unit = {},
        onArticleClick: (Article) -> Unit = {},
    ) {
        composeTestRule.setContent {
            NewsAppTheme {
                NewsScreen(
                    uiState = uiState,
                    onToggleViewMode = onToggleViewMode,
                    onRetry = onRetry,
                    onArticleClick = onArticleClick,
                )
            }
        }
    }

    @Test
    fun loadingState_showsLoadingIndicator() {
        setScreen(uiState = NewsUiState(content = NewsContentState.Loading))

        composeTestRule.onNodeWithTag(NewsTestTags.LOADING_INDICATOR).assertIsDisplayed()
        composeTestRule.onNodeWithTag(NewsTestTags.NEWS_LIST).assertDoesNotExist()
    }

    @Test
    fun errorState_showsMessage_andRetryInvokesCallback() {
        var retried = false
        setScreen(
            uiState = NewsUiState(content = NewsContentState.Error("Network trouble")),
            onRetry = { retried = true },
        )

        composeTestRule.onNodeWithTag(NewsTestTags.ERROR_MESSAGE).assertIsDisplayed()
        composeTestRule.onNodeWithText("Network trouble").assertIsDisplayed()

        composeTestRule.onNodeWithTag(NewsTestTags.RETRY_BUTTON).performClick()
        assertTrue("Retry button click should invoke onRetry", retried)
    }

    @Test
    fun successState_listMode_showsListAndArticleTitles() {
        val articles = listOf(sampleArticle(id = "1", title = "Headline One"))
        setScreen(
            uiState = NewsUiState(
                content = NewsContentState.Success(articles),
                viewMode = ViewMode.LIST,
            ),
        )

        composeTestRule.onNodeWithTag(NewsTestTags.NEWS_LIST).assertIsDisplayed()
        composeTestRule.onNodeWithTag(NewsTestTags.NEWS_GRID).assertDoesNotExist()
        composeTestRule.onNodeWithText("Headline One").assertIsDisplayed()
    }

    @Test
    fun successState_gridMode_showsGridInsteadOfList() {
        val articles = listOf(sampleArticle(id = "1", title = "Headline One"))
        setScreen(
            uiState = NewsUiState(
                content = NewsContentState.Success(articles),
                viewMode = ViewMode.GRID,
            ),
        )

        composeTestRule.onNodeWithTag(NewsTestTags.NEWS_GRID).assertIsDisplayed()
        composeTestRule.onNodeWithTag(NewsTestTags.NEWS_LIST).assertDoesNotExist()
    }

    @Test
    fun viewModeToggleButton_invokesCallback() {
        var toggleCount = 0
        setScreen(
            uiState = NewsUiState(content = NewsContentState.Success(listOf(sampleArticle()))),
            onToggleViewMode = { toggleCount++ },
        )

        composeTestRule.onNodeWithTag(NewsTestTags.VIEW_MODE_TOGGLE).performClick()

        assertEquals(1, toggleCount)
    }

    @Test
    fun clickingArticle_invokesOnArticleClickWithThatArticle() {
        val target = sampleArticle(id = "https://example.com/target", title = "Target headline")
        val other = sampleArticle(id = "https://example.com/other", title = "Other headline")
        var clicked: Article? = null
        setScreen(
            uiState = NewsUiState(content = NewsContentState.Success(listOf(target, other))),
            onArticleClick = { clicked = it },
        )

        composeTestRule.onNodeWithTag(NewsTestTags.articleItem(target.id)).performClick()

        assertEquals(target, clicked)
    }
}
