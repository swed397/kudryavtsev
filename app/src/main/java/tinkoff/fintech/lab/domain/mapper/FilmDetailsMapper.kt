package tinkoff.fintech.lab.domain.mapper

import tinkoff.fintech.lab.data.db.model.FilmEntity
import tinkoff.fintech.lab.data.network.model.CurrentFilmResponse
import tinkoff.fintech.lab.ui.details.FilmDetailsUiModel

fun CurrentFilmResponse.toUiModel(): FilmDetailsUiModel =
    FilmDetailsUiModel(
        filmId = filmId,
        filmName = nameRu ?: "",
        filmYear = year?.toInt() ?: 0,
        filmGenresString = genres.joinToString { it.genre ?: "" },
        filmCountriesString = countries.joinToString { it.country ?: "" },
        filmRating = rating ?: "0",
        posterUrl = posterUrl ?: "",
        description = description ?: "",
        filmLength = filmLength?.lengthToUiString() ?: ""
    )

fun FilmEntity.toUiModel(): FilmDetailsUiModel =
    FilmDetailsUiModel(
        filmId = id,
        filmName = nameRus,
        filmYear = year,
        filmGenresString = genresString,
        filmCountriesString = countriesString,
        filmRating = rating,
        posterUrl = posterUrl,
        description = description,
        filmLength = filmLength.lengthToUiString()
    )

private fun Int.lengthToUiString(): String {
    val doubleValue = this / 60.0
    val hours = doubleValue.toInt()
    val minutes = this - 60 * hours
    return "$hours ч $minutes мин"
}