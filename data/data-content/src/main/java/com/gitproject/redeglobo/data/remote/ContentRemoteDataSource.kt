package com.gitproject.redeglobo.data.remote

import com.gitproject.redeglobo.domain.model.Content
import com.gitproject.redeglobo.domain.model.ContentDetail
import io.reactivex.rxjava3.core.Single

interface ContentRemoteDataSource {
    fun getContentRails(page: Int): Single<List<Content>>
    fun getContentDetail(id: String): Single<ContentDetail>
    fun searchContent(query: String, page: Int): Single<List<Content>>
}
