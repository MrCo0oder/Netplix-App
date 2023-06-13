package com.example.netplix

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.netplix.pojo.MovieModel
import com.example.netplix.repository.Repo
import retrofit2.HttpException

class PagingMoviesSource(private val repo: Repo, private var id: Int) :
    PagingSource<Int, MovieModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieModel> {
        return try {
            when (id) {
                1 -> {
                    val currentPage = params.key ?: 1
                    val response = repo.getPopMovies(currentPage)
                    val data = response.body()?.results
                    val resData = mutableListOf<MovieModel>()
                    if (data != null) {
                        resData.addAll(data.sortedBy { movieModel -> movieModel.vote_average }.reversed())
                    }
                    LoadResult.Page(
                        data = resData, prevKey = if (currentPage == 1) null else -1,
                        nextKey = currentPage.plus(1)
                    )
                }

                2 -> {
                    val currentPage = params.key ?: 1
                    val response = repo.getTrendyMovies(currentPage)
                    val data = response.body()?.results
                    val resData = mutableListOf<MovieModel>()
                    if (data != null) {
                        resData.addAll(data)
                    }
                    LoadResult.Page(
                        data = resData, prevKey = if (currentPage == 1) null else -1,
                        nextKey = currentPage.plus(1)
                    )
                }

                else -> {

                    LoadResult.Page(
                        data = emptyList(), prevKey =-1,
                        nextKey = 0
                    )
                }
            }

        } catch (e: Exception) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieModel>): Int? {
        return null
    }
}