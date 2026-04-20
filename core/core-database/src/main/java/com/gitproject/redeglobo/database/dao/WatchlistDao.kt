package com.gitproject.redeglobo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gitproject.redeglobo.database.entity.WatchlistEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface WatchlistDao {
    @Query("SELECT * FROM watchlist ORDER BY addedAt DESC")
    fun observeWatchlist(): Flowable<List<WatchlistEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: WatchlistEntity): Completable

    @Query("DELETE FROM watchlist WHERE contentId = :id")
    fun delete(id: String): Completable
}
