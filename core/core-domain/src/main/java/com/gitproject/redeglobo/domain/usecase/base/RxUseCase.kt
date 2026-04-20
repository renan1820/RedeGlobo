package com.gitproject.redeglobo.domain.usecase.base

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

abstract class RxUseCase<Params : Any, Result : Any> {
    protected abstract fun buildUseCase(params: Params): Single<Result>

    fun execute(params: Params): Single<Result> =
        buildUseCase(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

abstract class RxObservableUseCase<Params : Any, Result : Any> {
    protected abstract fun buildUseCase(params: Params): Observable<Result>

    fun execute(params: Params): Observable<Result> =
        buildUseCase(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

abstract class RxCompletableUseCase<Params : Any> {
    protected abstract fun buildUseCase(params: Params): Completable

    fun execute(params: Params): Completable =
        buildUseCase(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}
