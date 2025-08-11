package com.brice.wheremycar.bluetooth.domain.usecase

import com.brice.wheremycar.bluetooth.domain.model.BluetoothSignal
import com.brice.wheremycar.bluetooth.domain.repository.BluetoothScanner
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ScanBluetoothSignalsUseCase @Inject constructor(
    private val bluetoothScanner: BluetoothScanner
) {
    operator fun invoke(): Flow<BluetoothSignal> {
        return bluetoothScanner.startScanning()
    }

    fun stopScanning() {
        bluetoothScanner.stopScanning()
    }
}