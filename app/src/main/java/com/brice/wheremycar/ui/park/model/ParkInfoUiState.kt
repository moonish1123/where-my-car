package com.brice.wheremycar.ui.park.model

import com.brice.wheremycar.data.parkplace.model.ParkInfoData

enum class ParkState {
    PARKED,
    TEMPORARILY_PARKED,
    ERROR;

    companion object {
        fun fromOrdinal(ordinal: Int): ParkState {
            return entries.getOrElse(ordinal) { ERROR }
        }
    }

    fun asParingStateString() = when (this) {
        PARKED -> "정상 주차"
        TEMPORARILY_PARKED -> "2중 주차"
        ERROR -> "Invalid State"
    }
    fun toggle() = if(this == PARKED) TEMPORARILY_PARKED else PARKED
}
data class ParkInfoUiState(
    val carNumber: String = "",
    val parkingFloor: Int = 0,
    val parkState: ParkState = ParkState.ERROR,
    val parkingZone: String = "",
) {
    constructor(data: ParkInfoData): this(
        carNumber = data.carNumber,
        parkingFloor = data.parkingFloor,
        parkState = ParkState.fromOrdinal(data.parkState),
        parkingZone = data.parkingZone
    )

    fun parkState() = parkState.asParingStateString()
}