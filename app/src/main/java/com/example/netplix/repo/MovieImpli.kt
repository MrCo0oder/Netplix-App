package com.example.netplix.repo

import androidx.lifecycle.MutableLiveData
import com.example.netplix.BuildConfig
import com.example.netplix.pojo.Page
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieImpli : MoviesRepo {
    private val apiKey = BuildConfig.API_KEY
    val weeklyTrendingMoviesMutableLiveData: MutableLiveData<Page> = MutableLiveData()
    val weeklyTrendingMoviesMutableLiveDataError : MutableLiveData<String> = MutableLiveData()
    val popMoviesMutableLiveData: MutableLiveData<Page> = MutableLiveData()
    val popMoviesMutableLiveDataError : MutableLiveData<String> = MutableLiveData()
    override fun getMovie(): Pair<MutableLiveData<Page>,MutableLiveData<String>> {
        RetrofitInstance.api.getPopularMovie(apiKey).enqueue(object : Callback<Page> {
            override fun onResponse(call: Call<Page>, response: Response<Page>) {
                popMoviesMutableLiveData.postValue(response.body())
            }

            override fun onFailure(call: Call<Page>, t: Throwable) {
                popMoviesMutableLiveDataError.postValue(t.message)
            }

        })
        return Pair(popMoviesMutableLiveData,popMoviesMutableLiveDataError)

    }
    override fun getWeekTrendingMovies(): Pair<MutableLiveData<Page>, MutableLiveData<String>> {
        RetrofitInstance.api.getWeekTrendingMovies(apiKey).enqueue(object : Callback<Page> {
            override fun onResponse(call: Call<Page>, response: Response<Page>) {
                weeklyTrendingMoviesMutableLiveData.postValue(response.body())
            }

            override fun onFailure(call: Call<Page>, t: Throwable) {
                popMoviesMutableLiveDataError.postValue(t.message)
            }

        })

        return Pair(weeklyTrendingMoviesMutableLiveData,weeklyTrendingMoviesMutableLiveDataError)
    }


    }





