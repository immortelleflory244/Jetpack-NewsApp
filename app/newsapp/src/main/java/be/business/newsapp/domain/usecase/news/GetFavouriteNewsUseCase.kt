package be.business.newsapp.domain.usecase.news

import be.business.newsapp.core.domain.model.Article
import be.business.newsapp.domain.usecase.ObservableUseCaseWithResult
import be.business.newsapp.feature.favorites.domain.ObserveFavoritesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface GetFavouriteNewsUseCase : ObservableUseCaseWithResult<List<Article>, Unit>

class GetFavouriteNewsUseCaseImpl @Inject constructor(
    private val observeFavoritesUseCase: ObserveFavoritesUseCase
) :
    GetFavouriteNewsUseCase {

    override fun invoke(params: Unit): Flow<Result<List<Article>>> =
        observeFavoritesUseCase().map { Result.success(it) }

}
