package sample.naveedali.newsapp.ui.news.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import sample.naveedali.newsapp.R
import sample.naveedali.newsapp.presentation.news.ViewMode
import sample.naveedali.newsapp.ui.news.NewsTestTags

/**
 * Single toggle button that flips [ViewMode]. Icon and content description both reflect the
 * mode the button will switch *to*, matching standard Material toggle-button conventions.
 */
@Composable
fun ViewModeToggleButton(
    currentMode: ViewMode,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val switchingToGrid = currentMode == ViewMode.LIST
    IconButton(
        onClick = onToggle,
        modifier = modifier.testTag(NewsTestTags.VIEW_MODE_TOGGLE),
    ) {
        Icon(
            imageVector = if (switchingToGrid) Icons.Filled.GridView else Icons.Filled.ViewList,
            contentDescription = stringResource(
                if (switchingToGrid) R.string.news_switch_to_grid_view else R.string.news_switch_to_list_view,
            ),
        )
    }
}
