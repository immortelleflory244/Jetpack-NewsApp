package be.business.newsapp.feature.home.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import be.business.newsapp.core.domain.model.Article
import be.business.newsapp.core.presentation.BaseStateViewModel
import be.business.newsapp.feature.favorites.domain.ToggleFavoriteUseCase
import be.business.newsapp.feature.home.domain.usecase.GetTopNewsUseCase
import coil.ImageLoader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val imageLoader: ImageLoader,
    private val getTopNewsUseCase: GetTopNewsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : BaseStateViewModel<HomeAction, HomeResult, HomeEvent, HomeState, HomeReducer>(
    initialState = HomeState(),
    reducer = HomeReducer()
) {

    var selectedCategory by mutableStateOf<String?>(null)
        private set
    var selectedCountry by mutableStateOf("us")
        private set

    init {
        action(HomeAction.FetchTopNewsData(selectedCountry))
    }

    override fun HomeAction.process(): Flow<HomeResult> = flow {
        when (this@process) {
            is HomeAction.FetchTopNewsData -> emitAll(fetchTopNews(country, category))
            is HomeAction.AddToFavourites -> emitAll(addToFavourites(article))
            HomeAction.Retry -> emitAll(fetchTopNews(selectedCountry, selectedCategory))
        }
    }

    fun selectCountry(country: String) {
        selectedCountry = country
        action(HomeAction.FetchTopNewsData(country, selectedCategory))
    }

    fun selectCategory(category: String?) {
        selectedCategory = category
        action(HomeAction.FetchTopNewsData(selectedCountry, category))
    }

    private fun addToFavourites(article: Article): Flow<HomeResult> = flow {
        toggleFavoriteUseCase(article).fold(
            onSuccess = { updatedArticle ->
                emit(HomeResult.FavouriteUpdated(updatedArticle))
                emitEvent(
                    HomeEvent.ShowToast(
                        if (updatedArticle.isFavorite) {
                            "Added to favorites"
                        } else {
                            "Removed from favorites"
                        }
                    )
                )
            },
            onFailure = {
                val message = it.message ?: "Failed to update favorite"
                emit(HomeResult.Error(message))
                if (message.contains("login", ignoreCase = true)) {
                    emitEvent(HomeEvent.NavigateToLogin)
                } else {
                    emitEvent(HomeEvent.ShowToast(message))
                }
            }
        )
    }

    private fun fetchTopNews(country: String, category: String? = null): Flow<HomeResult> = flow {
        emit(HomeResult.Loading)
        getTopNewsUseCase(country, category).fold(
            onSuccess = { emit(HomeResult.TopNewsSuccess(it.article)) },
            onFailure = { emit(HomeResult.Error(it.message ?: "Unable to load news")) }
        )
    }
}
