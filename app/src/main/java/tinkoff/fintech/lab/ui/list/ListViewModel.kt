package tinkoff.fintech.lab.ui.list

import android.content.res.Configuration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tinkoff.fintech.lab.data.repo.FilmRepo
import tinkoff.fintech.lab.domain.mapper.toEntity
import tinkoff.fintech.lab.domain.mapper.toUiModel
import tinkoff.fintech.lab.domain.model.FilmType
import tinkoff.fintech.lab.ui.main.FilmRoutes
import tinkoff.fintech.lab.util.runSuspendCatching

class ListViewModel @AssistedInject constructor(private val filmRepo: FilmRepo) : ViewModel() {

    private val _state = MutableStateFlow<ListState>(ListState.Loading)
    val state: StateFlow<ListState> = _state

    private val _actions = MutableSharedFlow<ListUiAction>()
    val actions: Flow<ListUiAction> = _actions

    init {
        getPopularFilms()
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
            runSuspendCatching(
                action = {
                    val favoriteFilmIds = filmRepo.getFavoriteFilms().map { it.id }
                    filmRepo.getAllPopularFilms().map { it.toUiModel(it.id in favoriteFilmIds) }
                },
                onSuccess = { data ->
                    _state.value = ListState.Data(
                        data = data,
                        visibleData = data,
                        filmType = FilmType.POPULAR
                    )
                },
                onError = { _state.value = ListState.Error }
            )
        }
    }

    private fun navigateToFilmDetails(filmId: Long, isFavorite: Boolean) {
        when (val currentState = _state.value) {
            is ListState.Data -> {
                if (currentState.filmDetails == null) {
                    viewModelScope.launch {
                        _actions.emit(
                            ListUiAction.Navigate(
                                "${FilmRoutes.DETAILS.name}?filmId=${filmId}&&" +
                                        "filmType=${if (isFavorite) FilmType.FAVORITE.name else FilmType.POPULAR.name}"
                            )
                        )
                    }
                } else {
                    viewModelScope.launch {
                        runSuspendCatching(
                            action = {
                                if (currentState.filmType == FilmType.POPULAR) {
                                    filmRepo.getPopularFilmById(filmId).toUiModel()
                                } else {
                                    filmRepo.getFavoriteFilmById(filmId).toUiModel()
                                }
                            },
                            onSuccess = { film ->
                                _state.value = currentState.copy(filmDetails = film)
                            },
                            onError = { _state.value = ListState.Error }
                        )
                    }
                }
            }

            ListState.Loading -> {}
            is ListState.NoContent -> {}
            ListState.Error -> {}
        }
    }

    private fun changeOrientation(orientation: Int) {
        when (val currentState = _state.value) {
            is ListState.Data -> {
                when (orientation) {
                    Configuration.ORIENTATION_PORTRAIT -> {
                        _state.value = currentState.copy(filmDetails = null)
                    }

                    Configuration.ORIENTATION_LANDSCAPE -> {
                        val filmId = currentState.filmDetails?.filmId
                            ?: currentState.data.firstOrNull()?.filmId
                            ?: return
                        viewModelScope.launch {

                            runSuspendCatching(
                                action = {
                                    if (currentState.filmType == FilmType.POPULAR) {
                                        filmRepo.getPopularFilmById(filmId).toUiModel()
                                    } else {
                                        filmRepo.getFavoriteFilmById(filmId).toUiModel()
                                    }
                                },
                                onSuccess = { film ->
                                    _state.value = currentState.copy(filmDetails = film)
                                },
                                onError = { _state.value = ListState.Error }
                            )
                        }
                    }
                }
            }

            ListState.Error -> {}
            ListState.Loading -> {}
            is ListState.NoContent -> {}
        }
    }

    private fun filterFilms(text: String) {
        when (val currentState = _state.value) {
            is ListState.Data -> {
                if (text.isEmpty()) {
                    _state.value = currentState.copy(visibleData = currentState.data)
                } else {
                    val data = currentState.data.toMutableList()
                    val newData = data.filter {
                        it.filmTitle.lowercase().contains(text.lowercase())
                    }
                    if (newData.isEmpty()) {
                        _state.value = ListState.NoContent(
                            data = currentState.data,
                            filmType = currentState.filmType
                        )
                    } else {
                        _state.value = currentState.copy(visibleData = newData)
                    }
                }
            }

            is ListState.Loading -> {}
            is ListState.NoContent -> {
                _state.value = ListState.Data(
                    data = currentState.data,
                    visibleData = currentState.data,
                    filmType = currentState.filmType
                )
            }

            ListState.Error -> {}
        }
    }

    private fun changeFilmTypes(filmType: FilmType) {
        when (val currentState = _state.value) {
            is ListState.Data -> {
                when (filmType) {
                    FilmType.POPULAR -> {
                        viewModelScope.launch {
                            runSuspendCatching(
                                action = {
                                    val favoriteFilmIds =
                                        filmRepo.getFavoriteFilms().map { it.id }
                                    filmRepo.getAllPopularFilms()
                                        .map { it.toUiModel(it.id in favoriteFilmIds) }
                                },
                                onSuccess = { data ->
                                    _state.value = currentState.copy(
                                        data = data,
                                        visibleData = data,
                                        filmType = FilmType.POPULAR
                                    )
                                },
                                onError = { _state.value = ListState.Error }
                            )
                        }
                    }

                    FilmType.FAVORITE -> {
                        viewModelScope.launch {
                            runSuspendCatching(
                                action = {
                                    filmRepo.getFavoriteFilms().map { it.toUiModel(true) }
                                },
                                onSuccess = { data ->
                                    _state.value = currentState.copy(
                                        data = data,
                                        visibleData = data,
                                        filmType = FilmType.FAVORITE,
                                    )
                                },
                                onError = { _state.value = ListState.Error }
                            )
                        }
                    }
                }
            }

            ListState.Loading -> {}
            is ListState.NoContent -> {}
            ListState.Error -> {
                viewModelScope.launch {
                    runSuspendCatching(
                        action = {
                            filmRepo.getFavoriteFilms().map { it.toUiModel(true) }
                        },
                        onSuccess = { data ->
                            _state.value = ListState.Data(
                                data = data,
                                visibleData = data,
                                filmType = FilmType.FAVORITE,
                            )
                        },
                        onError = { _state.value = ListState.Error }
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
                    mutableData[indexData] =
                        itemData.copy(isFavorite = itemData.isFavorite.not())

                    val indexVisibleData =
                        mutableVisibleData.indexOfFirst { it.filmId == filmId }
                    val itemVisibleData = mutableVisibleData[indexVisibleData]
                    mutableVisibleData[indexVisibleData] =
                        itemVisibleData.copy(isFavorite = itemVisibleData.isFavorite.not())

                    if (itemVisibleData.isFavorite.not()) {
                        runSuspendCatching(
                            action = {
                                val filmResponse = filmRepo.getPopularFilmById(filmId = filmId)
                                filmRepo.saveFilmInDb(filmResponse.toEntity())
                            },
                            onSuccess = {},
                            onError = {
                                _state.value = currentState.copy(
                                    data = mutableData,
                                    visibleData = mutableVisibleData
                                )

                            }
                        )
                    } else {
                        runSuspendCatching(
                            action = { filmRepo.deleteFilmFromDb(filmId) },
                            onSuccess = {},
                            onError = {
                                _state.value = currentState.copy(
                                    data = mutableData,
                                    visibleData = mutableVisibleData
                                )
                            }
                        )
                    }
                    _state.emit(
                        currentState.copy(data = mutableData, visibleData = mutableVisibleData)
                    )
                }

                ListState.Loading -> {}
                is ListState.NoContent -> {}
                ListState.Error -> {}
            }
        }
    }

    @AssistedFactory
    fun interface Factory {
        fun create(): ListViewModel
    }
}