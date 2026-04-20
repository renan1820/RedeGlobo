package com.gitproject.redeglobo.domain.model

data class Content(
    val id: String,
    val title: String,
    val thumbnailUrl: String,
    val status: ContentStatus,
    val genre: String,
    val originName: String,
    val episodeCount: Int
)

enum class ContentStatus { ALIVE, DEAD, UNKNOWN }
