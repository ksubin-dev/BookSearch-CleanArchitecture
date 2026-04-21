package com.subin.cleanbookstore.presentation.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.subin.cleanbookstore.core.DataResult
import com.subin.cleanbookstore.domain.model.Book
import com.subin.cleanbookstore.domain.usecase.DeleteSearchHistoryUseCase
import com.subin.cleanbookstore.domain.usecase.GetBookmarkListUseCase
import com.subin.cleanbookstore.domain.usecase.GetRecentSearchHistoryUseCase
import com.subin.cleanbookstore.domain.usecase.GetSearchBooksUseCase
import com.subin.cleanbookstore.domain.usecase.SaveSearchKeywordUseCase
import com.subin.cleanbookstore.domain.usecase.ToggleBookmarkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookSearchViewModel @Inject constructor(
    private val getSearchBooksUseCase: GetSearchBooksUseCase,
    private val getBookmarkListUseCase: GetBookmarkListUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase,
    private val getRecentSearchHistoryUseCase: GetRecentSearchHistoryUseCase,
    private val saveSearchKeywordUseCase: SaveSearchKeywordUseCase,
    private val deleteSearchHistoryUseCase: DeleteSearchHistoryUseCase
) : ViewModel() {

    val recentSearchHistory: StateFlow<List<String>> = getRecentSearchHistoryUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    private val _searchResults = MutableStateFlow<List<Book>>(emptyList())

    private val _loadState = MutableStateFlow<BookSearchUiState>(BookSearchUiState.Empty)

    val uiState: StateFlow<BookSearchUiState> = combine(
        _searchResults,
        getBookmarkListUseCase(),
        _loadState
    ) { results, bookmarks, loadState ->
        val syncBooks = results.map { book ->
            book.copy(isFavorite = bookmarks.any { it.id == book.id })
        }

        when (loadState) {
            is BookSearchUiState.Loading -> BookSearchUiState.Loading
            is BookSearchUiState.Error -> BookSearchUiState.Error(loadState.message)
            else -> {
                if (syncBooks.isEmpty()) BookSearchUiState.Empty
                else BookSearchUiState.Success(syncBooks)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = BookSearchUiState.Empty
    )

    private var searchJob: kotlinx.coroutines.Job? = null

    fun searchBooks(query: String) {
        if (query.isBlank()) return

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            saveSearchKeywordUseCase(query)

            _loadState.value = BookSearchUiState.Loading

            when (val result = getSearchBooksUseCase(query)) {
                is DataResult.Success -> {
                    _searchResults.value = result.data
                    _loadState.value = BookSearchUiState.Success(result.data)
                }
                is DataResult.Error -> {
                    val errorMessage = result.exception.message ?: "검색 중 오류가 발생했습니다."
                    _loadState.value = BookSearchUiState.Error(errorMessage)
                }
            }
        }
    }

    fun deleteHistory(keyword: String) {
        viewModelScope.launch {
            deleteSearchHistoryUseCase.deleteKeyword(keyword)
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            deleteSearchHistoryUseCase.clearAll()
        }
    }

    fun resetSearchState() {
        _searchResults.value = emptyList()
        _loadState.value = BookSearchUiState.Empty
    }

    fun onBookmarkClick(book: Book) {
        viewModelScope.launch {
            toggleBookmarkUseCase(book)
        }
    }
}