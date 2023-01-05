package com.example.netplix.di

import android.app.Application
import androidx.room.Room
import com.example.netplix.database.Dao
import com.example.netplix.database.NetplixDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
public class DatabaseModule {
    @Provides
    @Singleton
 public fun dbService(app: Application): NetplixDB {
        return Room.databaseBuilder(app, NetplixDB::class.java, "Netplix_DB")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }
    @Provides
    @Singleton
    public fun provideDao(netplixDB: NetplixDB): Dao {
        return netplixDB.dao()
    }
}