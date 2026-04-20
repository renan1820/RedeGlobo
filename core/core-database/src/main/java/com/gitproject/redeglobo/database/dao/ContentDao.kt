package com.gitproject.redeglobo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gitproject.redeglobo.database.entity.ContentEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

@Dao
interface ContentDao {
    @Query("SELECT * FROM content ORDER BY cachedAt DESC")
    fun observeAll(): Flowable<List<ContentEntity>>

    @Query("SELECT * FROM content ORDER BY cachedAt DESC LIMIT 1")
    fun getCached(): Single<List<ContentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(contents: List<ContentEntity>): Completable

    @Query("DELETE FROM content WHERE cachedAt < :expiryTime")
    fun deleteExpired(expiryTime: Long): Completable

    @Query("DELETE FROM content")
    fun deleteAll(): Completable
}
