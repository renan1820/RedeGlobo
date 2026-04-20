package com.gitproject.redeglobo.search

import com.gitproject.redeglobo.domain.model.Content
import com.gitproject.redeglobo.domain.model.ContentStatus
import com.gitproject.redeglobo.domain.usecase.SearchContentUseCase
import com.gitproject.redeglobo.search.presentation.SearchUiState
import com.gitproject.redeglobo.search.presentation.SearchViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.TestScheduler
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit

class SearchViewModelTest {

    private val searchContentUseCase: SearchContentUseCase = mockk()
    private val testScheduler = TestScheduler()
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { testScheduler }
        RxJavaPlugins.setIoSchedulerHandler { testScheduler }
        RxJavaPlugins.setComputationSchedulerHandler { testScheduler }

        viewModel = SearchViewModel(searchContentUseCase)
    }

    @Test
    fun `initial state is Idle`() {
        assertThat(viewModel.uiState.value).isInstanceOf(SearchUiState.Idle::class.java)
    }

    @Test
    fun `blank query resets to Idle state`() {
        viewModel.onQueryChanged("")
        assertThat(viewModel.uiState.value).isInstanceOf(SearchUiState.Idle::class.java)
    }

    @Test
    fun `query debounces 300ms before searching`() {
        every { searchContentUseCase.execute(any()) } returns Single.just(emptyList())

        viewModel.onQueryChanged("Rick")
        testScheduler.advanceTimeBy(200, TimeUnit.MILLISECONDS)
        assertThat(viewModel.uiState.value).isInstanceOf(SearchUiState.Idle::class.java)

        testScheduler.advanceTimeBy(200, TimeUnit.MILLISECONDS)
        assertThat(viewModel.uiState.value).isInstanceOf(SearchUiState.Empty::class.java)
    }

    @Test
    fun `successful search produces Success state`() {
        val results = listOf(Content("1", "Rick", "url", ContentStatus.ALIVE, "Human", "Earth", 51))
        every { searchContentUseCase.execute(any()) } returns Single.just(results)

        viewModel.onQueryChanged("Rick")
        testScheduler.advanceTimeBy(500, TimeUnit.MILLISECONDS)

        assertThat(viewModel.uiState.value).isInstanceOf(SearchUiState.Success::class.java)
    }

    @Test
    fun `empty results produce Empty state`() {
        every { searchContentUseCase.execute(any()) } returns Single.just(emptyList())

        viewModel.onQueryChanged("xyz")
        testScheduler.advanceTimeBy(500, TimeUnit.MILLISECONDS)

        assertThat(viewModel.uiState.value).isInstanceOf(SearchUiState.Empty::class.java)
    }

    @Test
    fun `error produces Error state`() {
        every { searchContentUseCase.execute(any()) } returns Single.error(RuntimeException())

        viewModel.onQueryChanged("Rick")
        testScheduler.advanceTimeBy(500, TimeUnit.MILLISECONDS)

        assertThat(viewModel.uiState.value).isInstanceOf(SearchUiState.Error::class.java)
    }

    @After
    fun tearDown() {
        RxAndroidPlugins.reset()
        RxJavaPlugins.reset()
    }
}
