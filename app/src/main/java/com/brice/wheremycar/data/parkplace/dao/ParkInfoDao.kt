package com.brice.wheremycar.data.parkplace.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.brice.wheremycar.data.parkplace.model.ParkInfoData
import kotlinx.coroutines.flow.Flow

@Dao
interface ParkInfoDao {
    /**
     * 가장 최근의 주차 정보를 Flow로 가져옵니다.
     * 정보가 없으면 null을 반환합니다.
     * Flow를 사용하면 데이터 변경 시 자동으로 UI에 반영할 수 있습니다.
     */
    @Query("SELECT * FROM park_info WHERE uid = :uid ORDER BY carNumber DESC LIMIT 1")
    fun getLatestParkInfoAsFlow(uid: String): Flow<ParkInfoData?>

    @Query("SELECT * FROM park_info WHERE uid = :uid ORDER BY carNumber DESC LIMIT 1")
    suspend fun getLatestParkInfo(uid: String): ParkInfoData?

    /**
     * 모든 주차 정보를 삭제합니다.
     */
    @Query("DELETE FROM park_info WHERE uid = :uid")
    suspend fun clearAll(uid: String)

    /**
     * 새로운 주차 정보를 저장합니다.
     * 이미 정보가 있다면 덮어쓰기(REPLACE) 위해 이전 정보를 모두 지우고 새로 추가합니다.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateParkInfo(parkInfo: ParkInfoData)

}