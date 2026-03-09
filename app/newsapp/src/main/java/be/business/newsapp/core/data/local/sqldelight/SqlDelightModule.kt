package be.business.newsapp.core.data.local.sqldelight

import android.content.Context
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import app.cash.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SqlDelightModule {

    @Provides
    @Singleton
    fun provideSqlDriver(@ApplicationContext context: Context): SqlDriver {
        return AndroidSqliteDriver(
            schema = NewsSqlDatabase.Schema,
            context = context,
            name = "news_app.db"
        )
    }

    @Provides
    @Singleton
    fun provideSqlDatabase(driver: SqlDriver): NewsSqlDatabase = NewsSqlDatabase(driver)
}
