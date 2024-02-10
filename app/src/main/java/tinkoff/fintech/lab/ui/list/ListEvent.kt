package tinkoff.fintech.lab.ui.list

sealed interface ListEvent {

    data class ChangeFilmType(val filmType: FilmType) : ListEvent
    data class AddFilmToFavorite(val filmId: Long): ListEvent
}