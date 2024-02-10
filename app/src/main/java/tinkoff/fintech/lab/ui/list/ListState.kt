package tinkoff.fintech.lab.ui.list

sealed interface ListState {
    val filmType: FilmType
        get() = FilmType.POPULAR

    data object Loading : ListState
    data class Data(
        val data: List<FilmListUiModel>,
        val visibleData: List<FilmListUiModel>,
        override val filmType: FilmType
    ) : ListState
}