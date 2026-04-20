package com.gitproject.redeglobo.search.presentation

import androidx.lifecycle.ViewModel
import com.gitproject.redeglobo.domain.usecase.SearchContentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchContentUseCase: SearchContentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val querySubject = PublishSubject.create<String>()
    private val disposables = CompositeDisposable()

    init {
        querySubject
            .debounce(300, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .filter { it.isNotBlank() }
            .switchMapSingle { query ->
                searchContentUseCase.execute(SearchContentUseCase.Params(query))
                    .doOnSubscribe { _uiState.value = SearchUiState.Loading }
                    .map { results ->
                        if (results.isEmpty()) SearchUiState.Empty(query)
                        else SearchUiState.Success(results)
                    }
                    .onErrorReturn { SearchUiState.Error(it.localizedMessage ?: "Erro na busca") }
            }
            .subscribe { state -> _uiState.value = state }
            .addTo(disposables)
    }

    fun onQueryChanged(query: String) {
        if (query.isBlank()) {
            _uiState.value = SearchUiState.Idle
        } else {
            querySubject.onNext(query)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}
