package com.gitproject.redeglobo.database.di

import android.content.Context
import androidx.room.Room
import com.gitproject.redeglobo.database.GloboDatabase
import com.gitproject.redeglobo.database.dao.ContentDao
import com.gitproject.redeglobo.database.dao.WatchlistDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): GloboDatabase =
        Room.databaseBuilder(context, GloboDatabase::class.java, "globo.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideContentDao(db: GloboDatabase): ContentDao = db.contentDao()

    @Provides
    fun provideWatchlistDao(db: GloboDatabase): WatchlistDao = db.watchlistDao()
}
