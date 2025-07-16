package com.napp.demoapp.data.di

import android.content.Context
import androidx.room.Room
import com.napp.demoapp.data.item.DefaultItemLocalDataSource
import com.napp.demoapp.data.item.DefaultItemRemoteDataSource
import com.napp.demoapp.data.item.DefaultItemRepository
import com.napp.demoapp.data.item.ItemApi
import com.napp.demoapp.data.item.ItemDao
import com.napp.demoapp.data.item.ItemDatabase
import com.napp.demoapp.data.item.ItemLocalDataSource
import com.napp.demoapp.data.item.ItemRemoteDataSource
import com.napp.demoapp.data.item.ItemRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideItemsDatabase(@ApplicationContext context: Context): ItemDatabase {
        return Room.databaseBuilder(
            context,
            ItemDatabase::class.java,
            "items_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideItemDao(database: ItemDatabase): ItemDao {
        return database.itemDao()
    }

    @Provides
    @Singleton
    fun provideItemLocalDataSource(itemDao: ItemDao): ItemLocalDataSource {
        return DefaultItemLocalDataSource(itemDao)
    }

    @Provides
    @Singleton
    fun provideItemRemoteDataSource(itemApi: ItemApi): ItemRemoteDataSource {
        return DefaultItemRemoteDataSource(itemApi)
    }

    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Provides
    @Singleton
    fun provideItemRepository(
        localDataSource: ItemLocalDataSource,
        remoteDataSource: ItemRemoteDataSource,
        dispatcher: CoroutineDispatcher
    ): ItemRepository {
        return DefaultItemRepository(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource,
            dispatcher = dispatcher
        )
    }
}
