package com.gitproject.redeglobo.home

import com.gitproject.redeglobo.domain.model.Content
import com.gitproject.redeglobo.domain.model.ContentStatus
import com.gitproject.redeglobo.domain.usecase.GetContentRailsUseCase
import com.gitproject.redeglobo.domain.usecase.GetWatchlistUseCase
import com.gitproject.redeglobo.home.presentation.HomeUiState
import com.gitproject.redeglobo.home.presentation.HomeViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.TestScheduler
import org.junit.After
import org.junit.Before
import org.junit.Test

class HomeViewModelTest {

    private val getContentRailsUseCase: GetContentRailsUseCase = mockk()
    private val getWatchlistUseCase: GetWatchlistUseCase = mockk()
    private val testScheduler = TestScheduler()
    private lateinit var viewModel: HomeViewModel

    private val fakeContents = listOf(
        Content("1", "Rick Sanchez", "url1", ContentStatus.ALIVE, "Human", "Earth C-137", 51),
        Content("2", "Morty Smith", "url2", ContentStatus.ALIVE, "Human", "Earth C-137", 51)
    )

    @Before
    fun setup() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { testScheduler }
        RxJavaPlugins.setIoSchedulerHandler { testScheduler }
        RxJavaPlugins.setComputationSchedulerHandler { testScheduler }

        every { getWatchlistUseCase.execute(Unit) } returns Observable.just(emptyList<Content>())
            .subscribeOn(testScheduler)
        every { getContentRailsUseCase.execute(any()) } returns Observable.just(fakeContents)
            .subscribeOn(testScheduler)

        viewModel = HomeViewModel(getContentRailsUseCase, getWatchlistUseCase)
    }

    @Test
    fun `initial state is Loading`() {
        assertThat(viewModel.uiState.value).isInstanceOf(HomeUiState.Loading::class.java)
    }

    @Test
    fun `state becomes Success after scheduler advances`() {
        testScheduler.triggerActions()
        assertThat(viewModel.uiState.value).isInstanceOf(HomeUiState.Success::class.java)
    }

    @Test
    fun `success state contains correct number of rails`() {
        testScheduler.triggerActions()
        val state = viewModel.uiState.value as HomeUiState.Success
        assertThat(state.rails).isNotEmpty()
    }

    @Test
    fun `error from useCase produces Error state`() {
        every { getContentRailsUseCase.execute(any()) } returns Observable.error<List<Content>>(
            RuntimeException("Network fail")
        ).subscribeOn(testScheduler)
        viewModel = HomeViewModel(getContentRailsUseCase, getWatchlistUseCase)

        testScheduler.triggerActions()

        assertThat(viewModel.uiState.value).isInstanceOf(HomeUiState.Error::class.java)
    }

    @Test
    fun `error state contains error message`() {
        val errorMessage = "Network fail"
        every { getContentRailsUseCase.execute(any()) } returns Observable.error<List<Content>>(
            RuntimeException(errorMessage)
        ).subscribeOn(testScheduler)
        viewModel = HomeViewModel(getContentRailsUseCase, getWatchlistUseCase)

        testScheduler.triggerActions()

        val state = viewModel.uiState.value as HomeUiState.Error
        assertThat(state.message).isEqualTo(errorMessage)
    }

    @After
    fun tearDown() {
        RxAndroidPlugins.reset()
        RxJavaPlugins.reset()
    }
}
