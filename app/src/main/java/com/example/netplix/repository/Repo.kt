package com.example.netplix.repository

import androidx.lifecycle.LiveData
import com.example.netplix.BuildConfig
import com.example.netplix.database.Dao
import com.example.netplix.network.Api
import com.example.netplix.pojo.MovieModel
import com.example.netplix.pojo.MoviesPage
import com.example.netplix.pojo.TvModel
import com.example.netplix.pojo.TvPage
import com.example.netplix.pojo.movieDetails.MovieDetails
import com.example.netplix.pojo.images.BackDrops
import com.example.netplix.pojo.stream.Links
import com.example.netplix.pojo.tvDetails.TvDetails
import io.reactivex.rxjava3.core.Observable
import retrofit2.Response
import java.util.Locale
import javax.inject.Inject

class Repo @Inject constructor(private val apiService: Api, private val dbService: Dao) {
    private val apiKey = BuildConfig.API_KEY

    //API
    //Movies
    suspend fun getPopMovies(page: Int): Response<MoviesPage> {
        return apiService
            .getPopularMovie(
                api_key = apiKey,
                language = Locale.getDefault().language.toString(),
                page = page
            )
    }

    suspend fun getTrendyMovies(page: Int): Response<MoviesPage> {
        return apiService
            .getWeekTrendingMovies(
                api_key = apiKey,
                language = Locale.getDefault().language.toString(),
                page = page
            )
    }

    public fun getMovieDetails(id: Int): Observable<MovieDetails> {
        return apiService
            .getMovie(
                api_key = apiKey,
                language = Locale.getDefault().language.toString(),
                id = id
            )
    }

    public fun getMovieImages(id: Int): Observable<BackDrops> {
        return apiService
            .getMovieImages(api_key = apiKey, id = id)
    }

    public fun getMovieLinks(id: Int): Observable<Links> {
        return apiService
            .getLinks(id = id, url = "https://seapi.link/")
    }

    public fun getUpComing(): Observable<MoviesPage> {
        return apiService
            .getUpcomingMovies(
                api_key = apiKey,
                language = Locale.getDefault().language.toString()
            )
    }

    //TV
    public fun getPopTv(): Observable<TvPage> {
        return apiService
            .getPopularTv(
                api_key = apiKey,
                language = Locale.getDefault().language.toString()
            )
    }

    public fun getTopRatedTv(): Observable<TvPage> {
        return apiService
            .getTopRatedTv(
                api_key = apiKey,
                language = Locale.getDefault().language.toString()
            )
    }

    public fun getLatestTv(): Observable<TvPage> {
        return apiService
            .getLatestTv(
                api_key = apiKey,
                language = Locale.getDefault().language.toString()
            )
    }

    public fun getTvDetails(id: Int): Observable<TvDetails> {
        return apiService
            .getShow(
                api_key = apiKey,
                language = Locale.getDefault().language.toString(),
                id = id
            )
    }

    public fun getShowImages(id: Int): Observable<BackDrops> {
        return apiService
            .getShowImages(api_key = apiKey, id = id)
    }
    //Search
    public fun getSearchMovies(query: String): Observable<MoviesPage> {
        return apiService
            .searchMovie(
                api_key = apiKey,
                query = query,
                language = Locale.getDefault().language.toString()
            )
    }

    public fun getSearchTv(query: String): Observable<TvPage> {
        return apiService
            .searchTV(
                api_key = apiKey,
                query = query,
                language = Locale.getDefault().language.toString()
            )
    }

    //Room
    //Movies
    public fun insertMovie(movieModel: MovieModel){
        dbService.insertMovie(movieModel)
    }

    public fun deleteMovie(movieId: Int) {
        dbService.deleteMovie(movieId)
    }

    public fun getAllMovies(): LiveData<List<MovieModel>> {
        return dbService.getAllMovies()
    }
    public fun findMovie(id: Int):Boolean{
        return dbService.findMovie(id)
    }

    //Tv
    public fun insertTv(tvModel: TvModel) {
        dbService.insertTv(tvModel)
    }

    public fun deleteTv(tvId: Int) {
        dbService.deleteTv(tvId)
    }

    public fun getAllTv(): LiveData<List<TvModel>> {
        return dbService.getAllTv()
    }
    public fun findTv(id: Int): Boolean {
        return dbService.findTv(id)
    }
}