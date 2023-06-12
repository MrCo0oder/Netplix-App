package com.example.netplix.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.netplix.PagingMoviesSource
import com.example.netplix.pojo.*
import com.example.netplix.repository.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(var repository: Repo) : ViewModel() {
    private val TAG = "MovieViewModel"
    private val popMoviesList: MutableLiveData<List<MovieModel>> =
        MutableLiveData<List<MovieModel>>()
    private val trendyList: MutableLiveData<List<MovieModel>> = MutableLiveData<List<MovieModel>>()
    private val upComingList: MutableLiveData<List<MovieModel>> =
        MutableLiveData<List<MovieModel>>()
    private val moviesSearchList: MutableLiveData<List<MovieModel>> =
        MutableLiveData<List<MovieModel>>()
    private val tvSearchList: MutableLiveData<List<TvModel>> = MutableLiveData<List<TvModel>>()
    private lateinit var moviesList: LiveData<List<MovieModel>>
    val loading=MutableLiveData<Boolean>()
    val  moviesPagingList=Pager(PagingConfig(1)){
        PagingMoviesSource(repository)
    }.flow.cachedIn(viewModelScope)
    fun getPopMoviesList(): MutableLiveData<List<MovieModel>> {
        return popMoviesList
    }

    fun getTrendyMoviesList(): MutableLiveData<List<MovieModel>> {
        return trendyList
    }

    fun getUpComingList(): MutableLiveData<List<MovieModel>> {
        return upComingList
    }

    fun getMoviesSearchList(): MutableLiveData<List<MovieModel>> {
        return moviesSearchList
    }

    fun getTvSearchList(): MutableLiveData<List<TvModel>> {
        return tvSearchList
    }

    fun getMoviesFromDB(): LiveData<List<MovieModel>> {
        return moviesList
    }

    @SuppressLint("CheckResult")
     fun getPopMovies() {
//        repository.getPopMovies()
//            .subscribeOn(Schedulers.io())
//            .map(object : Function<MoviesPage, List<MovieModel>> {
//                override fun apply(t: MoviesPage): List<MovieModel> {
//                    return t.results
//                }
//            })
//            .subscribeOn(AndroidSchedulers.mainThread()).debounce(2,TimeUnit.SECONDS)
//            .subscribe({ s -> popMoviesList.postValue(s) }, { it ->
//                Log.e(TAG, "getPopMovies: " + it)
//            })

    }
    @SuppressLint("CheckResult")
    fun getTrendyMovies() {
        repository.getTrendyMovies()
            .subscribeOn(Schedulers.io())
            .map(@SuppressLint("CheckResult")
            object : Function<MoviesPage, List<MovieModel>> {
                override fun apply(t: MoviesPage): List<MovieModel> {
                    return t.results
                }

            })
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe({ s -> trendyList.postValue(s) }, { it ->
                Log.e(TAG, "getTrendyMovies: " + it)
            })

    }
    @SuppressLint("CheckResult")
    fun getUpComing() {
        repository.getUpComing()
            .subscribeOn(Schedulers.io())
            .map(object : Function<MoviesPage, List<MovieModel>> {
                override fun apply(t: MoviesPage): List<MovieModel> {
                    return t.results
                }

            })
            .distinctUntilChanged()
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe({ s -> upComingList.postValue(s) }, { it ->
                Log.e(TAG, "getUpComingMovies: " + it)
            })

    }
    @SuppressLint("CheckResult")
    fun getSearchMovies(query: String) {
        repository.getSearchMovies(query).subscribeOn(Schedulers.io())
            .map(object : Function<MoviesPage, List<MovieModel>> {
                override fun apply(t: MoviesPage): List<MovieModel> {
                    return t.results
                }

            }).debounce(3,TimeUnit.SECONDS)
            .distinctUntilChanged()
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe({ s -> moviesSearchList.postValue(s) }, { it ->
                Log.e(TAG, "getMoviesSearch: " + it)
            })
    }
    @SuppressLint("CheckResult")
    fun getSearchTv(query: String) {
        repository.getSearchTv(query).subscribeOn(Schedulers.io())
            .map(object : Function<TvPage, List<TvModel>> {
                override fun apply(t: TvPage): List<TvModel> {
                    return t.results
                }

            }).debounce(3,TimeUnit.SECONDS)
            .distinctUntilChanged()
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe({ s -> tvSearchList.postValue(s) }, { it ->
                Log.e(TAG, "getTvSearch: " + it)
            })
    }
    //ROOM

    public fun insertMovie(movieModel: MovieModel) {
        repository.insertMovie(movieModel)
    }

    public fun deleteMovie(movieId: Int) {
        repository.deleteMovie(movieId)
    }

    public fun getAllMovies() {
        moviesList = repository.getAllMovies()
    }
}












