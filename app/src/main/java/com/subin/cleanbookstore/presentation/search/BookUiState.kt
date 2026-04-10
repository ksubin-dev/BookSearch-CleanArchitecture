package com.subin.cleanbookstore.presentation.search

import com.subin.cleanbookstore.domain.model.Book

sealed interface BookUiState {
    data object Loading : BookUiState
    data class Success(val books: List<Book>) : BookUiState
    data class Error(val message: String) : BookUiState
    data object Empty : BookUiState
}

