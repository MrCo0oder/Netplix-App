package com.example.netplix.di

import com.example.netplix.network.Api
import com.example.netplix.utils.Constants
import com.example.netplix.utils.Constants.Companion.BASE_URL
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

//@Module
//@InstallIn(SingletonComponent::class)
//public class RetrofitModule  {
//    @Provides
//    @Singleton
//     fun apiService(baseUrl:String): Api {
//       return Retrofit.Builder()
//          .baseUrl(baseUrl)
//           .addConverterFactory(GsonConverterFactory.create())
//           .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
//           .build()
//           .create(Api::class.java)
//    }
//}