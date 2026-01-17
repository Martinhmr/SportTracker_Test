package biz.itonline.sporttracker.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import biz.itonline.sporttracker.domain.model.SportRecord
import biz.itonline.sporttracker.domain.model.StorageType
import biz.itonline.sporttracker.domain.repository.SportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SportListViewModel @Inject constructor(
    private val repository: SportRepository
) : ViewModel() {

    private val _filter = MutableStateFlow(FilterType.ALL)
    val filter: StateFlow<FilterType> = _filter

    val uiState: StateFlow<SportListUiState> = combine(
        repository.getAllSports(),
        _filter
    ) { records, currentFilter ->
        val filteredList = when (currentFilter) {
            FilterType.ALL -> records
            FilterType.LOCAL -> records.filter { it.type == StorageType.LOCAL }
            FilterType.REMOTE -> records.filter { it.type == StorageType.REMOTE }
        }
        SportListUiState.Success(filteredList)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SportListUiState.Loading
    )

    fun onFilterChanged(newFilter: FilterType) {
        _filter.value = newFilter
    }

    fun onDeleteSport(sport: SportRecord) {
        viewModelScope.launch {
            repository.deleteSport(sport)
        }
    }
}

sealed interface SportListUiState {
    data object Loading : SportListUiState
    data class Success(val sports: List<SportRecord>) : SportListUiState
}

enum class FilterType {
    ALL, LOCAL, REMOTE
}