package be.business.newsapp.feature.favorites.presentation

import be.business.newsapp.core.domain.model.Article
import be.business.newsapp.core.presentation.MviAction
import be.business.newsapp.core.presentation.MviEvent
import be.business.newsapp.core.presentation.MviResult
import be.business.newsapp.core.presentation.MviStateReducer
import be.business.newsapp.core.presentation.MviViewState

sealed class FavoritesAction : MviAction {
    data object LoadFavorites : FavoritesAction()
    data class ToggleFavorite(val article: Article) : FavoritesAction()
}

sealed class FavoritesEvent : MviEvent, FavoritesResult() {
    data class ShowMessage(val message: String) : FavoritesEvent()
}

sealed class FavoritesResult : MviResult {
    data object Loading : FavoritesResult()
    data class Success(val articles: List<Article>) : FavoritesResult()
    data class Error(val message: String) : FavoritesResult()
    data class ToggleDone(val article: Article) : FavoritesResult()
}

data class FavoritesState(
    val isLoading: Boolean = false,
    val articles: List<Article> = emptyList(),
    val error: String? = null
) : MviViewState

class FavoritesReducer : MviStateReducer<FavoritesState, FavoritesResult> {
    override fun FavoritesState.reduce(result: FavoritesResult): FavoritesState {
        return when (result) {
            FavoritesResult.Loading -> copy(isLoading = true, error = null)
            is FavoritesResult.Success -> copy(isLoading = false, articles = result.articles, error = null)
            is FavoritesResult.Error -> copy(isLoading = false, error = result.message)
            is FavoritesResult.ToggleDone -> copy(
                articles = articles.filterNot { it.url == result.article.url },
                isLoading = false,
                error = null
            )
            is FavoritesEvent -> this
        }
    }
}
