package com.brice.wheremycar.bluetooth.domain.usecase

import com.brice.wheremycar.bluetooth.domain.model.BluetoothSignal
import com.brice.wheremycar.bluetooth.domain.repository.ParkingBeaconRepository
import javax.inject.Inject

class ResolveParkingLocationUseCase @Inject constructor(
    private val parkingBeaconRepository: ParkingBeaconRepository
) {
    suspend operator fun invoke(scannedSignals: List<BluetoothSignal>): Int? {
        val knownBeacons = parkingBeaconRepository.getKnownBeacons()

        if (knownBeacons.isEmpty()) {
            return null // No known beacons to resolve against
        }

        // Simple logic: find the known beacon with the strongest signal (closest)
        // and return its associated floor.
        val strongestSignalBeacon = scannedSignals
            .filter { signal -> knownBeacons.any { it.id == signal.address } }
            .maxByOrNull { it.rssi }

        return knownBeacons.find { it.id == strongestSignalBeacon?.address }?.associatedFloor
    }
}