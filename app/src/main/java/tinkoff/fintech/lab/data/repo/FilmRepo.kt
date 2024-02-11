package tinkoff.fintech.lab.data.repo

import tinkoff.fintech.lab.data.db.FilmDb
import tinkoff.fintech.lab.data.db.model.FilmEntity
import tinkoff.fintech.lab.data.network.KinopoiskApi
import tinkoff.fintech.lab.data.network.model.CurrentFilmResponse
import tinkoff.fintech.lab.domain.mapper.toModel
import tinkoff.fintech.lab.domain.model.FilmModel
import javax.inject.Inject

class FilmRepo @Inject constructor(
    private val filmDb: FilmDb,
    private val kinopoiskApi: KinopoiskApi
) {

    suspend fun getAllPopularFilms(): List<FilmModel> =
        kinopoiskApi.getTopFilms().films.map { it.toModel() }

    suspend fun getPopularFilmById(filmId: Long): CurrentFilmResponse =
        kinopoiskApi.getFilmById(filmId = filmId)

    suspend fun saveFilmInDb(filmEntity: FilmEntity) = filmDb.filmDao().insertFilm(filmEntity)

    suspend fun deleteFilmFromDb(filmId: Long) = filmDb.filmDao().deleteFilmById(filmId)

    suspend fun getFavoriteFilms(): List<FilmModel> =
        filmDb.filmDao().getAllFilms().map { it.toModel() }

    suspend fun getFavoriteFilmById(filmId: Long): FilmEntity =
        filmDb.filmDao().getFilmById(filmId = filmId)
}