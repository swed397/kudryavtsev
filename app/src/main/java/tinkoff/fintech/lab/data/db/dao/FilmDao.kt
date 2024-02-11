package tinkoff.fintech.lab.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tinkoff.fintech.lab.data.db.model.FilmEntity

@Dao
interface FilmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilm(filmEntity: FilmEntity)

    @Query("SELECT * FROM film")
    suspend fun getAllFilms(): List<FilmEntity>

    @Query("DELETE FROM film WHERE film.id = :filmId")
    suspend fun deleteFilmById(filmId: Long)

    @Query("SELECT * FROM film WHERE film.id = :filmId")
    suspend fun getFilmById(filmId: Long): FilmEntity
}