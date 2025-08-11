package com.brice.wheremycar.ui.park

import RegisterCarScreen
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.Button
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.brice.wheremycar.ui.auth.AuthActivity
import com.brice.wheremycar.ui.park.feature.home.HomeScreen
import com.brice.wheremycar.ui.park.model.ScreenState
import dagger.hilt.android.AndroidEntryPoint
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

@AndroidEntryPoint
class HomeActivity: AppCompatActivity() {
    private val mainViewModel: ParkHomeViewModel by viewModels()

    private val authActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // When AuthActivity returns, re-evaluate the screen state
        mainViewModel.checkAuthenticationAndCarRegistration()
    }

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
                    composable(ScreenState.NeedAuth.name) { 
                        LaunchedEffect(Unit) {
                            authActivityResultLauncher.launch(Intent(this@HomeActivity, AuthActivity::class.java))
                        }
                    }
                    composable(ScreenState.NeedRegisterCar.name) { 
                        RequestBluetoothPermissions { 
                            RegisterCarScreen(navController) 
                        }
                    }
                    composable(ScreenState.ShowHome.name) { 
                        RequestBluetoothPermissions { 
                            HomeScreen() 
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RequestBluetoothPermissions(content: @Composable () -> Unit) {
    val context = LocalContext.current
    val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        arrayOf(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    } else {
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    var allPermissionsGranted by remember { mutableStateOf(false) }
    var showPermissionRationale by remember { mutableStateOf(false) }

    val multiplePermissionsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        allPermissionsGranted = permissionsMap.all { it.value }
        if (!allPermissionsGranted) {
            showPermissionRationale = true
        }
    }

    LaunchedEffect(Unit) {
        val missingPermissions = permissions.filter { 
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }

        if (missingPermissions.isNotEmpty()) {
            multiplePermissionsLauncher.launch(missingPermissions.toTypedArray())
        } else {
            allPermissionsGranted = true
        }
    }

    if (allPermissionsGranted) {
        content()
    } else if (showPermissionRationale) {
        AlertDialog(
            onDismissRequest = { /* Dismissed */ },
            title = { Text("Permission Required") },
            text = { Text("Bluetooth and location permissions are required for scanning.") },
            confirmButton = {
                Button(onClick = { 
                    showPermissionRationale = false
                    multiplePermissionsLauncher.launch(permissions)
                }) { Text("Grant") }
            },
            dismissButton = {
                Button(onClick = { /* Dismissed */ }) { Text("Deny") }
            }
        )
    }
}