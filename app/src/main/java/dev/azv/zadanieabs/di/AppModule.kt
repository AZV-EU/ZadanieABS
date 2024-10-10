package dev.azv.zadanieabs.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.azv.zadanieabs.data.user.SessionManager
import dev.azv.zadanieabs.domain.user.SessionManagerImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideSessionManager(): SessionManager {
        return SessionManagerImpl()
    }

}