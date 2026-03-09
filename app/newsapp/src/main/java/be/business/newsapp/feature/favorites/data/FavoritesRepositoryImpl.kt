package be.business.newsapp.feature.favorites.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import be.business.newsapp.core.data.local.session.UserSessionStore
import be.business.newsapp.core.data.local.sqldelight.NewsSqlDatabase
import be.business.newsapp.core.domain.model.Article
import be.business.newsapp.core.domain.model.Source
import be.business.newsapp.feature.favorites.domain.FavoritesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepositoryImpl @Inject constructor(
    private val database: NewsSqlDatabase,
    private val sessionStore: UserSessionStore
) : FavoritesRepository {

    override fun observeFavoritesForCurrentUser(): Flow<List<Article>> {
        return sessionStore.currentUserId().flatMapLatest { userId ->
            if (userId == null) {
                flowOf(emptyList())
            } else {
                database.newsSqlDatabaseQueries
                    .selectFavoritesByUser(userId, ::mapFavorite)
                    .asFlow()
                    .mapToList(Dispatchers.IO)
            }
        }
    }

    override suspend fun getFavoriteArticleIdsForCurrentUser(): Set<String> {
        val userId = sessionStore.currentUserId().first() ?: return emptySet()
        return database.newsSqlDatabaseQueries
            .selectFavoriteArticleIdsByUser(userId)
            .executeAsList()
            .toSet()
    }

    override suspend fun toggleFavoriteForCurrentUser(article: Article): Result<Article> {
        val userId = sessionStore.currentUserId().first()
            ?: return Result.failure(IllegalStateException("Please login to manage favorites"))

        val articleId = article.url.orEmpty()
        if (articleId.isBlank()) {
            return Result.failure(IllegalArgumentException("Article id is missing"))
        }

        val existing = database.newsSqlDatabaseQueries
            .selectFavoriteByArticleAndUser(articleId, userId)
            .executeAsOneOrNull()

        return runCatching {
            if (existing == null) {
                database.newsSqlDatabaseQueries.insertFavorite(
                    id = null,
                    articleId = articleId,
                    userId = userId,
                    title = article.title,
                    author = article.author,
                    description = article.description,
                    imageUrl = article.urlToImage,
                    publishedAt = article.publishedAt,
                    content = article.content,
                    articleUrl = article.url
                )
                article.copy(isFavorite = true)
            } else {
                database.newsSqlDatabaseQueries.deleteFavoriteByArticleAndUser(articleId, userId)
                article.copy(isFavorite = false)
            }
        }
    }

    override suspend fun getFavoritesCountForCurrentUser(): Long {
        val userId = sessionStore.currentUserId().first() ?: return 0
        return database.newsSqlDatabaseQueries.countFavoritesByUser(userId).executeAsOne()
    }

    private fun mapFavorite(
        id: Long,
        articleId: String,
        userId: Long,
        title: String,
        author: String?,
        description: String?,
        imageUrl: String?,
        publishedAt: String?,
        content: String?,
        articleUrl: String?
    ): Article {
        return Article(
            source = Source(id = null, name = ""),
            author = author,
            title = title,
            description = description,
            url = articleUrl ?: articleId,
            urlToImage = imageUrl,
            publishedAt = publishedAt,
            content = content,
            isFavorite = true
        )
    }
}
