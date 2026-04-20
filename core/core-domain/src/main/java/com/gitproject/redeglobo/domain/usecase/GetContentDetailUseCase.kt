package com.gitproject.redeglobo.domain.usecase

import com.gitproject.redeglobo.domain.model.ContentDetail
import com.gitproject.redeglobo.domain.repository.ContentRepository
import com.gitproject.redeglobo.domain.usecase.base.RxUseCase
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetContentDetailUseCase @Inject constructor(
    private val repository: ContentRepository
) : RxUseCase<GetContentDetailUseCase.Params, ContentDetail>() {

    data class Params(val id: String)

    override fun buildUseCase(params: Params): Single<ContentDetail> =
        repository.getContentDetail(params.id)
}
