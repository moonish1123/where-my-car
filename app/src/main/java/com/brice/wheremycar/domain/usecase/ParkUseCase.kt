package com.brice.wheremycar.domain.usecase

import com.brice.wheremycar.data.auth.model.UserModel
import com.brice.wheremycar.data.parkplace.model.ParkInfoData
import com.brice.wheremycar.di.RoomRepository
import com.brice.wheremycar.domain.repository.ParkInfoRepository
import com.brice.wheremycar.ui.park.model.ParkInfoUiState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ParkUseCase @Inject constructor(
    @RoomRepository private val parkRepository: ParkInfoRepository
) {
    suspend fun getParkInfoAsFlow(userModel: UserModel): Flow<ParkInfoData?> = parkRepository.getLatestParkInfoAsFlow(userModel.uid)

    suspend fun getLatestParkInfo(userModel: UserModel): ParkInfoData? {
        return parkRepository.getLatestParkInfo(userModel.uid)
    }
    suspend fun addParkInfo(userModel: UserModel, parkInfo: ParkInfoUiState) {
        parkRepository.saveParkInfo(userModel.uid, parkInfo)
    }
    suspend fun clearParkInfo(userModel: UserModel) {
        parkRepository.clearParkInfo(userModel.uid)
    }
}
