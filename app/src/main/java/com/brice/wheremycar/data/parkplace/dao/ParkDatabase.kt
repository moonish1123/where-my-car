package com.brice.wheremycar.data.parkplace.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.brice.wheremycar.data.parkplace.model.ParkInfoData

@Database(entities = [ParkInfoData::class], version = 1, exportSchema = false)
abstract class ParkDatabase : RoomDatabase() {
    abstract fun parkInfoDao(): ParkInfoDao
}