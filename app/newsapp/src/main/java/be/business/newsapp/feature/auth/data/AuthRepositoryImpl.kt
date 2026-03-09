package be.business.newsapp.feature.auth.data

import be.business.newsapp.core.data.local.session.UserSessionStore
import be.business.newsapp.core.data.local.sqldelight.NewsSqlDatabase
import be.business.newsapp.feature.auth.domain.AuthRepository
import be.business.newsapp.feature.auth.domain.AuthUser
import be.business.newsapp.feature.auth.domain.LoginCredentials
import be.business.newsapp.feature.auth.domain.PasswordHasher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val database: NewsSqlDatabase,
    private val sessionStore: UserSessionStore,
    private val hasher: PasswordHasher
) : AuthRepository {

    override fun observeCurrentUser(): Flow<AuthUser?> {
        return sessionStore.currentUserId().map { id ->
            id?.let { userId ->
                database.newsSqlDatabaseQueries.selectUserById(userId).executeAsOneOrNull()?.let {
                    AuthUser(id = it.id, email = it.email)
                }
            }
        }
    }

    override suspend fun getCurrentUser(): AuthUser? {
        val id = sessionStore.currentUserId().first() ?: return null
        return database.newsSqlDatabaseQueries.selectUserById(id).executeAsOneOrNull()?.let {
            AuthUser(id = it.id, email = it.email)
        }
    }

    override suspend fun login(credentials: LoginCredentials): Result<AuthUser> {
        val normalizedEmail = credentials.email.trim().lowercase()
        if (normalizedEmail.isBlank() || credentials.password.isBlank()) {
            return Result.failure(IllegalArgumentException("Email and password are required"))
        }

        val passwordHash = hasher.hash(credentials.password)
        val existing = database.newsSqlDatabaseQueries
            .selectUserByEmail(normalizedEmail)
            .executeAsOneOrNull()

        if (existing == null) {
            return Result.failure(IllegalArgumentException("Account not found. Please register first."))
        }

        return runCatching {
            val user = existing.also {
                if (it.passwordHash != passwordHash) {
                    error("Invalid credentials")
                }
            }

            sessionStore.setCurrentUserId(user.id)
            AuthUser(id = user.id, email = user.email)
        }
    }

    override suspend fun register(credentials: LoginCredentials): Result<AuthUser> {
        val normalizedEmail = credentials.email.trim().lowercase()
        if (normalizedEmail.isBlank() || credentials.password.isBlank()) {
            return Result.failure(IllegalArgumentException("Email and password are required"))
        }

        val passwordHash = hasher.hash(credentials.password)
        val existing = database.newsSqlDatabaseQueries
            .selectUserByEmail(normalizedEmail)
            .executeAsOneOrNull()

        if (existing != null) {
            return Result.failure(IllegalArgumentException("Account already exists. Please login."))
        }

        return runCatching {
            database.newsSqlDatabaseQueries.insertUser(
                email = normalizedEmail,
                passwordHash = passwordHash
            )
            val created = database.newsSqlDatabaseQueries
                .selectUserByEmail(normalizedEmail)
                .executeAsOneOrNull()
                ?: error("Unable to create user")

            sessionStore.setCurrentUserId(created.id)
            AuthUser(id = created.id, email = created.email)
        }
    }

    override suspend fun logout() {
        sessionStore.clearSession()
    }
}
