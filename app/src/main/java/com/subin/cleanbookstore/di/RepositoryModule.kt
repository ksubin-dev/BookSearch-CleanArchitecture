package com.subin.cleanbookstore.di

import com.subin.cleanbookstore.data.repository.BookSearchRepositoryImpl
import com.subin.cleanbookstore.domain.repository.BookSearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindBookSearchRepository(
        bookSearchRepositoryImpl: BookSearchRepositoryImpl
    ): BookSearchRepository
}