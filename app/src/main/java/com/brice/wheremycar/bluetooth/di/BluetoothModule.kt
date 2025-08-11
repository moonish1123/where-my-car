package com.brice.wheremycar.bluetooth.di

import android.content.Context
import androidx.room.Room
import com.brice.wheremycar.bluetooth.data.AndroidBluetoothScanner
import com.brice.wheremycar.bluetooth.data.BluetoothSignalRepositoryImpl
import com.brice.wheremycar.bluetooth.data.LocalParkingBeaconRepository
import com.brice.wheremycar.bluetooth.data.local.BluetoothDatabase
import com.brice.wheremycar.bluetooth.data.local.BluetoothSignalDao
import com.brice.wheremycar.bluetooth.domain.repository.BluetoothScanner
import com.brice.wheremycar.bluetooth.domain.repository.BluetoothSignalRepository
import com.brice.wheremycar.bluetooth.domain.repository.ParkingBeaconRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BluetoothModule {

    @Binds
    @Singleton
    abstract fun bindBluetoothScanner(
        androidBluetoothScanner: AndroidBluetoothScanner
    ): BluetoothScanner

    @Binds
    @Singleton
    abstract fun bindParkingBeaconRepository(
        localParkingBeaconRepository: LocalParkingBeaconRepository
    ): ParkingBeaconRepository

    @Binds
    @Singleton
    abstract fun bindBluetoothSignalRepository(
        bluetoothSignalRepositoryImpl: BluetoothSignalRepositoryImpl
    ): BluetoothSignalRepository

    companion object {
        @Provides
        @Singleton
        fun provideBluetoothDatabase(@ApplicationContext context: Context): BluetoothDatabase {
            return Room.databaseBuilder(
                context,
                BluetoothDatabase::class.java,
                "bluetooth_database"
            ).build()
        }

        @Provides
        @Singleton
        fun provideBluetoothSignalDao(database: BluetoothDatabase): BluetoothSignalDao {
            return database.bluetoothSignalDao()
        }
    }
}