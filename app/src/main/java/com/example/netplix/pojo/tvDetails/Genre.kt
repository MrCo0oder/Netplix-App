package com.example.netplix.pojo.tvDetails


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose

@Keep
data class Genre(
    @SerializedName("id")
    @Expose
    val id: Int?, // 10765
    @SerializedName("name")
    @Expose
    val name: String? // Sci-Fi & Fantasy
)