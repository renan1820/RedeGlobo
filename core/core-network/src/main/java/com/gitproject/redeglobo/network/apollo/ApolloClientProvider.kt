package com.gitproject.redeglobo.network.apollo

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.cache.normalized.api.MemoryCacheFactory
import com.apollographql.apollo.cache.normalized.normalizedCache
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApolloClientProvider @Inject constructor() {
    val client: ApolloClient by lazy {
        ApolloClient.Builder()
            .serverUrl("https://rickandmortyapi.com/graphql")
            .normalizedCache(MemoryCacheFactory(maxSizeBytes = 10 * 1024 * 1024))
            .build()
    }
}
