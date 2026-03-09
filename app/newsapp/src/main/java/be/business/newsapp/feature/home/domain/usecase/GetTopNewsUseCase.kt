package be.business.newsapp.feature.home.domain.usecase

import be.business.newsapp.domain.model.NewsResponse
import be.business.newsapp.domain.repository.NewsRepository
import be.business.newsapp.feature.favorites.domain.GetFavoriteIdsUseCase
import javax.inject.Inject

class GetTopNewsUseCase @Inject constructor(
    private val repository: NewsRepository,
    private val getFavoriteIdsUseCase: GetFavoriteIdsUseCase
) {
    suspend operator fun invoke(country: String, category: String? = null): Result<NewsResponse> {
        return try {
            val result = repository.getTopHeadlines(country, category)
            result.fold(
                onSuccess = { dto ->
                    val favoritesUrls = getFavoriteIdsUseCase()
                    Result.success(dto.toDomain(favoritesUrls))
                },
                onFailure = { Result.failure(it) }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
