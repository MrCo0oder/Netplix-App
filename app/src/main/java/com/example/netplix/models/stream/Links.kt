package com.example.netplix.models.stream


import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class Links(
    @SerializedName("message")
    @Expose
    val message: String?, // OK
    @SerializedName("results")
    @Expose
    val results: List<Result?>?,
    @SerializedName("status")
    @Expose
    val status: Int?, // 200
    @SerializedName("title")
    @Expose
    val title: String? // Movie Title
)