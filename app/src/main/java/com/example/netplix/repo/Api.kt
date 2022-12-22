package com.example.netplix.repo

import com.example.netplix.pojo.Page
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("movie/popular")
    fun getPopularMovie(@Query("api_key") api_key: String): Call<Page>;
    @GET("trending/movie/week")
    fun getWeekTrendingMovies(@Query("api_key") api_key: String): Call<Page>;
}