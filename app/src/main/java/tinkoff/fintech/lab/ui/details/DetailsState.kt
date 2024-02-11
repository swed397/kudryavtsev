package tinkoff.fintech.lab.ui.details

sealed interface DetailsState {
    data object Loading : DetailsState
    data class Data(val filmDetails: FilmDetailsUiModel) : DetailsState
    data object Error: DetailsState
}