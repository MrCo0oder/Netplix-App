package com.example.netplix.models.movieDetails


import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class ProductionCompany(
    @SerializedName("id")
    @Expose
    val id: Int?, // 508
    @SerializedName("logo_path")
    @Expose
    val logoPath: String?, // /7cxRWzi4LsVm4Utfpr1hfARNurT.png
    @SerializedName("name")
    @Expose
    val name: String?, // Regency Enterprises
    @SerializedName("origin_country")
    @Expose
    val originCountry: String? // US
)