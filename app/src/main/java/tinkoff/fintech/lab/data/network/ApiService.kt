package tinkoff.fintech.lab.data.network

import tinkoff.fintech.lab.domain.mapper.toModel
import tinkoff.fintech.lab.domain.model.FilmModel
import javax.inject.Inject

class ApiService @Inject constructor(private val kinopoiskApi: KinopoiskApi) {

    suspend fun getAllFilms(): List<FilmModel> =
        kinopoiskApi.getTopFilms().films.map { it.toModel() }
}