package com.omarzg94.mytvshows.ui.view

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
import com.omarzg94.mytvshows.data.model.Show
import com.omarzg94.mytvshows.data.model.UiState
import com.omarzg94.mytvshows.ui.viewmodel.ShowViewModel
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun ShowListScreen(onShowSelected: (Show) -> Unit) {
    val scope = rememberCoroutineScope()
    val showViewModel: ShowViewModel = hiltViewModel()
    val schedule by showViewModel.schedule.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    var query by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.background(color = Color(0xFF121221)),
                title = { Text(stringResource(id = R.string.schedule_title), color = Color.White) },
                actions = {
                    IconButton(onClick = { /* Handle search icon click */ }) {
                        Icon(Icons.Default.DateRange, contentDescription = null, tint = Color.White)
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .background(color = Color(0xFF121221))
            ) {
                SearchBar(
                    query = query,
                    onQueryChanged = { query = it }
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
                                episodes.filter {
                                    it.show.name.lowercase().contains(
                                        query.lowercase().trim()
                                    ) || (it.show.network != null && it.show.network.name.lowercase()
                                        .contains(query.lowercase().trim()))
                                } else episodes
                        val (nowShows, nextShows) = segmentShowsByTime(
                            queryFilter.filter {
                                it.runtime != null && it.airtime.isNotBlank()
                            }
                        )
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp)
                        ) {
                            item {
                                Text(
                                    stringResource(id = R.string.schedule_now),
                                    style = MaterialTheme.typography.headlineMedium.copy(color = Color.White),
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                            items(nowShows, key = { it.id }) { show ->
                                ShowItem(show, onShowSelected)
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                            item {
                                Text(
                                    stringResource(id = R.string.schedule_next),
                                    style = MaterialTheme.typography.headlineMedium.copy(color = Color.White),
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                            items(nextShows, key = { it.id }) { show ->
                                ShowItem(show, onShowSelected)
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }

                    is UiState.Error -> {
                        val error = schedule as UiState.Error
                        val errorMessage = stringResource(id = R.string.error, error.message)
                        scope.launch {
                            snackBarHostState.showSnackbar(errorMessage)
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChanged: (String) -> Unit
) {
    TextField(
        value = query,
        onValueChange = onQueryChanged,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        placeholder = { Text(stringResource(id = R.string.schedule_search_bar_hint)) },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = null)
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    )
}

@Composable
fun ShowItem(episode: Episode, onShowSelected: (Show) -> Unit) {
    with(episode) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onShowSelected(show) },
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Row(
                modifier = Modifier.padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                show.image?.medium?.let {
                    Image(
                        painter = rememberAsyncImagePainter(model = it),
                        contentDescription = null,
                        modifier = Modifier
                            .size(70.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = show.name,
                        style = MaterialTheme.typography.titleSmall.copy(color = Color.White)
                    )
                    val formatter = DateTimeFormatter.ofPattern("HH:mm")
                    val startTime = LocalTime.parse(episode.airtime, formatter)
                    val endTime = startTime.plusMinutes(episode.runtime!!.toLong())
                    Text(
                        text = "${startTime.format(formatter)} - ${endTime.format(formatter)}",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF9494C7))
                    )
                }
            }
        }
    }
}

fun segmentShowsByTime(episodes: List<Episode>): Pair<List<Episode>, List<Episode>> {
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