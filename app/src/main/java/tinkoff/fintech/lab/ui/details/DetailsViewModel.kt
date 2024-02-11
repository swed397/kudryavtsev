package tinkoff.fintech.lab.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tinkoff.fintech.lab.data.repo.FilmRepo
import tinkoff.fintech.lab.domain.mapper.toUiModel
import tinkoff.fintech.lab.domain.model.FilmType
import tinkoff.fintech.lab.util.runSuspendCatching

class DetailsViewModel @AssistedInject constructor(
    private val filmRepo: FilmRepo,
    @Assisted private val filmId: Long,
    @Assisted private val filmType: FilmType
) : ViewModel() {

    private val _state = MutableStateFlow<DetailsState>(DetailsState.Loading)
    val state: StateFlow<DetailsState> = _state

    init {
        when (filmType) {
            FilmType.POPULAR -> getPopularFilmById(filmId)
            FilmType.FAVORITE -> getFavoriteFilmById(filmId)
        }
    }

    private fun getPopularFilmById(filmId: Long) {
        viewModelScope.launch {
            runSuspendCatching(
                action = { filmRepo.getPopularFilmById(filmId).toUiModel() },
                onSuccess = { data -> _state.value = DetailsState.Data(data) },
                onError = { _state.value = DetailsState.Error }
            )
        }
    }

    private fun getFavoriteFilmById(filmId: Long) {
        viewModelScope.launch {
            runSuspendCatching(
                action = { filmRepo.getFavoriteFilmById(filmId).toUiModel() },
                onSuccess = { data -> _state.value = DetailsState.Data(data) },
                onError = { _state.value = DetailsState.Error }
            )
        }
    }

    @AssistedFactory
    fun interface Factory {
        fun create(filmId: Long, filmType: FilmType): DetailsViewModel
    }
}