package com.gitproject.redeglobo.domain.usecase

import com.gitproject.redeglobo.domain.model.Content
import com.gitproject.redeglobo.domain.repository.ContentRepository
import com.gitproject.redeglobo.domain.usecase.base.RxCompletableUseCase
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class AddToWatchlistUseCase @Inject constructor(
    private val repository: ContentRepository
) : RxCompletableUseCase<Content>() {

    override fun buildUseCase(params: Content): Completable =
        repository.addToWatchlist(params)
}
