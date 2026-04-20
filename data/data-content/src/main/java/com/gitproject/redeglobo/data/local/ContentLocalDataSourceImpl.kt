package com.gitproject.redeglobo.data.local

import com.gitproject.redeglobo.database.dao.ContentDao
import com.gitproject.redeglobo.database.dao.WatchlistDao
import com.gitproject.redeglobo.data.mapper.ContentEntityMapper
import com.gitproject.redeglobo.domain.model.Content
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContentLocalDataSourceImpl @Inject constructor(
    private val contentDao: ContentDao,
    private val watchlistDao: WatchlistDao,
    private val mapper: ContentEntityMapper
) : ContentLocalDataSource {

    override fun getCachedContentRails(): Single<List<Content>> =
        contentDao.getCached()
            .map { entities -> entities.map { mapper.toDomain(it) } }

    override fun observeCachedContentRails(): Observable<List<Content>> =
        contentDao.observeAll()
            .map { entities -> entities.map { mapper.toDomain(it) } }
            .toObservable()

    override fun cacheContentRails(contents: List<Content>): Completable =
        contentDao.insertAll(contents.map { mapper.toEntity(it) })

    override fun getWatchlist(): Observable<List<Content>> =
        watchlistDao.observeWatchlist()
            .map { entities -> entities.map { mapper.fromWatchlistEntity(it) } }
            .toObservable()

    override fun addToWatchlist(content: Content): Completable =
        watchlistDao.insert(mapper.toWatchlistEntity(content))

    override fun removeFromWatchlist(contentId: String): Completable =
        watchlistDao.delete(contentId)
}
