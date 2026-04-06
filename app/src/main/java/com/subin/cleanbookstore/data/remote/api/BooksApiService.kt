package com.subin.cleanbookstore.data.remote.api

import com.subin.cleanbookstore.data.remote.dto.BookSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface BooksApiService {

    @GET("books/v1/volumes")
    suspend fun searchBooks(
        @Query("q") query: String,

        @Query("maxResults") maxResults: Int = 10
    ): BookSearchResponse

}