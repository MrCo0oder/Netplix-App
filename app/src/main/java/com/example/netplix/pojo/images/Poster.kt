package com.example.netplix.pojo.images


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose

@Keep
data class Poster(
    @SerializedName("aspect_ratio")
    @Expose
    val aspectRatio: Double?, // 0.667
    @SerializedName("file_path")
    @Expose
    val filePath: String?, // /r3pPehX4ik8NLYPpbDRAh0YRtMb.jpg
    @SerializedName("height")
    @Expose
    val height: Int?, // 900
    @SerializedName("iso_639_1")
    @Expose
    val iso6391: String?, // pt
    @SerializedName("vote_average")
    @Expose
    val voteAverage: Double?, // 5.258
    @SerializedName("vote_count")
    @Expose
    val voteCount: Int?, // 6
    @SerializedName("width")
    @Expose
    val width: Int? // 600
)