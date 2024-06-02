package com.omarzg94.mytvshows

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.omarzg94.mytvshows.ui.theme.MyTvShowsTheme
import com.omarzg94.mytvshows.ui.view.ShowDetailScreen
import com.omarzg94.mytvshows.ui.view.ShowListScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyTvShowsTheme {
                MyTvShowsApp()
            }
        }
    }
}

@Composable
fun MyTvShowsApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "showList") {
        composable("showList") {
            ShowListScreen(onShowSelected = { show ->
                navController.navigate("showDetail/${show.id}")
            })
        }
        composable("showDetail/{showId}") { backStackEntry ->
            val showId = backStackEntry.arguments?.getString("showId")?.toIntOrNull()
            showId?.let {
                ShowDetailScreen(showId = it)
            }
        }
    }
}