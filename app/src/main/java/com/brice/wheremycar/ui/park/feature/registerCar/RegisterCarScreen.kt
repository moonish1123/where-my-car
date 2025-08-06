import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.brice.wheremycar.ui.park.ParkHomeViewModel
// 공용 컴포넌트 import
import com.brice.wheremycar.ui.park.component.FloorSelectionDialog
import com.brice.wheremycar.ui.park.component.ParkStatusCard
import com.brice.wheremycar.ui.park.model.ParkState
import com.brice.wheremycar.ui.park.model.ScreenState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterCarScreen(
    navController: NavController,
    viewModel: ParkHomeViewModel = hiltViewModel()
) {
    var carNumber by remember { mutableStateOf("") }
    var floor by remember { mutableIntStateOf(0) }
    var parkState by remember { mutableStateOf(ParkState.PARKED) }
    var step by remember { mutableIntStateOf(1) }
    var showFloorDialog by remember { mutableStateOf(false) }

    // ✅ ViewModel의 이벤트를 구독하고, 이벤트 발생 시 화면을 전환하는 부분
    LaunchedEffect(key1 = Unit) {
        viewModel.parkInfoState.collect {
            Log.e("RegisterCarScreen", "ParkInfoState: $it")
            if (it.carNumber.isNotBlank()) {
                navController.navigate(ScreenState.ShowHome.name) {
                    // 'register_car' 화면을 백스택에서 제거하여 뒤로가기 시 다시 보이지 않도록 함
                    popUpTo(ScreenState.NeedRegisterCar.name) { inclusive = true }
                }
            }
        }
    }

    if (showFloorDialog) {
        FloorSelectionDialog(
            onDismissRequest = { showFloorDialog = false },
            onFloorSelected = { selectedFloor ->
                floor = selectedFloor
                showFloorDialog = false
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Step 1: 차량 번호 입력
        AnimatedVisibility(visible = step == 1) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                OutlinedTextField(
                    value = carNumber,
                    onValueChange = { carNumber = it },
                    label = { Text("예: 12가 3456") }
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = { step = 2 }, enabled = carNumber.isNotBlank()) {
                    Text("다음")
                }
            }
        }

        // Step 2: 주차 정보 입력
        AnimatedVisibility(visible = step == 2) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                ParkStatusCard(
                    floor = floor,
                    parkState = ParkState.PARKED,
                    onFloorClick = { showFloorDialog = true },
                    onStateToggle = { parkState = parkState.toggle() }
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        viewModel.saveCarAndParkInfo(carNumber, floor, parkState)
                    }
                ) {
                    Text("저장하고 시작하기")
                }
            }
        }
    }
}