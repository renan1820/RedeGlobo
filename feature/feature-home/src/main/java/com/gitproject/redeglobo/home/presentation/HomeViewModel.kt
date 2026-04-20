package com.gitproject.redeglobo.home.presentation

import androidx.lifecycle.ViewModel
import com.gitproject.redeglobo.domain.model.Content
import com.gitproject.redeglobo.domain.model.ContentRail
import com.gitproject.redeglobo.domain.usecase.GetContentRailsUseCase
import com.gitproject.redeglobo.domain.usecase.GetWatchlistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getContentRailsUseCase: GetContentRailsUseCase,
    private val getWatchlistUseCase: GetWatchlistUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val disposables = CompositeDisposable()
    private val currentPage = BehaviorSubject.createDefault(1)

    init {
        observeContentRails()
    }

    private fun observeContentRails() {
        currentPage
            .switchMap { page: Int ->
                getContentRailsUseCase.execute(GetContentRailsUseCase.Params(page))
                    .map { contents: List<Content> -> buildRails(contents) }
                    .onErrorReturn { t -> HomeUiState.Error(t.localizedMessage ?: "Erro desconhecido") }
                    .startWithItem(HomeUiState.Loading)
            }
            .subscribe { state -> _uiState.value = state }
            .addTo(disposables)
    }

    private fun buildRails(contents: List<Content>): HomeUiState = HomeUiState.Success(
        rails = listOf(
            ContentRail(title = "Em Alta", items = contents.take(10)),
            ContentRail(title = "Todos os Personagens", items = contents)
        )
    )

    fun refresh() {
        currentPage.onNext(currentPage.value ?: 1)
    }

    fun loadNextPage() {
        currentPage.onNext((currentPage.value ?: 1) + 1)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}
