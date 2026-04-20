package com.gitproject.redeglobo.data.repository

import com.gitproject.redeglobo.data.local.ContentLocalDataSource
import com.gitproject.redeglobo.data.remote.ContentRemoteDataSource
import com.gitproject.redeglobo.domain.model.Content
import com.gitproject.redeglobo.domain.model.ContentDetail
import com.gitproject.redeglobo.domain.repository.ContentRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContentRepositoryImpl @Inject constructor(
    private val remoteDataSource: ContentRemoteDataSource,
    private val localDataSource: ContentLocalDataSource
) : ContentRepository {

    override fun getContentRails(page: Int): Observable<List<Content>> =
        Observable.concat(
            localDataSource.getCachedContentRails().toObservable(),
            remoteDataSource.getContentRails(page)
                .doOnSuccess { localDataSource.cacheContentRails(it).subscribe() }
                .toObservable()
        )
        .distinctUntilChanged()
        .onErrorResumeNext { _: Throwable ->
            localDataSource.getCachedContentRails().toObservable()
        }

    override fun getContentDetail(id: String): Single<ContentDetail> =
        remoteDataSource.getContentDetail(id)

    override fun searchContent(query: String, page: Int): Single<List<Content>> =
        remoteDataSource.searchContent(query, page)

    override fun getWatchlist(): Observable<List<Content>> =
        localDataSource.getWatchlist()

    override fun addToWatchlist(content: Content): Completable =
        localDataSource.addToWatchlist(content)

    override fun removeFromWatchlist(contentId: String): Completable =
        localDataSource.removeFromWatchlist(contentId)
}
