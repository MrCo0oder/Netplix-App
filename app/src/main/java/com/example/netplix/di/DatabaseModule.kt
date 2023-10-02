package com.example.netplix.di

import android.app.Application
import androidx.room.Room
import com.example.netplix.database.Dao
import com.example.netplix.database.NetplixDB
import com.example.netplix.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
public class DatabaseModule @Inject constructor() {
    @Provides
    @Singleton
    fun dbService(app: Application): NetplixDB {
        return Room.databaseBuilder(app, NetplixDB::class.java, Constants.NETPLIX_DB)
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }
    @Provides
    @Singleton
    fun provideDao(netplixDB: NetplixDB): Dao {
        return netplixDB.dao()
    }
    fun deleteAll(netplixDB: NetplixDB) {
        return netplixDB.clearAllTables()
    }
}