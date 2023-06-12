package com.example.netplix

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.netplix.pojo.MovieModel
import com.example.netplix.repository.Repo
import io.reactivex.rxjava3.core.Observer
import retrofit2.HttpException

class PagingMoviesSource(private val repo: Repo ) : PagingSource <Int,MovieModel>() {

     override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieModel> {
        return try {
            val currentPage= params.key ?:1
            val response=repo.getPopMovies(currentPage)
            val data=response.body()?.results
            val resData= mutableListOf<MovieModel>(  )
            if (data != null) {
                resData.addAll(data)
            }
            LoadResult.Page(data=resData, prevKey = if (currentPage==1) null else-1,
            nextKey = currentPage.plus(1))
        }catch (e:Exception){
            LoadResult.Error(e)
        }catch (e:HttpException)
        {
            LoadResult.Error(e)
        }
     }

     override fun getRefreshKey(state: PagingState<Int, MovieModel>): Int? {
         return null
     }
 }