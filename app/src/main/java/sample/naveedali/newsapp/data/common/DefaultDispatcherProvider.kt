package sample.naveedali.newsapp.data.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import sample.naveedali.newsapp.domain.common.DispatcherProvider
import javax.inject.Inject

/** Production [DispatcherProvider] — the only place `Dispatchers.*` is referenced directly. */
class DefaultDispatcherProvider @Inject constructor() : DispatcherProvider {
    override val main: CoroutineDispatcher = Dispatchers.Main
    override val io: CoroutineDispatcher = Dispatchers.IO
    override val default: CoroutineDispatcher = Dispatchers.Default
}
