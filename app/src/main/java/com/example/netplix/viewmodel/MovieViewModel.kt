package com.example.netplix.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.netplix.pojo.Page
import com.example.netplix.repo.MovieImpli
import com.example.netplix.repo.MoviesRepo

class MovieViewModel : ViewModel() {

    private val instance: MoviesRepo = MovieImpli()

    fun getMovie(): Pair<MutableLiveData<Page>, MutableLiveData<String>> {
        return instance.getMovie()
    }
  fun getWeekTrendingMovies():Pair<MutableLiveData<Page>, MutableLiveData<String>> {
        return instance.getWeekTrendingMovies()
    }
    fun getUpcomingMovies():Pair<MutableLiveData<Page>, MutableLiveData<String>> {
        return instance.getUpcomingMovies()
    }
    fun getSearchMovies(query: String):Pair<MutableLiveData<Page>, MutableLiveData<String>> {
        return instance.getSearchMovies(query)
    }
fun getSearchTv(query: String):Pair<MutableLiveData<Page>, MutableLiveData<String>> {
    return instance.getSearchTv(query)
}
}