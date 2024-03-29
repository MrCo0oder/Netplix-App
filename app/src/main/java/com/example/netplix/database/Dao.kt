package com.example.netplix.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.netplix.models.MovieModel
import com.example.netplix.models.TvModel

@Dao
public interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public fun insertMovie(movie: MovieModel)

    @Query("DELETE FROM FILM_LIST WHERE id = :movieId")
    public fun deleteMovie(movieId: Int)

    @Query("SELECT * FROM FILM_LIST")
    public fun getAllMovies(): LiveData<List<MovieModel>>

    @Query("SELECT id FROM FILM_LIST WHERE EXISTS(SELECT id FROM FILM_LIST WHERE id = :id ) ")
    public fun findMovie(id: Int): Boolean

    @Query("SELECT id FROM film_list WHERE EXISTS(SELECT * FROM film_list) ")
    public fun isMovieTableExist(): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public fun insertTv(tv: TvModel)

    @Query("DELETE FROM TV_LIST WHERE id = :tvId")
    public fun deleteTv(tvId: Int)

    @Query("SELECT * FROM TV_LIST")
    public fun getAllTv(): LiveData<List<TvModel>>

    @Query("SELECT id FROM TV_LIST WHERE EXISTS(SELECT id FROM TV_LIST WHERE id = :id ) ")
    public fun findTv(id: Int): Boolean

    @Query("SELECT id FROM TV_LIST WHERE EXISTS(SELECT * FROM TV_LIST) ")
    public fun isTvTableExist(): Boolean
}