package sample.naveedali.newsapp.domain.common

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Abstraction over [kotlinx.coroutines.Dispatchers] so use cases, repositories and view
 * models never reference `Dispatchers.IO` / `Dispatchers.Main` directly. This is what makes
 * them testable with a deterministic test dispatcher instead of the real thread pools.
 *
 * The default implementation lives in the data/DI layer (Phase 2/4); tests provide a fake
 * that points every field at `StandardTestDispatcher` / `UnconfinedTestDispatcher`.
 */
interface DispatcherProvider {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
}
