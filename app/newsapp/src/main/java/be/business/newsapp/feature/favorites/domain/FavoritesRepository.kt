package be.business.newsapp.feature.favorites.domain

import be.business.newsapp.core.domain.model.Article
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun observeFavoritesForCurrentUser(): Flow<List<Article>>
    suspend fun getFavoriteArticleIdsForCurrentUser(): Set<String>
    suspend fun toggleFavoriteForCurrentUser(article: Article): Result<Article>
    suspend fun getFavoritesCountForCurrentUser(): Long
}
