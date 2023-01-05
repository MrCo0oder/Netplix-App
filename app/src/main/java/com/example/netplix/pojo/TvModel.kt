package com.example.netplix.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.io.Serializable

@Entity(tableName = "tv_list")
@TypeConverters(TypeConverterInt::class,TypeConverterString::class)
data class TvModel(
    val backdrop_path: String,
    val first_air_date: String,
    val genre_ids: List<Int>,
    @PrimaryKey
    val id: Int,
    val name: String,
    val origin_country: List<String>,
    val original_language: String,
    val original_name: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val vote_average: Double,
    val vote_count: Int
): Serializable