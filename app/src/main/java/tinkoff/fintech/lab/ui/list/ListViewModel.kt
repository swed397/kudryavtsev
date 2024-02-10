package tinkoff.fintech.lab.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tinkoff.fintech.lab.data.repo.FilmRepo
import tinkoff.fintech.lab.domain.mapper.toEntity

class ListViewModel @AssistedInject constructor(private val filmRepo: FilmRepo) : ViewModel() {

    private val _state = MutableStateFlow<ListState>(ListState.Loading)
    val state: StateFlow<ListState> = _state

    init {
        viewModelScope.launch {
            _state.emit(ListState.Data(data = filmRepo.getAllFilms(), filmType = FilmType.POPULAR))
        }
    }

    fun obtainEvent(listEvent: ListEvent) {
        when (listEvent) {
            is ListEvent.ChangeFilmType -> {
                viewModelScope.launch {
                    _state.emit(
                        ListState.Data(
                            data = filmRepo.getAllFilms(),
                            filmType = FilmType.FAVORITE
                        )
                    )
                }
            }

            is ListEvent.AddFilmToFavorite -> addFilmToFavorite(listEvent.filmId)
        }
    }

    private fun getFavoritesFilms() {

    }

    private fun addFilmToFavorite(filmId: Long) {
        viewModelScope.launch {
            val filmResponse = filmRepo.getFilmById(filmId = filmId)
            filmRepo.saveFilmInDb(filmResponse.toEntity())
        }
    }

    @AssistedFactory
    fun interface Factory {
        fun create(): ListViewModel
    }
}