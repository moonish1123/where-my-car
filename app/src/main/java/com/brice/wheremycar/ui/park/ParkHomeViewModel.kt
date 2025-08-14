package com.brice.wheremycar.ui.park

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brice.wheremycar.domain.usecase.UserUsecase
import com.brice.wheremycar.domain.usecase.ParkUseCase
import com.brice.wheremycar.ui.park.model.ParkInfoUiState
import com.brice.wheremycar.ui.park.model.ParkState
import com.brice.wheremycar.ui.park.model.ScreenState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParkHomeViewModel @Inject constructor(
    val userUseCase: UserUsecase,
    val parkUseCase: ParkUseCase,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _screenState = MutableStateFlow(ScreenState.Loading)
    val screenState: StateFlow<ScreenState> = _screenState.asStateFlow()

    init {
        checkAuthenticationAndCarRegistration()
    }

    fun checkAuthenticationAndCarRegistration() {
        viewModelScope.launch {
            if (auth.currentUser == null) {
                _screenState.value = ScreenState.NeedAuth
                return@launch
            }

            _screenState.value = if (parkUseCase.getLatestParkInfo()?.carNumber.isNullOrBlank()) {
                ScreenState.NeedRegisterCar
            } else {
                ScreenState.ShowHome
            }
        }
    }

    val state = userUseCase.getUserAsFlow().flatMapLatest { user ->
        if (user == null) {
            throw IllegalArgumentException("user not found")
        } else {
            parkUseCase.getParkInfoAsFlow(user)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5000L),
        initialValue = ParkInfoUiState() // 초기값은 로딩 상태
    )

    fun saveCarAndParkInfo(carNumber: String, floor: Int, state: ParkState) {
        viewModelScope.launch {
            val parkInfo = ParkInfoUiState(
                carNumber = carNumber,
                parkingFloor = floor,
                parkState = state,
                parkingZone = ""
            )

            parkUseCase.addParkInfo(parkInfo)
        }
    }

    fun saveCarAndParkInfo(state: ParkInfoUiState) {
        viewModelScope.launch {
            parkUseCase.addParkInfo(state)
        }
    }

    fun updateParkingFloor(newFloor: Int) {
        viewModelScope.launch {
            // 현재 상태를 기반으로 새로운 상태 객체 생성
            val currentState = parkInfoState.value
            saveCarAndParkInfo(currentState.copy(parkingFloor = newFloor))
        }
    }

    // 2. 주차 상태 토글 함수
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