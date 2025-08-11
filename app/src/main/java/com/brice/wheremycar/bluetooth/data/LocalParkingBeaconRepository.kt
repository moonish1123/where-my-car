package com.brice.wheremycar.bluetooth.data

import com.brice.wheremycar.bluetooth.domain.model.ParkingBeacon
import com.brice.wheremycar.bluetooth.domain.repository.ParkingBeaconRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalParkingBeaconRepository @Inject constructor() : ParkingBeaconRepository {
    private val knownBeacons = mutableListOf<ParkingBeacon>(
        // Example known beacons - these would typically be configured by the user or fetched from a backend
        ParkingBeacon(id = "AA:BB:CC:DD:EE:F1", associatedFloor = -1, name = "Beacon B1-A"),
        ParkingBeacon(id = "AA:BB:CC:DD:EE:F2", associatedFloor = -1, name = "Beacon B1-B"),
        ParkingBeacon(id = "AA:BB:CC:DD:EE:F3", associatedFloor = -2, name = "Beacon B2-A"),
        ParkingBeacon(id = "AA:BB:CC:DD:EE:F4", associatedFloor = -2, name = "Beacon B2-B")
    )

    override suspend fun getKnownBeacons(): List<ParkingBeacon> {
        return knownBeacons.toList()
    }

    override suspend fun saveBeacon(beacon: ParkingBeacon) {
        knownBeacons.removeAll { it.id == beacon.id }
        knownBeacons.add(beacon)
    }

    override suspend fun deleteBeacon(id: String) {
        knownBeacons.removeAll { it.id == id }
    }
}