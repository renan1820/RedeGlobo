package com.gitproject.redeglobo.detail.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.gitproject.redeglobo.domain.model.Content
import com.gitproject.redeglobo.domain.model.ContentStatus
import com.gitproject.redeglobo.domain.model.NavigationDestination
import com.gitproject.redeglobo.domain.usecase.AddToWatchlistUseCase
import com.gitproject.redeglobo.domain.usecase.GetContentDetailUseCase
import com.gitproject.redeglobo.domain.usecase.RemoveFromWatchlistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getContentDetailUseCase: GetContentDetailUseCase,
    private val addToWatchlistUseCase: AddToWatchlistUseCase,
    private val removeFromWatchlistUseCase: RemoveFromWatchlistUseCase
) : ViewModel() {

    private val contentId: String = checkNotNull(
        savedStateHandle[NavigationDestination.Detail.ARG_CONTENT_ID]
    )

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    private val disposables = CompositeDisposable()

    init {
        loadDetail()
    }

    private fun loadDetail() {
        getContentDetailUseCase.execute(GetContentDetailUseCase.Params(contentId))
            .subscribe(
                { detail -> _uiState.value = DetailUiState.Success(detail) },
                { error -> _uiState.value = DetailUiState.Error(error.localizedMessage ?: "Erro") }
            )
            .addTo(disposables)
    }

    fun toggleWatchlist() {
        val currentState = _uiState.value as? DetailUiState.Success ?: return
        val detail = currentState.detail

        if (currentState.isInWatchlist) {
            removeFromWatchlistUseCase.execute(detail.id)
                .subscribe { _uiState.update { (it as DetailUiState.Success).copy(isInWatchlist = false) } }
                .addTo(disposables)
        } else {
            val content = Content(
                id = detail.id,
                title = detail.title,
                thumbnailUrl = detail.posterUrl,
                status = detail.status,
                genre = detail.genre,
                originName = detail.origin.name,
                episodeCount = detail.episodes.size
            )
            addToWatchlistUseCase.execute(content)
                .subscribe { _uiState.update { (it as DetailUiState.Success).copy(isInWatchlist = true) } }
                .addTo(disposables)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}
