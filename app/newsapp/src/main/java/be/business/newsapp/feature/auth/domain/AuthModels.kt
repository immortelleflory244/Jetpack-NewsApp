package be.business.newsapp.feature.auth.domain

data class AuthUser(
    val id: Long,
    val email: String
)

data class LoginCredentials(
    val email: String,
    val password: String
)
