package tinkoff.fintech.lab.ui.list

data class FilmListUiModel(
    val filmId: Long,
    val filmPosterUrl: String,
    val filmTitle: String,
    val filmGenre: String,
    val filmYear: Int,
    val isFavorite: Boolean
)
