package com.omarzg94.mytvshows.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omarzg94.mytvshows.data.model.Episode
import com.omarzg94.mytvshows.data.model.UiState
import com.omarzg94.mytvshows.repository.ShowRepository
import com.omarzg94.mytvshows.utils.Constants.UNKNOWN_ERROR
import com.omarzg94.mytvshows.utils.Constants.YEAR_MONTH_DAY_FORMAT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ShowViewModel @Inject constructor(
    private val repository: ShowRepository
) : ViewModel() {

    private val _schedule = MutableStateFlow<UiState<List<Episode>>>(UiState.Loading)
    val schedule: StateFlow<UiState<List<Episode>>> get() = _schedule

    var currentDate: String
        private set


    init {
        currentDate = SimpleDateFormat(
            YEAR_MONTH_DAY_FORMAT,
            Locale.getDefault()
        ).format(Calendar.getInstance().time)
        fetchSchedule(currentDate)
    }

    fun fetchSchedule(date: String) {
        viewModelScope.launch {
            _schedule.value = UiState.Loading
            val result = repository.getSchedule(date)
            _schedule.value = result.fold(
                { UiState.Error(it.message ?: UNKNOWN_ERROR) },
                { UiState.Success(it.data) }
            )
        }
    }
}