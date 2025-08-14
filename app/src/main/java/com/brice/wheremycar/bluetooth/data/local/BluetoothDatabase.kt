package com.brice.wheremycar.bluetooth.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BluetoothSignalEntity::class], version = 1, exportSchema = false)
abstract class BluetoothDatabase : RoomDatabase() {
    abstract fun bluetoothSignalDao(): BluetoothSignalDao
}