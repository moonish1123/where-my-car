package com.brice.wheremycar.bluetooth.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bluetooth_signals")
data class BluetoothSignalEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val address: String,
    val rssi: Int,
    val name: String?,
    val timestamp: Long
)