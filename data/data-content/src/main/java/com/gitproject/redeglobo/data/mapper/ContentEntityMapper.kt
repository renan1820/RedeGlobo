package com.gitproject.redeglobo.data.mapper

import com.gitproject.redeglobo.database.entity.ContentEntity
import com.gitproject.redeglobo.database.entity.WatchlistEntity
import com.gitproject.redeglobo.domain.model.Content
import com.gitproject.redeglobo.domain.model.ContentStatus
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContentEntityMapper @Inject constructor() {

    fun toDomain(entity: ContentEntity): Content = Content(
        id = entity.id,
        title = entity.title,
        thumbnailUrl = entity.thumbnailUrl,
        status = ContentStatus.valueOf(entity.status),
        genre = entity.genre,
        originName = entity.originName,
        episodeCount = entity.episodeCount
    )

    fun toEntity(content: Content): ContentEntity = ContentEntity(
        id = content.id,
        title = content.title,
        thumbnailUrl = content.thumbnailUrl,
        status = content.status.name,
        genre = content.genre,
        originName = content.originName,
        episodeCount = content.episodeCount
    )

    fun toWatchlistEntity(content: Content): WatchlistEntity = WatchlistEntity(
        contentId = content.id,
        title = content.title,
        thumbnailUrl = content.thumbnailUrl,
        genre = content.genre
    )

    fun fromWatchlistEntity(entity: WatchlistEntity): Content = Content(
        id = entity.contentId,
        title = entity.title,
        thumbnailUrl = entity.thumbnailUrl,
        status = ContentStatus.UNKNOWN,
        genre = entity.genre,
        originName = "",
        episodeCount = 0
    )
}
