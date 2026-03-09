package be.business.newsapp.feature.profile.presentation

import be.business.newsapp.core.presentation.MviAction
import be.business.newsapp.core.presentation.MviEvent
import be.business.newsapp.core.presentation.MviResult
import be.business.newsapp.core.presentation.MviStateReducer
import be.business.newsapp.core.presentation.MviViewState

sealed class ProfileAction : MviAction {
    data object Load : ProfileAction()
    data object Logout : ProfileAction()
}

sealed class ProfileEvent : MviEvent, ProfileResult() {
    data object LoggedOut : ProfileEvent()
    data class ShowMessage(val message: String) : ProfileEvent()
}

sealed class ProfileResult : MviResult {
    data object Loading : ProfileResult()
    data class Loaded(val email: String?, val favoritesCount: Long) : ProfileResult()
    data class Error(val message: String) : ProfileResult()
}

data class ProfileState(
    val isLoading: Boolean = false,
    val email: String? = null,
    val favoritesCount: Long = 0,
    val error: String? = null
) : MviViewState {
    val isLoggedIn: Boolean get() = !email.isNullOrBlank()
}

class ProfileReducer : MviStateReducer<ProfileState, ProfileResult> {
    override fun ProfileState.reduce(result: ProfileResult): ProfileState {
        return when (result) {
            ProfileResult.Loading -> copy(isLoading = true, error = null)
            is ProfileResult.Loaded -> copy(
                isLoading = false,
                email = result.email,
                favoritesCount = result.favoritesCount,
                error = null
            )
            is ProfileResult.Error -> copy(isLoading = false, error = result.message)
            is ProfileEvent -> this
        }
    }
}
