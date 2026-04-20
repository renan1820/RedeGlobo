package com.gitproject.redeglobo.home.presentation

import com.gitproject.redeglobo.domain.model.Content
import com.gitproject.redeglobo.domain.model.ContentRail

sealed class HomeUiState {
    data object Loading : HomeUiState()
    data class Success(
        val rails: List<ContentRail>,
        val watchlist: List<Content> = emptyList()
    ) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}
