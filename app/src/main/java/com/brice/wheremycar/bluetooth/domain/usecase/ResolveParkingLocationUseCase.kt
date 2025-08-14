package com.brice.wheremycar.bluetooth.domain.usecase

import com.brice.wheremycar.bluetooth.domain.model.BluetoothSignal
import com.brice.wheremycar.bluetooth.domain.repository.BluetoothSignalRepository
import com.brice.wheremycar.bluetooth.domain.repository.ParkingBeaconRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import kotlin.math.abs

// Define a time window for analysis (e.g., 5 days in milliseconds)
private const val TIME_WINDOW_FOR_ANALYSIS_MS = 5L * 24 * 60 * 60 * 1000 // 5 days

// Threshold for considering a signal "strong enough" for scoring
private const val RSSI_THRESHOLD = -80 // dBm, adjust as needed

class ResolveParkingLocationUseCase @Inject constructor(
    private val parkingBeaconRepository: ParkingBeaconRepository,
    private val bluetoothSignalRepository: BluetoothSignalRepository
) {
    suspend operator fun invoke(): Int? {
        val knownBeacons = parkingBeaconRepository.getKnownBeacons()
        if (knownBeacons.isEmpty()) {
            return null // No known beacons to resolve against
        }

        val currentTime = System.currentTimeMillis()
        val startTime = currentTime - TIME_WINDOW_FOR_ANALYSIS_MS

        // Fetch historical signals within the defined time window
        val historicalSignals = bluetoothSignalRepository.getSignalsWithinTimeframe(startTime)
            .firstOrNull() ?: emptyList()

        if (historicalSignals.isEmpty()) {
            return null // No signals recorded within the timeframe
        }

        // Map to store exposure score for each known beacon ID
        val beaconExposureScores = mutableMapOf<String, Int>()

        // Calculate exposure scores
        historicalSignals.forEach { signal ->
            val matchingBeacon = knownBeacons.find { it.id == signal.address }
            if (matchingBeacon != null) {
                // Only consider signals stronger than the threshold
                if (signal.rssi >= RSSI_THRESHOLD) {
                    // Increment score for each strong detection
                    beaconExposureScores[signal.address] = (beaconExposureScores[signal.address] ?: 0) + 1
                }
            }
        }

        // Find the beacon with the highest exposure score
        val dominantBeaconId = beaconExposureScores.maxByOrNull { it.value }?.key

        // Return the floor associated with the dominant beacon
        return knownBeacons.find { it.id == dominantBeaconId }?.associatedFloor
    }
}