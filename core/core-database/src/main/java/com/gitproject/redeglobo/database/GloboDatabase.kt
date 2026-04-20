package com.gitproject.redeglobo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gitproject.redeglobo.database.dao.ContentDao
import com.gitproject.redeglobo.database.dao.WatchlistDao
import com.gitproject.redeglobo.database.entity.ContentEntity
import com.gitproject.redeglobo.database.entity.WatchlistEntity

@Database(
    entities = [ContentEntity::class, WatchlistEntity::class],
    version = 1,
    exportSchema = true
)
abstract class GloboDatabase : RoomDatabase() {
    abstract fun contentDao(): ContentDao
    abstract fun watchlistDao(): WatchlistDao
}
