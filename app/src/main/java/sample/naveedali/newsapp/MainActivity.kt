package sample.naveedali.newsapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import sample.naveedali.newsapp.ui.news.NewsRoute
import sample.naveedali.newsapp.ui.theme.NewsAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewsAppTheme {
                NewsRoute(
                    viewModel = hiltViewModel(),
                    onArticleClick = { article ->
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(article.articleUrl)))
                    },
                )
            }
        }
    }
}
