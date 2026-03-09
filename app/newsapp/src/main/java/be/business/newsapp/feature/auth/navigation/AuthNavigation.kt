package be.business.newsapp.feature.auth.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import be.business.newsapp.feature.auth.ui.LoginScreen
import be.business.newsapp.navigation.NewsScreen

fun EntryProviderScope<NavKey>.featureAuth(onLoginSuccess: () -> Unit) {
    entry<NewsScreen.Login> {
        LoginScreen(onLoginSuccess = onLoginSuccess)
    }
}
