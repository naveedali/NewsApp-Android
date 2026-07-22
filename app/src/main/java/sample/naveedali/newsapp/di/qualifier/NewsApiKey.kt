package sample.naveedali.newsapp.di.qualifier

import javax.inject.Qualifier

/**
 * Disambiguates the NewsAPI key `String` binding from any other `String` Hilt might one day
 * be asked to provide (base URLs, feature flags, etc.) — using `@Named("...")` string
 * literals for this is easy to typo, this annotation isn't.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NewsApiKey
