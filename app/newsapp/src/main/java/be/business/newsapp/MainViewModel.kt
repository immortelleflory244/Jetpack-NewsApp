package be.business.newsapp

import androidx.lifecycle.viewModelScope
import be.business.newsapp.core.data.local.datastore.DataManager
import be.business.newsapp.core.presentation.AppState
import be.business.newsapp.core.presentation.BaseViewModel
import be.business.newsapp.feature.auth.domain.ObserveCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val dataManager: DataManager,
    observeCurrentUserUseCase: ObserveCurrentUserUseCase
) :
    BaseViewModel<MainAction, MainState, MainEvent>(MainState.Initial) {

    val currentUser: StateFlow<be.business.newsapp.feature.auth.domain.AuthUser?> =
        observeCurrentUserUseCase().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    override fun MainAction.process() {
       when(this){
           is MainAction.ChangeTheme -> {
               onChangeTheme(theme)
           }
           else -> {}
       }
    }
    private fun onChangeTheme(value: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            AppState.updateTheme(value)
            dataManager.preferences.isDarkTheme(value)
        }
    }
    fun checkTheme() = viewModelScope.launch(Dispatchers.IO) {
        AppState.updateTheme(dataManager.preferences.isDarkTheme().first())
    }

}
