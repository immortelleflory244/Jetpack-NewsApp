package be.business.newsapp.feature.home

import be.business.newsapp.core.domain.model.Article
import be.business.newsapp.core.domain.model.Source
import be.business.newsapp.feature.home.presentation.HomeReducer
import be.business.newsapp.feature.home.presentation.HomeResult
import be.business.newsapp.feature.home.presentation.HomeState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HomeReducerTest {

    private val reducer = HomeReducer()

    @Test
    fun `loading should set loading true and clear error`() {
        val initial = HomeState(loading = false, error = "boom")

        val next = with(reducer) { initial.reduce(HomeResult.Loading) }

        assertTrue(next.loading)
        assertEquals(null, next.error)
    }

    @Test
    fun `favourite updated should replace matching article only`() {
        val first = article(url = "1", isFavorite = false)
        val second = article(url = "2", isFavorite = false)
        val initial = HomeState(articles = listOf(first, second), loading = true)

        val next = with(reducer) {
            initial.reduce(HomeResult.FavouriteUpdated(first.copy(isFavorite = true)))
        }

        assertFalse(next.loading)
        assertTrue(next.articles.first { it.url == "1" }.isFavorite)
        assertFalse(next.articles.first { it.url == "2" }.isFavorite)
    }

    private fun article(url: String, isFavorite: Boolean): Article {
        return Article(
            source = Source(id = null, name = "src"),
            author = null,
            title = "title-$url",
            description = null,
            url = url,
            urlToImage = null,
            publishedAt = null,
            content = null,
            isFavorite = isFavorite
        )
    }
}
