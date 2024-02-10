package tinkoff.fintech.lab.ui.list

import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tinkoff.fintech.lab.data.repo.FilmRepo
import tinkoff.fintech.lab.domain.mapper.toEntity
import tinkoff.fintech.lab.domain.mapper.toUiModel

class ListViewModel @AssistedInject constructor(private val filmRepo: FilmRepo) : ViewModel() {

    private val _state = MutableStateFlow<ListState>(ListState.Loading)
    val state: StateFlow<ListState> = _state

    init {
        viewModelScope.launch {
            val favoriteFilmIds = filmRepo.getFavoriteFilms().map { it.id }
            val data = filmRepo.getAllPopularFilms().map { it.toUiModel(it.id in favoriteFilmIds) }
            _state.emit(
                ListState.Data(
                    data = data,
                    visibleData = data,
                    filmType = FilmType.POPULAR
                )
            )
        }
    }

    fun obtainEvent(listEvent: ListEvent) {
        when (listEvent) {
            is ListEvent.ChangeFilmType -> changeFilmTypes(listEvent.filmType)
            is ListEvent.AddFilmToFavorite -> changeFilmFavoriteState(listEvent.filmId)
            is ListEvent.FilterByText -> filterFilms(listEvent.text)
        }
    }

    private fun filterFilms(text: String) {
        when (val currentState = _state.value) {
            is ListState.Data -> {
                viewModelScope.launch {
                    if (text.isEmpty()) {
                        _state.emit(currentState.copy(visibleData = currentState.data))
                    } else {
                        val data = currentState.data.toMutableList()
                        val newData = data.filter {
                            it.filmTitle.lowercase().contains(text.lowercase())
                        }
                        _state.emit(currentState.copy(visibleData = newData))
                    }
                }
            }

            is ListState.Loading -> {}
        }
    }

    private fun changeFilmTypes(filmType: FilmType) {
        when (filmType) {
            FilmType.POPULAR -> {
                viewModelScope.launch {
                    val favoriteFilmIds = filmRepo.getFavoriteFilms().map { it.id }
                    val data =
                        filmRepo.getAllPopularFilms()
                            .map { it.toUiModel(it.id in favoriteFilmIds) }
                    _state.emit(
                        ListState.Data(
                            data = data,
                            visibleData = data,
                            filmType = FilmType.POPULAR
                        )
                    )
                }
            }

            FilmType.FAVORITE -> {
                viewModelScope.launch {
                    val data = filmRepo.getFavoriteFilms().map { it.toUiModel(true) }
                    _state.emit(
                        ListState.Data(
                            data = data,
                            visibleData = data,
                            filmType = FilmType.FAVORITE
                        )
                    )
                }
            }
        }
    }

    private fun changeFilmFavoriteState(filmId: Long) {
        viewModelScope.launch {
            when (val currentState = _state.value) {
                is ListState.Data -> {
                    val mutableData = currentState.data.toMutableList()
                    val index = mutableData.indexOfFirst { it.filmId == filmId }
                    val item = mutableData[index]
                    mutableData[index] = item.copy(isFavorite = item.isFavorite.not())

                    if (item.isFavorite.not()) {
                        val filmResponse = filmRepo.getFilmById(filmId = filmId)
                        filmRepo.saveFilmInDb(filmResponse.toEntity())
                    } else {
                        filmRepo.deleteFilmFromDb(filmId)
                    }

                    _state.emit(
                        ListState.Data(
                            data = mutableData,
                            visibleData = mutableData,
                            filmType = currentState.filmType
                        )
                    )
                }

                ListState.Loading -> {}
            }
        }
    }

    private fun getPopularFilms() {

    }

    @AssistedFactory
    fun interface Factory {
        fun create(): ListViewModel
    }
}