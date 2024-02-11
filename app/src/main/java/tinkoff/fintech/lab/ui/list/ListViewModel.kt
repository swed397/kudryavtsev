package tinkoff.fintech.lab.ui.list

import android.content.res.Configuration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tinkoff.fintech.lab.data.repo.FilmRepo
import tinkoff.fintech.lab.domain.mapper.toEntity
import tinkoff.fintech.lab.domain.mapper.toUiModel
import tinkoff.fintech.lab.domain.model.FilmType
import tinkoff.fintech.lab.ui.main.FilmRoutes

class ListViewModel @AssistedInject constructor(
    private val filmRepo: FilmRepo,
    @Assisted private val navController: NavController
) : ViewModel() {

    private val _state = MutableStateFlow<ListState>(ListState.Loading)
    val state: StateFlow<ListState> = _state

    init {
        try {
            throw IllegalStateException()
            getPopularFilms()
        } catch (e: RuntimeException) {
            _state.value = ListState.Error
        }
    }

    fun obtainEvent(listEvent: ListEvent) {
        when (listEvent) {
            is ListEvent.ChangeFilmType -> changeFilmTypes(listEvent.filmType)
            is ListEvent.AddFilmToFavorite -> changeFilmFavoriteState(listEvent.filmId)
            is ListEvent.FilterByText -> filterFilms(listEvent.text)
            is ListEvent.OnOrientationChange -> changeOrientation(listEvent.orientation)
            is ListEvent.NavigateToFilmDetails -> navigateToFilmDetails(
                filmId = listEvent.filmId,
                isFavorite = listEvent.isFavorite
            )

            is ListEvent.ReloadData -> try {
                getPopularFilms()
            } catch (e: RuntimeException) {
                _state.value = ListState.Error
            }
        }
    }

    private fun getPopularFilms() {
        viewModelScope.launch {
            throw IllegalStateException()
            try {
                val favoriteFilmIds = filmRepo.getFavoriteFilms().map { it.id }
                val data =
                    filmRepo.getAllPopularFilms().map { it.toUiModel(it.id in favoriteFilmIds) }
                _state.emit(
                    ListState.Data(
                        data = data,
                        visibleData = data,
                        filmType = FilmType.POPULAR
                    )
                )
            } catch (e: RuntimeException) {
                _state.value = ListState.Error
            }
        }
    }

    private fun navigateToFilmDetails(filmId: Long, isFavorite: Boolean) {
        when (val currentState = _state.value) {
            is ListState.Data -> {
                try {
                    if (currentState.filmDetails == null) {
                        navController.navigate(
                            "${FilmRoutes.DETAILS.name}?filmId=${filmId}&&" +
                                    "filmType=${if (isFavorite) FilmType.FAVORITE.name else FilmType.POPULAR.name}"
                        )
                    } else {
                        viewModelScope.launch {
                            try {
                                _state.emit(
                                    currentState.copy(
                                        filmDetails = filmRepo.getPopularFilmById(
                                            filmId = filmId
                                        ).toUiModel()
                                    )
                                )
                            } catch (e: RuntimeException) {
                                _state.value = ListState.Error
                            }
                        }
                    }
                } catch (e: RuntimeException) {
                    _state.value = ListState.Error
                }
            }

            ListState.Loading -> {}
            is ListState.NoContent -> {}
            ListState.Error -> {}
        }
    }

    private fun changeOrientation(orientation: Int) {
        val currentState = _state.value as? ListState.Data ?: return

        when (orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                _state.value = currentState.copy(filmDetails = null)
            }

            Configuration.ORIENTATION_LANDSCAPE -> {
                val filmId = currentState.filmDetails?.filmId
                    ?: currentState.data.firstOrNull()?.filmId
                    ?: return

                try {
                    viewModelScope.launch {
                        try {
                            val film = filmRepo.getPopularFilmById(filmId)
                            _state.value = currentState.copy(
                                filmDetails = film.toUiModel()
                            )
                        } catch (e: RuntimeException) {
                            _state.value = ListState.Error
                        }
                    }
                } catch (e: RuntimeException) {
                    _state.value = ListState.Error
                }
            }
        }
    }

    private fun filterFilms(text: String) {
        when (val currentState = _state.value) {
            is ListState.Data -> {
                viewModelScope.launch {
                    try {
                        if (text.isEmpty()) {
                            _state.emit(
                                currentState.copy(visibleData = currentState.data)
                            )
                        } else {
                            val data = currentState.data.toMutableList()
                            val newData = data.filter {
                                it.filmTitle.lowercase().contains(text.lowercase())
                            }
                            if (newData.isEmpty()) {
                                _state.emit(
                                    ListState.NoContent(
                                        data = currentState.data,
                                        filmType = currentState.filmType
                                    )
                                )
                            } else {
                                _state.emit(currentState.copy(visibleData = newData))
                            }
                        }
                    } catch (e: RuntimeException) {
                        _state.value = ListState.Error
                    }
                }
            }

            is ListState.Loading -> {}
            is ListState.NoContent -> {
                viewModelScope.launch {
                    _state.emit(
                        ListState.Data(
                            data = currentState.data,
                            visibleData = currentState.data,
                            filmType = currentState.filmType
                        )
                    )
                }
            }

            ListState.Error -> {}
        }
    }

    private fun changeFilmTypes(filmType: FilmType) {
        when (val currentState = _state.value) {
            is ListState.Data -> {

                try {
                    when (filmType) {
                        FilmType.POPULAR -> {
                            viewModelScope.launch {
                                try {
                                    val favoriteFilmIds = filmRepo.getFavoriteFilms().map { it.id }
                                    val data =
                                        filmRepo.getAllPopularFilms()
                                            .map { it.toUiModel(it.id in favoriteFilmIds) }
                                    _state.emit(
                                        currentState.copy(
                                            data = data,
                                            visibleData = data,
                                            filmType = FilmType.POPULAR
                                        )
                                    )
                                } catch (e: RuntimeException) {
                                    _state.value = ListState.Error
                                }
                            }
                        }

                        FilmType.FAVORITE -> {
                            viewModelScope.launch {
                                try {
                                    val data =
                                        filmRepo.getFavoriteFilms().map { it.toUiModel(true) }

                                    _state.emit(
                                        currentState.copy(
                                            data = data,
                                            visibleData = data,
                                            filmType = FilmType.FAVORITE,
                                        )
                                    )
                                } catch (e: RuntimeException) {
                                    _state.value = ListState.Error
                                }
                            }
                        }
                    }
                } catch (e: RuntimeException) {
                    _state.value = ListState.Error
                }
            }

            ListState.Loading -> {}
            is ListState.NoContent -> {}
            ListState.Error -> {}
        }
    }

    private fun changeFilmFavoriteState(filmId: Long) {
        viewModelScope.launch {
            when (val currentState = _state.value) {
                is ListState.Data -> {
                    try {

                        val mutableVisibleData = currentState.visibleData.toMutableList()
                        val mutableData = currentState.data.toMutableList()

                        val indexData = mutableVisibleData.indexOfFirst { it.filmId == filmId }
                        val itemData = mutableVisibleData[indexData]
                        mutableData[indexData] =
                            itemData.copy(isFavorite = itemData.isFavorite.not())

                        val indexVisibleData =
                            mutableVisibleData.indexOfFirst { it.filmId == filmId }
                        val itemVisibleData = mutableVisibleData[indexVisibleData]
                        mutableVisibleData[indexVisibleData] =
                            itemVisibleData.copy(isFavorite = itemVisibleData.isFavorite.not())

                        if (itemVisibleData.isFavorite.not()) {
                            val filmResponse = filmRepo.getPopularFilmById(filmId = filmId)
                            filmRepo.saveFilmInDb(filmResponse.toEntity())
                        } else {
                            filmRepo.deleteFilmFromDb(filmId)
                        }

                        _state.emit(
                            currentState.copy(data = mutableData, visibleData = mutableVisibleData)
                        )
                    } catch (e: RuntimeException) {
                        _state.emit(ListState.Error)
                    }
                }

                ListState.Loading -> {}
                is ListState.NoContent -> {}
                ListState.Error -> {}
            }
        }
    }

    @AssistedFactory
    fun interface Factory {
        fun create(navController: NavController): ListViewModel
    }
}