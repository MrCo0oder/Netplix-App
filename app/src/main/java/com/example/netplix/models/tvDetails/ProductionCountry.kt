package com.example.netplix.models.tvDetails


import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class ProductionCountry(
    @SerializedName("iso_3166_1")
    @Expose
    val iso31661: String?, // GB
    @SerializedName("name")
    @Expose
    val name: String? // United Kingdom
)