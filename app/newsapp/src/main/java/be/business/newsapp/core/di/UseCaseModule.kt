package be.business.newsapp.core.di

import be.business.newsapp.domain.repository.NewsRepository
import be.business.newsapp.domain.usecase.news.AddToFavouritesUseCase
import be.business.newsapp.domain.usecase.news.AddToFavouritesUseCaseImpl
import be.business.newsapp.domain.usecase.news.GetFavouriteNewsUseCase
import be.business.newsapp.domain.usecase.news.GetFavouriteNewsUseCaseImpl
import be.business.newsapp.domain.usecase.news.GetTopHeadlinesUseCase
import be.business.newsapp.domain.usecase.news.GetTopHeadlinesUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetTopNewsUseCase(repo: NewsRepository): GetTopHeadlinesUseCase = GetTopHeadlinesUseCaseImpl(repo)

    @Provides
    @Singleton
    fun provideAddToFavouritesUseCase(impl: AddToFavouritesUseCaseImpl): AddToFavouritesUseCase = impl

    @Provides
    @Singleton
    fun provideGetFavouriteNewsUseCase(impl: GetFavouriteNewsUseCaseImpl): GetFavouriteNewsUseCase = impl
}
