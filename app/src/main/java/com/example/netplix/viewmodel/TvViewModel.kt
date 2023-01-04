package com.example.netplix.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.netplix.pojo.TvModel
import com.example.netplix.pojo.TvPage
import com.example.netplix.repository.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class TvViewModel @Inject constructor(var repository: Repo): ViewModel(){
    private val TAG = "TvViewModel"
    private val popTvList: MutableLiveData<List<TvModel>> = MutableLiveData<List<TvModel>>()
    private val topRatedList: MutableLiveData<List<TvModel>> = MutableLiveData<List<TvModel>>()
    private val latestList: MutableLiveData<List<TvModel>> = MutableLiveData<List<TvModel>>()


    fun getPopTvList(): MutableLiveData<List<TvModel>> {
        return popTvList
    }
    fun getTopRatedTvList(): MutableLiveData<List<TvModel>> {
        return topRatedList
    }
    fun getLatestList(): MutableLiveData<List<TvModel>> {
        return latestList
    }


    fun getPopTv(){
        repository.getPopTv()
            .subscribeOn(Schedulers.io())
            .map(object : Function<TvPage,List<TvModel>> {
                override fun apply(t: TvPage): List<TvModel> {
                    return t.results
                }

            })
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe ({ s ->popTvList.postValue(s)   },{
                it ->
                Log.e(TAG, "getPopTv: "+it )
            })

    }
    fun getTopRatedTv(){
        repository.getTopRatedTv()
            .subscribeOn(Schedulers.io())
            .map(object : Function<TvPage,List<TvModel>> {
                override fun apply(t: TvPage): List<TvModel> {
                    return t.results
                }

            })
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe ({ s ->topRatedList.postValue(s)   },{
                    it ->
                Log.e(TAG, "getTopRatedTv: "+it )
            })

    }
    fun getLatestTv(){
        repository.getLatestTv().subscribeOn(Schedulers.io()).map(object : Function<TvPage,List<TvModel>> {
            override fun apply(t: TvPage): List<TvModel> {
                return t.results
            }
        }).subscribeOn(AndroidSchedulers.mainThread())
            .subscribe ({ s ->latestList.postValue(s)   },
                { it -> Log.e(TAG, "getLatestTv: "+it ) })
    }

}










