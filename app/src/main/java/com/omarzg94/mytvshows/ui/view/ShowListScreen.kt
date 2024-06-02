package com.omarzg94.mytvshows.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.omarzg94.mytvshows.R
import com.omarzg94.mytvshows.data.model.Show
import com.omarzg94.mytvshows.data.model.UiState
import com.omarzg94.mytvshows.ui.viewmodel.ShowViewModel

@Composable
fun ShowListScreen(onShowSelected: (Show) -> Unit) {
    val showViewModel: ShowViewModel = hiltViewModel()
    val schedule by showViewModel.schedule.collectAsState()

    when (schedule) {
        is UiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is UiState.Success -> {
            val shows = (schedule as UiState.Success<List<Show>>).data
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(shows) {
                    ShowItem(it, onShowSelected)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
        is UiState.Error -> {
            val error = schedule as UiState.Error
            Text("Error: ${error.message}")
        }
    }
}

@Composable
fun ShowItem(show: Show, onShowSelected: (Show) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onShowSelected(show) }
            .padding(vertical = 4.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = show.name, style = MaterialTheme.typography.headlineMedium)
            Text(text = show.summary ?: stringResource(id = R.string.without_summary), style = MaterialTheme.typography.bodyMedium)
        }
    }
}