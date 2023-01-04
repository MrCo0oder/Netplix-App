package com.example.netplix.di

import com.example.netplix.network.Api
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
public class RetrofitModule  {
    @Provides
    @Singleton
     fun apiService(): Api {
       return Retrofit.Builder()
           .baseUrl("https://api.themoviedb.org/3/")
           .addConverterFactory(GsonConverterFactory.create())
           .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
           .build()
           .create(Api::class.java)
    }
}