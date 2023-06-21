package com.example.netplix.pojo.tvDetails


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose

@Keep
data class SpokenLanguage(
    @SerializedName("english_name")
    @Expose
    val englishName: String?, // English
    @SerializedName("iso_639_1")
    @Expose
    val iso6391: String?, // en
    @SerializedName("name")
    @Expose
    val name: String? // English
)