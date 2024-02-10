package tinkoff.fintech.lab.ui.list

import tinkoff.fintech.lab.domain.model.FilmModel

sealed interface ListState {
    val filmType: FilmType
        get() = FilmType.POPULAR

    data object Loading : ListState
    data class Data(val data: List<FilmModel>, override val filmType: FilmType) : ListState
}