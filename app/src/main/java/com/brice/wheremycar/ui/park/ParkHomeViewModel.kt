package com.brice.wheremycar.ui.park

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brice.wheremycar.domain.usecase.UserUsecase
import com.brice.wheremycar.domain.usecase.ParkUseCase
import com.brice.wheremycar.ui.park.model.ParkInfoUiState
import com.brice.wheremycar.ui.park.model.ParkState
import com.brice.wheremycar.ui.park.model.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParkHomeViewModel @Inject constructor(
    val userUseCase: UserUsecase,
    val parkUseCase: ParkUseCase,
) : ViewModel() {

    // ParkInfoData를 ParkInfoUiState로 변환
    val parkInfoState: StateFlow<ParkInfoUiState> = userUseCase.getUserAsFlow().flatMapLatest { user ->
        if (user == null) {
            throw IllegalArgumentException("user not found")
        } else {
            parkUseCase.getParkInfoAsFlow(user)
        }
    }.map { parkInfoData ->
        parkInfoData?.let { ParkInfoUiState(it) } ?: ParkInfoUiState()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5000L),
        initialValue = ParkInfoUiState() // 초기값
    )

    val screenState: StateFlow<ScreenState> = flow {
        val user = userUseCase.getUser()
        emit(
            if (user == null) {
                ScreenState.NeedAuth
            } else {
                val parkInfo = parkUseCase.getLatestParkInfo(user)
                if (parkInfo?.carNumber.isNullOrBlank()) {
                    ScreenState.NeedRegisterCar
                } else {
                    ScreenState.ShowHome
                }
            }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5000L),
        initialValue = ScreenState.Loading
    )

    fun saveCarAndParkInfo(carNumber: String, floor: Int, state: ParkState) {
        viewModelScope.launch {
            val user = userUseCase.getUser() ?: return@launch
            val parkInfo = ParkInfoUiState(
                carNumber = carNumber,
                parkingFloor = floor,
                parkState = state,
                parkingZone = ""
            )

            parkUseCase.addParkInfo(user, parkInfo)
        }
    }

    fun saveCarAndParkInfo(state: ParkInfoUiState) {
        viewModelScope.launch {
            val user = userUseCase.getUser() ?: return@launch
            parkUseCase.addParkInfo(user, state)
        }
    }

    fun updateParkingFloor(newFloor: Int) {
        viewModelScope.launch {
            val currentState = parkInfoState.value
            saveCarAndParkInfo(currentState.copy(parkingFloor = newFloor))
        }
    }

    fun toggleParkingState() {
        viewModelScope.launch {
            val currentState = parkInfoState.value
            val newParkState = if (currentState.parkState == ParkState.PARKED) {
                ParkState.TEMPORARILY_PARKED
            } else {
                ParkState.PARKED
            }
            saveCarAndParkInfo(currentState.copy(parkState = newParkState))
        }
    }
}