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

    override suspend fun getBookDetails(bookId: String): DataResult<Book> {
        return try {
            val response = apiService.getBookDetails(bookId)
            DataResult.Success(response.toDomain())
        } catch (e: java.net.UnknownHostException) {
            DataResult.Error(Exception("네트워크 연결을 확인해 주세요."))
        } catch (e: retrofit2.HttpException) {
            val message = if (e.code() == 429) "요청 한도가 초과되었습니다. 잠시 후 다시 시도해 주세요."
            else "도서 정보를 가져오는 중 서버 에러가 발생했습니다. (Error: ${e.code()})"
            DataResult.Error(Exception(message))
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

}