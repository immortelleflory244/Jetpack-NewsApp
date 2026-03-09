package be.business.newsapp.feature.favorites

import be.business.newsapp.core.domain.model.Article
import be.business.newsapp.core.domain.model.Source
import be.business.newsapp.feature.favorites.domain.FavoritesRepository
import be.business.newsapp.feature.favorites.domain.GetFavoriteIdsUseCase
import be.business.newsapp.feature.favorites.domain.GetFavoritesCountUseCase
import be.business.newsapp.feature.favorites.domain.ObserveFavoritesUseCase
import be.business.newsapp.feature.favorites.domain.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FavoritesUseCasesTest {

    @Test
    fun `observe favorites delegates to repository`() = runTest {
        val repo = FakeFavoritesRepository()
        val useCase = ObserveFavoritesUseCase(repo)

        repo.favorites.value = listOf(sampleArticle("abc", true))

        val result = useCase().first()

        assertEquals(1, result.size)
        assertEquals("abc", result.first().url)
    }

    @Test
    fun `toggle favorite delegates and returns result`() = runTest {
        val repo = FakeFavoritesRepository()
        val useCase = ToggleFavoriteUseCase(repo)

        val result = useCase(sampleArticle("id-1", false))

        assertTrue(result.isSuccess)
        assertTrue(result.getOrThrow().isFavorite)
    }

    @Test
    fun `ids and count use cases return repository values`() = runTest {
        val repo = FakeFavoritesRepository(
            ids = setOf("a", "b"),
            count = 2L
        )

        val idsUseCase = GetFavoriteIdsUseCase(repo)
        val countUseCase = GetFavoritesCountUseCase(repo)

        assertEquals(setOf("a", "b"), idsUseCase())
        assertEquals(2L, countUseCase())
    }

    private class FakeFavoritesRepository(
        private val ids: Set<String> = emptySet(),
        private val count: Long = 0L
    ) : FavoritesRepository {
        val favorites = MutableStateFlow<List<Article>>(emptyList())

        override fun observeFavoritesForCurrentUser(): Flow<List<Article>> = favorites

        override suspend fun getFavoriteArticleIdsForCurrentUser(): Set<String> = ids

        override suspend fun toggleFavoriteForCurrentUser(article: Article): Result<Article> {
            return Result.success(article.copy(isFavorite = !article.isFavorite))
        }

        override suspend fun getFavoritesCountForCurrentUser(): Long = count
    }

    private fun sampleArticle(id: String, favorite: Boolean): Article {
        return Article(
            source = Source(id = null, name = "source"),
            author = null,
            title = "title",
            description = null,
            url = id,
            urlToImage = null,
            publishedAt = null,
            content = null,
            isFavorite = favorite
        )
    }
}
