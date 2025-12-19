package com.example.travenor_customerapp.presentation.Login.viewModel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travenor_customerapp.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(val auth: FirebaseAuth, val authRepository: AuthRepository): ViewModel() {

    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState

    fun isLoggedIn(): Boolean = auth.currentUser != null


    fun onEmailChange(newEmail: String) {
        val trimmed = newEmail.trim()


        _loginUiState.update { state ->
            val emailValid = isValidEmail(trimmed)
            val pwdValid = state.password.length >= 6

            state.copy(
                email = trimmed,
                emailError = if (trimmed.isEmpty() || emailValid) null else "Enter a valid email",
                isSignInEnabled = emailValid && pwdValid
            )
        }
    }




    fun onPasswordChange(newPassword: String) {
        _loginUiState.update { state ->
            val pwdValid = newPassword.length >= 6
            val emailValid = isValidEmail(state.email)

            state.copy(
                password = newPassword,
                passwordError = if (newPassword.isEmpty() || pwdValid) null else "Min 6 characters",
                isSignInEnabled = emailValid && pwdValid
            )
        }
    }

    fun onAuthMessageShown() {
        _loginUiState.update { it.copy(authError = null) }
    }

    private fun isValidEmail(email: String): Boolean =
        email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()


    fun onSignInClicked() {

        val email = _loginUiState.value.email
        val password = _loginUiState.value.password

        viewModelScope.launch {

            _loginUiState.update { it.copy(isLoading = true, isSuccessLogin = false, authError = null) }

            try {
                authRepository.signInOrCreate(email = email, password = password)

                _loginUiState.update {
                    it.copy(
                        isLoading = false,
                        isSuccessLogin = true,
                        authError = null
                    )
                }
            } catch (e: Exception) {
                _loginUiState.update {
                    it.copy(
                        isLoading = false,
                        isSuccessLogin = false,
                        authError = e.message ?: "something went wring"
                    )
                }
            }
        }


    }
}




data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
    val authError: String? = null,
    val isSuccessLogin: Boolean = false,
    val isSignInEnabled: Boolean = false
)