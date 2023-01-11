package com.example.netplix.network

import com.example.netplix.pojo.MoviesPage
import com.example.netplix.pojo.TvPage
import io.reactivex.rxjava3.core.Observable

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.TimeZone

public interface Api {
    //Movies Api
    @GET("movie/popular")
    fun getPopularMovie(
        @Query("api_key") api_key: String,
        @Query("language") language: String,
        @Query("include_adult") adult: Boolean = false
    ): Observable<MoviesPage>;

    @GET("trending/movie/week")
    fun getWeekTrendingMovies(
        @Query("api_key") api_key: String,
        @Query("language") language: String,
        @Query("include_adult") adult: Boolean = false
    ): Observable<MoviesPage>;

    @GET("movie/upcoming")
    fun getUpcomingMovies(
        @Query("api_key") api_key: String,
        @Query("language") language: String,
        @Query("include_adult") adult: Boolean = false
    ): Observable<MoviesPage>;

    //TV Api
    @GET("tv/popular")
    fun getPopularTv(
        @Query("api_key") api_key: String,
        @Query("language") language: String,
        @Query("include_adult") adult: Boolean = false
    ): Observable<TvPage>;

    @GET("tv/top_rated")
    fun getTopRatedTv(
        @Query("api_key") api_key: String,
        @Query("language") language: String,
        @Query("include_adult") adult: Boolean = false
    ): Observable<TvPage>;

    @GET("tv/airing_today")
    fun getLatestTv(
        @Query("api_key") api_key: String,
        @Query("language") language: String,
        @Query("include_adult") adult: Boolean = false,
        @Query("timezone")timeZone: String="UTC+02:00"
    ): Observable<TvPage>;

    //Search
    @GET("search/movie")
    fun searchMovie(
        @Query("api_key") api_key: String,
        @Query("query") query: String,
        @Query("language") language: String,
        @Query("include_adult") adult: Boolean = false
    ): Observable<MoviesPage>;

    @GET("search/tv")
    fun searchTV(
        @Query("api_key") api_key: String,
        @Query("query") query: String,
        @Query("language") language: String,
        @Query("include_adult") adult: Boolean = false
    ): Observable<TvPage>;
}