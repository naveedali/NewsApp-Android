package sample.naveedali.newsapp.ui.news

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import sample.naveedali.newsapp.R
import sample.naveedali.newsapp.domain.model.Article
import sample.naveedali.newsapp.presentation.news.NewsContentState
import sample.naveedali.newsapp.presentation.news.NewsUiState
import sample.naveedali.newsapp.presentation.news.NewsViewModel
import sample.naveedali.newsapp.presentation.news.ViewMode
import sample.naveedali.newsapp.ui.news.components.NewsGridItem
import sample.naveedali.newsapp.ui.news.components.NewsListItem
import sample.naveedali.newsapp.ui.news.components.ViewModeToggleButton

/** Stateful entry point — collects [NewsViewModel] and delegates rendering to [NewsScreen]. */
@Composable
fun NewsRoute(
    viewModel: NewsViewModel,
    onArticleClick: (Article) -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    NewsScreen(
        uiState = uiState,
        onToggleViewMode = viewModel::onToggleViewMode,
        onRetry = { viewModel.loadHeadlines() },
        onArticleClick = onArticleClick,
        modifier = modifier,
    )
}

/**
 * Stateless screen — takes [NewsUiState] and callbacks only, so it can be rendered in
 * previews and Phase 5 UI tests without a real ViewModel or DI graph.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(
    uiState: NewsUiState,
    onToggleViewMode: () -> Unit,
    onRetry: () -> Unit,
    onArticleClick: (Article) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.news_top_headlines_title)) },
                actions = {
                    ViewModeToggleButton(
                        currentMode = uiState.viewMode,
                        onToggle = onToggleViewMode,
                    )
                },
            )
        },
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize(),
        ) {
            when (val content = uiState.content) {
                is NewsContentState.Loading -> LoadingContent()
                is NewsContentState.Error -> ErrorContent(message = content.message, onRetry = onRetry)
                is NewsContentState.Success -> ArticlesContent(
                    articles = content.articles,
                    viewMode = uiState.viewMode,
                    onArticleClick = onArticleClick,
                )
            }
        }
    }
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(modifier = Modifier.testTag(NewsTestTags.LOADING_INDICATOR))
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.testTag(NewsTestTags.ERROR_MESSAGE),
            )
            Button(
                onClick = onRetry,
                modifier = Modifier.testTag(NewsTestTags.RETRY_BUTTON),
            ) {
                Text(stringResource(R.string.news_retry))
            }
        }
    }
}

@Composable
private fun ArticlesContent(
    articles: List<Article>,
    viewMode: ViewMode,
    onArticleClick: (Article) -> Unit,
    modifier: Modifier = Modifier,
) {
    val contentPadding = PaddingValues(12.dp)
    when (viewMode) {
        ViewMode.LIST -> LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .testTag(NewsTestTags.NEWS_LIST),
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(articles, key = { it.id }) { article ->
                NewsListItem(
                    article = article,
                    onClick = onArticleClick,
                    modifier = Modifier.testTag(NewsTestTags.articleItem(article.id)),
                )
            }
        }

        ViewMode.GRID -> LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier
                .fillMaxSize()
                .testTag(NewsTestTags.NEWS_GRID),
            contentPadding = contentPadding,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            gridItems(articles, key = { it.id }) { article ->
                NewsGridItem(
                    article = article,
                    onClick = onArticleClick,
                    modifier = Modifier.testTag(NewsTestTags.articleItem(article.id)),
                )
            }
        }
    }
}
