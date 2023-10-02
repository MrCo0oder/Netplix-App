package com.example.netplix.models.movieDetails


import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class Genre(
    @SerializedName("id")
    @Expose
    val id: Int?, // 18
    @SerializedName("name")
    @Expose
    val name: String? // Drama
)