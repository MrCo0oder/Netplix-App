package com.example.netplix.pojo.tvDetails


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose

@Keep
data class Network(
    @SerializedName("id")
    @Expose
    val id: Int?, // 49
    @SerializedName("logo_path")
    @Expose
    val logoPath: String?, // /tuomPhY2UtuPTqqFnKMVHvSb724.png
    @SerializedName("name")
    @Expose
    val name: String?, // HBO
    @SerializedName("origin_country")
    @Expose
    val originCountry: String? // US
)