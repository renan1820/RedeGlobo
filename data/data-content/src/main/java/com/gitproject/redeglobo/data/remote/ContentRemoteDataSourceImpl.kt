package com.gitproject.redeglobo.data.remote

import com.apollographql.apollo.ApolloClient
import com.gitproject.redeglobo.data.mapper.CharacterMapper
import com.gitproject.redeglobo.domain.model.Content
import com.gitproject.redeglobo.domain.model.ContentDetail
import com.gitproject.redeglobo.graphql.ContentDetailQuery
import com.gitproject.redeglobo.graphql.HomeContentRailsQuery
import com.gitproject.redeglobo.graphql.SearchContentQuery
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.rx3.rxSingle
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContentRemoteDataSourceImpl @Inject constructor(
    private val apolloClient: ApolloClient,
    private val mapper: CharacterMapper
) : ContentRemoteDataSource {

    override fun getContentRails(page: Int): Single<List<Content>> =
        rxSingle(Dispatchers.IO) {
            apolloClient.query(HomeContentRailsQuery(page = page)).execute()
        }.map { response ->
            response.dataAssertNoErrors
                .characters
                ?.results
                ?.filterNotNull()
                ?.map { mapper.toDomain(it) }
                ?: emptyList()
        }

    override fun getContentDetail(id: String): Single<ContentDetail> =
        rxSingle(Dispatchers.IO) {
            apolloClient.query(ContentDetailQuery(id = id)).execute()
        }.map { response ->
            mapper.toDetailDomain(
                checkNotNull(response.dataAssertNoErrors.character) {
                    "Character not found for id=$id"
                }
            )
        }

    override fun searchContent(query: String, page: Int): Single<List<Content>> =
        rxSingle(Dispatchers.IO) {
            apolloClient.query(SearchContentQuery(name = query, page = page)).execute()
        }.map { response ->
            response.dataAssertNoErrors
                .characters
                ?.results
                ?.filterNotNull()
                ?.map { mapper.toDomain(it) }
                ?: emptyList()
        }
}
