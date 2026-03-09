package be.business.newsapp.core.di

import be.business.newsapp.core.data.local.datastore.DataManager
import be.business.newsapp.core.data.local.datastore.PreferenceRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDataManager(
        preferencesRepositoryImpl: PreferenceRepositoryImpl
    ): DataManager = DataManager(preferencesRepositoryImpl)
}
