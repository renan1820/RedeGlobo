package com.gitproject.redeglobo.domain.usecase

import com.gitproject.redeglobo.domain.model.Content
import com.gitproject.redeglobo.domain.repository.ContentRepository
import com.gitproject.redeglobo.domain.usecase.base.RxObservableUseCase
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class GetContentRailsUseCase @Inject constructor(
    private val repository: ContentRepository
) : RxObservableUseCase<GetContentRailsUseCase.Params, List<Content>>() {

    data class Params(val page: Int = 1)

    override fun buildUseCase(params: Params): Observable<List<Content>> =
        repository.getContentRails(params.page)
}
