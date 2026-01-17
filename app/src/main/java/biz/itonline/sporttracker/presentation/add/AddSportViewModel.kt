package biz.itonline.sporttracker.presentation.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import biz.itonline.sporttracker.domain.model.SportRecord
import biz.itonline.sporttracker.domain.model.StorageType
import biz.itonline.sporttracker.domain.repository.SportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddSportViewModel @Inject constructor(
    private val repository: SportRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddSportUiState())
    val uiState: StateFlow<AddSportUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<AddSportUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: AddSportEvent) {
        when (event) {
            is AddSportEvent.NameChanged -> _uiState.update { it.copy(name = event.name) }
            is AddSportEvent.PlaceChanged -> _uiState.update { it.copy(place = event.place) }
            is AddSportEvent.DurationChanged -> _uiState.update { it.copy(duration = event.duration) }
            is AddSportEvent.TypeChanged -> _uiState.update { it.copy(storageType = event.type) }
            AddSportEvent.SaveClicked -> saveSport()
        }
    }

    private fun saveSport() {
        val currentState = _uiState.value

        if (currentState.name.isBlank() || currentState.place.isBlank() || currentState.duration.isBlank()) {
            return // Tady by se dalo poslat chybové hlášení
        }

        viewModelScope.launch {
            // Simulace práce (loading), kdybychom chtěli
            _uiState.update { it.copy(isSaving = true) }

            val durationInt = currentState.duration.toIntOrNull() ?: 0

            val record = SportRecord(
                name = currentState.name,
                place = currentState.place,
                durationMinutes = durationInt,
                type = currentState.storageType
            )

            repository.saveSport(record)

            _uiEvent.send(AddSportUiEvent.SaveSuccess)
        }
    }
}

data class AddSportUiState(
    val name: String = "",
    val place: String = "",
    val duration: String = "",
    val storageType: StorageType = StorageType.LOCAL,
    val isSaving: Boolean = false
)

sealed interface AddSportEvent {
    data class NameChanged(val name: String) : AddSportEvent
    data class PlaceChanged(val place: String) : AddSportEvent
    data class DurationChanged(val duration: String) : AddSportEvent
    data class TypeChanged(val type: StorageType) : AddSportEvent
    data object SaveClicked : AddSportEvent
}

sealed interface AddSportUiEvent {
    data object SaveSuccess : AddSportUiEvent
}