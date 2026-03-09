package be.business.newsapp.feature.auth.presentation

import be.business.newsapp.core.presentation.MviAction
import be.business.newsapp.core.presentation.MviEvent
import be.business.newsapp.core.presentation.MviResult
import be.business.newsapp.core.presentation.MviStateReducer
import be.business.newsapp.core.presentation.MviViewState
import be.business.newsapp.feature.auth.domain.AuthUser

sealed class AuthAction : MviAction {
    data class EmailChanged(val email: String) : AuthAction()
    data class PasswordChanged(val password: String) : AuthAction()
    data class ModeChanged(val registerMode: Boolean) : AuthAction()
    data object SubmitLogin : AuthAction()
    data object SubmitRegister : AuthAction()
}

sealed class AuthEvent : MviEvent, AuthResult() {
    data object LoginSuccess : AuthEvent()
    data class ShowMessage(val message: String) : AuthEvent()
}

sealed class AuthResult : MviResult {
    data class FormUpdated(val email: String, val password: String) : AuthResult()
    data class ModeUpdated(val registerMode: Boolean) : AuthResult()
    data object Loading : AuthResult()
    data class LoginSuccess(val user: AuthUser) : AuthResult()
    data class Error(val message: String) : AuthResult()
}

data class AuthState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val currentUser: AuthUser? = null,
    val errorMessage: String? = null,
    val isRegisterMode: Boolean = false
) : MviViewState {
    val isFormValid: Boolean get() = email.isNotBlank() && password.isNotBlank()
}

class AuthReducer : MviStateReducer<AuthState, AuthResult> {
    override fun AuthState.reduce(result: AuthResult): AuthState {
        return when (result) {
            is AuthResult.FormUpdated -> copy(
                email = result.email,
                password = result.password,
                errorMessage = null
            )

            is AuthResult.ModeUpdated -> copy(
                isRegisterMode = result.registerMode,
                errorMessage = null
            )

            AuthResult.Loading -> copy(isLoading = true, errorMessage = null)
            is AuthResult.LoginSuccess -> copy(isLoading = false, currentUser = result.user, errorMessage = null)
            is AuthResult.Error -> copy(isLoading = false, errorMessage = result.message)
            is AuthEvent -> this
        }
    }
}
