package com.brice.wheremycar.bluetooth.domain.repository

import com.brice.wheremycar.bluetooth.domain.model.BluetoothSignal
import kotlinx.coroutines.flow.Flow

interface BluetoothScanner {
    fun startScanning(): Flow<BluetoothSignal>
    fun stopScanning()
}