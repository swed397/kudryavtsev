package tinkoff.fintech.lab.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tinkoff.fintech.lab.data.network.ApiService

class ListViewModel @AssistedInject constructor(apiService: ApiService) : ViewModel() {

    private val _state = MutableStateFlow<ListState>(ListState.Loading)
    val state: StateFlow<ListState> = _state

    init {
        viewModelScope.launch {
            _state.emit(ListState.Data(apiService.getAllFilms()))
        }
    }

    @AssistedFactory
    fun interface Factory {
        fun create(): ListViewModel
    }
}