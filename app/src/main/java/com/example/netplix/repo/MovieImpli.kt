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
    val weeklyTrendingMoviesMutableLiveDataError: MutableLiveData<String> = MutableLiveData()

    val popMoviesMutableLiveData: MutableLiveData<Page> = MutableLiveData()
    val popMoviesMutableLiveDataError: MutableLiveData<String> = MutableLiveData()

    val upComingMoviesMutableLiveData: MutableLiveData<Page> = MutableLiveData()
    val upComingMoviesMutableLiveDataError: MutableLiveData<String> = MutableLiveData()

    val searchMoviesMutableLiveData: MutableLiveData<Page> = MutableLiveData()
    val searchMoviesMutableLiveDataError: MutableLiveData<String> = MutableLiveData()

    val searchTvMutableLiveData: MutableLiveData<Page> = MutableLiveData()
    val searchTvMutableLiveDataError: MutableLiveData<String> = MutableLiveData()
    override fun getMovie(): Pair<MutableLiveData<Page>, MutableLiveData<String>> {
        RetrofitInstance.api.getPopularMovie(apiKey).enqueue(object : Callback<Page> {
            override fun onResponse(call: Call<Page>, response: Response<Page>) {

                if (response.isSuccessful && response.code() == 200) {
                    popMoviesMutableLiveData.postValue(response.body())
                } else {
                    popMoviesMutableLiveDataError.postValue(response.message() + response.errorBody() + " " + response.code())
                }
            }

            override fun onFailure(call: Call<Page>, t: Throwable) {
                popMoviesMutableLiveDataError.postValue(t.message)
            }

        })
        return Pair(popMoviesMutableLiveData, popMoviesMutableLiveDataError)

    }

    override fun getWeekTrendingMovies(): Pair<MutableLiveData<Page>, MutableLiveData<String>> {
        RetrofitInstance.api.getWeekTrendingMovies(apiKey).enqueue(object : Callback<Page> {
            override fun onResponse(call: Call<Page>, response: Response<Page>) {

                if (response.isSuccessful && response.code() == 200) {
                    weeklyTrendingMoviesMutableLiveData.postValue(response.body())
                } else {
                    weeklyTrendingMoviesMutableLiveDataError.postValue(response.message() + response.errorBody() + " " + response.code())
                }
            }

            override fun onFailure(call: Call<Page>, t: Throwable) {
                weeklyTrendingMoviesMutableLiveDataError.postValue(t.message)
            }

        })

        return Pair(weeklyTrendingMoviesMutableLiveData, weeklyTrendingMoviesMutableLiveDataError)
    }

    override fun getUpcomingMovies(): Pair<MutableLiveData<Page>, MutableLiveData<String>> {
        RetrofitInstance.api.getUpcomingMovies(apiKey).enqueue(object : Callback<Page> {
            override fun onResponse(call: Call<Page>, response: Response<Page>) {
                if (response.isSuccessful && response.code() == 200) {
                    upComingMoviesMutableLiveData.postValue(response.body())
                } else {
                    upComingMoviesMutableLiveDataError.postValue(response.message() + response.errorBody() + " " + response.code())
                }

            }

            override fun onFailure(call: Call<Page>, t: Throwable) {
                upComingMoviesMutableLiveDataError.postValue(t.message)
            }

        })

        return Pair(upComingMoviesMutableLiveData, upComingMoviesMutableLiveDataError)
    }

    override fun getSearchMovies(query: String): Pair<MutableLiveData<Page>, MutableLiveData<String>> {
        RetrofitInstance.api.searchMovie(apiKey, query).enqueue(object : Callback<Page> {
            override fun onResponse(call: Call<Page>, response: Response<Page>) {
                if (response.isSuccessful && response.code() == 200) {
                    searchMoviesMutableLiveData.postValue(response.body())
                } else {
                    searchMoviesMutableLiveDataError.postValue(response.message() + response.errorBody() + " " + response.code())
                }
            }

            override fun onFailure(call: Call<Page>, t: Throwable) {
                searchMoviesMutableLiveDataError.postValue(t.message)
            }

        })
        return Pair(searchMoviesMutableLiveData, searchMoviesMutableLiveDataError)
    }
    override fun getSearchTv(query: String): Pair<MutableLiveData<Page>, MutableLiveData<String>> {
        RetrofitInstance.api.searchTV(apiKey, query).enqueue(object : Callback<Page> {
            override fun onResponse(call: Call<Page>, response: Response<Page>) {
                if (response.isSuccessful && response.code() == 200) {
                    searchTvMutableLiveData.postValue(response.body())
                } else {
                    searchTvMutableLiveDataError.postValue(response.message() + response.errorBody() + " " + response.code())
                }
            }

            override fun onFailure(call: Call<Page>, t: Throwable) {
                searchTvMutableLiveDataError.postValue(t.message)
            }

        })
        return Pair(searchTvMutableLiveData, searchTvMutableLiveDataError)
    }
}





