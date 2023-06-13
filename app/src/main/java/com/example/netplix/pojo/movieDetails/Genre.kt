package com.example.netplix.pojo.movieDetails


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose

@Keep
data class Genre(
    @SerializedName("id")
    @Expose
    val id: Int?, // 18
    @SerializedName("name")
    @Expose
    val name: String? // Drama
)