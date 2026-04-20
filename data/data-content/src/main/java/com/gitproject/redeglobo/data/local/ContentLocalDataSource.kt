package com.gitproject.redeglobo.data.local

import com.gitproject.redeglobo.domain.model.Content
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface ContentLocalDataSource {
    fun getCachedContentRails(): Single<List<Content>>
    fun observeCachedContentRails(): Observable<List<Content>>
    fun cacheContentRails(contents: List<Content>): Completable
    fun getWatchlist(): Observable<List<Content>>
    fun addToWatchlist(content: Content): Completable
    fun removeFromWatchlist(contentId: String): Completable
}
