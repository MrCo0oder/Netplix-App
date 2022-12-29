package com.example.netplix.repo

import androidx.lifecycle.MutableLiveData
import com.example.netplix.pojo.Page

interface MoviesRepo {
   public fun getMovie(): Pair<MutableLiveData<Page>, MutableLiveData<String>>;
    public fun getWeekTrendingMovies(): Pair<MutableLiveData<Page>, MutableLiveData<String>>;
    public fun getUpcomingMovies(): Pair<MutableLiveData<Page>, MutableLiveData<String>>;
    public fun getSearchMovies(query: String): Pair<MutableLiveData<Page>, MutableLiveData<String>>;
    public fun getSearchTv(query: String): Pair<MutableLiveData<Page>, MutableLiveData<String>>;
}