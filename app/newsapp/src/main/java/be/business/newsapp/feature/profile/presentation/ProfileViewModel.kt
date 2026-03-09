package be.business.newsapp.feature.profile.presentation

import be.business.newsapp.core.presentation.BaseStateViewModel
import be.business.newsapp.feature.auth.domain.GetCurrentUserUseCase
import be.business.newsapp.feature.auth.domain.LogoutUseCase
import be.business.newsapp.feature.favorites.domain.GetFavoritesCountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getFavoritesCountUseCase: GetFavoritesCountUseCase,
    private val logoutUseCase: LogoutUseCase
) : BaseStateViewModel<ProfileAction, ProfileResult, ProfileEvent, ProfileState, ProfileReducer>(
    initialState = ProfileState(),
    reducer = ProfileReducer()
) {

    init {
        action(ProfileAction.Load)
    }

    override fun ProfileAction.process(): Flow<ProfileResult> = flow {
        when (this@process) {
            ProfileAction.Load -> emitAll(loadProfile())
            ProfileAction.Logout -> emitAll(logout())
        }
    }

    private fun loadProfile(): Flow<ProfileResult> = flow {
        emit(ProfileResult.Loading)
        val user = getCurrentUserUseCase()
        val count = getFavoritesCountUseCase()
        emit(ProfileResult.Loaded(email = user?.email, favoritesCount = count))
    }

    private fun logout(): Flow<ProfileResult> = flow {
        logoutUseCase()
        emit(ProfileResult.Loaded(email = null, favoritesCount = 0))
        emitEvent(ProfileEvent.ShowMessage("Logged out"))
        emitEvent(ProfileEvent.LoggedOut)
    }
}
