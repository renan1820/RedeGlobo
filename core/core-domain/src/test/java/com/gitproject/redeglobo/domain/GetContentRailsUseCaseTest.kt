package com.gitproject.redeglobo.domain

import com.gitproject.redeglobo.domain.model.Content
import com.gitproject.redeglobo.domain.model.ContentStatus
import com.gitproject.redeglobo.domain.repository.ContentRepository
import com.gitproject.redeglobo.domain.usecase.GetContentRailsUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Test

class GetContentRailsUseCaseTest {

    private val repository: ContentRepository = mockk()
    private lateinit var useCase: GetContentRailsUseCase

    private val fakeContent = Content(
        id = "1",
        title = "Rick Sanchez",
        thumbnailUrl = "https://example.com/rick.png",
        status = ContentStatus.ALIVE,
        genre = "Human",
        originName = "Earth",
        episodeCount = 51
    )

    @Before
    fun setup() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        useCase = GetContentRailsUseCase(repository)
    }

    @After
    fun tearDown() {
        RxAndroidPlugins.reset()
        RxJavaPlugins.reset()
    }

    @Test
    fun `execute returns content list on success`() {
        val expected = listOf(fakeContent)
        every { repository.getContentRails(1) } returns Observable.just(expected)

        val result = useCase.execute(GetContentRailsUseCase.Params(page = 1)).test()

        result.assertValue(expected)
        result.assertNoErrors()
        verify(exactly = 1) { repository.getContentRails(1) }
    }

    @Test
    fun `execute propagates repository error`() {
        val error = RuntimeException("Network error")
        every { repository.getContentRails(any()) } returns Observable.error(error)

        val result = useCase.execute(GetContentRailsUseCase.Params(page = 1)).test()

        result.assertError(RuntimeException::class.java)
    }

    @Test
    fun `execute returns empty list when repository returns empty`() {
        every { repository.getContentRails(any()) } returns Observable.just(emptyList())

        val result = useCase.execute(GetContentRailsUseCase.Params(page = 1)).test()

        result.assertValue { it.isEmpty() }
    }

    @Test
    fun `execute uses correct page parameter`() {
        every { repository.getContentRails(3) } returns Observable.just(listOf(fakeContent))

        useCase.execute(GetContentRailsUseCase.Params(page = 3)).test()

        verify(exactly = 1) { repository.getContentRails(3) }
    }
}
