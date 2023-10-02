package com.example.netplix.models.tvDetails


import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class CreatedBy(
    @SerializedName("credit_id")
    @Expose
    val creditId: String?, // 5256c8c219c2956ff604858a
    @SerializedName("gender")
    @Expose
    val gender: Int?, // 2
    @SerializedName("id")
    @Expose
    val id: Int?, // 9813
    @SerializedName("name")
    @Expose
    val name: String?, // David Benioff
    @SerializedName("profile_path")
    @Expose
    val profilePath: String? // /xvNN5huL0X8yJ7h3IZfGG4O2zBD.jpg
)