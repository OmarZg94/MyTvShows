package com.omarzg94.mytvshows.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.omarzg94.mytvshows.R
import com.omarzg94.mytvshows.data.model.UiState
import com.omarzg94.mytvshows.ui.viewmodel.ShowViewModel
import kotlinx.coroutines.launch

@Composable
fun ShowDetailScreen(showId: Int) {
    val scope = rememberCoroutineScope()
    val showViewModel: ShowViewModel = hiltViewModel()
    val selectedShow by showViewModel.selectedShow.collectAsState()
    val snackBarHostState = remember{ SnackbarHostState() }

    LaunchedEffect(showId) {
        showViewModel.fetchShowDetails(showId)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        content = {
            when (selectedShow) {
                is UiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is UiState.Success -> {
                    val show = (selectedShow as UiState.Success).data
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
                is UiState.Error -> {
                    val error = selectedShow as UiState.Error
                    val errorMessage = stringResource(id = R.string.error,error.message)
                    scope.launch {
                        snackBarHostState.showSnackbar(errorMessage)
                    }
                }
            }
        }
    )
}