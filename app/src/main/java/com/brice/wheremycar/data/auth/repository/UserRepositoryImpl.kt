package com.brice.wheremycar.data.auth.repository

import com.brice.wheremycar.data.auth.model.UserModel
import com.brice.wheremycar.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(store: FirebaseFirestore): UserRepository {
    private val usersCollection = store.collection("users")

    override suspend fun createOrUpdateUser(user: FirebaseUser) {
        UserModel(
            uid = user.uid,
            email = user.email,
            displayName = user.displayName,
            photoUrl = user.photoUrl?.toString()
        ).let { userModel ->
            try {
                usersCollection.document(user.uid)
                    .set(userModel, SetOptions.merge())
                    .await()

                println("Firestore에 사용자 정보 저장/업데이트 성공: $userModel")
            } catch (e: Exception) {
                println("Firestore 저장 실패: ${e.message}")
            }
        }
    }

    override suspend fun getUserById(uid: String): Result<UserModel?> {
        return try {
            val user = usersCollection.document(uid).get().await().toObject<UserModel>()
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteUser(uid: String): Result<Unit> {
        return try {
            usersCollection.document(uid).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}