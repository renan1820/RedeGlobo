package com.gitproject.redeglobo.data.di

import com.gitproject.redeglobo.data.local.ContentLocalDataSource
import com.gitproject.redeglobo.data.local.ContentLocalDataSourceImpl
import com.gitproject.redeglobo.data.remote.ContentRemoteDataSource
import com.gitproject.redeglobo.data.remote.ContentRemoteDataSourceImpl
import com.gitproject.redeglobo.data.repository.ContentRepositoryImpl
import com.gitproject.redeglobo.domain.repository.ContentRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindContentRepository(impl: ContentRepositoryImpl): ContentRepository

    @Binds
    @Singleton
    abstract fun bindContentRemoteDataSource(impl: ContentRemoteDataSourceImpl): ContentRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindContentLocalDataSource(impl: ContentLocalDataSourceImpl): ContentLocalDataSource
}
