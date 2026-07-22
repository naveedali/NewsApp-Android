package sample.naveedali.newsapp.presentation.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import sample.naveedali.newsapp.domain.common.AppResult
import sample.naveedali.newsapp.domain.usecase.GetTopHeadlinesUseCase
import javax.inject.Inject

/** Owns [NewsUiState] for the headlines screen, backed by the Hilt DI graph. */
@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getTopHeadlines: GetTopHeadlinesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewsUiState())
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()

    init {
        loadHeadlines()
    }

    /** Re-fetches headlines, e.g. on first load or when the user taps "retry". */
    fun loadHeadlines(country: String = "us") {
        viewModelScope.launch {
            _uiState.update { it.copy(content = NewsContentState.Loading) }
            when (val result = getTopHeadlines(country = country)) {
                is AppResult.Success -> _uiState.update {
                    it.copy(content = NewsContentState.Success(result.data))
                }
                is AppResult.Error -> _uiState.update {
                    it.copy(content = NewsContentState.Error(result.error.message ?: "Something went wrong."))
                }
            }
        }
    }

    /** Toggles list/grid layout without touching the fetched content or triggering a refetch. */
    fun onToggleViewMode() {
        _uiState.update { it.copy(viewMode = it.viewMode.toggled()) }
    }
}
