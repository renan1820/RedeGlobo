package com.gitproject.redeglobo.domain.usecase

import com.gitproject.redeglobo.domain.model.Content
import com.gitproject.redeglobo.domain.repository.ContentRepository
import com.gitproject.redeglobo.domain.usecase.base.RxObservableUseCase
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class GetWatchlistUseCase @Inject constructor(
    private val repository: ContentRepository
) : RxObservableUseCase<Unit, List<Content>>() {

    override fun buildUseCase(params: Unit): Observable<List<Content>> =
        repository.getWatchlist()
}
