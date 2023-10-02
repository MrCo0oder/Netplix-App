package com.example.netplix.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.netplix.models.MovieModel
import com.example.netplix.models.TvModel

@Database(entities = [MovieModel::class, TvModel::class], version = 2, exportSchema = false)
public abstract class NetplixDB : RoomDatabase() {
    public abstract fun dao(): Dao
}