package be.business.newsapp.feature.favorites.domain

import be.business.newsapp.core.domain.model.Article
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveFavoritesUseCase @Inject constructor(
    private val repository: FavoritesRepository
) {
    operator fun invoke(): Flow<List<Article>> = repository.observeFavoritesForCurrentUser()
}

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: FavoritesRepository
) {
    suspend operator fun invoke(article: Article): Result<Article> = repository.toggleFavoriteForCurrentUser(article)
}

class GetFavoriteIdsUseCase @Inject constructor(
    private val repository: FavoritesRepository
) {
    suspend operator fun invoke(): Set<String> = repository.getFavoriteArticleIdsForCurrentUser()
}

class GetFavoritesCountUseCase @Inject constructor(
    private val repository: FavoritesRepository
) {
    suspend operator fun invoke(): Long = repository.getFavoritesCountForCurrentUser()
}
