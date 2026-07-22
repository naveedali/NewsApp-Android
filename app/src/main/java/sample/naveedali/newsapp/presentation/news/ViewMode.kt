package sample.naveedali.newsapp.presentation.news

/** How the article collection is laid out on screen — toggled by the user via the app bar. */
enum class ViewMode {
    LIST,
    GRID;

    fun toggled(): ViewMode = if (this == LIST) GRID else LIST
}
