package be.business.newsapp.feature.favorites.presentation

import be.business.newsapp.core.presentation.BaseStateViewModel
import be.business.newsapp.feature.favorites.domain.ObserveFavoritesUseCase
import be.business.newsapp.feature.favorites.domain.ToggleFavoriteUseCase
import coil.ImageLoader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val observeFavoritesUseCase: ObserveFavoritesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    val imageLoader: ImageLoader
) : BaseStateViewModel<FavoritesAction, FavoritesResult, FavoritesEvent, FavoritesState, FavoritesReducer>(
    initialState = FavoritesState(),
    reducer = FavoritesReducer()
) {

    init {
        action(FavoritesAction.LoadFavorites)
    }

    override fun FavoritesAction.process(): Flow<FavoritesResult> = flow {
        when (this@process) {
            FavoritesAction.LoadFavorites -> emitAll(loadFavorites())
            is FavoritesAction.ToggleFavorite -> emitAll(toggleFavorite(article))
        }
    }

    private fun loadFavorites(): Flow<FavoritesResult> = flow {
        emit(FavoritesResult.Loading)
        emitAll(
            observeFavoritesUseCase().map { FavoritesResult.Success(it) }
        )
    }

    private fun toggleFavorite(article: be.business.newsapp.core.domain.model.Article): Flow<FavoritesResult> = flow {
        toggleFavoriteUseCase(article).fold(
            onSuccess = {
                emit(FavoritesResult.ToggleDone(it))
                emitEvent(FavoritesEvent.ShowMessage("Favorite removed"))
            },
            onFailure = {
                emit(FavoritesResult.Error(it.message ?: "Failed to update favorite"))
                emitEvent(FavoritesEvent.ShowMessage(it.message ?: "Failed to update favorite"))
            }
        )
    }
}
