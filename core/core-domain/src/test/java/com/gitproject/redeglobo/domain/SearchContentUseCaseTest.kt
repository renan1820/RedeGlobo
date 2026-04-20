package com.gitproject.redeglobo.domain

import com.gitproject.redeglobo.domain.model.Content
import com.gitproject.redeglobo.domain.model.ContentStatus
import com.gitproject.redeglobo.domain.repository.ContentRepository
import com.gitproject.redeglobo.domain.usecase.SearchContentUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Test

class SearchContentUseCaseTest {

    private val repository: ContentRepository = mockk()
    private lateinit var useCase: SearchContentUseCase

    @Before
    fun setup() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        useCase = SearchContentUseCase(repository)
    }

    @After
    fun tearDown() {
        RxAndroidPlugins.reset()
        RxJavaPlugins.reset()
    }

    @Test
    fun `execute searches with correct query`() {
        val query = "Rick"
        val expected = listOf(
            Content("1", "Rick Sanchez", "", ContentStatus.ALIVE, "Human", "Earth", 51)
        )
        every { repository.searchContent(query, 1) } returns Single.just(expected)

        useCase.execute(SearchContentUseCase.Params(query = query))
            .test()
            .assertValue(expected)

        verify { repository.searchContent(query, 1) }
    }

    @Test
    fun `execute returns empty list for unknown query`() {
        every { repository.searchContent(any(), any()) } returns Single.just(emptyList())

        useCase.execute(SearchContentUseCase.Params("xyzunknown"))
            .test()
            .assertValue { it.isEmpty() }
    }

    @Test
    fun `execute propagates network error`() {
        every { repository.searchContent(any(), any()) } returns Single.error(RuntimeException())

        useCase.execute(SearchContentUseCase.Params("Rick"))
            .test()
            .assertError(RuntimeException::class.java)
    }
}
