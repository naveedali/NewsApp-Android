package sample.naveedali.newsapp.domain.error

/**
 * Domain-level error taxonomy. Every failure that can reach the UI is normalized into one
 * of these cases by the data layer, so the presentation layer never has to deal with
 * Retrofit/IO/serialization exceptions directly.
 */
sealed class AppError(message: String, cause: Throwable? = null) : Exception(message, cause) {

    /** No connectivity, DNS failure, timeout, socket errors, etc. */
    class Network(cause: Throwable? = null) :
        AppError("No internet connection or the network request timed out.", cause)

    /** Server responded with a non-2xx HTTP status. */
    class Server(val code: Int, serverMessage: String? = null, cause: Throwable? = null) :
        AppError(serverMessage ?: "The server returned an error (HTTP $code).", cause)

    /** Response body could not be parsed into the expected shape. */
    class Serialization(cause: Throwable? = null) :
        AppError("Received a response we couldn't understand.", cause)

    /** Request succeeded but returned no usable results. */
    object EmptyResult : AppError("No articles were found.")

    /** Anything that doesn't fit the categories above. */
    class Unknown(cause: Throwable? = null) :
        AppError(cause?.message ?: "An unexpected error occurred.", cause)
}
