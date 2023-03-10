package com.example.netplix.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.netplix.pojo.MovieModel
import com.example.netplix.pojo.TvModel

@Database(entities = [MovieModel::class, TvModel::class], version = 1, exportSchema = false)
public abstract class NetplixDB : RoomDatabase() {
    public abstract fun dao(): Dao
}