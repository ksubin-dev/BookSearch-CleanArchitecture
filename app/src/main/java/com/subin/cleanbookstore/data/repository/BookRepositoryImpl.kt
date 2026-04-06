package com.subin.cleanbookstore.data.repository

import com.subin.cleanbookstore.core.DataResult
import com.subin.cleanbookstore.data.mapper.toDomain
import com.subin.cleanbookstore.data.remote.api.BooksApiService
import com.subin.cleanbookstore.domain.model.Book
import com.subin.cleanbookstore.domain.repository.BookRepository

class BookRepositoryImpl(
    private val apiService: BooksApiService
) : BookRepository {

    override suspend fun searchBooks(query: String): DataResult<List<Book>> {
        return try {
            val response = apiService.searchBooks(query)
            val books = response.items?.map { it.toDomain() } ?: emptyList()
            DataResult.Success(books)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }
}