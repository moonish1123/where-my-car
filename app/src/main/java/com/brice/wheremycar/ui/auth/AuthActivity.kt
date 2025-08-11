package com.brice.wheremycar.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.brice.wheremycar.R
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthActivity: AppCompatActivity() {
    private val viewModel: AuthViewModel by viewModels()

    // FirebaseUI 로그인 결과 처리를 위한 런처
    private val signInLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(FirebaseAuthUIActivityResultContract()) { res ->
            this.onSignInResult(res)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            viewModel.getCurrentUser()?.let { user ->
                navigateToMain()
                return@launch
            }

            launchSignInFlow()
        }



        // ViewModel 결과 관찰
        observeViewModel()
    }

    private suspend fun launchSignInFlow() {
        // 로그인 옵션 설정 (이메일, 구글)
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        // FirebaseUI 로그인 Intent 생성
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setLogo(R.mipmap.ic_launcher) // 앱 로고 설정
            .setTheme(R.style.Theme_WhereMyCar) // 앱 테마 설정 (중요)
            .build()

        // 런처 실행
        withContext(Dispatchers.Main) {
            signInLauncher.launch(signInIntent)
        }
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == RESULT_OK) {
            // 로그인 성공
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                // ViewModel에 로그인 성공 처리 요청
                viewModel.handleLoginSuccess(user)
            } else {
                Toast.makeText(this, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        } else {
            // 로그인 실패 또는 취소
            val error = result.idpResponse?.error
            Toast.makeText(this, "로그인에 실패했습니다: ${error?.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.loginResult.collect { result ->
                if (result.isSuccess) {
                    // DB 처리까지 성공하면 메인으로 이동
                    navigateToMain()
                } else {
                    Toast.makeText(
                        this@AuthActivity,
                        "프로필 생성에 실패했습니다: ${result.exceptionOrNull()?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, com.brice.wheremycar.ui.MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}