package com.gitproject.redeglobo.detail.presentation

import com.gitproject.redeglobo.domain.model.ContentDetail

sealed class DetailUiState {
    data object Loading : DetailUiState()
    data class Success(val detail: ContentDetail, val isInWatchlist: Boolean = false) : DetailUiState()
    data class Error(val message: String) : DetailUiState()
}
