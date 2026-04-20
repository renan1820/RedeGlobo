package com.gitproject.redeglobo.data

import com.gitproject.redeglobo.data.local.ContentLocalDataSource
import com.gitproject.redeglobo.data.remote.ContentRemoteDataSource
import com.gitproject.redeglobo.data.repository.ContentRepositoryImpl
import com.gitproject.redeglobo.domain.model.Content
import com.gitproject.redeglobo.domain.model.ContentStatus
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Test
import java.io.IOException

class ContentRepositoryImplTest {

    private val remoteDataSource: ContentRemoteDataSource = mockk()
    private val localDataSource: ContentLocalDataSource = mockk()
    private lateinit var repository: ContentRepositoryImpl

    private val fakeContents = listOf(
        Content("1", "Rick Sanchez", "url", ContentStatus.ALIVE, "Human", "Earth", 51)
    )

    @Before
    fun setup() {
        repository = ContentRepositoryImpl(remoteDataSource, localDataSource)
    }

    @Test
    fun `getContentRails emits cache first then network`() {
        val cachedContents = listOf(Content("cached", "Cached", "", ContentStatus.ALIVE, "", "", 0))
        every { localDataSource.getCachedContentRails() } returns Single.just(cachedContents)
        every { remoteDataSource.getContentRails(1) } returns Single.just(fakeContents)
        every { localDataSource.cacheContentRails(any()) } returns Completable.complete()

        val emissions = mutableListOf<List<Content>>()
        repository.getContentRails(1).subscribe { emissions.add(it) }

        assertThat(emissions).hasSize(2)
        assertThat(emissions[0]).isEqualTo(cachedContents)
        assertThat(emissions[1]).isEqualTo(fakeContents)
    }

    @Test
    fun `getContentRails caches remote results`() {
        every { localDataSource.getCachedContentRails() } returns Single.just(emptyList())
        every { remoteDataSource.getContentRails(1) } returns Single.just(fakeContents)
        every { localDataSource.cacheContentRails(fakeContents) } returns Completable.complete()

        repository.getContentRails(1).subscribe()

        verify { localDataSource.cacheContentRails(fakeContents) }
    }

    @Test
    fun `getContentRails falls back to cache on network error`() {
        every { localDataSource.getCachedContentRails() } returns Single.just(fakeContents)
        every { remoteDataSource.getContentRails(any()) } returns Single.error(IOException())

        val testObserver = repository.getContentRails(1).test()

        testObserver.assertNoErrors()
        assertThat(testObserver.values()).isNotEmpty()
    }
}
