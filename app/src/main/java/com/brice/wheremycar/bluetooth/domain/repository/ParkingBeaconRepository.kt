package com.brice.wheremycar.bluetooth.domain.repository

import com.brice.wheremycar.bluetooth.domain.model.ParkingBeacon

interface ParkingBeaconRepository {
    suspend fun getKnownBeacons(): List<ParkingBeacon>
    suspend fun saveBeacon(beacon: ParkingBeacon)
    suspend fun deleteBeacon(id: String)
}