package com.gitproject.redeglobo.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "content")
data class ContentEntity(
    @PrimaryKey val id: String,
    val title: String,
    val thumbnailUrl: String,
    val status: String,
    val genre: String,
    val originName: String,
    val episodeCount: Int,
    val cachedAt: Long = System.currentTimeMillis()
)
