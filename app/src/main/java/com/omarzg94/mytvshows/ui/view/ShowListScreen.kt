package com.omarzg94.mytvshows.ui.view

import android.app.DatePickerDialog
import android.icu.util.Calendar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.omarzg94.mytvshows.R
import com.omarzg94.mytvshows.data.model.Episode
import com.omarzg94.mytvshows.data.model.UiState
import com.omarzg94.mytvshows.ui.theme.screenBackground
import com.omarzg94.mytvshows.ui.viewmodel.ShowViewModel
import com.omarzg94.mytvshows.utils.Constants.EMPTY
import com.omarzg94.mytvshows.utils.Constants.HOUR_MINUTE_FORMAT
import com.omarzg94.mytvshows.utils.Constants.NormalPadding
import com.omarzg94.mytvshows.utils.Constants.SmallPadding
import com.omarzg94.mytvshows.utils.Constants.YEAR_MONTH_DAY_FORMAT
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

private const val TWO_WEEKS: Long = 14

@Composable
fun ShowListScreen() {
    val showViewModel: ShowViewModel = hiltViewModel()
    val schedule by showViewModel.schedule.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val snackBarHostState = remember { SnackbarHostState() }
    var query by remember { mutableStateOf(EMPTY) }
    var selectedShow by remember { mutableStateOf<Episode?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(showViewModel.currentDate) }

    val calendar = Calendar.getInstance()
    val minDate = calendar.timeInMillis - TimeUnit.DAYS.toMillis(TWO_WEEKS)
    val maxDate = calendar.timeInMillis + TimeUnit.DAYS.toMillis(TWO_WEEKS)

    val datePickerDialog = rememberDatePickerDialog(
        onDateSet = { year, month, day ->
            calendar.apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, day)
            }
            selectedDate =
                SimpleDateFormat(YEAR_MONTH_DAY_FORMAT, Locale.getDefault()).format(calendar.time)
        },
        minDate = minDate,
        maxDate = maxDate
    )

    LaunchedEffect(selectedDate) {
        if (selectedDate.isNotEmpty()) {
            showViewModel.fetchSchedule(selectedDate)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            ShowListTopBar(datePickerDialog)
        },
        content = {
            ShowListContent(
                schedule = schedule,
                query = query,
                onQueryChanged = { query = it },
                selectedDate = selectedDate,
                onShowSelected = { show ->
                    selectedShow = show
                    showBottomSheet = true
                },
                snackBarHostState = snackBarHostState,
                showBottomSheet = showBottomSheet,
                sheetState = sheetState,
                selectedShow = selectedShow,
                onBottomSheetDismiss = { showBottomSheet = false }
            )
        }
    )
}

@Composable
fun ShowListTopBar(datePickerDialog: DatePickerDialog) {
    TopAppBar(
        modifier = Modifier.background(color = screenBackground),
        title = { Text(stringResource(id = R.string.schedule_title), color = Color.White) },
        actions = {
            IconButton(onClick = { datePickerDialog.show() }) {
                Icon(Icons.Default.DateRange, contentDescription = null, tint = Color.White)
            }
        }
    )
}

@Composable
fun ShowListContent(
    schedule: UiState<List<Episode>>,
    query: String,
    onQueryChanged: (String) -> Unit,
    selectedDate: String,
    onShowSelected: (Episode) -> Unit,
    snackBarHostState: SnackbarHostState,
    showBottomSheet: Boolean,
    sheetState: SheetState,
    selectedShow: Episode?,
    onBottomSheetDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = screenBackground)
    ) {
        SearchBar(
            query = query,
            onQueryChanged = onQueryChanged
        )
        Spacer(modifier = Modifier.height(NormalPadding))
        when (schedule) {
            is UiState.Loading -> LoadingIndicator()
            is UiState.Success -> ShowList(
                episodes = schedule.data,
                query = query,
                selectedDate = selectedDate,
                onShowSelected = onShowSelected
            )

            is UiState.Error -> {
                val errorMessage = stringResource(id = R.string.error, schedule.message)
                LaunchedEffect(snackBarHostState) {
                    snackBarHostState.showSnackbar(errorMessage)
                }
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = onBottomSheetDismiss,
            sheetState = sheetState
        ) {
            selectedShow?.let { ShowDetailContent(episode = it) }
        }
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color.White)
    }
}

@Composable
fun ShowList(
    episodes: List<Episode>,
    query: String,
    selectedDate: String,
    onShowSelected: (Episode) -> Unit
) {
    val queryFilter = episodes.filter {
        it.show.name.contains(query, ignoreCase = true) ||
                it.show.network?.name?.contains(query, ignoreCase = true) ?: false
    }
    val today = SimpleDateFormat(YEAR_MONTH_DAY_FORMAT, Locale.getDefault()).format(Date())
    val isToday = selectedDate == today
    val (nowShows, nextShows) = if (isToday) segmentShowsByTime(queryFilter) else Pair(
        queryFilter,
        emptyList()
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = NormalPadding)
    ) {
        if (isToday) {
            item { SectionHeader(title = stringResource(id = R.string.schedule_now)) }
            items(nowShows, key = { it.id }) { ShowItem(it, onShowSelected) }
            item { SectionHeader(title = stringResource(id = R.string.schedule_next)) }
            items(nextShows, key = { it.id }) { ShowItem(it, onShowSelected) }
        } else {
            item { SectionHeader(title = stringResource(id = R.string.schedule_for, selectedDate)) }
            items(nowShows, key = { it.id }) { ShowItem(it, onShowSelected) }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineMedium.copy(color = Color.White),
        modifier = Modifier.padding(vertical = SmallPadding)
    )
}

private fun segmentShowsByTime(episodes: List<Episode>): Pair<List<Episode>, List<Episode>> {
    val now = LocalTime.now()
    val formatter = DateTimeFormatter.ofPattern(HOUR_MINUTE_FORMAT)

    val nowShows = episodes.filter { episode ->
        val showTime = LocalTime.parse(episode.airtime, formatter)
        showTime.isBefore(now) && showTime.plusMinutes(episode.runtime?.toLong() ?: 0).isAfter(now)
    }
    val nextShows = episodes.filter { episode ->
        val showTime = LocalTime.parse(episode.airtime, formatter)
        showTime.isAfter(now)
    }
    return Pair(nowShows, nextShows)
}
