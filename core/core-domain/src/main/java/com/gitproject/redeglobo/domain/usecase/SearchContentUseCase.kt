package com.gitproject.redeglobo.domain.usecase

import com.gitproject.redeglobo.domain.model.Content
import com.gitproject.redeglobo.domain.repository.ContentRepository
import com.gitproject.redeglobo.domain.usecase.base.RxUseCase
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class SearchContentUseCase @Inject constructor(
    private val repository: ContentRepository
) : RxUseCase<SearchContentUseCase.Params, List<Content>>() {

    data class Params(val query: String, val page: Int = 1)

    override fun buildUseCase(params: Params): Single<List<Content>> =
        repository.searchContent(params.query, params.page)
}
