package tinkoff.fintech.lab.ui.list

import tinkoff.fintech.lab.domain.model.FilmType

sealed interface ListEvent {
    data class ChangeFilmType(val filmType: FilmType) : ListEvent
    data class AddFilmToFavorite(val filmId: Long) : ListEvent
    data class FilterByText(val text: String) : ListEvent
    data class OnOrientationChange(val orientation: Int) : ListEvent
    data class NavigateToFilmDetails(val filmId: Long, val isFavorite: Boolean) : ListEvent
    data object ReloadData : ListEvent
}

sealed interface ListUiAction {
    data class Navigate(val route: String) : ListUiAction
}