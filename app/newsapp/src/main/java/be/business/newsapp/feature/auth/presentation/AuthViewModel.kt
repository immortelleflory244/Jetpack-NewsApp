package be.business.newsapp.feature.auth.presentation

import be.business.newsapp.core.presentation.BaseStateViewModel
import be.business.newsapp.feature.auth.domain.LoginCredentials
import be.business.newsapp.feature.auth.domain.LoginUseCase
import be.business.newsapp.feature.auth.domain.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : BaseStateViewModel<AuthAction, AuthResult, AuthEvent, AuthState, AuthReducer>(
    initialState = AuthState(),
    reducer = AuthReducer()
) {

    override fun AuthAction.process(): Flow<AuthResult> = flow {
        when (this@process) {
            is AuthAction.EmailChanged -> emit(
                AuthResult.FormUpdated(
                    email = email,
                    password = state.value.password
                )
            )

            is AuthAction.PasswordChanged -> emit(
                AuthResult.FormUpdated(
                    email = state.value.email,
                    password = password
                )
            )

            is AuthAction.ModeChanged -> emit(AuthResult.ModeUpdated(registerMode))
            AuthAction.SubmitLogin -> emitAll(submitLogin())
            AuthAction.SubmitRegister -> emitAll(submitRegister())
        }
    }

    private fun submitLogin(): Flow<AuthResult> = flow {
        val current = state.value
        if (!current.isFormValid) {
            emit(AuthResult.Error("Email and password are required"))
            return@flow
        }

        emit(AuthResult.Loading)

        loginUseCase(LoginCredentials(current.email, current.password)).fold(
            onSuccess = {
                emit(AuthResult.LoginSuccess(it))
                emitEvent(AuthEvent.ShowMessage("Welcome ${it.email}"))
                emitEvent(AuthEvent.LoginSuccess)
            },
            onFailure = {
                emit(AuthResult.Error(it.message ?: "Login failed"))
                emitEvent(AuthEvent.ShowMessage(it.message ?: "Login failed"))
            }
        )
    }

    private fun submitRegister(): Flow<AuthResult> = flow {
        val current = state.value
        if (!current.isFormValid) {
            emit(AuthResult.Error("Email and password are required"))
            return@flow
        }

        emit(AuthResult.Loading)

        registerUseCase(LoginCredentials(current.email, current.password)).fold(
            onSuccess = {
                emit(AuthResult.LoginSuccess(it))
                emitEvent(AuthEvent.ShowMessage("Account created for ${it.email}"))
                emitEvent(AuthEvent.LoginSuccess)
            },
            onFailure = {
                emit(AuthResult.Error(it.message ?: "Registration failed"))
                emitEvent(AuthEvent.ShowMessage(it.message ?: "Registration failed"))
            }
        )
    }
}
