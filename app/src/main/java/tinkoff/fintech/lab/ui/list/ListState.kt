package tinkoff.fintech.lab.ui.list

import tinkoff.fintech.lab.domain.model.FilmType
import tinkoff.fintech.lab.ui.details.FilmDetailsUiModel

sealed interface ListState {
    val filmType: FilmType
        get() = FilmType.POPULAR

    data object Loading : ListState
    data class Data(
        val data: List<FilmListUiModel>,
        val visibleData: List<FilmListUiModel>,
        override val filmType: FilmType,
        val filmDetails: FilmDetailsUiModel? = null
    ) : ListState

    data class NoContent(
        val data: List<FilmListUiModel>,
        override val filmType: FilmType
    ) : ListState

    data object Error : ListState
}