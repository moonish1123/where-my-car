package com.brice.wheremycar.data.parkplace.repository

import com.brice.wheremycar.data.parkplace.dao.ParkInfoDao
import com.brice.wheremycar.data.parkplace.model.ParkInfoData
import com.brice.wheremycar.domain.repository.ParkInfoRepository
import com.brice.wheremycar.ui.park.model.ParkInfoUiState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ParkInfoRoomRepositoryImpl @Inject constructor(
    private val parkInfoDao: ParkInfoDao
) : ParkInfoRepository {
    override suspend fun getLatestParkInfoAsFlow(uid: String): Flow<ParkInfoData?> {
        return parkInfoDao.getLatestParkInfoAsFlow(uid)
    }

    override suspend fun getLatestParkInfo(uid: String): ParkInfoData? {
        return parkInfoDao.getLatestParkInfo(uid)
    }

    override suspend fun saveParkInfo(
        uid: String,
        parkInfo: ParkInfoUiState
    ) {
        parkInfoDao.insertOrUpdateParkInfo(ParkInfoData(uid, parkInfo))
    }

    override suspend fun clearParkInfo(uid: String) {
        parkInfoDao.clearAll(uid)
    }
}