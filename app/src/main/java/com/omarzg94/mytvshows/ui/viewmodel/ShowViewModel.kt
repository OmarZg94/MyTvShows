package com.omarzg94.mytvshows.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.omarzg94.mytvshows.data.model.Episode
import com.omarzg94.mytvshows.data.model.NetworkResult
import com.omarzg94.mytvshows.data.model.Show
import com.omarzg94.mytvshows.data.model.UiState
import com.omarzg94.mytvshows.repository.ShowRepository
import com.omarzg94.mytvshows.utils.Constants.UNKNOWN_ERROR
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowViewModel @Inject constructor(
    private val repository: ShowRepository
) : ViewModel() {

    private val _schedule = MutableStateFlow<UiState<List<Episode>>>(UiState.Loading)
    val schedule: StateFlow<UiState<List<Episode>>> get() = _schedule

    private val _selectedShow = MutableStateFlow<UiState<Show>>(UiState.Loading)
    val selectedShow: StateFlow<UiState<Show>> get() = _selectedShow


    init {
        fetchSchedule()
    }

    private fun fetchSchedule() {
        viewModelScope.launch {
            _schedule.value = UiState.Loading
            val result = repository.getSchedule()
            _schedule.value = result.fold(
                { UiState.Error(it.message ?: UNKNOWN_ERROR) },
                { UiState.Success(it.data) }
            )
        }
    }

    fun fetchShowDetails(id: Int) {
        viewModelScope.launch {
            _selectedShow.value = UiState.Loading
            val result = repository.getShowDetails(id)
            _selectedShow.value = result.fold(
                { UiState.Error(it.message ?: UNKNOWN_ERROR) },
                { UiState.Success(it.data) }
            )
        }
    }
}