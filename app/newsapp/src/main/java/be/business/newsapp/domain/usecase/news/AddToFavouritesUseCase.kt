package be.business.newsapp.domain.usecase.news

import be.business.newsapp.core.domain.model.Article
import be.business.newsapp.domain.usecase.SuspendableUseCase
import be.business.newsapp.feature.favorites.domain.ToggleFavoriteUseCase
import javax.inject.Inject

interface AddToFavouritesUseCase : SuspendableUseCase<Article, Article>

class AddToFavouritesUseCaseImpl @Inject constructor(
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : AddToFavouritesUseCase {

    override suspend fun invoke(params: Article): Result<Article> {
        return toggleFavoriteUseCase(params)
    }
}
