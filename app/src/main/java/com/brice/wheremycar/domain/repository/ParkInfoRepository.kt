package com.brice.wheremycar.domain.repository

import com.brice.wheremycar.data.parkplace.model.ParkInfoData
import com.brice.wheremycar.ui.park.model.ParkInfoUiState
import kotlinx.coroutines.flow.Flow

interface ParkInfoRepository {
    companion object {
        const val DOCUMENT_NAME = "parkInfo"
    }
    suspend fun getLatestParkInfoAsFlow(uid: String): Flow<ParkInfoData?>
    suspend fun getLatestParkInfo(uid: String): ParkInfoData?
    suspend fun saveParkInfo(uid: String, parkInfo: ParkInfoUiState)
    suspend fun clearParkInfo(uid: String)
}