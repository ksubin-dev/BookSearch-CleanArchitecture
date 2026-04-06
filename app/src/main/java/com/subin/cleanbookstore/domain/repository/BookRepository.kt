package com.subin.cleanbookstore.domain.repository

import com.subin.cleanbookstore.core.DataResult
import com.subin.cleanbookstore.domain.model.Book

interface BookRepository {
    suspend fun searchBooks(query: String): DataResult<List<Book>>
}