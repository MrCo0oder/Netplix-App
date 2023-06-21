package com.example.netplix.pojo.images


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose

@Keep
data class Logo(
    @SerializedName("aspect_ratio")
    @Expose
    val aspectRatio: Double?, // 5.203
    @SerializedName("file_path")
    @Expose
    val filePath: String?, // /c1KLulrIhUqY5fT42nmC5aERGCp.png
    @SerializedName("height")
    @Expose
    val height: Int?, // 79
    @SerializedName("iso_639_1")
    @Expose
    val iso6391: String?, // he
    @SerializedName("vote_average")
    @Expose
    val voteAverage: Double?, // 5.312
    @SerializedName("vote_count")
    @Expose
    val voteCount: Int?, // 1
    @SerializedName("width")
    @Expose
    val width: Int? // 411
)