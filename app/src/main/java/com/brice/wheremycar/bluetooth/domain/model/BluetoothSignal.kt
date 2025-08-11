package com.brice.wheremycar.bluetooth.domain.model

data class BluetoothSignal(
    val address: String,
    val rssi: Int, // Received Signal Strength Indicator
    val name: String? = null
)