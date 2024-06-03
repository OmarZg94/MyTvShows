package com.omarzg94.mytvshows.ui.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.omarzg94.mytvshows.ui.theme.MyTvShowsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            ShowListScreen()
        }
    }
}