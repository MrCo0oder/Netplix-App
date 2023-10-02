package com.example.netplix.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "film_list")
@TypeConverters(TypeConverterInt::class, TypeConverterString::class)
data class MovieModel(
    @SerializedName("adult")
    var adult: Boolean,
    @SerializedName("backdrop_path")
    var backdrop_path: String? = "",
    @SerializedName("genre_ids")
    var genre_ids: List<Int>,
    @SerializedName("id")
    @PrimaryKey
    var id: Int,
    @SerializedName("original_language")
    var original_language: String,
    @SerializedName("original_title")
    var original_title: String,
    @SerializedName("overview")
    var overview: String,
    @SerializedName("popularity")
    var popularity: Double,
    @SerializedName("poster_path")
    var poster_path: String? = "",
    @SerializedName("release_date")
    var release_date: String,
    @SerializedName("title")
    var title: String,
    @SerializedName("video")
    var video: Boolean,
    @SerializedName("vote_average")
    var vote_average: Double,
    @SerializedName("vote_count")
    var vote_count: Int
) : Serializable {
    constructor() : this(
        false,
        "",
        mutableListOf(),
        -1,
        "",
        "",
        "",
        0.0,
        "",
        "",
        "",
        false,
        0.0,
        -1
    )
}

