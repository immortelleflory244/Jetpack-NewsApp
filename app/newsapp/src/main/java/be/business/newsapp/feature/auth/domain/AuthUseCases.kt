package be.business.newsapp.feature.auth.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(credentials: LoginCredentials): Result<AuthUser> = repository.login(credentials)
}

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(credentials: LoginCredentials): Result<AuthUser> = repository.register(credentials)
}

class LogoutUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke() = repository.logout()
}

class ObserveCurrentUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(): Flow<AuthUser?> = repository.observeCurrentUser()
}

class GetCurrentUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): AuthUser? = repository.getCurrentUser()
}
