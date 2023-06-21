package com.example.netplix.pojo.tvDetails


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose

@Keep
data class Season(
    @SerializedName("air_date")
    @Expose
    val airDate: String?, // 2010-12-05
    @SerializedName("episode_count")
    @Expose
    val episodeCount: Int?, // 270
    @SerializedName("id")
    @Expose
    val id: Int?, // 3627
    @SerializedName("name")
    @Expose
    val name: String?, // Specials
    @SerializedName("overview")
    @Expose
    val overview: String?,
    @SerializedName("poster_path")
    @Expose
    val posterPath: String?, // /kMTcwNRfFKCZ0O2OaBZS0nZ2AIe.jpg
    @SerializedName("season_number")
    @Expose
    val seasonNumber: Int? // 0
)