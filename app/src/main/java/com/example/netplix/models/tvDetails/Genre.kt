package com.example.netplix.models.tvDetails


import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class Genre(
    @SerializedName("id")
    @Expose
    val id: Int?, // 10765
    @SerializedName("name")
    @Expose
    val name: String? // Sci-Fi & Fantasy
)