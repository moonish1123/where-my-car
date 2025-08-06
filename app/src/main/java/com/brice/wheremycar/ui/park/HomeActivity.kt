package com.brice.wheremycar.ui.park

import RegisterCarScreen
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.brice.wheremycar.ui.park.feature.home.HomeScreen
import com.brice.wheremycar.ui.park.model.ScreenState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity: AppCompatActivity() {
    private val mainViewModel: ParkHomeViewModel by viewModels()

    override fun onCreate(
        savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val screenState by mainViewModel.screenState.collectAsStateWithLifecycle()

            Log.e("HomeActivity", "screenState: $screenState")

            if (screenState == ScreenState.Loading) {
                Box(
                    modifier = Modifier.Companion.fillMaxSize(),
                    contentAlignment = Alignment.Companion.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                NavHost(
                    navController = navController,
                    startDestination = screenState.name // ScreenState에 route 프로퍼티 정의 추천
                ) {
                    composable(ScreenState.NeedAuth.name) { /* ... */ }
                    composable(ScreenState.NeedRegisterCar.name) { RegisterCarScreen(navController) }
                    composable(ScreenState.ShowHome.name) { HomeScreen() }
                }
            }
        }
    }
}