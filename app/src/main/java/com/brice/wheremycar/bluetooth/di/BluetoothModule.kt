package com.brice.wheremycar.bluetooth.di

import com.brice.wheremycar.bluetooth.data.AndroidBluetoothScanner
import com.brice.wheremycar.bluetooth.data.LocalParkingBeaconRepository
import com.brice.wheremycar.bluetooth.domain.repository.BluetoothScanner
import com.brice.wheremycar.bluetooth.domain.repository.ParkingBeaconRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
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
}