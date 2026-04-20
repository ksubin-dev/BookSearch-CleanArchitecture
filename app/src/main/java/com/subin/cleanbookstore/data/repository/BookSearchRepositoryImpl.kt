package com.subin.cleanbookstore.data.repository

import com.subin.cleanbookstore.core.DataResult
import com.subin.cleanbookstore.data.mapper.toDomain
import com.subin.cleanbookstore.data.remote.api.BooksApiService
import com.subin.cleanbookstore.domain.model.Book
import com.subin.cleanbookstore.domain.repository.BookSearchRepository
import javax.inject.Inject

class BookSearchRepositoryImpl @Inject constructor(
    private val apiService: BooksApiService
) : BookSearchRepository {

    override suspend fun searchBooks(query: String): DataResult<List<Book>> {
        return try {
            val response = apiService.searchBooks(query)
            val books = response.items?.map { it.toDomain() } ?: emptyList()
            DataResult.Success(books)
        } catch (e: java.net.UnknownHostException) {
            DataResult.Error(Exception("네트워크 연결을 확인해 주세요."))
        } catch (e: retrofit2.HttpException) {
            DataResult.Error(Exception("서버 응답에 문제가 발생했습니다. (Error: ${e.code()})"))
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }
}