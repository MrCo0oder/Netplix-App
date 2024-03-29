package com.example.netplix.ui.tv

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.netplix.di.FirebaseModule
import com.example.netplix.models.MovieModel
import com.example.netplix.models.TvModel
import com.example.netplix.models.TvPage
import com.example.netplix.models.images.BackDrops
import com.example.netplix.models.tvDetails.TvDetails
import com.example.netplix.repository.Repo
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@SuppressLint("CheckResult")
@HiltViewModel
class TvViewModel @Inject constructor(var repository: Repo, var firebaseModule: FirebaseModule) :
    ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private val TAG = this.javaClass.simpleName
    private val popTvList: MutableLiveData<List<TvModel>> = MutableLiveData<List<TvModel>>()
    private val topRatedList: MutableLiveData<List<TvModel>> = MutableLiveData<List<TvModel>>()
    private val latestList: MutableLiveData<List<TvModel>> = MutableLiveData<List<TvModel>>()
    private val movieDetails: MutableLiveData<TvDetails> =
        MutableLiveData<TvDetails>()
    private lateinit var tvList: LiveData<List<TvModel>>
    private val showsImages: MutableLiveData<BackDrops> =
        MutableLiveData<BackDrops>()


    fun getPopTvList(): MutableLiveData<List<TvModel>> {
        return popTvList
    }

    fun getTopRatedTvList(): MutableLiveData<List<TvModel>> {
        return topRatedList
    }

    fun getLatestList(): MutableLiveData<List<TvModel>> {
        return latestList
    }

    fun getTVFromDB(): LiveData<List<TvModel>> {
        return tvList
    }

    fun getTvDetails() = movieDetails
    fun getTvImages() = showsImages

    fun getPopTv() {
        repository.getPopTv()
            .subscribeOn(Schedulers.io())
            .map(object : Function<TvPage, List<TvModel>> {
                override fun apply(t: TvPage): List<TvModel> {
                    return t.results
                }

            })
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe({ s -> popTvList.postValue(s) }, { it ->
                Log.e(TAG, "getPopTv: " + it)
            })

    }

    fun getTopRatedTv() {
        repository.getTopRatedTv()
            .subscribeOn(Schedulers.io())
            .map(object : Function<TvPage, List<TvModel>> {
                override fun apply(t: TvPage): List<TvModel> {
                    return t.results
                }

            })
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe({ s -> topRatedList.postValue(s) }, { it ->
                Log.e(TAG, "getTopRatedTv: " + it)
            })

    }

    fun getLatestTv() {
        repository.getLatestTv().subscribeOn(Schedulers.io())
            .map(object : Function<TvPage, List<TvModel>> {
                override fun apply(t: TvPage): List<TvModel> {
                    return t.results
                }
            }).subscribeOn(AndroidSchedulers.mainThread())
            .subscribe({ s -> latestList.postValue(s) },
                { it -> Log.e(TAG, "getLatestTv: " + it) })
    }

    fun getTv(id: Int) {
        compositeDisposable.add(
            repository.getTvDetails(id)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({ s -> movieDetails.postValue(s) }, { it ->
                    Log.e(TAG, "getTv: " + it)
                })
        )
    }

    fun getMovieImages(id: Int) {
        compositeDisposable.add(
            repository.getShowImages(id)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({ s -> showsImages.postValue(s) }, { it ->
                    Log.e(TAG, "getMovieImages: " + it)
                })
        )
    }

    //firebase
    fun isFav(id: String, callback: (Boolean, String) -> Unit) {
        firebaseModule.isFavTvShow(id) { b, s ->
            callback(b, s)
        }
    }

    fun deleteTvFromFB(id: String, callback: (Task<Void>) -> Unit) {
        firebaseModule.removeTvShow(id) {
            callback(it)
        }
    }

    fun addTvToFB(tvModel: TvModel, callback: (Boolean, String) -> Unit) {
        firebaseModule.addTvShowToList(tvModel) { b, s ->
            callback(b, s)
        }
    }
    //ROOM

    public fun insertTv(tvModel: TvModel) {
        repository.insertTv(tvModel)
    }

    public fun deleteTv(tvId: Int) {
        repository.deleteTv(tvId)
    }

    public fun getAllTv() {
        tvList = repository.getAllTv()
    }

    fun findTv(id: Int): Boolean {
        return repository.findTv(id)
    }

    public fun isTvListExists(): Boolean {
        return repository.isTvListExists()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}










