package com.gitproject.redeglobo.domain.usecase

import com.gitproject.redeglobo.domain.repository.ContentRepository
import com.gitproject.redeglobo.domain.usecase.base.RxCompletableUseCase
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class RemoveFromWatchlistUseCase @Inject constructor(
    private val repository: ContentRepository
) : RxCompletableUseCase<String>() {

    override fun buildUseCase(params: String): Completable =
        repository.removeFromWatchlist(params)
}
