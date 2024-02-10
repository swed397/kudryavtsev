package tinkoff.fintech.lab.domain.mapper

import tinkoff.fintech.lab.data.db.model.FilmEntity
import tinkoff.fintech.lab.data.network.model.CurrentFilmResponse
import tinkoff.fintech.lab.data.network.model.FilmPreviewResponse
import tinkoff.fintech.lab.domain.model.FilmModel

fun FilmPreviewResponse.toModel(): FilmModel =
    FilmModel(
        id = filmId,
        nameRus = nameRu ?: "",
        nameEng = nameEn ?: "",
        year = year?.toInt() ?: 0,
        genres = genres.map { it.genre ?: "" },
        countries = countries.map { it.country ?: "" },
        rating = rating ?: "",
        posterUrlPreview = posterUrlPreview ?: "",
        posterUrl = posterUrl ?: ""
    )

fun CurrentFilmResponse.toEntity(): FilmEntity =
    FilmEntity(
        id = filmId,
        nameRus = nameRu ?: "",
        nameEng = nameEn ?: "",
        year = year?.toInt() ?: 0,
        genresString = genres.joinToString(", "),
        countriesString = countries.joinToString(", "),
        rating = rating ?: "",
        posterUrlPreview = posterUrlPreview ?: "",
        posterUrl = posterUrl ?: "",
        filmLength = filmLength ?: "",
        description = description ?: ""
    )