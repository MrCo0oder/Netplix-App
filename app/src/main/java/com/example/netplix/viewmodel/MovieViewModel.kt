package com.example.netplix.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.netplix.pojo.Page
import com.example.netplix.repo.MovieImpli
import com.example.netplix.repo.MoviesRepo

class MovieViewModel : ViewModel() {

    private val getMovie: MoviesRepo = MovieImpli()
    private val getWeekTrendingMovies: MoviesRepo = MovieImpli()
    fun getMovie(): Pair<MutableLiveData<Page>, MutableLiveData<String>> {
        return getMovie.getMovie()
    }
  fun getWeekTrendingMovies():Pair<MutableLiveData<Page>, MutableLiveData<String>> {
        return getWeekTrendingMovies.getWeekTrendingMovies()
    }
}