package com.brice.wheremycar.bluetooth.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BluetoothSignalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSignal(signal: BluetoothSignalEntity)

    @Query("SELECT * FROM bluetooth_signals WHERE timestamp >= :startTime ORDER BY timestamp DESC")
    fun getSignalsWithinTimeframe(startTime: Long): Flow<List<BluetoothSignalEntity>>

    @Query("DELETE FROM bluetooth_signals WHERE timestamp < :endTime")
    suspend fun deleteOldSignals(endTime: Long)

    @Query("DELETE FROM bluetooth_signals")
    suspend fun deleteAllSignals()
}