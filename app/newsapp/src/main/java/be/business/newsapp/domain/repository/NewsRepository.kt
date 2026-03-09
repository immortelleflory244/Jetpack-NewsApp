package be.business.newsapp.domain.repository

import be.business.newsapp.domain.model.newsresponse.NewsResponseDto

interface NewsRepository {
    suspend fun getTopHeadlines(country: String,category: String? = null): Result<NewsResponseDto>
    suspend fun getTopHeadlinesOfChannels(sources: String): Result<NewsResponseDto>

}
