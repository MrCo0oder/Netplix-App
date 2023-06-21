package com.example.netplix.network

import com.example.netplix.pojo.MoviesPage
import com.example.netplix.pojo.TvPage
import com.example.netplix.pojo.movieDetails.MovieDetails
import com.example.netplix.pojo.images.BackDrops
import com.example.netplix.pojo.stream.Links
import com.example.netplix.pojo.tvDetails.TvDetails
import io.reactivex.rxjava3.core.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

public interface Api {
    //Movies Api
    @GET("movie/popular")
    suspend fun getPopularMovie(
        @Query("api_key") api_key: String,
        @Query("language") language: String,
        @Query("include_adult") adult: Boolean = false,
        @Query("page") page: Int
    ): Response<MoviesPage>

    @GET("movie/{movie_id}")
    fun getMovie(
        @Path("movie_id") id: Int,
        @Query("api_key") api_key: String,
        @Query("language") language: String,
        @Query("include_adult") adult: Boolean = false
    ): Observable<MovieDetails>

    @GET("movie/{movie_id}/images")
    fun getMovieImages(
        @Path("movie_id") id: Int,
        @Query("api_key") api_key: String,
        @Query("include_adult") adult: Boolean = false
    ): Observable<BackDrops>
    @GET("tv/{series_id}/images")
    fun getShowImages(
        @Path("series_id") id: Int,
        @Query("api_key") api_key: String,
        @Query("include_adult") adult: Boolean = false
    ): Observable<BackDrops>
    @GET
    fun getLinks(
        @Url url: String,
        @Query("type") type: String = "tmdb",
        @Query("id") id: Int,
        @Query("season") season: Int = 1,
        @Query("episode") episode: Int = 1,
        @Query("max_results") max_results: Int = 1
    ): Observable<Links>

    @GET("trending/movie/week")
    suspend fun getWeekTrendingMovies(
        @Query("api_key") api_key: String,
        @Query("language") language: String,
        @Query("include_adult") adult: Boolean = false,
        @Query("page") page: Int
    ): Response<MoviesPage>

    @GET("movie/upcoming")
    fun getUpcomingMovies(
        @Query("api_key") api_key: String,
        @Query("language") language: String,
        @Query("include_adult") adult: Boolean = false
    ): Observable<MoviesPage>

    //TV Api
    @GET("tv/popular")
    fun getPopularTv(
        @Query("api_key") api_key: String,
        @Query("language") language: String,
        @Query("include_adult") adult: Boolean = false
    ): Observable<TvPage>

    @GET("tv/top_rated")
    fun getTopRatedTv(
        @Query("api_key") api_key: String,
        @Query("language") language: String,
        @Query("include_adult") adult: Boolean = false
    ): Observable<TvPage>

    @GET("tv/airing_today")
    fun getLatestTv(
        @Query("api_key") api_key: String,
        @Query("language") language: String,
        @Query("include_adult") adult: Boolean = false,
        @Query("timezone") timeZone: String = "UTC+03:00"
    ): Observable<TvPage>

    @GET("tv/{show_id}")
    fun getShow(
        @Path("show_id") id: Int,
        @Query("api_key") api_key: String,
        @Query("language") language: String,
        @Query("include_adult") adult: Boolean = false
    ): Observable<TvDetails>
    //Search
    @GET("search/movie")
    fun searchMovie(
        @Query("api_key") api_key: String,
        @Query("query") query: String,
        @Query("language") language: String,
        @Query("include_adult") adult: Boolean = false
    ): Observable<MoviesPage>

    @GET("search/tv")
    fun searchTV(
        @Query("api_key") api_key: String,
        @Query("query") query: String,
        @Query("language") language: String,
        @Query("include_adult") adult: Boolean = false
    ): Observable<TvPage>
}