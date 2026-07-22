package sample.naveedali.newsapp.testutil

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestDispatcher
import sample.naveedali.newsapp.domain.common.DispatcherProvider

/** Routes every dispatcher through the same [TestDispatcher] so coroutine tests are deterministic. */
class FakeDispatcherProvider(dispatcher: TestDispatcher) : DispatcherProvider {
    override val main: CoroutineDispatcher = dispatcher
    override val io: CoroutineDispatcher = dispatcher
    override val default: CoroutineDispatcher = dispatcher
}
