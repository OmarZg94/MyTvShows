package com.omarzg94.mytvshows.ui.view

import android.icu.util.Calendar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.omarzg94.mytvshows.R
import com.omarzg94.mytvshows.data.model.Episode
import com.omarzg94.mytvshows.data.model.UiState
import com.omarzg94.mytvshows.ui.viewmodel.ShowViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@Composable
fun ShowListScreen() {
    val scope = rememberCoroutineScope()
    val showViewModel: ShowViewModel = hiltViewModel()
    val schedule by showViewModel.schedule.collectAsState()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val snackBarHostState = remember { SnackbarHostState() }
    var query by remember { mutableStateOf("") }
    var selectedShow by remember { mutableStateOf<Episode?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(showViewModel.currentDate) }

    val datePickerDialog = rememberDatePickerDialog { year, month, day ->
        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, day)
        }
        selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
    }

    if (selectedDate.isNotEmpty()) {
        showViewModel.fetchSchedule(selectedDate)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            TopAppBar(
                modifier = Modifier.background(color = Color(0xFF121221)),
                title = { Text(stringResource(id = R.string.schedule_title), color = Color.White) },
                actions = {
                    IconButton(onClick = { datePickerDialog.show() }) {
                        Icon(Icons.Default.DateRange, contentDescription = null, tint = Color.White)
                    }
                }
            )
        },
        content = {
            Scaffold(modifier = Modifier.padding(it)) { paddingValues ->
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .background(color = Color(0xFF121221))
                ) {
                    SearchBar(
                        query = query,
                        onQueryChanged = { queryChanged -> query = queryChanged }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    when (schedule) {
                        is UiState.Loading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = Color.White)
                            }
                        }

                        is UiState.Success -> {
                            val episodes = (schedule as UiState.Success<List<Episode>>).data
                            val queryFilter =
                                if (query.isNotBlank())
                                    episodes.filter { ep ->
                                        ep.show.name.lowercase().contains(
                                            query.lowercase().trim()
                                        ) || (ep.show.network != null && ep.show.network.name.lowercase()
                                            .contains(query.lowercase().trim()))
                                    } else episodes
                            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                Date()
                            )
                            val isToday = selectedDate == today
                            val filteredShows = if (isToday) {
                                val (nowShows, nextShows) = segmentShowsByTime(
                                    queryFilter.filter { ep ->
                                        ep.runtime != null && ep.airtime.isNotBlank()
                                    }
                                )
                                Pair(nowShows, nextShows)
                            } else {
                                Pair(queryFilter, emptyList())
                            }
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp)
                            ) {
                                if (isToday) {
                                    item {
                                        Text(
                                            stringResource(id = R.string.schedule_now),
                                            style = MaterialTheme.typography.headlineMedium.copy(color = Color.White),
                                            modifier = Modifier.padding(vertical = 8.dp)
                                        )
                                    }
                                    items(filteredShows.first, key = { key -> key.id }) { show ->
                                        ShowItem(show) {
                                            scope.launch {
                                                selectedShow = it
                                                showBottomSheet = true
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                    item {
                                        Text(
                                            stringResource(id = R.string.schedule_next),
                                            style = MaterialTheme.typography.headlineMedium.copy(color = Color.White),
                                            modifier = Modifier.padding(vertical = 8.dp)
                                        )
                                    }
                                    items(filteredShows.second, key = { key -> key.id }) { show ->
                                        ShowItem(show) {
                                            scope.launch {
                                                selectedShow = it
                                                showBottomSheet = true
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                } else {
                                    item {
                                        Text(
                                            stringResource(id = R.string.schedule_for, selectedDate),
                                            style = MaterialTheme.typography.headlineMedium.copy(color = Color.White),
                                            modifier = Modifier.padding(vertical = 8.dp)
                                        )
                                    }
                                    items(filteredShows.first, key = { key -> key.id }) { show ->
                                        ShowItem(show) {
                                            scope.launch {
                                                selectedShow = it
                                                showBottomSheet = true
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                }
                            }
                        }

                        is UiState.Error -> {
                            val error = schedule as UiState.Error
                            val errorMessage = stringResource(id = R.string.error, error.message)
                            LaunchedEffect(snackBarHostState) {
                                snackBarHostState.showSnackbar(errorMessage)
                            }
                        }
                    }
                }

                if (showBottomSheet) {
                    ModalBottomSheet(
                        onDismissRequest = {
                            showBottomSheet = false
                        },
                        sheetState = sheetState
                    ) {
                        selectedShow?.let { ep ->
                            ShowDetailContent(episode = ep)
                        }
                    }
                }
            }
        }
    )
}

private fun segmentShowsByTime(episodes: List<Episode>): Pair<List<Episode>, List<Episode>> {
    val now = LocalTime.now()
    val formatter = DateTimeFormatter.ofPattern("HH:mm")

    val nowShows = episodes.filter { episode ->
        val showTime = LocalTime.parse(episode.airtime, formatter)
        showTime.isBefore(now) && showTime.plusMinutes(episode.runtime!!.toLong()).isAfter(now)
    }
    val nextShows = episodes.filter { episode ->
        val showTime = LocalTime.parse(episode.airtime, formatter)
        showTime.isAfter(now)
    }
    return Pair(nowShows, nextShows)
}