package com.example.netplix.pojo.movieDetails


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
@Keep
data class MovieDetails(
    @SerializedName("adult")
    @Expose
    val adult: Boolean?, // false
    @SerializedName("backdrop_path")
    @Expose
    val backdropPath: String?, // /hZkgoQYus5vegHoetLkCJzb17zJ.jpg

    @SerializedName("budget")
    @Expose
    val budget: Int?, // 63000000
    @SerializedName("genres")
    @Expose
    val genres: List<Genre?>?,
    @SerializedName("homepage")
    @Expose
    val homepage: String?, // http://www.foxmovies.com/movies/fight-club
    @SerializedName("id")
    @Expose
    val id: Int?, // 550
    @SerializedName("imdb_id")
    @Expose
    val imdbId: String?, // tt0137523
    @SerializedName("original_language")
    @Expose
    val originalLanguage: String?, // en
    @SerializedName("original_title")
    @Expose
    val originalTitle: String?, // Fight Club
    @SerializedName("overview")
    @Expose
    val overview: String?, // A ticking-time-bomb insomniac and a slippery soap salesman channel primal male aggression into a shocking new form of therapy. Their concept catches on, with underground "fight clubs" forming in every town, until an eccentric gets in the way and ignites an out-of-control spiral toward oblivion.
    @SerializedName("popularity")
    @Expose
    val popularity: Float?, // 61.416
    @SerializedName("poster_path")
    @Expose
    val posterPath: String?, // /pB8BM7pdSp6B6Ih7QZ4DrQ3PmJK.jpg
    @SerializedName("production_companies")
    @Expose
    val productionCompanies: List<ProductionCompany?>?,
    @SerializedName("production_countries")
    @Expose
    val productionCountries: List<ProductionCountry?>?,
    @SerializedName("release_date")
    @Expose
    val releaseDate: String?, // 1999-10-15
    @SerializedName("revenue")
    @Expose
    val revenue: Int?, // 100853753
    @SerializedName("runtime")
    @Expose
    val runtime: Int?, // 139
    @SerializedName("spoken_languages")
    @Expose
    val spokenLanguages: List<SpokenLanguage?>?,
    @SerializedName("status")
    @Expose
    val status: String?, // Released
    @SerializedName("tagline")
    @Expose
    val tagline: String?, // Mischief. Mayhem. Soap.
    @SerializedName("title")
    @Expose
    val title: String?, // Fight Club
    @SerializedName("video")
    @Expose
    val video: Boolean?, // false
    @SerializedName("vote_average")
    @Expose
    val voteAverage: Float?, // 8.433
    @SerializedName("vote_count")
    @Expose
    val voteCount: Int? // 26280
)