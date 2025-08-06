package com.brice.wheremycar.domain.usecase

import com.brice.wheremycar.data.auth.datasource.GoogleLoginDataSource
import com.brice.wheremycar.data.auth.datasource.PlainLoginDataSource
import com.brice.wheremycar.data.auth.model.UserModel
import com.brice.wheremycar.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserUsecase @Inject constructor(
    val loginDataSource: PlainLoginDataSource,
    val googleLoginDataSource: GoogleLoginDataSource,
    val firebaseAuth: FirebaseAuth,
    val userRepository: UserRepository
) {
    suspend fun login(email: String, password: String) = loginDataSource.login(email, password)
    suspend fun singUp(email: String, password: String) = loginDataSource.signUp(email, password)
    suspend fun resetPassword(email: String) = loginDataSource.resetPassword(email)

    suspend fun login(idToken: String) = googleLoginDataSource.login(idToken)
    suspend fun logout() {
        firebaseAuth.signOut()
    }

    suspend fun processLoginResult(user: FirebaseUser) {
        userRepository.createOrUpdateUser(user)
    }

    suspend fun getUser(): UserModel? {
        if (firebaseAuth.currentUser == null) {
            return null
        }
        return userRepository.getUserById(firebaseAuth.currentUser!!.uid).getOrNull()
    }

    fun getUserAsFlow(): Flow<UserModel?> = flow {
        val user = firebaseAuth.currentUser ?: throw RuntimeException("사용자가 로그인되지 않았습니다.")
        userRepository.getUserById(user.uid).getOrNull()?.let {
            emit(it)
        } ?: throw RuntimeException("사용자 정보를 가져올 수 없습니다.")
    }
}