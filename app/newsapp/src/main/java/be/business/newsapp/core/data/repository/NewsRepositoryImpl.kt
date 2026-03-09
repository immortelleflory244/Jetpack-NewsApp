package be.business.newsapp.core.data.repository

import be.business.newsapp.core.data.remote.apis.ApiService
import be.business.newsapp.domain.model.newsresponse.NewsResponseDto
import be.business.newsapp.domain.repository.NewsRepository
import jakarta.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : NewsRepository {
    override suspend fun getTopHeadlines(country: String, category: String?): Result<NewsResponseDto> {
        return apiService.getTopHeadlinesOfCountry(country,category)
    }

    override suspend fun getTopHeadlinesOfChannels(sources: String): Result<NewsResponseDto> {
        return apiService.getTopHeadlinesOfChannel(sources)
    }
}
