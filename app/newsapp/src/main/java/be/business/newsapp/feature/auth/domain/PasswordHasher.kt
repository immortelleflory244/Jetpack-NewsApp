package be.business.newsapp.feature.auth.domain

import java.security.MessageDigest
import javax.inject.Inject

class PasswordHasher @Inject constructor() {
    fun hash(raw: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        return digest.digest(raw.toByteArray())
            .joinToString(separator = "") { byte -> "%02x".format(byte) }
    }
}
