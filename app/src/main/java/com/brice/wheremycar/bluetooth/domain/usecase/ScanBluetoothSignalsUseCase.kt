package com.brice.wheremycar.bluetooth.domain.usecase

import com.brice.wheremycar.bluetooth.domain.model.BluetoothSignal
import com.brice.wheremycar.bluetooth.domain.repository.BluetoothScanner
import com.brice.wheremycar.bluetooth.domain.repository.BluetoothSignalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class ScanBluetoothSignalsUseCase @Inject constructor(
    private val bluetoothScanner: BluetoothScanner,
    private val bluetoothSignalRepository: BluetoothSignalRepository
) {
    operator fun invoke(): Flow<BluetoothSignal> {
        return bluetoothScanner.startScanning().onEach {
            bluetoothSignalRepository.saveSignal(it)
        }
    }

    fun stopScanning() {
        bluetoothScanner.stopScanning()
    }
}