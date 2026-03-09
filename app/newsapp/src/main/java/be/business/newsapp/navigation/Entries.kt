package be.business.newsapp.navigation

import androidx.navigation3.runtime.entryProvider
import be.business.newsapp.feature.auth.navigation.featureAuth
import be.business.newsapp.feature.favorites.navigation.featureFavorites
import be.business.newsapp.feature.home.navigation.featureHome
import be.business.newsapp.feature.profile.navigation.featureProfile

fun entryProvider(navigator: Navigator, isLoggedIn: Boolean) = entryProvider {
    featureHome(
        onArticleClick = { navigator.navigate(NewsScreen.ArticleDetails(it)) },
        onLoginRequired = { navigator.navigate(NewsScreen.Login) }
    )
    featureFavorites(
        isLoggedIn = isLoggedIn,
        onLoginClick = { navigator.navigate(NewsScreen.Login) }
    )
    featureProfile(onLoginClick = { navigator.navigate(NewsScreen.Login) })
    featureAuth(onLoginSuccess = { navigator.goBack() })
}
