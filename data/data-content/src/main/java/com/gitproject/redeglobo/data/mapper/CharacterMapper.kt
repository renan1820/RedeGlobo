package com.gitproject.redeglobo.data.mapper

import com.gitproject.redeglobo.domain.model.Content
import com.gitproject.redeglobo.domain.model.ContentDetail
import com.gitproject.redeglobo.domain.model.ContentLocation
import com.gitproject.redeglobo.domain.model.ContentStatus
import com.gitproject.redeglobo.domain.model.Episode
import com.gitproject.redeglobo.domain.model.Origin
import com.gitproject.redeglobo.graphql.ContentDetailQuery
import com.gitproject.redeglobo.graphql.HomeContentRailsQuery
import com.gitproject.redeglobo.graphql.SearchContentQuery
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterMapper @Inject constructor() {

    fun toDomain(character: HomeContentRailsQuery.Result): Content = Content(
        id = character.id.orEmpty(),
        title = character.name.orEmpty(),
        thumbnailUrl = character.image.orEmpty(),
        status = mapStatus(character.status),
        genre = character.species.orEmpty(),
        originName = character.origin?.name.orEmpty(),
        episodeCount = character.episode.size
    )

    fun toDomain(character: SearchContentQuery.Result): Content = Content(
        id = character.id.orEmpty(),
        title = character.name.orEmpty(),
        thumbnailUrl = character.image.orEmpty(),
        status = mapStatus(character.status),
        genre = character.species.orEmpty(),
        originName = character.origin?.name.orEmpty(),
        episodeCount = 0
    )

    fun toDetailDomain(character: ContentDetailQuery.Character): ContentDetail = ContentDetail(
        id = character.id.orEmpty(),
        title = character.name.orEmpty(),
        posterUrl = character.image.orEmpty(),
        status = mapStatus(character.status),
        genre = character.species.orEmpty(),
        type = character.type.orEmpty(),
        gender = character.gender.orEmpty(),
        origin = Origin(
            id = character.origin?.id.orEmpty(),
            name = character.origin?.name.orEmpty(),
            type = character.origin?.type.orEmpty(),
            dimension = character.origin?.dimension.orEmpty()
        ),
        location = ContentLocation(
            id = character.location?.id.orEmpty(),
            name = character.location?.name.orEmpty(),
            type = character.location?.type.orEmpty()
        ),
        episodes = character.episode.mapNotNull { ep ->
            ep?.let {
                Episode(
                    id = it.id.orEmpty(),
                    title = it.name.orEmpty(),
                    code = it.episode.orEmpty(),
                    airDate = it.air_date.orEmpty()
                )
            }
        },
        createdAt = character.created.orEmpty()
    )

    fun mapStatus(status: String?): ContentStatus = when (status?.uppercase()) {
        "ALIVE" -> ContentStatus.ALIVE
        "DEAD" -> ContentStatus.DEAD
        else -> ContentStatus.UNKNOWN
    }
}
