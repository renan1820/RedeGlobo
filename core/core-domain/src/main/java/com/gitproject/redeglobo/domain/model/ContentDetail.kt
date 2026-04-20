package com.gitproject.redeglobo.domain.model

data class ContentDetail(
    val id: String,
    val title: String,
    val posterUrl: String,
    val status: ContentStatus,
    val genre: String,
    val type: String,
    val gender: String,
    val origin: Origin,
    val location: ContentLocation,
    val episodes: List<Episode>,
    val createdAt: String
)

data class Origin(
    val id: String,
    val name: String,
    val type: String,
    val dimension: String
)

data class ContentLocation(
    val id: String,
    val name: String,
    val type: String
)

data class Episode(
    val id: String,
    val title: String,
    val code: String,
    val airDate: String
)
