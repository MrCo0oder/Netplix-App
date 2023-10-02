package com.example.netplix.models.images


import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class Backdrop(
    @SerializedName("aspect_ratio")
    @Expose
    val aspectRatio: Double?, // 1.778
    @SerializedName("file_path")
    @Expose
    val filePath: String?, // /hZkgoQYus5vegHoetLkCJzb17zJ.jpg
    @SerializedName("height")
    @Expose
    val height: Int?, // 800
    @SerializedName("iso_639_1")
    @Expose
    val iso6391: String?, // en
    @SerializedName("vote_average")
    @Expose
    val voteAverage: Double?, // 5.622
    @SerializedName("vote_count")
    @Expose
    val voteCount: Int?, // 20
    @SerializedName("width")
    @Expose
    val width: Int? // 1422
)