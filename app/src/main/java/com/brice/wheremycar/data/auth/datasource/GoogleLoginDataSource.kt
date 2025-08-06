package com.brice.wheremycar.data.auth.datasource

import com.brice.wheremycar.data.auth.model.UserModel
import com.brice.wheremycar.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GoogleLoginDataSource @Inject constructor (
    val firebaseAuth: FirebaseAuth,
    val userRepository: UserRepository
) {
    suspend fun login(idToken: String): UserModel? {
        return try {
            // 1. idToken으로 인증 정보(credential) 생성
            val credential = GoogleAuthProvider.getCredential(idToken, null)

            // 2. 인증 정보로 Firebase에 로그인
            firebaseAuth.signInWithCredential(credential).await()

            // 3. 현재 유저 정보 가져오기 (이후 로직은 PlainLoginDataSource와 동일)
            val user = firebaseAuth.currentUser ?: throw Exception("사용자가 로그인되지 않았습니다.")

            // 4. Firestore에 유저 정보 생성 또는 업데이트
            userRepository.createOrUpdateUser(user)

            // 5. 최종 유저 모델 반환
            userRepository.getUserById(user.uid).getOrNull()

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    //login code 가 애초에 아이디가 없으면 가입까지 진행함.
    suspend fun signup(idToken: String): UserModel? = login(idToken)

    suspend fun logout() {
        firebaseAuth.signOut()
    }
}