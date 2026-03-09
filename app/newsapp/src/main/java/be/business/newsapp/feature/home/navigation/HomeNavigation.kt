package be.business.newsapp.feature.home.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import be.business.newsapp.feature.articledetail.ArticleDetailsScreen
import be.business.newsapp.feature.home.presentation.HomeScreen
import be.business.newsapp.navigation.NewsScreen

fun EntryProviderScope<NavKey>.featureHome(
    onArticleClick: (String) -> Unit,
    onLoginRequired: () -> Unit
) {
    entry<NewsScreen.Home> {
        HomeScreen(
            onArticleClick = { id ->
               onArticleClick(id)
            },
            onLoginRequired = onLoginRequired
        )
    }

    entry<NewsScreen.ArticleDetails> { route ->
        ArticleDetailsScreen(
            articleUrl = route.articleId
        )
    }
}
