package sample.naveedali.newsapp.testutil

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * Swaps [Dispatchers.Main] for a [TestDispatcher] around each test so `viewModelScope.launch`
 * (which normally targets `Dispatchers.Main.immediate`) runs on a deterministic, virtual-time
 * scheduler instead of crashing with "Main looper not available" on the JVM.
 *
 * Uses [UnconfinedTestDispatcher] so coroutines launched in `init {}` blocks (like
 * [sample.naveedali.newsapp.presentation.news.NewsViewModel]'s initial load) run eagerly and
 * are visible to assertions immediately, without every test needing `advanceUntilIdle()`.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    val testDispatcher: TestDispatcher = UnconfinedTestDispatcher(),
) : TestWatcher() {

    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
