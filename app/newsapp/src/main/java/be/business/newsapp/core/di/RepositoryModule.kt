package be.business.newsapp.core.di

import be.business.newsapp.core.data.remote.apis.ApiService
import be.business.newsapp.core.data.repository.NewsRepositoryImpl
import be.business.newsapp.domain.repository.NewsRepository
import be.business.newsapp.feature.auth.data.AuthRepositoryImpl
import be.business.newsapp.feature.auth.domain.AuthRepository
import be.business.newsapp.feature.favorites.data.FavoritesRepositoryImpl
import be.business.newsapp.feature.favorites.domain.FavoritesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideNewsRepository(apiService: ApiService): NewsRepository = NewsRepositoryImpl(apiService)

    @Provides
    @Singleton
    fun provideFavoritesRepository(impl: FavoritesRepositoryImpl): FavoritesRepository = impl

    @Provides
    @Singleton
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository = impl

}
