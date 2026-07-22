package sample.naveedali.newsapp.presentation.news

import sample.naveedali.newsapp.domain.model.Article

/**
 * State of the article fetch itself. Kept separate from [ViewMode] because switching between
 * list/grid must never restart the fetch or reset scroll position.
 */
sealed interface NewsContentState {
    data object Loading : NewsContentState
    data class Success(val articles: List<Article>) : NewsContentState
    data class Error(val message: String) : NewsContentState
}

data class NewsUiState(
    val content: NewsContentState = NewsContentState.Loading,
    val viewMode: ViewMode = ViewMode.LIST,
)
