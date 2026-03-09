package be.business.newsapp.feature.favorites.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import be.business.newsapp.feature.favorites.ui.FavoritesLoginPrompt
import be.business.newsapp.feature.favorites.ui.FavoritesScreen
import be.business.newsapp.navigation.NewsScreen

fun EntryProviderScope<NavKey>.featureFavorites(
    isLoggedIn: Boolean,
    onLoginClick: () -> Unit
) {
    entry<NewsScreen.Favourites> {
        if (isLoggedIn) {
            FavoritesScreen()
        } else {
            FavoritesLoginPrompt(onLoginClick = onLoginClick)
        }
    }
}
