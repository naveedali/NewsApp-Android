package sample.naveedali.newsapp.domain.common

import sample.naveedali.newsapp.domain.error.AppError

/**
 * A typed alternative to exceptions for expressing success/failure across layer boundaries.
 * Named "AppResult" (not "Result") to avoid clashing with kotlin.Result and to keep the
 * error type fixed to [AppError] so every layer handles the same taxonomy.
 */
sealed class AppResult<out T> {
    data class Success<out T>(val data: T) : AppResult<T>()
    data class Error(val error: AppError) : AppResult<Nothing>()
}

inline fun <T> AppResult<T>.onSuccess(action: (T) -> Unit): AppResult<T> {
    if (this is AppResult.Success) action(data)
    return this
}

inline fun <T> AppResult<T>.onError(action: (AppError) -> Unit): AppResult<T> {
    if (this is AppResult.Error) action(error)
    return this
}

inline fun <T, R> AppResult<T>.map(transform: (T) -> R): AppResult<R> = when (this) {
    is AppResult.Success -> AppResult.Success(transform(data))
    is AppResult.Error -> this
}

fun <T> AppResult<T>.getOrNull(): T? = (this as? AppResult.Success)?.data
