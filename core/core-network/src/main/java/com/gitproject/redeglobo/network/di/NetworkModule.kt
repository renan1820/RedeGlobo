package com.gitproject.redeglobo.network.di

import com.apollographql.apollo.ApolloClient
import com.gitproject.redeglobo.network.apollo.ApolloClientProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideApolloClient(provider: ApolloClientProvider): ApolloClient =
        provider.client
}
