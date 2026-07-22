package sample.naveedali.newsapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/** Root of the Hilt DI graph — required for any `@AndroidEntryPoint`/`@HiltViewModel` to work. */
@HiltAndroidApp
class NewsApplication : Application()
