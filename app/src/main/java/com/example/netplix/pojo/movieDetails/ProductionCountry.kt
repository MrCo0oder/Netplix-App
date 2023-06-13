package com.example.netplix.pojo.movieDetails


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose

@Keep
data class ProductionCountry(
    @SerializedName("iso_3166_1")
    @Expose
    val iso31661: String?, // US
    @SerializedName("name")
    @Expose
    val name: String? // United States of America
)