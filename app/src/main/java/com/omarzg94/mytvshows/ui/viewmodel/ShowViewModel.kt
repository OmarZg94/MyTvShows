package com.omarzg94.mytvshows.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omarzg94.mytvshows.data.model.Show
import com.omarzg94.mytvshows.repository.ShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowViewModel @Inject constructor(
    private val repository: ShowRepository
) : ViewModel() {

    private val _schedule = MutableStateFlow<List<Show>>(emptyList())
    val schedule: StateFlow<List<Show>> get() = _schedule

    private val _selectedShow = MutableStateFlow<Show?>(null)
    val selectedShow: StateFlow<Show?> get() = _selectedShow

    init {
        fetchSchedule()
    }

    private fun fetchSchedule() {
        viewModelScope.launch {
            _schedule.value = repository.getSchedule()
        }
    }

    fun fetchShowDetails(id: Int) {
        viewModelScope.launch {
            _selectedShow.value = repository.getShowDetails(id)
        }
    }
}