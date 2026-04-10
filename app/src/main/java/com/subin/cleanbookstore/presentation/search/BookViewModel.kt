package com.subin.cleanbookstore.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.subin.cleanbookstore.core.DataResult
import com.subin.cleanbookstore.domain.usecase.GetSearchBooksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val getSearchBooksUseCase: GetSearchBooksUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<BookUiState>(BookUiState.Empty)
    val uiState: StateFlow<BookUiState> = _uiState.asStateFlow()

    fun searchBooks(query: String) {
        if (query.isBlank()) return

        viewModelScope.launch {
            _uiState.value = BookUiState.Loading

            val result = getSearchBooksUseCase(query)

            when (result) {
                is DataResult.Success -> {
                    val books = result.data
                    if (books.isEmpty()) {
                        _uiState.value = BookUiState.Empty
                    } else {
                        _uiState.value = BookUiState.Success(books)
                    }
                }
                is DataResult.Error -> {
                    val errorMessage = result.exception.message ?: "검색 중 오류가 발생했습니다."
                    _uiState.value = BookUiState.Error(errorMessage)
                }
            }
        }
    }
}