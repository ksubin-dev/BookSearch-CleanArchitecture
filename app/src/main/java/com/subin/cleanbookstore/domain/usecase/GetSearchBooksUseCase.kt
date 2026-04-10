package com.subin.cleanbookstore.domain.usecase

import com.subin.cleanbookstore.core.DataResult
import com.subin.cleanbookstore.domain.model.Book
import com.subin.cleanbookstore.domain.repository.BookSearchRepository
import javax.inject.Inject

class GetSearchBooksUseCase @Inject constructor(
    private val repository: BookSearchRepository
) {
    suspend operator fun invoke(query: String): DataResult<List<Book>> {
        return repository.searchBooks(query)
    }
}