package com.example.netplix.models.tvDetails


import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class LastEpisodeToAir(
    @SerializedName("air_date")
    @Expose
    val airDate: String?, // 2019-05-19
    @SerializedName("episode_number")
    @Expose
    val episodeNumber: Int?, // 6
    @SerializedName("id")
    @Expose
    val id: Int?, // 1551830
    @SerializedName("name")
    @Expose
    val name: String?, // The Iron Throne
    @SerializedName("overview")
    @Expose
    val overview: String?, // In the aftermath of the devastating attack on King's Landing, Daenerys must face the survivors.
    @SerializedName("production_code")
    @Expose
    val productionCode: String?, // 806
    @SerializedName("runtime")
    @Expose
    val runtime: Int?, // 80
    @SerializedName("season_number")
    @Expose
    val seasonNumber: Int?, // 8
    @SerializedName("show_id")
    @Expose
    val showId: Int?, // 1399
    @SerializedName("still_path")
    @Expose
    val stillPath: String?, // /zBi2O5EJfgTS6Ae0HdAYLm9o2nf.jpg
    @SerializedName("vote_average")
    @Expose
    val voteAverage: Double?, // 4.8
    @SerializedName("vote_count")
    @Expose
    val voteCount: Int? // 229
)