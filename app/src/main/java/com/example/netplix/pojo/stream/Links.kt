package com.example.netplix.pojo.stream


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose

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