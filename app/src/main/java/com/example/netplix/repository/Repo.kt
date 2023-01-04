package com.example.netplix.repository


import com.example.netplix.BuildConfig
import com.example.netplix.network.Api
import com.example.netplix.pojo.MoviesPage
import com.example.netplix.pojo.TvPage
import io.reactivex.rxjava3.core.Observable
import java.util.*
import javax.inject.Inject

class Repo {
    private val apiKey = BuildConfig.API_KEY
    private  val apiService: Api
    @Inject
    constructor(apiService: Api) {
        this.apiService = apiService
    }
    //Movies
    public fun getPopMovies():Observable<MoviesPage>{
        return apiService.getPopularMovie(apiKey, Locale.getDefault().language.toString())
    }
    public fun getTrendyMovies():Observable<MoviesPage>{
        return apiService.getWeekTrendingMovies(apiKey, Locale.getDefault().language.toString())
    }
    public fun getUpComing():Observable<MoviesPage>{
        return apiService.getUpcomingMovies(apiKey, Locale.getDefault().language.toString())
    }
    //TV
    public fun getPopTv():Observable<TvPage>{
        return apiService.getPopularTv(apiKey, Locale.getDefault().language.toString())
    }
    public fun getTopRatedTv():Observable<TvPage>{
        return apiService.getTopRatedTv(apiKey, Locale.getDefault().language.toString())
    }
    public fun getLatestTv():Observable<TvPage>{
        return apiService.getLatestTv(apiKey, Locale.getDefault().language.toString())
    }
    //Search
    public fun  getSearchMovies(query: String):Observable<MoviesPage>{
        return apiService.searchMovie(apiKey,query, Locale.getDefault().language.toString())
    }
    public fun  getSearchTv(query: String):Observable<TvPage>{
        return apiService.searchTV(apiKey,query, Locale.getDefault().language.toString())
    }
}