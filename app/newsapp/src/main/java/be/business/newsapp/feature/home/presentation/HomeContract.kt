package be.business.newsapp.feature.home.presentation

import be.business.newsapp.core.domain.model.Article
import be.business.newsapp.core.presentation.MviAction
import be.business.newsapp.core.presentation.MviEvent
import be.business.newsapp.core.presentation.MviResult
import be.business.newsapp.core.presentation.MviStateReducer
import be.business.newsapp.core.presentation.MviViewState

sealed class HomeAction : MviAction {
    data class FetchTopNewsData(val country: String, val category: String? = null) : HomeAction()
    data class AddToFavourites(val article: Article) : HomeAction()
    data object Retry : HomeAction()
}

sealed class HomeEvent : MviEvent, HomeResult() {
    data class ShowToast(val message: String) : HomeEvent()
    data object NavigateToLogin : HomeEvent()
}

sealed class HomeResult : MviResult {
    data object Loading : HomeResult()
    data class TopNewsSuccess(val articles: List<Article>) : HomeResult()
    data class FavouriteUpdated(val article: Article) : HomeResult()
    data class Error(val message: String) : HomeResult()
}

data class HomeState(
    val loading: Boolean = false,
    val articles: List<Article> = emptyList(),
    val error: String? = null,
    val selectedCountry: String = "us",
    val selectedCategory: String? = null
) : MviViewState

class HomeReducer : MviStateReducer<HomeState, HomeResult> {
    override fun HomeState.reduce(result: HomeResult): HomeState {
        return when (result) {
            HomeResult.Loading -> copy(loading = true, error = null)
            is HomeResult.TopNewsSuccess -> copy(articles = result.articles, loading = false, error = null)
            is HomeResult.FavouriteUpdated -> copy(
                articles = articles.map { if (it.url == result.article.url) result.article else it },
                loading = false,
                error = null
            )
            is HomeResult.Error -> copy(loading = false, error = result.message)
            is HomeEvent -> this
        }
    }
}
