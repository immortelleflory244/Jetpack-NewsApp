package be.business.newsapp.feature.auth

import be.business.newsapp.feature.auth.domain.AuthUser
import be.business.newsapp.feature.auth.presentation.AuthEvent
import be.business.newsapp.feature.auth.presentation.AuthReducer
import be.business.newsapp.feature.auth.presentation.AuthResult
import be.business.newsapp.feature.auth.presentation.AuthState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthReducerTest {

    private val reducer = AuthReducer()

    @Test
    fun `form updated should update fields and clear error`() {
        val initial = AuthState(errorMessage = "old")

        val next = with(reducer) {
            initial.reduce(AuthResult.FormUpdated(email = "test@mail.com", password = "123456"))
        }

        assertEquals("test@mail.com", next.email)
        assertEquals("123456", next.password)
        assertNull(next.errorMessage)
        assertTrue(next.isFormValid)
    }

    @Test
    fun `login success should set current user and stop loading`() {
        val initial = AuthState(isLoading = true)

        val next = with(reducer) {
            initial.reduce(AuthResult.LoginSuccess(AuthUser(id = 1, email = "user@mail.com")))
        }

        assertFalse(next.isLoading)
        assertEquals("user@mail.com", next.currentUser?.email)
    }

    @Test
    fun `event should not mutate state`() {
        val initial = AuthState(email = "a", password = "b")

        val next = with(reducer) {
            initial.reduce(AuthEvent.LoginSuccess)
        }

        assertEquals(initial, next)
    }
}
