package com.brice.wheremycar.domain.repository

import com.brice.wheremycar.data.auth.model.UserModel
import com.google.firebase.auth.FirebaseUser

interface UserRepository {
    suspend fun createOrUpdateUser(user: FirebaseUser)
    suspend fun getUserById(uid: String): Result<UserModel?>
    suspend fun deleteUser(uid: String): Result<Unit>
}