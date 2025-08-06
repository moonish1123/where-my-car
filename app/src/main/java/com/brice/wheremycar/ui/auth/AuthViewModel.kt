package com.brice.wheremycar.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brice.wheremycar.data.auth.model.UserModel
import com.brice.wheremycar.domain.usecase.UserUsecase
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: UserUsecase
): ViewModel() {
    private val _loginResult = MutableSharedFlow<Result<Unit>>()
    val loginResult = _loginResult.asSharedFlow()

    suspend fun getCurrentUser(): UserModel? {
        return loginUseCase.getUser()
    }

    // Firebase Auth UI에서 로그인 성공 시 이 함수를 호출
    fun handleLoginSuccess(user: FirebaseUser) {
        viewModelScope.launch {
            try {
                loginUseCase.processLoginResult(user) // UseCase 실행
                _loginResult.emit(Result.success(Unit))
            } catch (e: Exception) {
                _loginResult.emit(Result.failure(e))
            }
        }
    }
}