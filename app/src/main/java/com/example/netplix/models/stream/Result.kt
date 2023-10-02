package com.example.netplix.models.stream


import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class Result(
    @SerializedName("exact_match")
    @Expose
    val exactMatch: Int?, // 1
    @SerializedName("quality")
    @Expose
    val quality: String?, // 1080p
    @SerializedName("server")
    @Expose
    val server: String?, // streamtape
    @SerializedName("size")
    @Expose
    val size: Int?, // 215131368
    @SerializedName("title")
    @Expose
    val title: String?, // Source Title
    @SerializedName("url")
    @Expose
    val url: String? // https://playerdomain.com/play/aFJkY05aTXc0b3FORjB2WGtlb2JVcTlQMnlKUmlEbW1TTDlMcU
)