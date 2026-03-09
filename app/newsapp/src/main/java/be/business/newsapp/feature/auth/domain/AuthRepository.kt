package be.business.newsapp.feature.auth.domain

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun observeCurrentUser(): Flow<AuthUser?>
    suspend fun getCurrentUser(): AuthUser?
    suspend fun login(credentials: LoginCredentials): Result<AuthUser>
    suspend fun register(credentials: LoginCredentials): Result<AuthUser>
    suspend fun logout()
}
