package com.brice.wheremycar.data.auth.datasource

import androidx.credentials.exceptions.domerrors.InvalidStateError
import com.brice.wheremycar.data.auth.model.UserModel
import com.brice.wheremycar.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PlainLoginDataSource @Inject constructor (
    val firebaseAuth: FirebaseAuth,
    val userRepository: UserRepository
) {
    suspend fun login(email: String, password: String): UserModel? {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            return successProcess()
        } catch (e: FirebaseAuthInvalidUserException) {
            e.printStackTrace()
            return signUp(email, password)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun signUp(email: String, password: String): UserModel? {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            return successProcess()
        } catch (e: FirebaseAuthUserCollisionException) {
            e.printStackTrace()
            //TODO 안내를 해주거나 로그인을 유도한다.
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private suspend fun successProcess(): UserModel? {
        val user = firebaseAuth.currentUser ?: throw RuntimeException("사용자가 로그인되지 않았습니다.")
        userRepository.createOrUpdateUser(user)
        return userRepository.getUserById(user.uid).getOrNull()
    }

    suspend fun resetPassword(email: String): Boolean {
        return try {
            FirebaseAuth.getInstance().sendPasswordResetEmail(email).await()
            true
        } catch (e: FirebaseAuthInvalidUserException) {
            e.printStackTrace()
            false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}