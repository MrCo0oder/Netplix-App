package com.example.netplix.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.netplix.pojo.MovieModel
import com.example.netplix.pojo.TvModel
import com.example.netplix.pojo.movieDetails.MovieDetails

@Dao
public interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public fun insertMovie(movie: MovieModel)

    @Query("DELETE FROM FILM_LIST WHERE id = :movieId")
    public fun deleteMovie(movieId: Int)

    @Query("SELECT * FROM FILM_LIST")
    public fun getAllMovies(): LiveData<List<MovieModel>>
    @Query("SELECT id FROM FILM_LIST WHERE EXISTS(SELECT id FROM FILM_LIST WHERE id = :id ) ")
    public fun findMovie(id: Int):Boolean
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public fun insertTv(tv: TvModel)

    @Query("DELETE FROM TV_LIST WHERE id = :tvId")
    public fun deleteTv(tvId: Int)

    @Query("SELECT * FROM TV_LIST")
    public fun getAllTv(): LiveData<List<TvModel>>

}