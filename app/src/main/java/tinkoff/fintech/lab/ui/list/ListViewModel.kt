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
                    val mutableVisibleData = currentState.visibleData.toMutableList()
                    val mutableData = currentState.data.toMutableList()

                    val indexData = mutableVisibleData.indexOfFirst { it.filmId == filmId }
                    val itemData = mutableVisibleData[indexData]
                    mutableData[indexData] = itemData.copy(isFavorite = itemData.isFavorite.not())

                    val indexVisibleData = mutableVisibleData.indexOfFirst { it.filmId == filmId }
                    val itemVisibleData = mutableVisibleData[indexVisibleData]
                    mutableVisibleData[indexVisibleData] =
                        itemVisibleData.copy(isFavorite = itemVisibleData.isFavorite.not())

                    if (itemVisibleData.isFavorite.not()) {
                        val filmResponse = filmRepo.getFilmById(filmId = filmId)
                        filmRepo.saveFilmInDb(filmResponse.toEntity())
                    } else {
                        filmRepo.deleteFilmFromDb(filmId)
                    }

                    _state.emit(
                        currentState.copy(data = mutableData, visibleData = mutableVisibleData)
                    )
                }

                ListState.Loading -> {}
            }
        }
    }

    @AssistedFactory
    fun interface Factory {
        fun create(): ListViewModel
    }
}