package com.brice.wheremycar.ui.park.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.brice.wheremycar.ui.park.ParkHomeViewModel
import com.brice.wheremycar.ui.park.component.FloorSelectionDialog
import com.brice.wheremycar.ui.park.component.ParkStatusCard


@Composable
fun HomeScreen(
    viewModel: ParkHomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.parkInfoState.collectAsStateWithLifecycle()
    var showFloorDialog by remember { mutableStateOf(false) }

    // 1. 다이얼로그가 열려있을 때 표시
    if (showFloorDialog) {
        FloorSelectionDialog(
            onDismissRequest = { showFloorDialog = false },
            onFloorSelected = { selectedFloor ->
                viewModel.updateParkingFloor(selectedFloor)
                showFloorDialog = false
            }
        )
    }

    // 2. 메인 UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 차량 번호 표시
        Text(
            text = uiState.carNumber,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(32.dp))

        // 3. 주차 정보 카드 (클릭 및 토글 이벤트 처리)
        ParkStatusCard(
            floor = uiState.parkingFloor,
            parkState = uiState.parkState,
            onFloorClick = { showFloorDialog = true }, // 층 정보 클릭 시 다이얼로그 표시
            onStateToggle = { viewModel.toggleParkingState() } // 스위치 토글 시 ViewModel 함수 호출
        )
    }
}


