package com.brice.wheremycar.data.auth.repository

import com.brice.wheremycar.data.auth.model.UserModel
import com.brice.wheremycar.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserRepository {

    private val usersCollection = firestore.collection("users")

    override suspend fun createOrUpdateUser(user: FirebaseUser) {
        val userModel = UserModel(
            uid = user.uid,
            email = user.email,
            displayName = user.displayName,
            photoUrl = user.photoUrl?.toString()
        )
        usersCollection.document(user.uid).set(userModel).await()
    }

    override suspend fun getUserById(uid: String): Result<UserModel?> {
        return try {
            val document = usersCollection.document(uid).get().await()
            val user = document.toObject(UserModel::class.java)
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
