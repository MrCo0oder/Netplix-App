package com.example.netplix.pojo.tvDetails


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose

@Keep
data class ProductionCompany(
    @SerializedName("id")
    @Expose
    val id: Int?, // 76043
    @SerializedName("logo_path")
    @Expose
    val logoPath: String?, // /9RO2vbQ67otPrBLXCaC8UMp3Qat.png
    @SerializedName("name")
    @Expose
    val name: String?, // Revolution Sun Studios
    @SerializedName("origin_country")
    @Expose
    val originCountry: String? // US
)