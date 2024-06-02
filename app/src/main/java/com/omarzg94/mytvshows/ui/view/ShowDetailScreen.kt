package com.omarzg94.mytvshows.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.omarzg94.mytvshows.R
import com.omarzg94.mytvshows.ui.viewmodel.ShowViewModel

@Composable
fun ShowDetailScreen(showId: Int) {
    val showViewModel: ShowViewModel = hiltViewModel()
    val selectedShow by showViewModel.selectedShow.collectAsState()

    LaunchedEffect(showId) {
        showViewModel.fetchShowDetails(showId)
    }

    selectedShow?.let { show ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = show.name, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = show.summary ?: stringResource(id = R.string.without_summary), style = MaterialTheme.typography.bodyMedium)
        }
    }
}