package com.example.netplix.utils

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.netplix.models.MovieModel
import com.example.netplix.repository.Repo
import retrofit2.HttpException

class PagingMoviesSource(
    private val repo: Repo,
    private var id: Int,
    private var callback: (NetworkState) -> Unit
) :
    PagingSource<Int, MovieModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieModel> {
        return try {
            callback(NetworkState.LOADING)
            when (id) {
                1 -> {
                    val currentPage = params.key ?: 1
                    val response = repo.getPopMovies(currentPage)
                    val data = response.body()?.results
                    val resData = mutableListOf<MovieModel>()
                    if (data != null) {
                        callback(NetworkState.LOADED)
                        resData.addAll(data.sortedBy { movieModel -> movieModel.vote_average }
                            .reversed())
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
                    if (params.key == 1) {
                        if (response.isSuccessful) {

                        } else
                            callback(NetworkState.ERROR)
                    }
                    if (data != null) {
                        callback(NetworkState.LOADED)
                        resData.addAll(data)
                    }
                    LoadResult.Page(
                        data = resData, prevKey = if (currentPage == 1) null else -1,
                        nextKey = currentPage.plus(1)
                    )
                }

                else -> {
                    callback(NetworkState.ERROR)
                    LoadResult.Page(
                        data = emptyList(), prevKey = -1,
                        nextKey = 0
                    )
                }
            }

        } catch (e: Exception) {
            callback(NetworkState.ERROR)
            LoadResult.Error(e)
        } catch (e: HttpException) {
            callback(NetworkState.ERROR)
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieModel>): Int? {
        return null
    }
}