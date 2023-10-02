package com.example.netplix.models.images


import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class BackDrops(
    @SerializedName("backdrops")
    @Expose
    val backdrops: List<Backdrop?>?,
    @SerializedName("id")
    @Expose
    val id: Int?, // 550
    @SerializedName("logos")
    @Expose
    val logos: List<Logo?>?,
    @SerializedName("posters")
    @Expose
    val posters: List<Poster?>?
)