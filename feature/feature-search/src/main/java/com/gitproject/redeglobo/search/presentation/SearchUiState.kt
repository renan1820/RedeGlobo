package com.gitproject.redeglobo.search.presentation

import com.gitproject.redeglobo.domain.model.Content

sealed class SearchUiState {
    data object Idle : SearchUiState()
    data object Loading : SearchUiState()
    data class Success(val results: List<Content>) : SearchUiState()
    data class Empty(val query: String) : SearchUiState()
    data class Error(val message: String) : SearchUiState()
}
