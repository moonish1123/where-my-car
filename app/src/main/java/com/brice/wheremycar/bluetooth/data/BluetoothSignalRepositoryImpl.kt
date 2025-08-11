package com.brice.wheremycar.bluetooth.data

import com.brice.wheremycar.bluetooth.data.local.BluetoothSignalDao
import com.brice.wheremycar.bluetooth.data.local.BluetoothSignalEntity
import com.brice.wheremycar.bluetooth.domain.model.BluetoothSignal
import com.brice.wheremycar.bluetooth.domain.repository.BluetoothSignalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BluetoothSignalRepositoryImpl @Inject constructor(
    private val bluetoothSignalDao: BluetoothSignalDao
) : BluetoothSignalRepository {

    override suspend fun saveSignal(signal: BluetoothSignal) {
        bluetoothSignalDao.insertSignal(signal.toEntity())
    }

    override fun getSignalsWithinTimeframe(startTime: Long): Flow<List<BluetoothSignal>> {
        return bluetoothSignalDao.getSignalsWithinTimeframe(startTime).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun deleteOldSignals(endTime: Long) {
        bluetoothSignalDao.deleteOldSignals(endTime)
    }

    override suspend fun deleteAllSignals() {
        bluetoothSignalDao.deleteAllSignals()
    }
}

// Mappers
fun BluetoothSignal.toEntity(): BluetoothSignalEntity {
    return BluetoothSignalEntity(
        address = this.address,
        rssi = this.rssi,
        name = this.name,
        timestamp = this.timestamp
    )
}

fun BluetoothSignalEntity.toDomain(): BluetoothSignal {
    return BluetoothSignal(
        address = this.address,
        rssi = this.rssi,
        name = this.name,
        timestamp = this.timestamp
    )
}