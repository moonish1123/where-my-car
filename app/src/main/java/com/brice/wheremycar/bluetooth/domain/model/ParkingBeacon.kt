package com.brice.wheremycar.bluetooth.domain.model

data class ParkingBeacon(
    val id: String, // Unique identifier for the beacon (e.g., Bluetooth address)
    val associatedFloor: Int, // e.g., -1 for B1, -2 for B2
    val name: String? = null
)