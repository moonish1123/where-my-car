package com.brice.wheremycar.data.parkplace.repository

import android.util.Log
import com.brice.wheremycar.data.parkplace.model.ParkInfoData
import com.brice.wheremycar.domain.repository.ParkInfoRepository
import com.brice.wheremycar.ui.park.model.ParkInfoUiState
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ParkInfoFirebaseStoreRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ParkInfoRepository {
    private val usersCollection = firestore.collection("users")

    override suspend fun getLatestParkInfoAsFlow(uid: String): Flow<ParkInfoData?> {
        return usersCollection.document(uid)
            .snapshots(MetadataChanges.INCLUDE)
            .map { snapshot -> snapshot.get(ParkInfoRepository.DOCUMENT_NAME, ParkInfoData::class.java) }
    }



    /**
     * 특정 사용자의 최신 주차 정보를 한 번만 가져옵니다.
     */
    override suspend fun getLatestParkInfo(uid: String): ParkInfoData? {
        return try {
            val snapshot = usersCollection.document(uid).get().await()
            val parkInfo = snapshot.get(ParkInfoRepository.DOCUMENT_NAME, ParkInfoData::class.java)
            parkInfo
        } catch (e: Exception) {
            Log.e("ParkInfoRepo", "Failed to get latest park info for user: $uid", e)
            null
        }
    }

    override suspend fun saveParkInfo(
        uid: String,
        parkInfo: ParkInfoUiState
    ) {
        try {
            usersCollection.document(uid).update(ParkInfoRepository.DOCUMENT_NAME, parkInfo).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 특정 사용자의 문서에서 'parkInfo' 필드를 완전히 삭제합니다.
     */
    override suspend fun clearParkInfo(uid: String) {
        try {
            val updates = mapOf(ParkInfoRepository.DOCUMENT_NAME to FieldValue.delete())
            usersCollection.document(uid).update(updates).await()
        } catch (e: Exception) {
            Log.e("ParkInfoRepo", "Failed to clear park info for user: $uid", e)
        }
    }
}