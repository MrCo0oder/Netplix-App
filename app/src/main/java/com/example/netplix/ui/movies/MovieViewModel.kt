package com.example.netplix.ui.movies

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.netplix.di.FirebaseModule
import com.example.netplix.models.MovieModel
import com.example.netplix.models.MoviesPage
import com.example.netplix.models.TvModel
import com.example.netplix.models.TvPage
import com.example.netplix.models.images.BackDrops
import com.example.netplix.models.movieDetails.MovieDetails
import com.example.netplix.models.stream.Links
import com.example.netplix.repository.Repo
import com.example.netplix.utils.NetworkState
import com.example.netplix.utils.PagingMoviesSource
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(var repository: Repo, var firebaseModule: FirebaseModule) :
    ViewModel() {
    private var movieLinks: MutableLiveData<Links> =
        MutableLiveData<Links>()
    private val TAG = "MovieViewModel"
    var compositeDisposable = CompositeDisposable()

    private val upComingList: MutableLiveData<List<MovieModel>> =
        MutableLiveData<List<MovieModel>>()
    private val movieDetails: MutableLiveData<MovieDetails> =
        MutableLiveData<MovieDetails>()
    private val movieImages: MutableLiveData<BackDrops> =
        MutableLiveData<BackDrops>()

    private val moviesSearchList: MutableLiveData<List<MovieModel>> =
        MutableLiveData<List<MovieModel>>()

    private val popularNetworkState: MutableLiveData<NetworkState> = MutableLiveData<NetworkState>()
    private val trendyNetworkState: MutableLiveData<NetworkState> = MutableLiveData<NetworkState>()
    private val upComingNetworkState: MutableLiveData<NetworkState> =
        MutableLiveData<NetworkState>()

    private val tvSearchList: MutableLiveData<List<TvModel>> = MutableLiveData<List<TvModel>>()
    private lateinit var moviesList: LiveData<List<MovieModel>>

    val popularMoviesList = Pager(PagingConfig(1)) {
        PagingMoviesSource(repository, 1) {
            popularNetworkState.postValue(it)
        }
    }.flow.cachedIn(viewModelScope)
    val trendyMoviesList = Pager(PagingConfig(1)) {
        PagingMoviesSource(repository, 2) {
            trendyNetworkState.postValue(it)
        }
    }.flow.cachedIn(viewModelScope)

    init {
        movieDetails.postValue(null)
        popularNetworkState.postValue(NetworkState.LOADING)
        trendyNetworkState.postValue(NetworkState.LOADING)
        upComingNetworkState.postValue(NetworkState.LOADING)
    }

    fun getUpComingList(): MutableLiveData<List<MovieModel>> {
        return upComingList
    }

    fun getMovieDetails() = movieDetails
    fun getMovieImages() = movieImages
    fun getMovieLinks() = movieLinks
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

    fun getMovieImages(id: Int) {
        compositeDisposable.add(
            repository.getMovieImages(id)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({ s -> movieImages.postValue(s) }, { it ->
                    Log.e(TAG, "getUpComingMovies: " + it)
                })
        )
    }

    fun getMovieLinks(id: Int) {
        compositeDisposable.add(
            repository.getMovieLinks(id)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({ s -> movieLinks.postValue(s) }, { it ->
                    Log.e("Links", it.message.toString())
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
                .subscribe({ s ->
                    upComingList.postValue(s)
                    upComingNetworkState.postValue(NetworkState.LOADED)
                }, { it ->
                    upComingNetworkState.postValue(NetworkState.ERROR)
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

    //firebase
    fun isFav(id: String, callback: (Boolean, String) -> Unit) {
        firebaseModule.isFavMovie(id) { b, s ->
            callback(b, s)
        }
    }

    fun deleteMovieFromFB(id: String, callback: (Task<Void>) -> Unit) {
        firebaseModule.removeMovie(id) {
            callback(it)
        }
    }

    fun addMovieToFB(movieModel: MovieModel, callback: (Boolean, String) -> Unit) {
        firebaseModule.addMovieToList(movieModel) { b, s ->
            callback(b, s)
        }
    }

    fun getSearchTv(query: String) {
        compositeDisposable.add(
            repository.getSearchTv(query).subscribeOn(Schedulers.io())
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

    public fun isMoviesListExists(): Boolean {
        return repository.isMoviesListExists()
    }

    fun DELETE_DATABASE() {
        repository.DELETE_DATABASE()
    }

    fun DATABASE() {
        repository.DATABASE()
    }

    fun getPopularNetworkState() = popularNetworkState
    fun getTrendyNetworkState() = trendyNetworkState
    fun getupComingNetworkState() = upComingNetworkState
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}












