package com.brice.wheremycar.bluetooth.domain.repository

import com.brice.wheremycar.bluetooth.domain.model.BluetoothSignal
import kotlinx.coroutines.flow.Flow

interface BluetoothSignalRepository {
    suspend fun saveSignal(signal: BluetoothSignal)
    fun getSignalsWithinTimeframe(startTime: Long): Flow<List<BluetoothSignal>>
    suspend fun deleteOldSignals(endTime: Long)
    suspend fun deleteAllSignals()
}