package sample.naveedali.newsapp.ui.news

/**
 * Centralized Compose test tags. Kept as constants (rather than inline strings scattered
 * across composables) so Phase 5 UI tests and production code can't drift apart.
 */
object NewsTestTags {
    const val LOADING_INDICATOR = "news_loading_indicator"
    const val ERROR_MESSAGE = "news_error_message"
    const val RETRY_BUTTON = "news_retry_button"
    const val VIEW_MODE_TOGGLE = "news_view_mode_toggle"
    const val NEWS_LIST = "news_list"
    const val NEWS_GRID = "news_grid"

    fun articleItem(id: String) = "news_article_item_$id"
}
