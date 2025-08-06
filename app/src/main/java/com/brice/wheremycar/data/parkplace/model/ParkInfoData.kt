package com.brice.wheremycar.data.parkplace.model

import androidx.room.Entity
import com.brice.wheremycar.ui.park.model.ParkInfoUiState
import com.brice.wheremycar.ui.park.model.ParkState
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "park_info", primaryKeys = ["uid", "carNumber"])
class ParkInfoData(
    val uid: String = "",
    val carNumber: String = "",
    val parkingFloor: Int = 0,
    val parkState: Int = ParkState.ERROR.ordinal,
    val parkingZone: String = "",
) {
    constructor(uid: String, uiState: ParkInfoUiState): this(
        uid,
        carNumber = uiState.carNumber,
        parkingFloor = uiState.parkingFloor,
        parkState = uiState.parkState.ordinal,
        parkingZone = uiState.parkingZone
    )
}