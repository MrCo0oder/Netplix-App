package com.example.netplix.repo

import androidx.lifecycle.MutableLiveData
import com.example.netplix.pojo.Page

interface MoviesRepo {
   public fun getMovie(): Pair<MutableLiveData<Page>, MutableLiveData<String>>;
    public fun getWeekTrendingMovies(): Pair<MutableLiveData<Page>, MutableLiveData<String>>;

}