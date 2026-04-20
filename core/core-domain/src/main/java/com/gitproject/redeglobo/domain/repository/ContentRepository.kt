package com.gitproject.redeglobo.domain.repository

import com.gitproject.redeglobo.domain.model.Content
import com.gitproject.redeglobo.domain.model.ContentDetail
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface ContentRepository {
    fun getContentRails(page: Int): Observable<List<Content>>
    fun getContentDetail(id: String): Single<ContentDetail>
    fun searchContent(query: String, page: Int): Single<List<Content>>
    fun getWatchlist(): Observable<List<Content>>
    fun addToWatchlist(content: Content): Completable
    fun removeFromWatchlist(contentId: String): Completable
}
