package tinkoff.fintech.lab.ui.list

import tinkoff.fintech.lab.domain.model.FilmModel

sealed interface ListState {
    data object Loading : ListState
    data class Data(val data: List<FilmModel>) : ListState
}