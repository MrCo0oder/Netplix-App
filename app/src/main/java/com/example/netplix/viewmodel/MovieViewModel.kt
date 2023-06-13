package com.example.netplix.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.netplix.PagingMoviesSource
import com.example.netplix.databinding.FragmentMoviesBinding
import com.example.netplix.pojo.*
import com.example.netplix.pojo.movieDetails.MovieDetails
import com.example.netplix.repository.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(var repository: Repo) : ViewModel() {
    private val TAG = "MovieViewModel"
    var compositeDisposable = CompositeDisposable()

    private val upComingList: MutableLiveData<List<MovieModel>> =
        MutableLiveData<List<MovieModel>>()
    private val movieDetails: MutableLiveData<MovieDetails> =
        MutableLiveData<MovieDetails>()

    private val moviesSearchList: MutableLiveData<List<MovieModel>> =
        MutableLiveData<List<MovieModel>>()
    private val tvSearchList: MutableLiveData<List<TvModel>> = MutableLiveData<List<TvModel>>()
    private lateinit var moviesList: LiveData<List<MovieModel>>
    val moviesPagingList = Pager(PagingConfig(1)) {
        PagingMoviesSource(repository, 1)
    }.flow.cachedIn(viewModelScope)
    val trendyMoviesList = Pager(PagingConfig(2)) {
        PagingMoviesSource(repository, 2)
    }.flow.cachedIn(viewModelScope)

    init {
        movieDetails.postValue(null)
    }

    fun getUpComingList(): MutableLiveData<List<MovieModel>> {
        return upComingList
    }

    fun getMovieDetails() = movieDetails
    fun getMoviesSearchList(): MutableLiveData<List<MovieModel>> {
        return moviesSearchList
    }

    fun getTvSearchList(): MutableLiveData<List<TvModel>> {
        return tvSearchList
    }

    fun getMoviesFromDB(): LiveData<List<MovieModel>> {
        return moviesList
    }



    fun getMovie(id: Int) {
        compositeDisposable.add(
            repository.getMovieDetails(id)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({ s -> movieDetails.postValue(s) }, { it ->
                    Log.e(TAG, "getUpComingMovies: " + it)
                })
        )
    }

    fun getUpComing() {
        compositeDisposable.add(
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
        )
    }

    fun getSearchMovies(query: String) {
        compositeDisposable.add(
            repository.getSearchMovies(query).subscribeOn(Schedulers.io())
                .map(object : Function<MoviesPage, List<MovieModel>> {
                    override fun apply(t: MoviesPage): List<MovieModel> {
                        return t.results
                    }

                }).debounce(3, TimeUnit.SECONDS)
                .distinctUntilChanged()
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({ s -> moviesSearchList.postValue(s) }, { it ->
                    Log.e(TAG, "getMoviesSearch: " + it)
                })
        )
    }

    fun getSearchTv(query: String) {
        compositeDisposable.add(repository.getSearchTv(query).subscribeOn(Schedulers.io())
            .map(object : Function<TvPage, List<TvModel>> {
                override fun apply(t: TvPage): List<TvModel> {
                    return t.results
                }

            }).debounce(3, TimeUnit.SECONDS)
            .distinctUntilChanged()
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe({ s -> tvSearchList.postValue(s) }, { it ->
                Log.e(TAG, "getTvSearch: " + it)
            })
        )

    }
    //ROOM

     fun insertMovie(movieModel: MovieModel) {
        repository.insertMovie(movieModel)
    }

     fun deleteMovie(movieId: Int) {
        repository.deleteMovie(movieId)
    }

     fun getAllMovies() {
        moviesList = repository.getAllMovies()
    }
     fun findMovie(movieId: Int): Boolean {
       return repository.findMovie(movieId)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}












