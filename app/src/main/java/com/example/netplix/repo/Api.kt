package com.example.netplix.repo

import com.example.netplix.pojo.Page
import retrofit2.Call

import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    //Movies Api
    @GET("movie/popular")
    fun getPopularMovie(@Query("api_key") api_key: String): Call<Page>;
    @GET("trending/movie/week")
    fun getWeekTrendingMovies(@Query("api_key") api_key: String): Call<Page>;
    @GET("movie/upcoming")
    fun getUpcomingMovies(@Query("api_key") api_key: String): Call<Page>;
    //TV Api

    //Search
    @GET("search/movie")
    fun searchMovie(@Query("api_key") api_key: String,@Query("query") query: String):Call<Page>;
    @GET("search/tv")
    fun searchTV(@Query("api_key") api_key: String,@Query("query") query: String): Call<Page>;
}