package com.gitproject.redeglobo.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watchlist")
data class WatchlistEntity(
    @PrimaryKey val contentId: String,
    val title: String,
    val thumbnailUrl: String,
    val genre: String,
    val addedAt: Long = System.currentTimeMillis()
)
