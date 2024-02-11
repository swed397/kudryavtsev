package tinkoff.fintech.lab.ui.details

data class FilmDetailsUiModel(
    val filmId: Long,
    val filmName: String,
    val filmYear: Int,
    val filmGenresString: String,
    val filmCountriesString: String,
    val filmRating: String,
    val posterUrl: String,
    val filmLength: String,
    val description: String
)