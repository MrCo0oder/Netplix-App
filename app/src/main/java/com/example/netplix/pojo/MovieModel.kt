package com.example.netplix.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import java.io.Serializable
@Entity(tableName = "film_list")
@TypeConverters(TypeConverterInt::class,TypeConverterString::class)
data class MovieModel(
    @SerializedName("adult")
    var adult:Boolean,
    var backdrop_path: String,
    var genre_ids: List<Int>,
    @PrimaryKey
    var id: Int,
    var original_language: String,
    var original_title: String,
    var overview: String,
    var popularity: Double,
    var poster_path: String,
    var release_date: String,
    var title: String,
    var video: Boolean,
    var vote_average: Double,
    var vote_count: Int
): Serializable

