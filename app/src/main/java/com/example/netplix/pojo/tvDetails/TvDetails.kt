package com.example.netplix.pojo.tvDetails


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose

@Keep
data class TvDetails(
    @SerializedName("adult")
    @Expose
    val adult: Boolean?, // false
    @SerializedName("backdrop_path")
    @Expose
    val backdropPath: String?, // /2OMB0ynKlyIenMJWI2Dy9IWT4c.jpg
    @SerializedName("created_by")
    @Expose
    val createdBy: List<CreatedBy?>?,
    @SerializedName("episode_run_time")
    @Expose
    val episodeRunTime: List<Int?>?,
    @SerializedName("first_air_date")
    @Expose
    val firstAirDate: String?, // 2011-04-17
    @SerializedName("genres")
    @Expose
    val genres: List<Genre?>?,
    @SerializedName("homepage")
    @Expose
    val homepage: String?, // http://www.hbo.com/game-of-thrones
    @SerializedName("id")
    @Expose
    val id: Int?, // 1399
    @SerializedName("in_production")
    @Expose
    val inProduction: Boolean?, // false
    @SerializedName("languages")
    @Expose
    val languages: List<String?>?,
    @SerializedName("last_air_date")
    @Expose
    val lastAirDate: String?, // 2019-05-19
    @SerializedName("last_episode_to_air")
    @Expose
    val lastEpisodeToAir: LastEpisodeToAir?,
    @SerializedName("name")
    @Expose
    val name: String?, // Game of Thrones
    @SerializedName("networks")
    @Expose
    val networks: List<Network?>?,
    @SerializedName("next_episode_to_air")
    @Expose
    val nextEpisodeToAir: Any?, // null
    @SerializedName("number_of_episodes")
    @Expose
    val numberOfEpisodes: Int?, // 73
    @SerializedName("number_of_seasons")
    @Expose
    val numberOfSeasons: Int?, // 8
    @SerializedName("origin_country")
    @Expose
    val originCountry: List<String?>?,
    @SerializedName("original_language")
    @Expose
    val originalLanguage: String?, // en
    @SerializedName("original_name")
    @Expose
    val originalName: String?, // Game of Thrones
    @SerializedName("overview")
    @Expose
    val overview: String?, // Seven noble families fight for control of the mythical land of Westeros. Friction between the houses leads to full-scale war. All while a very ancient evil awakens in the farthest north. Amidst the war, a neglected military order of misfits, the Night's Watch, is all that stands between the realms of men and icy horrors beyond.
    @SerializedName("popularity")
    @Expose
    val popularity: Double?, // 316.645
    @SerializedName("poster_path")
    @Expose
    val posterPath: String?, // /7WUHnWGx5OO145IRxPDUkQSh4C7.jpg
    @SerializedName("production_companies")
    @Expose
    val productionCompanies: List<ProductionCompany?>?,
    @SerializedName("production_countries")
    @Expose
    val productionCountries: List<ProductionCountry?>?,
    @SerializedName("seasons")
    @Expose
    val seasons: List<Season?>?,
    @SerializedName("spoken_languages")
    @Expose
    val spokenLanguages: List<SpokenLanguage?>?,
    @SerializedName("status")
    @Expose
    val status: String?, // Ended
    @SerializedName("tagline")
    @Expose
    val tagline: String?, // Winter Is Coming
    @SerializedName("type")
    @Expose
    val type: String?, // Scripted
    @SerializedName("vote_average")
    @Expose
    val voteAverage: Double?, // 8.436
    @SerializedName("vote_count")
    @Expose
    val voteCount: Int? // 20993
)